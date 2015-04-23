import akka.actor._
import java.io.File
import com.typesafe.config.ConfigFactory
import local.{token, Node, Msg}

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
  val nodes = List(new Node("127.0.0.1", 2551, "LocalActor"), new Node("127.0.0.1", 2552, "RemoteActor"),
    new Node("127.0.0.1", 2553, "Remote1Actor"), new Node("127.0.0.1", 2554, "Remote2Actor"))

  // try to connect to the remote actor
  //val remote = context.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:2552/user/RemoteActor")
  val remote = context.actorSelection("akka.tcp://ClientSystem@" + nodes(0).ip + ":" + nodes(0).port + "/user/" + nodes(0).name )
  val remote1 = context.actorSelection("akka.tcp://RemoteSystem@" + nodes(1).ip + ":" + nodes(1).port + "/user/" + nodes(1).name )
  val remote2 = context.actorSelection("akka.tcp://Remote1System@" + nodes(2).ip + ":" + nodes(2).port + "/user/" + nodes(2).name )

  // counter is the state maintained at each node
  var counter = 0
  var ready = false
  var leader = false
  val local = new Node("127.0.0.1", 2554, "LocalActor")

  val remotess = List[String](nodes(1).toString, nodes(0).toString, nodes(2).toString, nodes(3).toString)
  val sortedRemotes = remotess.sortWith(_<_)
  println(sortedRemotes)
  if(local.toString == sortedRemotes(0).toString)
    leader = true
  val i = 0
  var next = 0
  for(i <- 0 to 3)
    if(sortedRemotes(i) == local.toString)
      next = (i + 1) % 3
  val remoteActor = List(remote, remote1, remote2)
  var nextActor = remote
  //  for(e <- remoteActor)
  //    if(e.anchorPath.toString.contains(sortedRemotes(next)))
  //      nextActor = e
  println("NextActor" + nextActor)
  println("isLeader? " + leader)


  def prepare() = {
    //shuffle deck, distribute deck, exchange cards

    //send token to node who has red❤3?️
    remote ! token

  }

  //case object Msg (from: String, to: String)
  def receive = {
    case "hi" =>
      sender() ! "act"

    case "act" => {
      counter += 1
      if (counter == 3) {
        ready = true
        println("I'm ready")
        if(leader)
          prepare()
      }
    }

    case token =>
      println("Receive token, my phase starts!")
      //here is the function play the game
      println("Phase ends, pass the token");
      nextActor ! token
  }
}
