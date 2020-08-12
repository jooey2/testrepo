package model

trait ErrorResponse{
  def getMessage : String
}

class UserDoesNotExistError extends ErrorResponse{
  def getMessage = "User can't be found, this may be because they're private3"
}

class UnknownError(status : Int) extends ErrorResponse{
  def getMessage : String = s"Unexpected error status code : $status"
}

class JsonError(message : String) extends ErrorResponse{
  def getMessage : String = message
}