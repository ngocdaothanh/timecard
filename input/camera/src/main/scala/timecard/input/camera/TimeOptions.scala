package main.scala.timecard.input.camera

// See: http://code.google.com/p/v4l4j/wiki/Examples

import _root_.timecard.input.camera.{DB, DigitalClock}
import java.awt.Container
import javax.swing._
import java.awt.event.{ActionListener, ActionEvent, WindowAdapter, WindowEvent}
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
    setLayout(new java.awt.FlowLayout)

    val group = new ButtonGroup
    val b1 = new JRadioButton(timeArray(0));   b1.setSelected(true)
    val b2 = new JRadioButton(timeArray(1))
    val b3 = new JRadioButton(timeArray(2))
    val b4 = new JRadioButton(timeArray(3))
    group.add(b1)
    group.add(b2)
    group.add(b3)
    group.add(b4)

    getContentPane().add(b1)
    getContentPane().add(b2)
    getContentPane().add(b3)
    getContentPane().add(b4)

    //Get UserName from UserId
    //val name = DB.getName(userId)
    val lblUserName = new JLabel("Name : " + name)

    val fmTime = formatTime(time)
    val lblTime = new JLabel("Time : " + fmTime)
    val btnOk = new JButton("Ok")
    val btnCancel = new JButton("Cancel")

    getContentPane().add(lblUserName)
    getContentPane().add(lblTime)
    getContentPane().add(btnOk)
    getContentPane().add(btnCancel)

    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)

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
