package events

/**
  * Created by daz on 28/08/2016.
  */
case class NewMessageEvent(from: String, to: String, text: String, timestamp: Long, uuid: String)
case class RejectedMessageEvent(body: String)
case class JoinedChannelEvent(userId: String, channelId: String)
case class LeftChannelEvent(userId: String, channelId: String)
