import akka.actor.Actor

object Greeter {
  case object Greet
  case object Done
  case object HW
}

class Greeter extends Actor {
  def receive = {
    case Greeter.Greet => {
      println("Hello world!")
      sender() ! Greeter.HW
      //sender() ! Greeter.Done
    }
  }
}