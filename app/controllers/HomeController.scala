package controllers

import javax.inject._
import play.api.Configuration
import play.api.mvc._
import service.HomeService

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

object NoSuchRepoException extends Exception

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, homeService: HomeService)(config : Configuration)(implicit val ec: ExecutionContext) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def formPage(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.formPage())
  }

  def index(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val username = request.body.asFormUrlEncoded.map { args =>
      args("username").head
    }.getOrElse(Redirect(routes.HomeController.formPage())).toString
    homeService.getData(username).map {
      case Right(data) => Ok(views.html.index(data))
      case Left(error) => Ok(error.getMessage)
    }
  }

  def index2(username: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    homeService.getData(username).map {
      case Right(data) => Ok(views.html.index(data))
      case Left(error) => Ok(error.getMessage)
    }
  }


  def repoDir(username: String, repoName: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    homeService.getRepoContent(username, repoName).map {
      case Right(content) => Ok(views.html.repoDir(username, repoName, content))
      case Left(error) => Ok(error.getMessage)
    }
  }

  def openDir(username: String, repoName: String, path: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val repoData = homeService.getRepo(username, repoName).map {
      case Right(value) => value
      case _ => throw NoSuchRepoException
    }
    for {
      rd <- repoData
      repoContent <- homeService.getDirContent(username, rd, path)
    } yield {
      repoContent match {
        case Right(repoContent) => Ok(views.html.directory(username, rd, repoContent, path))
        case Left(error) => Ok(error.getMessage)
      }
    }
  }


  def openFile(username: String, repoName: String, path: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val repoData = homeService.getRepo(username, repoName).map {
      case Right(value) => value
      case _ => throw NoSuchRepoException
    }
    for {
      rd <- repoData
      file <- homeService.getFile(username, rd, path)
    } yield {
      file match {
        case Right(file) => Ok(views.html.fileContents(username, rd, file))
        case Left(error) => Ok(error.getMessage)
      }
    }

  }

  def searchUsers(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val query = request.body.asFormUrlEncoded.map { args =>
      args("searchUsername").head
    }.getOrElse(Redirect(routes.HomeController.formPage())).toString
    homeService.searchUsers(query).map {
      case Right(data) => Ok(views.html.searchUsersResults(data))
      case Left(error) => Ok(error.getMessage)
    }
  }

  def searchRepos(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val query = request.body.asFormUrlEncoded.map { args =>
      args("searchRepo").head
    }.getOrElse(Redirect(routes.HomeController.formPage())).toString
    homeService.searchRepos(query).map {
      case Right(data) => Ok(views.html.searchReposResults(data))
      case Left(error) => Ok(error.getMessage)
    }
  }
}


