import akka.actor.Actor
import akka.actor.Props

class HelloWorld extends Actor {

  override def preStart(): Unit = {
    println("preStart...")
    val SN = context.actorOf(Props[SuperNode], "supernode")
    SN ! SuperNode.FindPlayer
  }

  def receive: Receive = {
    case SuperNode.Response => {
      println("Got players for a game")
      sender() ! SuperNode.Response
    }
    case SuperNode.Done => {
      println("gonna stop itself")
    }
    case _ => println("received something")

  }
}