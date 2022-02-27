import java.util.Date

class Post(postId: Int, userId: Int, itemId: Int, itemStatus: String, postDate: Date){
  val post_id: Int = postId
  val user_id: Int = userId
  val item_id: Int = itemId
  val item_status: String = itemStatus
  val post_date: Date = postDate
  
  def GetFormatString(): String = {
    ""
  }
}
