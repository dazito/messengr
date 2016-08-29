package json

/**
  * Created by daz on 28/08/2016.
  */

import play.api.libs.json._
import play.api.libs.json.Writes._

case class Message(from: String, to: String, text: String, timestamp: Long, uuid: String)

object Message {
  implicit val messageWrites = new Writes[Message] {
    def writes(message: Message) = Json.obj(
      "from" -> message.from,
      "to" -> message.to,
      "text" -> message.text,
      "uuid" -> message.uuid,
      "timestamp" -> message.timestamp
    )
  }
}

