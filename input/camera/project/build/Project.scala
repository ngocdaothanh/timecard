import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) {
  val localMavenRepo = "Local Maven Repo" at
          "file://" + Path.userHome + "/.m2/repository"

  val localIvyRepo = "Local Ivy Repo" at
          "file://" + Path.userHome + "/.ivy2/local"

  val v4l4j = "au.edu.jcu" % "v4l4j" % "latest" from "file:///usr/share/java/v4l4j.jar"

  override def libraryDependencies =
    Set(
      "org.scala-lang" % "scala-swing" % "2.8.0"
      ) ++ super.libraryDependencies

  override def mainClass = Some("timecard.input.camera.Main")

  //----------------------------------------------------------------------------

  // We neet to add "/usr/lib/jni" which stores libv4l4j.so to the java.library.path
  // See http://lwjgl.org/forum/index.php?topic=3416.0
  override def fork = Some(new ForkScalaRun {
    val libv4l4jPath = "/usr/lib/jni"
    val newPath = System.getProperty("java.library.path") + ":" + libv4l4jPath

    override def runJVMOptions =
      super.runJVMOptions ++ Seq("-Djava.library.path=" + newPath)

    override def scalaJars = Seq(
      new java.io.File("project/boot/scala-2.8.0/lib/scala-compiler.jar"),
      new java.io.File("project/boot/scala-2.8.0/lib/scala-library.jar"))
  })
}
