import sun.jvm.hotspot.HelloWorld

/**
 * Created by Caesar on 3/31/15.
 */

object Main {
  def main(args: Array[String]): Unit = {
    akka.Main.main(Array(classOf[HelloWorld].getName))
  }
}