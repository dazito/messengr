package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import events.RejectedMessageEvent
import json.Message
import play.api.libs.json.{JsResult, Json}

/**
  * Created by daz on 06/08/2016.
  */
class WebSocketActor(clientActor: ActorRef, userId: String) extends Actor with ActorLogging{

  var messagePersistentActor: ActorRef = _
  
  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    messagePersistentActor = context.system.actorOf(MessagePersistentActor.props(self, userId))
    super.preStart()
  }
  
  override def receive: Receive = {
    case msg: String => {
      log.info("Received from socket: {}", msg)
      
      implicit val userReads = Json.reads[Message]
      
      val parsedMessage: JsResult[Message] = Json.fromJson(Json.parse(msg))
      
      val message = parsedMessage.getOrElse {
        log.warning("Invalid json: {}", msg)
        RejectedMessageEvent(msg)
      }
      
      clientActor ! msg
      messagePersistentActor ! message
    }
    case msg: _ => {
      log.info("case _")
      unhandled(msg)
    }
  }
}


object WebSocketActor {
  def props(clientActor: ActorRef, userId: String) = Props(new WebSocketActor(clientActor, userId))
}