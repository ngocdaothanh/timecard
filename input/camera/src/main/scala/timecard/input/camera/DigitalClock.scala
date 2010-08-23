package timecard.input.camera

import java.awt.{Dimension, FontMetrics, Graphics, Font}
import java.text.DecimalFormat
import java.util.Calendar
import javax.swing.JComponent

class DigitalClock extends JComponent {
  private val tflz = new DecimalFormat("00")
  private val tf   = new DecimalFormat("#0")

  private var newFont: Font = null
  private var stopped = false

  new Thread(new Runnable() {
    def run() {
      while (!stopped) {
        try {
          repaint()
          Thread.sleep(1000)
        } catch {
          case e =>
        }
      }
    }
  }).start()

  def stop() {
    stopped = true
  }

  // Paint H:MM:SS at the center of this component
  override def paint(g: Graphics) {
    val cal = Calendar.getInstance
    val sb  = new StringBuffer
    sb.append(tf.format(cal.get(Calendar.HOUR_OF_DAY)))
    sb.append(':')
    sb.append(tflz.format(cal.get(Calendar.MINUTE)))
    sb.append(':')
    sb.append(tflz.format(cal.get(Calendar.SECOND)))
    val s = sb.toString
    val fm = getFontMetrics(getFont())
    val x = (getSize().width - fm.stringWidth(s)) / 2

    // Set font once
    if (newFont == null) {
      newFont = getFont
      val fontSize = newFont.getSize2D + 30
      setFont(newFont.deriveFont(Font.BOLD).deriveFont(fontSize))
    }

    g.drawString(s, x, 40)
  }

  override def getPreferredSize = new Dimension(240, 40)

  override def getMinimumSize = new Dimension(240, 40)
}
