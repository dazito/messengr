package actors

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.persistence.PersistentActor
import commands.NewMessageCommand
import events.RejectedMessageEvent
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
    case newMessageCommand: NewMessageCommand => {
      log.info("uuid{} | Yey! Persistent actor received a NewMessageCommand!", newMessageCommand.uuid)
      persist(Json.toJson(newMessageCommand)) {
        event => log.info("uuid:{} | Event persisted", newMessageCommand.uuid)
          // 200 is OK code
          clientActor ! 200
      }
    }
    case msg: String => log.info("Got a string - Error? Lets see: {}", msg)
    case msg: RejectedMessageEvent => {
      log.info("Received a RejectedMessageEvent - Body: {}", msg.body)
      clientActor ! 400
    }
  }
  
  override def receiveRecover: Receive = {
    case "" => updateState("")
  }
}

object MessagePersistentActor {
  def props(actorRef: ActorRef, userId: String) = Props(new MessagePersistentActor(actorRef, userId))
}