package actors

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSelection, Props}
import commands.{ListPublicRoomsCommand, NewMessageCommand, UserJoinRoomCommand, UserLeftRoomCommand}
import events.{JoinedChannelEvent, NewMessageEvent, RoomEventType}
import json.{IncomingRoomMessage, OutgoingRoomMessage}
import org.joda.time.DateTime

/**
  * Created by daz on 15/09/2016.
  */
class RoomClientActor(actorRef: ActorRef, userId: String) extends Actor with ActorLogging {
  
  var roomMasterActor: ActorSelection = _
  
  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    super.preStart()
    roomMasterActor = context.actorSelection("/user/" + RoomMasterActor.getClass.getSimpleName)
  }
  
  override def receive: Receive = {
    case msg: IncomingRoomMessage => processIncomingRoomMessage(msg)
    case msg: JoinedChannelEvent => actorRef ! new OutgoingRoomMessage(msg.channelId, msg.userId, RoomEventType.JOIN.toString, None, DateTime.now().getMillis)
    case msg: NewMessageEvent => actorRef ! new OutgoingRoomMessage(msg.from, msg.from, RoomEventType.MESSAGE.toString, Option(msg.text), msg.timestamp)
    case msg: Any => unhandled(msg)
  }
  
  def processIncomingRoomMessage(msg: IncomingRoomMessage): Unit = {
    val eventType = RoomEventType.withName(msg.eventType)
    
    eventType match {
      case RoomEventType.JOIN => {
        roomMasterActor ! new UserJoinRoomCommand(msg.roomId, userId)
      }
      case RoomEventType.MESSAGE => {
        msg.text match {
          case text: Some[String] => roomMasterActor ! new NewMessageCommand(userId, msg.roomId, msg.text.get, UUID.randomUUID().toString, DateTime.now.getMillis)
          case None => sender() ! "Invalid message"
        }
      }
      case RoomEventType.LEAVE => roomMasterActor ! new UserLeftRoomCommand(msg.roomId, userId)
      case RoomEventType.LIST => roomMasterActor ! new ListPublicRoomsCommand
    }
  }
  
}

object RoomClientActor {
  def props(actorRef: ActorRef, userId: String) = Props(new RoomClientActor(actorRef, userId))
}