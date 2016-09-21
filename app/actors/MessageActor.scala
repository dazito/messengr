package actors

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import json.{IncomingMessage, OutgoingMessage}
import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json.{JsResult, Json}

/**
  * Created by daz on 06/08/2016.
  */
class MessageActor(clientActor: ActorRef, userId: String) extends Actor with ActorLogging{

  var persistentActor: ActorRef = _
  
  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    persistentActor = context.system.actorOf(MessagePersistentActor.props(self, userId))
    super.preStart()
  }
  
  override def receive: Receive = {
    case msg: IncomingMessage => {
  
      val isUserVerified = verifyDestination(msg.to)
  
      if(isUserVerified) {
        persistentActor ! msg
      }
      else {
        clientActor ! OutgoingMessage("401", "Unauthorized", DateTime.now().getMillis)
      }
    }
      
    case msg: Any => {
      log.info("case _ => unhandled")
      unhandled(msg)
    }
  }
  
  // TODO: Implement
  def verifyDestination(userId: String): Boolean = {
    false
  }
}


object MessageActor {
  def props(clientActor: ActorRef, userId: String) = Props(new MessageActor(clientActor, userId))
}