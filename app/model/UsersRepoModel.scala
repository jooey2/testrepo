package model

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json._
import play.api.libs.json.Reads._

case class UsersRepoModel(allRepoData : List[RepoData], commitNs : List[Int])

object UsersRepoModel{
  implicit val format: Format[UsersRepoModel] = Json.format[UsersRepoModel]
}


case class RepoData(name : String, contributors_url : String, contents_url : String)


object RepoData{
  implicit val format: Format[RepoData]= Json.format[RepoData]
}

case class Contributor(contributions : Int)

object Contributor{
  implicit val format: Format[Contributor] = Json.format[Contributor]
}

case class RepoContent(name : String, contentType : String)

object RepoContent{
  val repoContentReads : Reads[RepoContent] = (
    (__ \ "name").read[String] and
    (__ \ "type").read[String]
    ) (RepoContent.apply _)
  implicit val format: Format[RepoContent] = Format(repoContentReads,Json.writes[RepoContent])

}





