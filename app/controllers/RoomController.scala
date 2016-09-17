package controllers

import java.util.UUID
import javax.inject.{Inject, Singleton}

import actors.RoomMasterActor
import akka.actor.ActorSystem
import akka.stream.Materializer
import commands.CreateRoomCommand
import play.api.mvc.{Action, Controller}

/**
  * Created by daz on 04/08/2016.
  */
@Singleton
class RoomController @Inject() (implicit system: ActorSystem, materializer: Materializer) extends Controller {
  
 
  def roomList = Action {
    Ok("chat list")
  }
  
  def roomPrivateList = Action {
    Ok("chat private list")
  }
  
  def roomPublicList = Action {
    Ok("chat public list")
  }
  
  def getUserDetails(id: String) = Action {
    Ok(id + ": user details")
  }
  
  def createRoom(name: String) = Action {
    val actor = system.actorSelection("/user/" + RoomMasterActor.getClass.getSimpleName)
    // TODO: Proper implementation - Use the real user ID. Parse the json sent by the client with the channel properties
    // TODO: Handle error cases and make it async
    actor ! new CreateRoomCommand(name, UUID.randomUUID().toString, false)
    
    Ok()
  }
  
}
