import java.sql.ResultSet
import scala.annotation.tailrec
import scala.io.StdIn.{readChar,readLine}

object PostingPage {
  private val connection = DataSource.connectionPool.getConnection
  @tailrec
  def PostingPageLoop(user: User): Unit = {
    println("You could do the following Operations:\n" +
      "- View all the Postings: [1]\n" +
      "- Choose an item and Add it to your Shopping Cart: [2]\n" +
      "- Go Back to Main Page: [0]"
    )
    val choice = readChar()
    choice match {
      case 49 =>
        println("TODO: Show All the postings.")
        ShowAllPostings()
        PostingPageLoop(user)
      case 50 =>
        println("TODO: Choose an item and Add it to your Shopping Cart.")
        AddToShoppingCart(user)
        PostingPageLoop(user)
      case 48 =>
        println("Go Back to Main Page")
    }
  }

  def ShowAllPostings(): Unit = {
    val stmt = connection.createStatement()
    val rs: ResultSet = stmt.executeQuery("SELECT post_id,user_id,post_date,post.item_id as item_id" +
      ", item_name,item_description, item_price, item_condition, item_category, item_status from " +
      "post join item " +
      "on post.item_id = item.item_id;")
    while (rs.next()){
      val post_id = rs.getInt("post_id")
      val user_id = rs.getInt("user_id")
      val post_date = rs.getDate("post_date")
      val item_id = rs.getInt("item_id")
      val item_name = rs.getString("item_name")
      val item_description = rs.getString("item_description")
      val item_price = rs.getFloat("item_price")
      val item_condition = rs.getString("item_condition")
      val item_category = rs.getString("item_category")
      val item_status = rs.getByte("item_status")
      println("post_id" + "," + "user_id" + "," + "post_date" + "," + "item_id" + "," + "item_name" + "," +
        "item_description" + "," + "item_price" + "," + "item_condition" + "," + "item_category" + "," + "item_status")
      println(s"$post_id, $user_id, $post_date, $item_id, $item_name, $item_description, $item_price" +
        s", $item_condition, $item_category, $item_status")
    }
  }

  def AddToShoppingCart(user: User): Unit = {
    val stmt = connection.createStatement()
    println("Please enter choose the item.")
    val item_id = readLine("Enter item id: ")
    val user_id = user.id
    val rsCurdate: ResultSet = stmt.executeQuery("SELECT CURDATE() as addDate;")
    if (rsCurdate.next()) {
      val add_date = rsCurdate.getDate("addDate")
      stmt.executeUpdate("INSERT INTO cart (user_id, item_id, add_date) " +
      "VALUES " +
      s"('$user_id','$item_id','$add_date')")
      println("Successfully add the item to your shopping cart!")
    }
    else{
      println("Error: please try again.")
    }
  }
}
