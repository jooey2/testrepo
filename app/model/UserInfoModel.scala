package model


import play.api.libs.json.{Format, Json}


case class UserInfoModel(name : String, location : String, email : String){
}

object UserInfoModel{
  implicit val format: Format[UserInfoModel] = Json.format[UserInfoModel]
}




