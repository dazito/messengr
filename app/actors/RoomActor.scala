package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import json.IncomingRoomMessage

/**
  * Created by daz on 06/08/2016.
  */
class RoomActor(clientActor: ActorRef, roomId: String, userId: String) extends Actor with ActorLogging{
  
  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info("ChatRoomActor started...!")
    super.preStart()
  }
  
  implicit
  
  override def receive: Receive = {
    case msg: IncomingRoomMessage => {
      log.warning("IncomingRoomMessage message {}", msg.text)
    }
  }
  
  
}

object RoomActor {
  def props(sender: ActorRef, roomId: String, userId: String) = Props(new RoomActor(sender, roomId, userId))
}