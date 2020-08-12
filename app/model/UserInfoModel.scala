package model


import play.api.libs.json.{Format, Json}


case class UserInfoModel(name : Option[String], location : Option[String], email : Option[String]){
}

object UserInfoModel{
  implicit val format: Format[UserInfoModel] = Json.format[UserInfoModel]
}




