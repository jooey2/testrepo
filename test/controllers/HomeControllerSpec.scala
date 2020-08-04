package controllers

import model.{GithubUserModel, UserInfoModel, UsersRepoModel}
import org.mockito.{ArgumentMatcher, ArgumentMatchers}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import service.HomeService
import org.mockito.Mockito._
import play.api.test.CSRFTokenHelper._

import scala.concurrent.{ExecutionContext, Future}

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with MockitoSugar with Injecting {


  private val mockHomeService = mock[HomeService]

  //In unit tests we're not expecting concurrent users, so it's not considered bad to use this here
  private implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global


  "HomeController POST" should {

    "Render the form page with a users data provided" in {
      val response = GithubUserModel("jxr227", UsersRepoModel(List.empty, List.empty), UserInfoModel("jxr227", "Telford", "jamesrees94@live.co.uk"))

      when(mockHomeService.getData(ArgumentMatchers.eq("jxr227"))(ArgumentMatchers.any())).thenReturn(Future.successful(response))

      val controller = new HomeController(stubControllerComponents(), mockHomeService)
      val home = controller.index().apply(FakeRequest(POST, "/github/users").withFormUrlEncodedBody("username" -> "jxr227").withCSRFToken)

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("jamesrees94@live.co.uk")
    }

  }

  "HomeController GET" should {

    "render the form page from a new instance of controller" in {
      val controller = new HomeController(stubControllerComponents(), mockHomeService)
      val home = controller.formPage().apply(FakeRequest(GET, "/github/users").withCSRFToken)

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Username")
    }

    "render the form page page from the application" in {
      val controller = inject[HomeController]
      val home = controller.formPage().apply(FakeRequest(GET, "/github/users").withCSRFToken)

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Username")
    }

    "render the form page from the router" in {
      val request = FakeRequest(GET, "/github/users")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Username")
    }
  }
}
