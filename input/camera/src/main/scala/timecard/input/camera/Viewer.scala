// See: http://code.google.com/p/v4l4j/wiki/Examples

package timecard.input.camera

import java.awt.event.{WindowAdapter, WindowEvent}
import java.io.IOException
import java.nio.ByteBuffer

import java.awt.{Graphics2D, Image}
import java.awt.image.BufferedImage
import javax.swing.{JFrame, WindowConstants, JLabel, ImageIcon}

import au.edu.jcu.v4l4j.{VideoDevice, FrameGrabber, V4L4JConstants}
import au.edu.jcu.v4l4j.exceptions.V4L4JException

/**
 * @param dev the video device file to capture from
 */
class Viewer(dev: String) extends WindowAdapter {
  private var vd:       VideoDevice  = null
  private var fg:       FrameGrabber = null
  private var fgThread: Thread       = null

  private var f: JFrame = null
  private var l: JLabel = null

  private var qrcode: QRCode = null
  private var stop = false

  init()

  private def init() {
    qrcode = new QRCode
    initFrameGrabber()
    initGUI()
    initFrameGrabberThread()
  }

  /**
   * Catch window closing event so we can free up resources before exiting
   * @param e
   */
  override def windowClosing(e: WindowEvent) {
    stop = true
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
  }

  /**
   * Creates the graphical interface components and initialises them
   */
  private def initGUI() {
    f = new JFrame
    l = new JLabel
    f.getContentPane.add(l)
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    f.addWindowListener(this)
    f.setVisible(true)
    f.setSize(fg.getWidth, fg.getHeight)
  }

  private def initFrameGrabber() {
    val w   = 640
    val h   = 480
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
          while (!stop) {
            val bb = fg.getFrame()
            val b = new Array[Byte](bb.limit)
            bb.get(b)

            val icon = new ImageIcon(b)
            l.setIcon(icon)

            // Create a BufferedImage from the icon
            val i = icon.getImage
            val bi = (f.createImage(i.getWidth(f), i.getHeight(f))).asInstanceOf[BufferedImage]
            val g = bi.createGraphics()  // Get a Graphics2D object
            g.drawImage(i, 0, 0, f)      // Draw the Image data into the BufferedImage
            val text = qrcode.read(bi)
            if (text != null) {
              System.out.println(text)
            } else {
              System.out.println("")
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
