package connector

import com.google.inject.Inject
import model.{Contributor, RepoData, UserInfoModel}
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class GithubConnector  @Inject() (ws : WSClient){


  def getUsersRepo(username : String)(implicit ec : ExecutionContext): Future [List[RepoData]] ={
    ws.url(s"https://api.github.com/users/$username/repos").addHttpHeaders(("Authorization", "Basic ZDZmODg4MmM4YjRlMjc5MjAyM2ZiYmNkNjA1Y2Y5ZTYxYjI5MjIxNA=="))
      .get.map{_.json.as[List[RepoData]]}
  }

  def getUsersInfo(username : String)(implicit ec : ExecutionContext): Future [UserInfoModel] = {
    ws.url(s"https://api.github.com/users/$username").addHttpHeaders(("Authorization", "Basic ZDZmODg4MmM4YjRlMjc5MjAyM2ZiYmNkNjA1Y2Y5ZTYxYjI5MjIxNA=="))
      .get.map{_.json.as[UserInfoModel]}

  }

  def getContributors(url : String)(implicit ec : ExecutionContext) : Future[List[Contributor]] = {
ws.url(url).addHttpHeaders(("Authorization", "Basic ZDZmODg4MmM4YjRlMjc5MjAyM2ZiYmNkNjA1Y2Y5ZTYxYjI5MjIxNA=="))
    .get.map{_.json.as[List[Contributor]]}
  }


}
