package actors

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
  * Created by daz on 06/08/2016.
  */
class WebSocketActor(actor: ActorRef) extends Actor with ActorLogging{
  override def receive: Receive = {
    case msg: String => {
      log.info("Received from socket: {}", msg)
      actor ! msg
    }
    case _ => log.info("case _")
  }
}


object WebSocketActor {
  def props(out: ActorRef) = Props(new WebSocketActor(out))
}