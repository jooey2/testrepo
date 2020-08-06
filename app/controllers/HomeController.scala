package controllers

import javax.inject._
import play.api.mvc._
import service.HomeService

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

object NoSuchRepoException extends Exception

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

  def index2(username : String):Action[AnyContent] = Action.async{implicit request: Request[AnyContent]=>
    homeService.getData(username).map{data =>
      Ok(views.html.index(data))
    }
  }

  def repoDir(username : String, repoName: String):Action [AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val repoData = homeService.getRepo(username, repoName).map {
      case Some(value) => value
      case None => throw NoSuchRepoException
    }
    for {
      rd <- repoData
      repoContent <- homeService.getRepoContent(username,rd)
    } yield Ok(views.html.repoDir(username, rd, repoContent))
  }

  def repoDir2(username : String, repoName : String, path : String):Action [AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val repoData = homeService.getRepo(username, repoName).map {
      case Some(value) => value
      case None => throw NoSuchRepoException
    }
    for {
      rd <- repoData
      repoContent <- homeService.getRepoContent(username,rd,path)
    } yield Ok(views.html.repoDir(username,rd, repoContent))
  }


  def openFile(username: String, repoName : String,path : String ):Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val repoData = homeService.getRepo(username,repoName).map{
      case Some(value) => value
      case None => throw NoSuchRepoException
    }
    for{
      rd<-repoData
      file <- homeService.getFile(username,rd,path)
    }yield Ok(views.html.fileContents(username,file))

  }
}


