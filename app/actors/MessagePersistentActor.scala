package actors

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.persistence.PersistentActor
import events.RejectedMessageEvent
import json.Message
import play.api.libs.json.Json

/**
  * Created by daz on 27/08/2016.
  */
class MessagePersistentActor(clientActor: ActorRef, userId: String) extends PersistentActor with ActorLogging{
  override def persistenceId: String = userId
  
  var messageList: List[String]  = List.empty
  
  def updateState(message: String): Unit = {
    messageList = messageList :+ message
  }
  
  override def receiveCommand: Receive = {
    case msg: Message => {
      log.info("uuid{} | Yey! Persistent actor received an event!", msg.uuid)
      persist(Json.toJson(msg)) {
        event => log.info("uuid:{} | Event persisted", msg.uuid)
      }
    }
    case msg: String => log.info("Got a string - Error? Lets see: {}", msg)
    case msg: RejectedMessageEvent => log.info("Received a RejectedMessageEvent - Body: {}", msg.body)
  }
  
  override def receiveRecover: Receive = {
    case "" => updateState("")
  }
}

object MessagePersistentActor {
  def props(actorRef: ActorRef, userId: String) = Props(new MessagePersistentActor(actorRef, userId))
}