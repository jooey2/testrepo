package model

import play.api.libs.json.{Format, Json}

case class GithubUserModel(username : String, usersRepoModel: UsersRepoModel, userInfo : UserInfoModel){}
