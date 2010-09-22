// See: http://code.google.com/p/v4l4j/wiki/Examples

package timecard.input.camera

import java.awt.event.{WindowAdapter, WindowEvent}
import java.awt.image.BufferedImage
import au.edu.jcu.v4l4j.{VideoDevice, FrameGrabber, V4L4JConstants}
import javax.swing._
/**
 * @param dev the video device file to capture from
 */
class Viewer(dev: String) extends WindowAdapter {
  private var vd: VideoDevice = null
  private var fg: FrameGrabber = null
  private var fgThread: Thread = null

  private var f: JFrame = null
  private var l: JLabel = null
  private var customDialog: JDialog = null

  private var qrcode: QRCode = null
  private var stopped = false

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
              val name = DB.getName(userId)
              if (name != null) {
                fg.stopCapture

                //Show Modal dialog
                val now = new java.util.Date
                //val dummyUserId = "ae5e44340c2a0f3e3f68a785ca4eb394"
                customDialog = new TimeOptions(f, true, userId, name, now)
                customDialog.pack
                customDialog.setVisible(true)

                fg.startCapture
              }
            } else {
              //System.out.println("")
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
}
