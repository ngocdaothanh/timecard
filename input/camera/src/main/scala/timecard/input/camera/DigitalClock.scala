package timecard.input.camera

import java.awt.{Dimension, FontMetrics, Graphics, Font}
import java.text.DecimalFormat
import java.util.Calendar
import javax.swing.JComponent

class DigitalClock extends JComponent {
  private val tflz = new DecimalFormat("00")
  private val tf = new DecimalFormat("#0")
  private val yf = new DecimalFormat("0000")

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
    val sb = new StringBuffer

    //date
    sb.append(yf.format(cal.get(Calendar.YEAR)))
    sb.append('-')
    sb.append(tflz.format(cal.get(Calendar.MONTH)))
    sb.append('-')
    sb.append(tflz.format(cal.get(Calendar.DAY_OF_MONTH)))
    val date = "Date: " + sb.toString
    sb.delete(0, sb.length)


    //time
    sb.append(tf.format(cal.get(Calendar.HOUR_OF_DAY)))
    sb.append(':')
    sb.append(tflz.format(cal.get(Calendar.MINUTE)))
    sb.append(':')
    sb.append(tflz.format(cal.get(Calendar.SECOND)))
    val time = "Time: " + sb.toString

    val fm = getFontMetrics(getFont())
    val x = (getSize().width - fm.stringWidth(date)) / 2

    // Set font once
    if (newFont == null) {
      newFont = getFont
      val fontSize = newFont.getSize2D + 10
      setFont(newFont.deriveFont(Font.BOLD).deriveFont(fontSize))
    }

    g.drawString(date, x, 40)
    g.drawString(time, x, 70)
  }

  override def getPreferredSize = new Dimension(300, 80)

  override def getMinimumSize = new Dimension(300, 80)
}
