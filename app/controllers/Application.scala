package controllers

import javax.inject._
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.db._

import models._

// injects Play's default ActorSystem into our controller
@Singleton
class Application @Inject()(actorSystem: ActorSystem) extends Controller {

  // creates actor of type chat described above
  val chat = actorSystem.actorOf(Props[Chat], "chat")

  /*
   Specifies how to wrap an out-actor that will represent
   WebSocket connection for a given request.
  */
  def socket = WebSocket.acceptWithActor[String, String] {
    (request: RequestHeader) =>
    (out: ActorRef) =>
    Props(new ClientActor(out, chat))
  }

  def getAllMessages(): ArrayBuffer[String] = {
    var currMessages = ArrayBuffer[String]()
    DB.withConnection { conn =>
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("SELECT text from messages order by id")
      while (rs.next()) {
        currMessages += rs.getString("text")
      }
    }

    currMessages
  }

  def index = Action {
    implicit val timeout = Timeout(5 seconds)
    //val futureCurrMessages = chat ? AskCurrMessages
    //val currMessages = Await.result(futureCurrMessages, timeout.duration).asInstanceOf[ArrayBuffer[String]].toArray
    val currMessages = getAllMessages().toArray
    Ok(views.html.index("Hello.", currMessages))
  }
}
