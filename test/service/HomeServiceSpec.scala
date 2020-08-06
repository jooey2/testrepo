package service

import connector.GithubConnector
import model.{Contributor, GithubUserModel, RepoData, UserInfoModel, UsersRepoModel}
import org.mockito.{ArgumentMatcher, ArgumentMatchers}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import service.HomeService
import org.mockito.Mockito.{mock, _}
import play.api.libs.ws.{WSClient, WSRequest}
import play.api.test.CSRFTokenHelper._
import scala.concurrent.ExecutionContext.global

import scala.concurrent.{ExecutionContext, Future}

class InvalidGetContributorsUrl extends Exception

class stubGithubConnector(ard : List[RepoData], uim : UserInfoModel, contributors : Map[String,List[Contributor]])(ws : WSClient) extends GithubConnector(ws : WSClient){
  override def getUsersRepo(username: String)(implicit ec: ExecutionContext): Future[List[RepoData]] =
    Future.successful(ard)
  override def getUsersInfo(username: String)(implicit ec: ExecutionContext): Future[UserInfoModel]  =
    Future.successful(uim)
  override def getContributors(url: String)(implicit ec: ExecutionContext): Future[List[Contributor]] =
    Future.successful(contributors.get(url) match {
      case Some(x) => x
      case None => throw new InvalidGetContributorsUrl
    })
}


class HomeServiceSpec extends PlaySpec with GuiceOneAppPerTest with MockitoSugar with Injecting {
  val ec : ExecutionContext = ExecutionContext.global
  "GET DATA" should{

    "return the correct github user model" in{
      val repo1 = new RepoData("project1","pj")
      val repo2 = new RepoData ("testrepo","tr")
      val ard = List(repo1, repo2)
      val uim = new UserInfoModel("JoeyC","Telford", "joey.chen@capgemini.com")
      val contributors = Map(
        "pj" ->                    List(4,6,7,2,9).map{
        x => new Contributor(x)
      },
      "tr" -> List(new Contributor(1)))

      val connector = new stubGithubConnector(ard,uim,contributors)(mock[WSClient])
      val service = new HomeService(connector)
      val data = await(service.getData("joey")(ec))
      data.username mustBe "joey"
      data.userInfo.fullName mustBe "JoeyC"
      data.userInfo.location mustBe "Telford"
      data.userInfo.email mustBe "joey.chen@capgemini.com"
      data.usersRepoModel.allRepoData must contain (repo1)
      data.usersRepoModel.allRepoData must contain (repo2)
      data.usersRepoModel.commitNs must contain (28)
      data.usersRepoModel.commitNs must contain (1)
    }
  }
}
