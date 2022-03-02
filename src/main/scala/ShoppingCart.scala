import java.sql.ResultSet
import scala.annotation.tailrec
import scala.io.StdIn.{readChar,readLine}

object ShoppingCart {
  private val connection = DataSource.connectionPool.getConnection

  @tailrec
  def ShoppingCartLoop(user: User): Unit = {
    println("You could do the following Operations:\n" +
      "- View your Shopping Cart: [1]\n" +
      "- Choose an item and Add it to your Shopping Cart: [2]\n" +
      "- Check out and remove an item from your Shopping Cart: [3]\n" +
      "- Go Back to Main Page: [0]"
    )
    val choice = readChar()
    choice match {
      case 49 =>
        println("TODO: Show your Shopping Cart.")
        //TODO:Show Shopping Cart.
        ShowShoppingCart(user)
        ShoppingCartLoop(user)
      case 50 =>
        println("TODO: Choose an item and Add it to your Shopping Cart")
        //TODO:Choose an item and Add it to your Shopping Cart
        PostingPage.AddToShoppingCart(user)
        ShoppingCartLoop(user)
      case 51 =>
        println("TODO: Check out and remove an item from your Shopping Cart")
        //TODO:Remove an item from your Shopping Cart
        RemoveFromShoppingCart(user)
        ShoppingCartLoop(user)
      case 48 =>
        println("Go Back to Main Page")
    }

  }

  def ShowShoppingCart(user: User): Unit = {
    val stmt = connection.createStatement()
    val user_id = user.userId
    val rs: ResultSet = stmt.executeQuery("SELECT cart_id,add_date,post.item_id as item_id" +
      ", item_name,item_description, item_price, item_condition, item_category, item_status from" +
      "cart join item" +
      "on cart.item_id = item.item_id" +
      s"WHERE cart.user_id = '$user_id'")
    while (rs.next()) {
      val cart_id = rs.getInt("post_id")
      val user_id = rs.getInt("user_id")
      val add_date = rs.getDate("add_date")
      val item_id = rs.getInt("item_id")
      val item_name = rs.getString("item_name")
      val item_description = rs.getString("item_description")
      val item_price = rs.getFloat("item_price")
      val item_condition = rs.getString("item_condition")
      val item_category = rs.getString("item_category")
      val item_status = rs.getByte("item_status")
      println("cart_id" + "," + "user_id" + "," + "add_date" + "," + "item_id" + "," + "item_name" + "," +
        "item_description" + "," + "item_price" + "," + "item_condition" + "," + "item_category" + "," + "item_status") +
      println(s"'$cart_id','$user_id','$add_date','$item_id','$item_name','$item_description','$item_price'" +
        s",'$item_condition','$item_category','$item_status'")

    }

  }


  def RemoveFromShoppingCart(user: User): Unit = {
    val stmt = connection.createStatement()
    println("Select the item you want to pay for.")
    val item_id = readLine("Enter the item id: ")
    stmt.executeUpdate("DELETE FROM cart " +
      s"WHERE item_id = '$item_id' and user_id='$user_id'")

  }
}