package json

/**
  * Created by daz on 28/08/2016.
  */

import play.api.libs.json._
import play.api.libs.json.Writes._

case class OutgoingMessage(code: Int, message: String)

object OutgoingMessage {
  implicit val format = Json.format[OutgoingMessage]
}

case class IncomingMessage(to: String = "No receiver", text: String = "Not defined")

object IncomingMessage {
  implicit val format = Json.format[IncomingMessage]
}




case class IncomingRoomMessage(text: String = "Not defined")

object IncomingRoomMessage {
  implicit val format = Json.format[IncomingRoomMessage]
}

case class OutgoingRoomMessage(roomId: String = "No receiver", roomName: String, eventType: String, text: String = "Not defined")

object OutgoingRoomMessage {
  implicit val format = Json.format[OutgoingRoomMessage]
}


