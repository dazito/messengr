package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}
import commands.UserJoinRoomCommand
import events.JoinedChannelEvent
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
    case msg: Any => unhandled(msg)
  }
  
  def processUserJoinRoomCommand(msg: UserJoinRoomCommand): Unit = {
    log.info("User {} joined channel {}", msg.userId, msg.roomId)
    
    // Notify all users in the channel
    for((key, userActor) <- users) userActor ! new JoinedChannelEvent(msg.userId, msg.roomId)
    
    // Add the user to the list of user in the channel
    users += (msg.userId -> sender())
  }
  
  
}

object RoomActor {
  def props(sender: ActorRef, roomId: String, userId: String, isPrivate: Boolean) = Props(new RoomActor(sender, roomId, userId, isPrivate))
}