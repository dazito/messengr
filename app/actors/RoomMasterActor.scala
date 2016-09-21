package actors

import java.util.UUID
import javax.inject.Inject

import akka.actor.{ActorLogging, ActorRef, ActorSystem, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}
import commands.{CreateRoomCommand, ListPublicRoomsCommand, NewMessageCommand, UserJoinRoomCommand}
import events.{JoinedChannelEvent, NewMessageEvent}

import scala.collection.mutable.{Map => MutableMap}

/**
  * Created by daz on 12/09/2016.
  */
class RoomMasterActor @Inject() (system: ActorSystem) extends PersistentActor with ActorLogging {
  
  var chatRooms: MutableMap[String, ActorRef] = MutableMap()
  
  override def receiveRecover: Receive = {
    case SnapshotOffer(_, snap: MutableMap[String, ActorRef]) => chatRooms = snap
  }
  
  override def persistenceId: String = getClass.getSimpleName
  
   
  override def receiveCommand: Receive = {
    case msg: UserJoinRoomCommand => processUserJoinRoomCommand(msg)
    case msg: CreateRoomCommand => processCreateRoomCommand(msg)
    case msg: ListPublicRoomsCommand => {
      chatRooms.foreach { actor => log.info("Channel id: {}", actor._1)}
    }
    case msg: NewMessageCommand => processNewMessageCommand(msg)
    case msg: String => log.info("{} - Received: {}", RoomMasterActor.getClass.getSimpleName, msg)
    case msg: Any => unhandled(msg)
  }
    
  def processUserJoinRoomCommand(msg: UserJoinRoomCommand): Unit = {
    // Notify all users in the channel
    val actorRoom = chatRooms.getOrElse(msg.roomId, None)
    
    actorRoom match {
      case room: ActorRef => room forward  msg
      case None => log.warning("userId:{}|roomId:{}|room not found", msg.userId, msg.roomId)
    }
  }
  
  def processCreateRoomCommand(msg: CreateRoomCommand): Unit = {
    val roomId = UUID.randomUUID().toString
    val roomActor = system.actorOf(RoomActor.props(self, roomId, msg.userId, msg.isPrivate))
    chatRooms += (roomId -> roomActor)
    
    persist(msg) {
      // TODO: implement thos logic
      event => log.info("Room created - Name {} | Room ID: {}", msg.name, roomId)
    }
  }
  
  
  def processNewMessageCommand(msg: NewMessageCommand): Unit = {
    log.info("process new message command")
    val actorRoom = chatRooms.getOrElse(msg.to, None)
    
    actorRoom match {
//      case room: ActorRef => room forward new NewMessageEvent(msg.fromUser, msg.to, msg.text, msg.uuid, msg.timestamp)
      case room: ActorRef => room forward msg
      case None => log.warning("userId:{}|roomId:{}|room not found", msg.fromUser, msg.to)
    }
  }
}

object RoomMasterActor {
  def props(system: ActorSystem) = Props(new RoomMasterActor(system))
}

