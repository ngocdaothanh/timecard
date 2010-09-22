package timecard.input.camera

// See: http://code.google.com/p/v4l4j/wiki/Examples

import java.awt._
import javax.swing._
import java.awt.event.{ActionListener, ActionEvent}
import java.util._
import java.text.SimpleDateFormat

class TimeOptions(parent: JFrame, modal: Boolean, userId: String, name:String, time: Date) extends JDialog(parent, modal) {
  val timeArray = Array("Arrive", "Out", "Return", "Leave")
  var timeOption = timeArray(0)

  initGUI()

  /**
   * Creates the graphical interface components and initialises them
   */
  private def initGUI() {
    setTitle("Time Options")
    setLayout(new GridLayout(4, 1))
    val cp = getContentPane()

    //Row 4 radio buttons
    val group = new ButtonGroup
    val b1 = new JRadioButton(timeArray(0));   b1.setSelected(true)
    val b2 = new JRadioButton(timeArray(1))
    val b3 = new JRadioButton(timeArray(2))
    val b4 = new JRadioButton(timeArray(3))
    group.add(b1)
    group.add(b2)
    group.add(b3)
    group.add(b4)

    val radioPanel = new JPanel
    //radioPanel.setLayout(new FlowLayout)
    radioPanel.add(b1)
    radioPanel.add(b2)
    radioPanel.add(b3)
    radioPanel.add(b4)
    //radioPanel.setAlignmentX(Component.LEFT_ALIGNMENT)

    //Row Name
    val lblUserName = new JLabel("Name : " + name)

    //Row Time
    val fmTime = formatTime(time)
    val lblTime = new JLabel("Time : " + fmTime)

    //Row OK, Cancel button
    val btnOk = new JButton("Ok")
    val btnCancel = new JButton("Cancel")
    val buttonPane = new JPanel
    //buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT))
    buttonPane.add(btnOk)
    buttonPane.add(btnCancel)
    //buttonPane.setBorder(BorderFactory.createLineBorder(Color.black))

    //Add to GUI
    cp.add(new JPanel().add(lblUserName))
    cp.add(new JPanel().add(lblTime))
    cp.add(radioPanel)
    cp.add(buttonPane)

    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    setResizable(false)

    btnCancel.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) {
        dispose
      }
    })

    btnOk.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent) {
        //Save to database
        val timeOptionStr =
          if (timeOption == timeArray(0))
            "arrive_at"
          else if (timeOption == timeArray(1))
            "out_at"
          else if (timeOption == timeArray(2))
            "return_at"
          else
            "leave_at"
        DB.saveTimeEntry(userId, timeOptionStr, time)
        dispose
      }
    })

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

  def formatTime(time: Date): String = {
    new SimpleDateFormat("HH:mm").format(time);
  }
}
