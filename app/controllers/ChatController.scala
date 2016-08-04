package controllers

import javax.inject.Singleton

import play.api.mvc.{Action, Controller}

/**
  * Created by daz on 04/08/2016.
  */
@Singleton
class ChatController extends Controller {
  
  def chatList = Action {
    Ok("chat list")
  }
  
  def chatPrivateList = Action {
    Ok("chat private list")
  }
  
  def chatPublicList = Action {
    Ok("chat public list")
  }
  
  def getUserDetails(id: String) = Action {
    Ok(id + ": user details")
  }
  
}
