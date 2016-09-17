package controllers

import javax.inject.Inject

import actors.{MessageActor, RoomActor, RoomClientActor, RoomMasterActor}
import akka.actor.ActorSystem
import akka.stream.Materializer
import json.{IncomingMessage, IncomingRoomMessage, OutgoingMessage, OutgoingRoomMessage}
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc.{Controller, WebSocket}

/**
  * Created by daz on 06/08/2016.
  */
class WebSocketController @Inject() (implicit system: ActorSystem, materializer: Materializer) extends Controller{
  
  implicit val inMessageFormat = Json.format[IncomingMessage]
  implicit val outMessageFormat = Json.format[OutgoingMessage]
  implicit val inRoomMessageFormat = Json.format[IncomingRoomMessage]
  implicit val outRoomMessageFormat = Json.format[OutgoingRoomMessage]
  
  implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[IncomingMessage, OutgoingMessage]
  implicit val roomFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[IncomingRoomMessage, OutgoingRoomMessage]
  
  def messageWebSocket(userId: String) = WebSocket.accept[IncomingMessage, OutgoingMessage] {
    request => ActorFlow.actorRef(out => MessageActor.props(out, userId))
  }
  
  def roomWebSocket(userId: String) = WebSocket.accept[IncomingRoomMessage, OutgoingRoomMessage] {
    request => ActorFlow.actorRef(out => RoomClientActor.props(out, userId))
  }
  
}
