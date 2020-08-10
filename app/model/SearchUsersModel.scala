package model

import play.api.libs.json.{Format, Json}


case class SearchUsersModel(items : List[SearchUserResult])

object SearchUsersModel{
  implicit val format: Format[SearchUsersModel] = Json.format[SearchUsersModel]
}

case class SearchUserResult(login : String)

object SearchUserResult{
  implicit val format : Format[SearchUserResult] = Json.format[SearchUserResult]
}

