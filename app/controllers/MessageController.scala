package controllers

import javax.inject.Singleton

import play.api.mvc.{Action, Controller}

/**
  * Created by daz on 04/08/2016.
  */
@Singleton
class MessageController extends Controller {
  
  def getMessageById(id: String) = Action {
    Ok(id + ": this is a message")
  }
  
  def sendPrivateMessage(id: String) = Action {
    Ok("Sending a private message to " + id)
  }
  
  def sendPublicMessage(id: String) = Action {
    Ok("Sending a public message to: " + id)
  }
  
  def deleteMessage(id: String) = Action {
    Ok("Deleting message id " + id)
  }
  
}
