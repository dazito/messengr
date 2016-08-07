package actors.module

import javax.inject.Inject

import actors.ChatRoomActor
import akka.actor.ActorSystem

/**
  * Created by daz on 06/08/2016.
  *
  * Define here the actors required at the application start up
  */

class MessengrStartUpActors @Inject()(system: ActorSystem) {
  system.actorOf(ChatRoomActor.props, name = ChatRoomActor.getClass.getSimpleName)
}

