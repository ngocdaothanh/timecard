package timecard.input.camera

import org.jcouchdb.db._
import org.jcouchdb.exception.NotFoundException
import java.util._
import org.jcouchdb.util.CouchDBUpdater
import java.io.{IOException, File}
import java.text.{SimpleDateFormat, DateFormat}
import org.jcouchdb.document.{ViewAndDocumentsResult, BaseDocument}

class DB(config:Config) {
  val server = new ServerImpl(config.SERVER, config.SERVER_PORT)
  val databases = server.listDatabases

  if (!databases.contains(config.DB_NAME)) {
    server.createDatabase(config.DB_NAME)
  }
  val db = new Database(config.SERVER, config.DB_NAME)

  createMapReduceJS("timecard-views")

  def getName(userId: String): String = {
    try {
      val doc: Map[String, String] = db.getDocument(classOf[Map[String, String]], userId)
      doc.get("name")
    } catch {
      case nfe: NotFoundException => {
        null
      }
    }
  }

  def saveTimeEntry(user_id: String, timeOption:String, time:Date, erase:Boolean = false) {
    val date: String = new SimpleDateFormat("yyyy-MM-dd").format(time)

    //Find by user_id and date
    val result = db.queryViewAndDocumentsByKeys("timeentry/find_by_userid_and_date", classOf[AnyRef],
      classOf[Map[String, String]], Arrays.asList(Arrays.asList(user_id, date)), null, null)

    //println(result.getRows().size());
    if (result.getRows.size != 0) {
      val doc: Map[String, String] = result.getRows.get(0).getDocument
      if (erase)
        doc.put(timeOption, null)
      else
        doc.put(timeOption, timeToGMT(time))

      db.createOrUpdateDocument(doc)
    }
    else {
      val doc: Map[String, String] = new HashMap[String, String]
      doc.put("user_id", user_id)
      doc.put("date", date)
      doc.put(timeOption, timeToGMT(time))
      doc.put("ruby_class", "Timeentry")
      db.createDocument(doc)
    }
  }

  private def timeToGMT(time: Date):String = {
    val tz: TimeZone = TimeZone.getTimeZone("GMT:00")
    val dfGMT = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG)
    dfGMT.setTimeZone(tz)
    dfGMT.format(time)
  }

  def createMapReduceJS(viewDir: String) {
    val updater = new CouchDBUpdater
    updater.setDatabase(db)
    val dir = new File(viewDir)
    updater.setDesignDocumentDir(dir)
    try {
      updater.updateDesignDocuments
    } catch {
      case e: IOException => {
        e.printStackTrace
      }
    }
  }

  /*
  def main(args: Array[String]) {
    //println(getName("ae5e44340c2a0f3e3f68a785ca4eb394"))
    saveTimeEntry("","",new java.util.Date)
  } */

}