package service

import com.google.inject.Inject
import connector.GithubConnector
import model._
import scala.concurrent.{ExecutionContext, Future}

class RepoDoesNotExistException extends Exception

class HomeService @Inject()(githubConnector: GithubConnector){

  def getData(username : String)(implicit ec : ExecutionContext) : Future[GithubUserModel] = {
        for{
          allRepoData <- getUsersRepo(username)
          info <- getInfo(username)
        } yield GithubUserModel(username, allRepoData,info)
      }

  private def getUsersRepo (username : String) (implicit ec : ExecutionContext): Future[UsersRepoModel]= {
    for{
      allRepoData <- githubConnector.getUsersRepo(username)
      commitN <- getCommitN(allRepoData)
    }
      yield UsersRepoModel(allRepoData,commitN)
  }


  private def getCommitN (allRepoData: List[RepoData])(implicit ec : ExecutionContext): Future[List[Int]] ={
    Future.sequence(allRepoData.map {repoData =>
      githubConnector.getContributors(repoData.contributors_url).map {
        _.foldRight(0)((a, b) => a.contributions + b)
      }
    })
  }

  private def getInfo(username : String) (implicit ec : ExecutionContext) : Future [UserInfoModel] ={
    githubConnector.getUsersInfo(username)
  }


  def getRepo(username : String, repoName : String)(implicit ec : ExecutionContext): Future[Option[RepoData]] ={
    githubConnector.getUsersRepo(username).map{ userRepoData =>
      userRepoData.find(_.name == repoName)
    }
  }

  def getRepoContent(username: String, repoData : RepoData)(implicit ec : ExecutionContext) : Future[List[RepoContent]] = {
    githubConnector.getRepoContent(username,repoData.name)
  }

  def getRepoContent(username:String, repoName : String) (implicit  ec : ExecutionContext) : Future[List[RepoContent]]  = {
    githubConnector.getRepoContent(username,repoName)
  }

  def getDirContent(username: String, repoData : RepoData,path : String)(implicit ec : ExecutionContext) : Future[List[RepoContent]] = {
    githubConnector.getDirContent(username,repoData.name,path)
  }

  def getFile(username: String, repoData : RepoData,path : String)(implicit ec : ExecutionContext) : Future[File] = {
    githubConnector.getFile(username,repoData.name,path)
  }

  def searchUsers(query: String)(implicit ec : ExecutionContext) : Future[SearchUsersModel] = {
    githubConnector.searchUsers(query)
  }

  def searchRepos(query: String)(implicit ec : ExecutionContext) : Future[SearchReposModel] = {
    githubConnector.searchRepos(query)
  }





}
