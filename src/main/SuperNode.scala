import akka.actor.Actor

import scala.collection.mutable.ListBuffer

/**
 * Created by Caesar on 3/31/15.
 */
import akka.actor.Actor

object SuperNode {
  case object FindPlayer
  case object Done
  case object Response
}

class SuperNode extends Actor {
  var state: Integer = 0;
  var search = Vector.empty
  def receive = {

    case SuperNode.FindPlayer => {
      state += 1
      println("The other node wants some players!")
      println("the sn's state is " + state)
      sender() ! SuperNode.Response
    }

    case SuperNode.Done => {
      println("The other node has got enough players")
    }

    case SuperNode.Response => {
      println(state)
    }

  }
}