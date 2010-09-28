// See: http://code.google.com/p/v4l4j/wiki/Examples

package timecard.input.camera

import java.awt._
import javax.swing._
import java.awt.event.{WindowAdapter, WindowEvent, ActionListener, ActionEvent}
import javax.swing.{JFrame, WindowConstants, JLabel, ButtonGroup, JRadioButton}
import java.awt.Container


class Controller extends WindowAdapter {
  var lblUserName: JLabel = null
  var lblTime: JLabel = null
  var txtLogScreen: TextArea = null
  val timeArray = Array("Arrive    ", "Out    ", "Return    ", "Leave    ")
  var timeOption = timeArray(0)

  private var f: JFrame = null
  private var rbtnPanel: JPanel = null
  private var userInfoPanel: JPanel = null
  private var cp: Container = null
  private var c: DigitalClock = null

  init()

  private def init() {
    initGUI()
  }

  override def windowClosing(e: WindowEvent) {
    // Do not let user close the window
    /*
    c.stop
    f.dispose()
    */
  }

  /**
   * Creates the graphical interface components and initialises them
   */
  private def initGUI() {
    f = new JFrame
    cp = f.getContentPane
    cp.setLayout(new java.awt.FlowLayout)

    //datetime
    c = new DigitalClock
    cp.add(c)

    //employee log UI
    userInfoPanel = new JPanel
    userInfoPanel.setLayout(new GridLayout(2,2))
    val label1 = new JLabel("User name: ")
    lblUserName = new JLabel("")
    val label2 = new JLabel("Log at: ")
    lblTime = new JLabel("")
    userInfoPanel.add(label1)
    userInfoPanel.add(lblUserName)
    userInfoPanel.add(label2)
    userInfoPanel.add(lblTime)
    cp.add(userInfoPanel)

    //log type UI
    val group = new ButtonGroup
    val b1 = new JRadioButton(timeArray(0))
    b1.setSelected(true)
    val b2 = new JRadioButton(timeArray(1))
    val b3 = new JRadioButton(timeArray(2))
    val b4 = new JRadioButton(timeArray(3))
    group.add(b1)
    group.add(b2)
    group.add(b3)
    group.add(b4)

    rbtnPanel = new JPanel
    rbtnPanel.add(b1)
    rbtnPanel.add(b2)
    rbtnPanel.add(b3)
    rbtnPanel.add(b4)
    cp.add(rbtnPanel)

    //log information UI
    txtLogScreen = new TextArea("", 32, 50, TextArea.SCROLLBARS_VERTICAL_ONLY)
    txtLogScreen.setForeground(new Color(255,255,255))
    txtLogScreen.setBackground(new Color(0,0,0))
    txtLogScreen.setEditable(false)
    cp.add(txtLogScreen)

    //frame setting
    f.setTitle("Time Card")
    f.setSize(425, 690)
    f.setResizable(false)
    f.setVisible(true)

    //f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
    f.addWindowListener(this)

    //Register a listener for the radio buttons.
    b1.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) {
        timeOption = timeArray(0)
      }
    });

    b2.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) {
        timeOption = timeArray(1)
      }
    });

    b3.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) {
        timeOption = timeArray(2)
      }
    });

    b4.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) {
        timeOption = timeArray(3)
      }
    });
  }
}
