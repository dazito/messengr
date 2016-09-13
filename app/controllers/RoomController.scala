package controllers

import javax.inject.Singleton

import play.api.mvc.{Action, Controller}

/**
  * Created by daz on 04/08/2016.
  */
@Singleton
class RoomController extends Controller {
  
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
  
}
