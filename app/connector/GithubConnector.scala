package connector

import com.google.inject.Inject
import model.{Contributor, File, RepoContent, RepoData, SearchReposModel, SearchUsersModel, UserInfoModel}
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

  def getRepoContent(url : String)(implicit ec : ExecutionContext) : Future[List[RepoContent]] = {
    ws.url(url).addHttpHeaders(("Authorization", "Basic ZDZmODg4MmM4YjRlMjc5MjAyM2ZiYmNkNjA1Y2Y5ZTYxYjI5MjIxNA=="))
      .get.map{_.json.as[List[RepoContent]]}
  }

  def getRepoContent(username: String, repoName : String )(implicit  ec : ExecutionContext) : Future [List[RepoContent]] ={
    ws.url(s"https://api.github.com/repos/$username/$repoName/contents").addHttpHeaders(("Authorization", "Basic ZDZmODg4MmM4YjRlMjc5MjAyM2ZiYmNkNjA1Y2Y5ZTYxYjI5MjIxNA=="))
      .get.map{_.json.as[List[RepoContent]]}
  }
  def getDirContent(username : String,repoName : String, path : String )(implicit ec : ExecutionContext) : Future [List[RepoContent]] = {
    ws.url(s"https://api.github.com/repos/$username/$repoName/contents/$path").addHttpHeaders(("Authorization", "Basic ZDZmODg4MmM4YjRlMjc5MjAyM2ZiYmNkNjA1Y2Y5ZTYxYjI5MjIxNA=="))
          .get.map{_.json.as[List[RepoContent]]}
  }

  def getFile(username : String,repoName : String, path : String )(implicit ec : ExecutionContext) : Future [File] = {
    ws.url(s"https://api.github.com/repos/$username/$repoName/contents/$path").addHttpHeaders(("Authorization", "Basic ZDZmODg4MmM4YjRlMjc5MjAyM2ZiYmNkNjA1Y2Y5ZTYxYjI5MjIxNA=="))
      .get.map{_.json.as[File]}
  }

  def searchUsers (query : String)(implicit ec : ExecutionContext) : Future [SearchUsersModel] = {
    ws.url(s"https://api.github.com/search/users?q=$query").addHttpHeaders(("Authorization", "Basic ZDZmODg4MmM4YjRlMjc5MjAyM2ZiYmNkNjA1Y2Y5ZTYxYjI5MjIxNA=="))
      .get.map{_.json.as[SearchUsersModel]}
  }


  def searchRepos (query : String)(implicit ec : ExecutionContext) : Future [SearchReposModel] = {
    ws.url(s"https://api.github.com/search/Repositories?q=$query").addHttpHeaders(("Authorization", "Basic ZDZmODg4MmM4YjRlMjc5MjAyM2ZiYmNkNjA1Y2Y5ZTYxYjI5MjIxNA=="))
      .get.map{_.json.as[SearchReposModel]}
  }


}
