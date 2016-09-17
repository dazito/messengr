package events

/**
  * Created by daz on 12/09/2016.
  */
object RoomEventType extends Enumeration {
  type roomEvent = Value
  val MESSAGE, JOIN, LEAVE, CREATE, DELETE, LIST = Value
}
