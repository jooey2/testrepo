package service

import com.google.inject.Inject
import connector.GithubConnector
import model._
import scala.concurrent.{ExecutionContext, Future}

class HomeService @Inject()(githubConnector: GithubConnector){

  def getData(username : String)(implicit ec : ExecutionContext) : Future[GithubUserModel] = {
        for{
          allRepoData <- getRepo(username)
          info <- getInfo(username)
        } yield GithubUserModel(username, allRepoData,info)
      }

  def getRepo (username : String) (implicit ec : ExecutionContext): Future[UsersRepoModel]= {
    val  allRepoData = githubConnector.getUsersRepo(username)
    for{
      repoData <- allRepoData
      commitN <- allRepoData.map{ard =>
        Future.sequence(ard.map{x =>
          getCommitN(x)
        })
        }
      c<-commitN

    }
      yield UsersRepoModel(repoData,c)
  }

  def getCommitN (repoData: RepoData)(implicit ec : ExecutionContext): Future[Int] ={
    githubConnector.getContributors(repoData.contributors_url).map{
      _.foldRight(0)((a,b) => a.contributions+b)
    }
  }

  def getInfo(username : String) (implicit ec : ExecutionContext) : Future [UserInfoModel] ={
    githubConnector.getUsersInfo(username)
  }


}
