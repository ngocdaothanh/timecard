package timecard.input.camera

object Main {
  def main(args: Array[String]) {
    new Viewer("/dev/video0")
  }
}
