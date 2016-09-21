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
      log.info("uuid{}|fromUser:{}|to:{}|timestamp:{}|received a NewMessageCommand!",
        newMessageCommand.uuid, newMessageCommand.fromUser, newMessageCommand.to, newMessageCommand.timestamp)
      persist(Json.toJson(newMessageCommand)) {
        event =>
          log.info("uuid{}|fromUser:{}|to:{}|timestamp:{}|event persisted",
            newMessageCommand.uuid, newMessageCommand.fromUser, newMessageCommand.to, newMessageCommand.timestamp)
          // 200 is OK code
          clientActor ! 200
      }
    }
    case msg: RejectedMessageEvent => {
      log.info("fromUser:{}|rejected message event - body: {}",
        msg.from, msg.body)
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