// See: http://code.google.com/p/v4l4j/wiki/Examples

package timecard.input.camera

import java.awt.event.{WindowAdapter, WindowEvent}
import java.awt.image.BufferedImage
import au.edu.jcu.v4l4j.{VideoDevice, FrameGrabber, V4L4JConstants}
import javax.swing._
import java.text.SimpleDateFormat
import java.util._
import collection.immutable.HashMap

/**
 * @param dev the video device file to capture from
 */
class Viewer(dev: String, cf: Controller, config:Config) extends WindowAdapter {
  private var vd: VideoDevice = null
  private var fg: FrameGrabber = null
  private var fgThread: Thread = null

  private var f: JFrame = null
  private var l: JLabel = null

  private var qrcode: QRCode = null
  private var stopped = false

  private var logLine = 0
  private val db = new DB(config)
  private val sdf = new SimpleDateFormat("HH:mm")
  private val savedLog: java.util.Map[String, ArrayList[AnyRef]] = new java.util.HashMap
  private val mapTimeOptions = HashMap("arrive_at" -> "arrives", "out_at" -> "is out",
                                "return_at" -> "returns", "leave_at" -> "leaves")

  init()

  private def init() {
    qrcode = new QRCode
    initFrameGrabber()
    initGUI()
    initFrameGrabberThread()
  }

  /**
   * Catch window closing event so we can free up resources before exiting
   */
  override def windowClosing(e: WindowEvent) {
    // Do not let user close the window
    /*
    stopped = true
    if (fgThread.isAlive) {
      try {
        fgThread.join()
      } catch {
        case _ =>
      }
    }

    fg.stopCapture()
    vd.releaseFrameGrabber()
    f.dispose()
    */
  }

  private def initGUI() {
    f = new JFrame
    l = new JLabel
    f.getContentPane.add(l)

    f.setTitle(dev)
    f.setSize(fg.getWidth, fg.getHeight + 30)
    f.setResizable(false)
    f.setVisible(true)

    //f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
    f.addWindowListener(this)
  }

  private def initFrameGrabber() {
    val w = 640
    val h = 480
    val std = V4L4JConstants.STANDARD_WEBCAM
    val chn = 0
    val qty = 60

    vd = new VideoDevice(dev)
    fg = vd.getJPEGFrameGrabber(w, h, chn, std, qty)
    fg.startCapture()
  }

  private def initFrameGrabberThread() {
    fgThread = new Thread(new Runnable() {
      // Gets frames and display
      def run() {
        try {
          while (!stopped) {
            val bb = fg.getFrame()
            val b = new Array[Byte](bb.limit)
            bb.get(b)

            val icon = new ImageIcon(b)
            l.setIcon(icon)

            // Create a BufferedImage from the icon
            val i = icon.getImage
            val bi = (f.createImage(i.getWidth(f), i.getHeight(f))).asInstanceOf[BufferedImage]
            val g = bi.createGraphics() // Get a Graphics2D object
            g.drawImage(i, 0, 0, f) // Draw the Image data into the BufferedImage
            val userId = qrcode.read(bi)
            if (userId != null) {
              System.out.println(userId)

              //Check if userId is exists
              val name = db.getName(userId)
              if (name != null) {
                fg.stopCapture

                //Show employee name and log time
                val now = new java.util.Date
                val nowFmt = formatTime(now)

                cf.lblTime.setText(nowFmt)
                cf.lblUserName.setText(name)

                //Store into database
                val timeOptionStr =
                  if (cf.timeOption == cf.timeArray(0))
                    "arrive_at"
                  else if (cf.timeOption == cf.timeArray(1))
                    "out_at"
                  else if (cf.timeOption == cf.timeArray(2))
                    "return_at"
                  else
                    "leave_at"

                //Check if user input 2 times in 5 minutes (5*60*1000 = 300000 ms)
                var under5minutes = false
                val list: ArrayList[AnyRef] = new ArrayList

                val l = savedLog.get(userId)
                if (l != null) {
                  val old_timeoption = l.get(0).asInstanceOf[String]
                  val old_time = l.get(1).asInstanceOf[Date]
                  if (now.getTime - old_time.getTime <= 300000) {
                    //Delete old_timeoption
                    db.saveTimeEntry(userId, old_timeoption, old_time, true)
                    //Save new time_option -> old_time
                    db.saveTimeEntry(userId, timeOptionStr, old_time)

                    list.add(timeOptionStr)
                    list.add(old_time)
                    savedLog.put(userId, list)
                    under5minutes = true

                    toLog(name, timeOptionStr, formatTime(old_time))
                  }
                }

                //Save to Map
                if (!under5minutes) {
                  list.add(timeOptionStr)
                  list.add(now)
                  savedLog.put(userId, list)
                  db.saveTimeEntry(userId, timeOptionStr, now)

                  //Insert log info
                  toLog(name, timeOptionStr, nowFmt)
                }

                Thread.sleep(2000)

                fg.startCapture
              }
            } else {
              //System.out.println("no matching")
            }
          }
        } catch {
          case e =>
            e.printStackTrace()
            System.out.println("Failed to capture image")
        }
      }
    })

    fgThread.start()
  }

  private def toLog(name:String, timeOptionStr:String, nowFmt:String) {
     //Insert log info
     if (logLine > 99) {
       cf.txtLogScreen.setText("")
       logLine = 0
     }
     logLine += 1
     cf.txtLogScreen.append("- " + name + " " + mapTimeOptions(timeOptionStr) + " at " + nowFmt + "\n")
     cf.txtLogScreen.setCaretPosition(cf.txtLogScreen.getDocument.getLength)   // View the last log line
  }

  private def formatTime(time: Date): String = sdf.format(time)
}
