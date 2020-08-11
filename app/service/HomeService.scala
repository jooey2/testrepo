package service

import com.google.inject.Inject
import connector.GithubConnector
import model._
import scala.concurrent.{ExecutionContext, Future}

class RepoDoesNotExistException extends Exception

class HomeService @Inject()(githubConnector: GithubConnector) {

  def getData(username: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, GithubUserModel]] = {
    for {
      allRepoData <- getUsersRepo(username)
      info <- githubConnector.getUsersInfo(username)
    } yield {
      (allRepoData, info) match {
        case (Right(ard), Right(i)) =>
          Right(GithubUserModel(username, ard, i))
        case (Left(e), _) => Left(e)
        case (_, Left(e)) => Left(e)
      }
    }
  }

  private def getUsersRepo(username: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, UsersRepoModel]] = {
    for {
      allRepoData <- githubConnector.getUsersRepo(username)
      commitN <- getCommitN(allRepoData)
    }
      yield {
        (allRepoData, commitN) match {
          case (Right(ard), Right(n)) => Right(UsersRepoModel(ard, n))
          case (Left(e), _) => Left(e)
          case (_, Left(e)) => Left(e)
        }
      }
  }


  private def getCommitN(allRepoData: Either[ErrorResponse, List[RepoData]])(implicit ec: ExecutionContext): Future[Either[ErrorResponse, List[Int]]] = {
    allRepoData match {
      case Right(allRepoData) =>
        Future.sequence(allRepoData.map { repoData =>
          githubConnector.getContributors(repoData.contributors_url).map {
            case Right(x) =>
              Right(x.foldRight(0)((a, b) => a.contributions + b))
            case Left(e) => Left(e)
          }
        }).map {
          _.partition(_.isLeft) match {
            case (Nil, ints) => Right(for (Right(i) <- ints) yield i)
            case (errors, _) => Left(errors.head match { case Left(e) => e })
          }
        }
      case Left(e) => Future.successful(Left(e))
    }
  }


  private def getInfo(username: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, UserInfoModel]] = {
    githubConnector.getUsersInfo(username)
  }


  def getRepo(username: String, repoName: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, RepoData]] = {
    githubConnector.getUsersRepo(username).map {
      case Right(userRepoData) => userRepoData.find(_.name == repoName) match {
        case Some(x) => Right(x)
        case None => Left(new ErrorResponse {})
      }
      case Left(e) => Left(e)
    }
  }


  def getRepoContent(username: String, repoData: RepoData)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, List[RepoContent]]] = {
    githubConnector.getRepoContent(username, repoData.name)
  }

  def getRepoContent(username: String, repoName: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, List[RepoContent]]] = {
    githubConnector.getRepoContent(username, repoName)
  }

  def getDirContent(username: String, repoData: RepoData, path: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, List[RepoContent]]] = {
    githubConnector.getDirContent(username, repoData.name, path)
  }

  def getFile(username: String, repoData: RepoData, path: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, File]] = {
    githubConnector.getFile(username, repoData.name, path)
  }

  def searchUsers(query: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, SearchUsersModel]] = {
    githubConnector.searchUsers(query)
  }

  def searchRepos(query: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, SearchReposModel]] = {
    githubConnector.searchRepos(query)
  }


}
