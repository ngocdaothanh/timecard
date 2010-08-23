// See: http://code.google.com/p/v4l4j/wiki/Examples

package timecard.input.camera

import java.awt.event.{WindowAdapter, WindowEvent}
import javax.swing.{JFrame, WindowConstants, JLabel, ButtonGroup, JRadioButton}

class Controller extends WindowAdapter {
  private var f: JFrame = null
  private var c: DigitalClock = null

  init()

  private def init() {
    initGUI()
  }

  override def windowClosing(e: WindowEvent) {
    c.stop
    f.dispose()
  }

  /**
   * Creates the graphical interface components and initialises them
   */
  private def initGUI() {
    f = new JFrame
    f.getContentPane.setLayout(new java.awt.FlowLayout)

    c = new DigitalClock
    f.getContentPane.add(c)

    val group = new ButtonGroup
    val b1 = new JRadioButton("出");  b1.setSelected(true)
    val b2 = new JRadioButton("外")
    val b3 = new JRadioButton("戻")
    val b4 = new JRadioButton("退")
    group.add(b1)
    group.add(b2)
    group.add(b3)
    group.add(b4)

    f.getContentPane.add(b1)
    f.getContentPane.add(b2)
    f.getContentPane.add(b3)
    f.getContentPane.add(b4)

    f.setTitle("Time Card")
    f.setSize(240, 480)
    f.setResizable(false)
    f.setVisible(true)

    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    f.addWindowListener(this)
  }
}
