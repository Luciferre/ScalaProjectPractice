package local

import java.io.File
import akka.actor.{Props, Actor, ActorSystem}
import com.typesafe.config.ConfigFactory

object Local extends App {
  // read from config file
  val configFile = getClass.getClassLoader.getResource("resources/local_application.conf").getFile
  val config = ConfigFactory.parseFile(new File(configFile))
  // create an ActorSystem based on the config file
  val system = ActorSystem("ClientSystem",config)
  // create a LocalActor
  val localActor = system.actorOf(Props[LocalActor], name="local")

}
case class Msg(var f: String, var t: String, var k: String, var m :String) extends Serializable{
  var from: String = f
  var to: String = t
  var kind: String = k
  var message: String = m
  override def toString = s"Message from($from) to ($to), kind: $kind, message: $message"
}

class Node(var i: String, var p: Integer, var n: String){
  var ip = i
  var port = p
  var name = n
}


class LocalActor extends Actor {

  val nodes = List(new Node("127.0.0.1", 2552, "RemoteActor"),
  new Node("127.0.0.1", 2553, "Remote1Actor"), new Node("127.0.0.1", 2554, "Remote2Actor"))

  // try to connect to the remote actor
  //val remote = context.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:2552/user/RemoteActor")

  val remote = context.actorSelection("akka.tcp://RemoteSystem@" + nodes(0).ip + ":" + nodes(0).port + "/user/" + nodes(0).name)
  val remote1 = context.actorSelection("akka.tcp://Remote1System@" + nodes(1).ip + ":" + nodes(1).port + "/user/" + nodes(1).name)
  val remote2 = context.actorSelection("akka.tcp://Remote2System@" + nodes(2).ip + ":" + nodes(2).port + "/user/" + nodes(2).name)

  println("akka.tcp://RemoteSystem" + nodes(0).ip + ":" + nodes(0).port + "/user/" + nodes(0).name)
  // counter is the state maintained at each node
  var counter = 0
  var ready = false
  override def preStart(): Unit = {
    /*
      Connect to remote actor. The following are the different parts of actor path
      akka.tcp : enabled-transports  of remote_application.conf
      RemoteSystem : name of the actor system used to create remote actor
      127.0.0.1:5150 : host and port
      user : The actor is user defined
      remote : name of the actor, passed as parameter to system.actorOf call
     */
    //println("That 's remote:" + remote)
    remote ! "hi"
    remote1 ! "hi"
    remote2 ! "hi"
  }

//case object Msg (from: String, to: String)
  def receive = {
//    case "START" =>
//      remote ! "Hello from the LocalActor"
//    case msg: String => {
//      println(s"LocalActor received message: '$msg'")
//      if (counter < 5) {
//        sender !  new Msg("1", "2")
//        counter += 1
//      }
//    }
    case "hi" =>
      sender() ! "act"
    case "act" => {
      counter += 1
      if (counter == 3) {
        ready = true
        println("I'm ready")
      }
    }
  }
}