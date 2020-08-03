package model

import play.api.libs.json.{Format, Json}

case class UsersRepoModel(allRepoData : List[RepoData], commitNs : List[Int])

object UsersRepoModel{
  implicit val format: Format[UsersRepoModel] = Json.format[UsersRepoModel]
}


case class RepoData(name : String, contributors_url : String){
}

object RepoData{
  implicit val format: Format[RepoData]= Json.format[RepoData]
}

case class Contributor(contributions : Int)

object Contributor{
  implicit val format: Format[Contributor] = Json.format[Contributor]
}