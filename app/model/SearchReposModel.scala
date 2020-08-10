package model

import play.api.libs.json.{Format, Json}


case class SearchReposModel(items : List[SearchReposResult])

object SearchReposModel{
  implicit val format: Format[SearchReposModel] = Json.format[SearchReposModel]
}

case class SearchReposResult(name : String, full_name : String){
  def getUser: String = {
    full_name.split("/").head
  }
}

object SearchReposResult{
  implicit val format : Format[SearchReposResult] = Json.format[SearchReposResult]
}

