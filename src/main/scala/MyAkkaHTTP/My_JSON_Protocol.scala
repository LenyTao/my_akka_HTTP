package MyAkkaHTTP

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import MyAkkaHTTP.Movie_Library._

trait My_JSON_Protocol extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object FilmJsonFormat extends RootJsonFormat[Film] {
    override def write(obj: Film) = JsObject(
      "name" -> JsString(obj.name),
      "id" -> JsNumber(obj.id)
    )

    override def read(json: JsValue): Film = {
      json.asJsObject.getFields("name", "id") match {
        case Seq(JsString(name), JsNumber(id)) => new Film(name, id.toLong)
        case _ => throw new DeserializationException("Film excepted")
      }
    }
  }


}
