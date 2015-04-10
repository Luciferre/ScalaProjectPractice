import akka.actor._
import java.io.File
import com.typesafe.config.ConfigFactory
import local.{Node, Msg}

object HelloRemote2 extends App {

  //get the configuration file from classpath
  val configFile = getClass.getClassLoader.getResource("resources/remote2_application.conf").getFile
  //parse the config
  val config = ConfigFactory.parseFile(new File(configFile))
  //create an actor system with that config
  val system = ActorSystem("Remote2System" , config)
  //create a remote actor from actorSystem
  val remote = system.actorOf(Props[Remote2Actor], name="Remote2Actor")
  println("remote2 is ready")

}

class Remote2Actor extends Actor {
  val nodes = List(new Node("127.0.0.1", 2551, "LocalActor"),
    new Node("127.0.0.1", 2552, "RemoteActor"), new Node("127.0.0.1", 2553, "Remote1Actor"))

  // try to connect to the remote actor
  //val remote = context.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:2552/user/RemoteActor")
  val remote = context.actorSelection("akka.tcp://ClientSystem@" + nodes(0).ip + ":" + nodes(0).port + "/user/" + nodes(0).name )
  val remote1 = context.actorSelection("akka.tcp://RemoteSystem@" + nodes(1).ip + ":" + nodes(1).port + "/user/" + nodes(1).name )
  val remote2 = context.actorSelection("akka.tcp://Remote1System@" + nodes(2).ip + ":" + nodes(2).port + "/user/" + nodes(2).name )

  // counter is the state maintained at each node
  var counter = 0
  var ready = false

  def receive = {
    case "hi" => {
      sender() ! "act"
      counter += 1
      if (counter == 3) {
        ready = true
        println("I'm ready")
      }
    }

//
//    case i: Integer => {
//      println("received a message from myself " + i)
//    }
//
//    case msg: String => {
//      println(s"RemoteActor received message '$msg'")
//      sender ! "Hello from the RemoteActor"
//    }
    case Msg(f, t, k, m) =>{
        println(f)
        println(t)
        println(k)
        println(m)
      }
  }
}
