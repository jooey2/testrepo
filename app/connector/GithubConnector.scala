package connector

import com.google.inject.Inject
import model._
import play.api.Configuration
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class GithubConnector @Inject()(config : Configuration)(ws: WSClient) {

  def getUsersRepo(username: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, List[RepoData]]] = {
    ws.url(s"https://api.github.com/users/$username/repos").addHttpHeaders(("Authorization",config.get[String]("token")))
      .get.map { data =>
      data.status match {
        case 200 => Right(data.json.as[List[RepoData]])
        case status: Int => Left(new UnknownError(status))
      }
    }
  }


  def getUsersInfo(username: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, UserInfoModel]] = {
    ws.url(s"https://api.github.com/users/$username").addHttpHeaders(("Authorization", config.get[String]("token")))
      .get.map { data =>
      data.status match {
        case 200 => Right(data.json.as[UserInfoModel])
        case status: Int => Left(new UnknownError(status))
      }
    }
  }

  def getContributors(url: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, List[Contributor]]] = {
    ws.url(url).addHttpHeaders(("Authorization", config.get[String]("token")))
      .get.map { data =>
      data.status match {
        case 200 => Right(data.json.as[List[Contributor]])
        case status: Int => Left(new UnknownError(status))
      }
    }
  }

  def getRepoContent(username: String, repoName: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, List[RepoContent]]] = {
    ws.url(s"https://api.github.com/repos/$username/$repoName/contents").addHttpHeaders(("Authorization", config.get[String]("token")))
      .get.map { data =>
      data.status match {
        case 200 => Right(data.json.as[List[RepoContent]])
        case status: Int => Left(new UnknownError(status))
      }
    }
  }


  def getDirContent(username: String, repoName: String, path: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, List[RepoContent]]] = {
    ws.url(s"https://api.github.com/repos/$username/$repoName/contents/$path").addHttpHeaders(("Authorization", config.get[String]("token")))
      .get.map { data =>
      data.status match {
        case 200 => Right(data.json.as[List[RepoContent]])
        case status: Int => Left(new UnknownError(status))
      }
    }
  }

  def getFile(username: String, repoName: String, path: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, File]] = {
    ws.url(s"https://api.github.com/repos/$username/$repoName/contents/$path").addHttpHeaders(("Authorization", config.get[String]("token")))
      .get.map { data =>
      data.status match {
        case 200 => Right(data.json.as[File])
        case status: Int => Left(new UnknownError(status))
      }
    }
  }

  def searchUsers(query: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, SearchUsersModel]] = {
    ws.url(s"https://api.github.com/search/users?q=$query").addHttpHeaders(("Authorization", config.get[String]("token")))
      .get.map { data =>
      data.status match {
        case 200 => Right(data.json.as[SearchUsersModel])
        case status: Int => Left(new UnknownError(status))
      }
    }
  }

  def searchRepos(query: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, SearchReposModel]] = {
    ws.url(s"https://api.github.com/search/repositories?q=$query").addHttpHeaders(("Authorization", config.get[String]("token")))
      .get.map { data =>
      data.status match {
        case 200 => Right(data.json.as[SearchReposModel])
        case status: Int => Left(new UnknownError(status))
      }
    }
  }


}
