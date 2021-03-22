package MyAkkaHTTP

object Movie_Library {

  final case class Film(name: String, id: Long)

  var listWithFilms: Set[Film] = Set.empty

  def addInLibrary(film: Film) = {
    listWithFilms = listWithFilms.+(film)
  }

  def removeFromLibrary(film: String) = {
    listWithFilms = listWithFilms.filterNot(_.name.equals(film))
  }

  def getLibrary(): String = {
    listWithFilms
      .map(
        x => ("<h1><li>" + x.id.toString + ". " + x.name + "<br></li></h1>")
      ).mkString
  }
}
