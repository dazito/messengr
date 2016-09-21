package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}
import commands.{NewMessageCommand, UserJoinRoomCommand}
import events.{JoinedChannelEvent, NewMessageEvent}
import json.IncomingRoomMessage

import scala.collection.mutable.{Map => MutableMap}

/**
  * Created by daz on 06/08/2016.
  */
class RoomActor(clientActor: ActorRef, name: String, userId: String, isPrivate: Boolean) extends PersistentActor with ActorLogging{
  
  var users: MutableMap[String, ActorRef] = MutableMap()
  
  override def receiveRecover: Receive = {
    case SnapshotOffer(_, snap: MutableMap[String, ActorRef]) => users = snap
  }
  
  override def persistenceId: String = getClass.getSimpleName
    
  override def receiveCommand: Receive = {
    case msg: IncomingRoomMessage =>  log.warning("IncomingRoomMessage message {}", msg.text)
    case msg: UserJoinRoomCommand => processUserJoinRoomCommand(msg)
    case msg: NewMessageCommand => processNewMessageCommand(msg)
    case msg: Any => unhandled(msg)
  }
  
  def processUserJoinRoomCommand(msg: UserJoinRoomCommand): Unit = {
    log.info("userId:{}|roomId:{}|user joined command", msg.userId, msg.roomId)
    
    // Notify all users in the channel
    for((key, userActor) <- users) userActor ! new JoinedChannelEvent(msg.userId, msg.roomId)
    
    // Add the user to the list of user in the channel
    users += (msg.userId -> sender())
    
    // TODO: Implement the persist logic
  }
  
  def processNewMessageCommand(msg: NewMessageCommand) = {
    log.info("userId:{}|roomId:{}|process new message command", msg.fromUser, msg.to)
    
    // Send the message to all room members
    for((key, userActor) <- users) userActor ! new NewMessageEvent(msg.fromUser, msg.to, msg.text, msg.uuid, msg.timestamp)
  }
  
  
}

object RoomActor {
  def props(sender: ActorRef, roomId: String, userId: String, isPrivate: Boolean) = Props(new RoomActor(sender, roomId, userId, isPrivate))
}