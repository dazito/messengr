package actors

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, Props}

/**
  * Created by daz on 06/08/2016.
  */
class ChatRoomActor extends Actor with ActorLogging{
  override def receive: Receive = {
    case message => {
      log.warning("unhandled message {}", message)
      unhandled(message)
    }
  }
  
  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info("ChatRoomActor started...!")
    super.preStart()
  }
}

object ChatRoomActor {
  def props = Props[ChatRoomActor]
}