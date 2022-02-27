class User(userId: Int, userName: String, userEmail: String, userPassword: String) {
  val id: Int = userId
  var name: String = userName
  var email: String = userEmail
  private var password: String = userPassword

  def PrintShoppingCartList(items: List[Item]): Unit = {

  }

  // TODO: change return value type from `Unit` to `List[Item]`
  def GetShoppingCartList(): Unit = {

  }

  def AddToShoppingCart(item: Item): Boolean = {
    false
  }

  def DeleteFromShoppingCart(item: Item): Boolean = {
    false
  }

  // TODO: change return value type from `Unit` to `List[Post]`
  def GetPostingList(): Unit = {

  }

  def AddPost(post: Post): Boolean = {
    false
  }

  def DeletePost(post: Post): Boolean = {
    false
  }
}
