package MyAkkaHTTP

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import MyAkkaHTTP.Movie_Library._
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

class Routes extends My_JSON_Protocol {
  implicit val system = ActorSystem(Behaviors.empty, "HttpServer")
  implicit val executionContext = system.executionContext

  def routes: Route = {
    val mainPage =
      """  <html>
        | <head>
        |  <meta charset="utf-8">
        |  <title>Main Page</title>
        | </head>
        | <body>
        |<center>
        |<h1>
        |Hello, my application is a personal movie list, where you can add your favorite movies, delete them and of course see your entire collection.
        |</h1>
        |</center>
        |  <form>
        |  <center>
        |    <p><a href="addFilm">Add the first movie to your collection</a></p>
        |  </center>
        |  </form>
        | </body>
        |</html>
        |""".stripMargin
    concat(
      path("main") {
        get {
          complete(
            HttpEntity(
              ContentTypes.`text/html(UTF-8)`, mainPage
            )
          )
        }
      } ~
        path("addFilm") {
          val myAddPage =
            s"""  <html>
               | <head>
               |  <meta charset="utf-8">
               |  <title>Add Form</title>
               | </head>
               | <body>
               | <center>
               |  <h1>Add a movie to your collection</h1>
               |</center>
               |  <form>
               |  <center>
               |  In order to add a movie, enter in the terminal:
               |<br>
               |<h3>
               |curl -XPOST -H "Content-Type: application/json" -d "{\\"id\\":0, \\"name\\":\\"Zorro\\"}" localhost:5000/addFilm
               |</h3>
               | <br>
               |Where Zorro - your name film for Add and 0 - id film
               |       <p><a href="removeFilm">Or remove Film</a></p>
               |   </center>
               |   <center>
               |  <h2>Your collection so far: </h2>
               |  </center>
               |  </form>
               | </body>
               |</html>
               |""".stripMargin
          get {
            complete(
              HttpEntity(
                ContentTypes.`text/html(UTF-8)`, (myAddPage + getLibrary())
              )
            )
          }
        } ~
        (path("addFilm") & post) {

          /**
           * //Как сделать пост запрос
           * //curl -XPOST -H "Content-Type: application/json" -d "{\"id\":25, \"name\":\"Das\"}" localhost:5000/addFilm
           */
          entity(as[Film]) { film =>
            Movie_Library.addInLibrary(film)
            complete(
              "POST OK"
            )
          }
        } ~
        path("removeFilm") {
          val myDelPage =
            s"""  <html>
               | <head>
               |  <meta charset="utf-8">
               |  <title>Remove Form</title>
               | </head>
               | <body>
               | <center>
               |  <h1>Remove a movie from your collection</h1>
               |</center>
               |  <form>
               |  <center>
               |  In order to delete a movie, enter in the terminal:
               |<br>
               |<h3>
               |curl -XDELETE localhost:5000/removeFilm/Zorro
               |
               |</h3>
               | <br>
               |Where Zorro - your film for Delete
               |       <p><a href="addFilm">Or add Film</a></p>
               |   </center>
               |   <center>
               |  <h2>Your collection so far: </h2>
               |  </center>
               |  </form>
               | </body>
               |</html>
               |""".stripMargin

          get {
            complete(
              HttpEntity(
                ContentTypes.`text/html(UTF-8)`, (myDelPage + getLibrary())
              )
            )
          }
        } ~
        (path("removeFilm" / Segment) & delete) { film_name =>

          /**
           * //Как сделать DEL запрос
           * //curl -XDELETE -H "Content-Type: application/json" -d "{\"id\":25, \"name\":\"Das\"}" localhost:5000/removeFilm
           */
          Movie_Library.removeFromLibrary(film_name)
          complete("DEL OK")
        }
    )
  }
}

