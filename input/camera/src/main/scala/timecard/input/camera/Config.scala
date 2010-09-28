package timecard.input.camera

import java.util.Properties
import java.io.FileInputStream

class Config(path: String) {
  private val properties = new Properties
  properties.load(new FileInputStream(path))

  val SERVER = properties.getProperty("SERVER")
  val SERVER_PORT = properties.getProperty("SERVER_PORT").toInt
  val DB_NAME = properties.getProperty("DB_NAME")
}