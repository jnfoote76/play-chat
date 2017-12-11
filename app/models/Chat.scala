package models

import akka.actor._
import scala.collection.mutable.ArrayBuffer

// our domain message protocol
case object Join
case object Leave
case object AskCurrMessages
final case class ClientSentMessage(text: String)

// Chat actor
class Chat extends Actor {
  private var currMessages = ArrayBuffer[String]()
  // initial message-handling behavior
  def receive = process(Set.empty)

  def process(subscribers: Set[ActorRef]): Receive = {
    case Join =>
      // replaces message-handling behavior by the new one
      context become process(subscribers + sender)

    case Leave =>
      context become process(subscribers - sender)

    case AskCurrMessages =>
      sender ! currMessages

    case msg: ClientSentMessage => {
      // send messages to all subscribers except sender
      this.currMessages += msg.text
      (subscribers - sender).foreach { _ ! msg }
    }
  }

  def getCurrMessages(): ArrayBuffer[String] = {
    currMessages
  }
}
