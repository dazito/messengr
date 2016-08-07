package controllers

import javax.inject.Inject

import actors.WebSocketActor
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.streams.ActorFlow
import play.api.mvc.{Action, Controller, WebSocket}

/**
  * Created by daz on 06/08/2016.
  */
class WebSocketController @Inject() (implicit system: ActorSystem, materializer: Materializer) extends Controller{
  
  def createWebSocket = WebSocket.accept[String, String] {
    request => ActorFlow.actorRef(out => WebSocketActor.props(out))
  }
  
}
