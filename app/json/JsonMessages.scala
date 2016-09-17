package json

/**
  * Created by daz on 28/08/2016.
  */

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class OutgoingMessage(from: String, message: String, timestamp: Long)

object OutgoingMessage {
  implicit val writer = Json.format[OutgoingMessage]
}

case class IncomingMessage(to: String, text: String)

object IncomingMessage {
  implicit val reader = Json.format[IncomingMessage]
}




case class IncomingRoomMessage(roomId: String, text: Option[String], eventType: String)

object IncomingRoomMessage {
  implicit val reader = (
        (__ \ 'roomId ).read[String] ~
                (__ \ 'text ).readNullable[String] ~
                (__ \ 'eventType ).read[String]
        )(IncomingRoomMessage.apply _)
}

case class OutgoingRoomMessage(roomId: String, userId: String, eventType: String, text: Option[String], timestamp: Long)

object OutgoingRoomMessage {
  implicit val writer = Json.format[OutgoingRoomMessage]
}


