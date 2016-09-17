package commands

import play.api.libs.json.{Json, Writes}

/**
  * Created by daz on 07/08/2016.
  */
case class NewMessageCommand(fromUser: String, to: String, text: String, uuid: String, timestamp: Long)
case class RegisterUserCommand(uuid: String, name: String, phonenumber: String, email: String)
case class CreateRoomCommand(name: String, userId: String, isPrivate: Boolean)
case class ListPublicRoomsCommand()
case class UserJoinRoomCommand(roomId: String, userId: String)
case class UserLeftRoomCommand(roomId: String, userId: String)

object NewMessageCommand {
  implicit val messageWrites = new Writes[NewMessageCommand] {
    def writes(message: NewMessageCommand) = Json.obj(
      "fromUser" -> message.fromUser,
      "toUser" -> message.to,
      "text" -> message.text,
      "uuid" -> message.uuid,
      "timestamp" -> message.timestamp
    )
  }
}

object CreateRoomCommand {
  implicit val messageWrites = new Writes[CreateRoomCommand] {
    def writes(newRoomCommand: CreateRoomCommand) = Json.obj(
      "name" -> newRoomCommand.name,
      "userId" -> newRoomCommand.userId,
      "isPrivate" -> newRoomCommand.isPrivate
    )
  }
}