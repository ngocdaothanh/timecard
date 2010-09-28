package timecard.input.camera

import java.io.File


object Main {
  var c: Controller = null

  def main(args: Array[String]) {
    val devs = videoDevices
    if (devs.length == 0) {
      println("No camera")
    } else {
      c = new Controller
      for (dev <- devs) new Viewer(dev, c)
    }
  }

  // Returns array of video devices: /dev/video0, /dev/video1...
  // See: https://code.google.com/p/v4l4j/source/browse/v4l4j/trunk/src/au/edu/jcu/v4l4j/examples/DeviceChooser.java
  private def videoDevices: Array[String] = {
    val dir = new File("/sys/class/video4linux/")
    val devs = dir.list.filter(dev => dev.indexOf("video") != -1)
    devs.map(dev => "/dev/" + dev)
  }
}
