package local

import java.io.File
import akka.actor.{Props, Actor, ActorSystem}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable

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
  override def toString = ip + ":" + port +  "\n"
}

case class token()


class LocalActor extends Actor {

  val nodes = List(new Node("127.0.0.1", 2551, "LocalActor"), new Node("127.0.0.1", 2552, "RemoteActor"),
  new Node("127.0.0.1", 2553, "Remote1Actor"), new Node("127.0.0.1", 2554, "Remote2Actor"))

  // try to connect to the remote actor
  //val remote = context.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:2552/user/RemoteActor")

  val remote = context.actorSelection("akka.tcp://RemoteSystem@" + nodes(1).ip + ":" + nodes(1).port + "/user/" + nodes(1).name)
  val remote1 = context.actorSelection("akka.tcp://Remote1System@" + nodes(2).ip + ":" + nodes(2).port + "/user/" + nodes(2).name)
  val remote2 = context.actorSelection("akka.tcp://Remote2System@" + nodes(3).ip + ":" + nodes(3).port + "/user/" + nodes(3).name)
  val remoteActor = List(remote, remote1, remote2)

  // counter is the state maintained at each node
  var counter = 0
  var ready = false
  var leader = false
  val local = new Node("127.0.0.1", 2551, "LocalActor")
//
//  val remotes = mutable.MutableList[Int]()
//  remotes += remote.hashCode
//  remotes += remote1.hashCode
//  remotes += remote2.hashCode

  // sort four nodes according to ip and port, and then check if it is a leader
  val remotess = List[String](nodes(1).toString, nodes(0).toString, nodes(2).toString, nodes(3).toString)
  val sortedRemotes = remotess.sortWith(_<_)
  println(sortedRemotes)
  if(local.toString == sortedRemotes(0).toString)
    leader = true
  //compute the next node who should receive the token
  val i = 0
  var next = 0
  for(i <- 0 to 3)
    if(sortedRemotes(i) == local.toString)
      next = (i + 1) % 3
  // because we don't have the local actorref, so the nextActor is hardcoded
  var nextActor = remote
  //  for(e <- remoteActor)
//    if(e.anchorPath.toString.contains(sortedRemotes(next)))
//      nextActor = e
  println("NextActor" + nextActor)
  println("isLeader? " + leader)


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
//      nextActor ! token
  }
}