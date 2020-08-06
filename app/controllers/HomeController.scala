package controllers

import javax.inject._
import model.GithubUserModel
import play.api._
import play.api.mvc._
import service.HomeService
import model.UsersRepoModel._

import scala.concurrent.{ExecutionContext, Future}
import play.api.data._
import play.api.data.Form._
import play.api.data.Forms._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, homeService : HomeService)(implicit val ec : ExecutionContext) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def formPage(): Action[AnyContent] = Action {implicit request: Request[AnyContent] =>
    Ok(views.html.formPage())
  }

  def index(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val username  = request.body.asFormUrlEncoded.map{ args =>
      args("username").head
    }.getOrElse(Redirect(routes.HomeController.formPage())).toString
        homeService.getData(username).map {data =>
          Ok(views.html.index(data))
        }
  }


}


