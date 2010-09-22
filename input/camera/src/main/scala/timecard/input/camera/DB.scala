package timecard.input.camera

import java.util.Date
import org.jcouchdb.db._
import org.jcouchdb.document.BaseDocument
import lantra.DBJava
import java.io.File


object DB {
  val HOST = "localhost"
  val HOST_PORT = Server.DEFAULT_PORT
  val DB_NAME = "timecard"

  val server = new ServerImpl(HOST, HOST_PORT)
  val databases = server.listDatabases

  if (!databases.contains(DB_NAME)) {
    server.createDatabase(DB_NAME)
  }
  //val db = new Database(HOST, DB_NAME)

  val dbjava = new DBJava(HOST, DB_NAME)

  dbjava.createMapReduceJS("timecard-views")

  def getName(userId: String): String = {
    dbjava.getName(userId)
  }

  def saveTimeEntry(user_id: String, timeOption:String, time:Date) {
    //println("Trong saveTimeEntry : " + user_id  + ":" + timeOption + ":" + time)
    dbjava.saveTimeEntry(user_id, timeOption, time)
  }

  /*
  def main(args: Array[String]) {
    //println(getName("ae5e44340c2a0f3e3f68a785ca4eb3944"))
    //saveTimeEntry("","",new java.util.Date)
    val f = new File("timecard-views")
    println(f.getAbsoluteFile)
  }
  */
}