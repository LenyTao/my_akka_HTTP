package MyAkkaHTTP

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http

import scala.io.StdIn

object App {

  def main(args: Array[String]): Unit = {
    startServer()
  }

  def startServer() = {
    implicit val system = ActorSystem(Behaviors.empty, "HttpServer")
    implicit val executionContext = system.executionContext

    val route = (new Routes).routes

    val bindingFuture = Http().newServerAt("localhost", 5000).bind(route)

    println(s"Server online at http://localhost:5000/\n Press any button to stop...")

    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
