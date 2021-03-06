import java.sql.ResultSet
import scala.annotation.tailrec
import scala.io.StdIn.{readChar, readLine}

object PostingSpace {
  private val connection = DataSource.connectionPool.getConnection

  @tailrec
  def PostingSpaceLoop(user: User): Unit = {
    println("You could do the following Operations:\n" +
      "- View all your Postings: [1]\n" +
      "- Add one Posting: [2]\n" +
      "- Edit one posting: [3]\n" +
      "- Delete one posting: [4]\n" +
      "- Go Back to Main Page: [0]"
    )
    val choice = readChar()
    choice match {
      case 49 =>
        // Show All the postings.
        ShowAllPostings(user)
        println("============================================================================")
        PostingSpaceLoop(user)
      case 50 =>
        // Add one Posting.
        AddOnePosting(user)
        println("============================================================================")
        PostingSpaceLoop(user)
      case 51 =>
        // Edit one posting.
        EditOnePosting(user)
        println("============================================================================")
        PostingSpaceLoop(user)
      case 52 =>
        // Delete one posting.
        DeleteOnePosting(user)
        println("============================================================================")
        PostingSpaceLoop(user)
      case 48 =>
        println("Go Back to Main Page")
    }
  }

  def ShowAllPostings(user: User): Unit = {

    val stmt = connection.createStatement()
    val userid = user.id
    val rs: ResultSet = stmt.executeQuery(s"SELECT * From (SELECT * from post WHERE user_id ='$userid') a join (SELECT * from item) b on a.item_id = b.item_id;")
    println("post_id" + ", " + "post_date" + ", " + "item_id" + ", " + "item_name" + ", " + "item_description" + ","
      + "item_price" + ", " + "item_condition" + ", " + "item_category" + ", " + "item_status")

    while (rs.next()) {
      val post_id = rs.getInt("post_id")
      val post_date = rs.getDate("post_date")
      val item_id = rs.getInt("item_id")
      val item_name = rs.getString("item_name")
      val item_description = rs.getString("item_description")
      val item_price = rs.getFloat("item_price")
      val item_condition = rs.getString("item_condition")
      val item_category = rs.getString("item_category")
      val item_status = rs.getByte("item_status")

      println(post_id + ", " + post_date + ", " + item_id + ", " + item_name + ", " + item_description + ","
        + item_price + ", " + item_condition + ", " + item_category + ", " + item_status)
    }
  }

  def AddOnePosting(user: User): Unit = {
    val stmt = connection.createStatement()
    val userid = user.id
    println("Please enter post information")
    val item_name = readLine("Enter item name: ")
    val item_description = readLine("Enter item description: ")
    val item_price = readLine("Enter item price: ").toFloat
    val item_condition = readLine("Enter item condition: ")
    val item_category = readLine("Enter item category: ")


    // insert new item info into Table item
    stmt.executeUpdate("INSERT INTO item " +
      "(item_name, item_description, item_price, item_condition, item_category, item_status) " +
      "VALUES "+
      s"('$item_name','$item_description','$item_price','$item_condition','$item_category',0);")

    // get the item_id that added into Table item just now
    val rs_itemid: ResultSet = stmt.executeQuery("SELECT MAX(item_id) as item_id FROM item;")
    rs_itemid.next()
    val item_id = rs_itemid.getInt("item_id")

    // get current date as post date
    val rs_curdate: ResultSet = stmt.executeQuery("SELECT CURDATE() as curdate;")
    rs_curdate.next()
    val post_date = rs_curdate.getDate("curdate")

    // insert new item info into Table post
    stmt.executeUpdate("INSERT INTO post " +
      "(user_id, item_id, post_date) " +
      "VALUES "+
      s"('$userid','$item_id','$post_date');")

    println("One post added.")
  }


  def EditOnePosting(user: User): Unit = {
    val stmt = connection.createStatement()
    val userid = user.id
    // First, choose one post to edit
    println("Please choose one post to edit")
    val post_id = readLine("Enter post id of the posting that you're going to edit: ")

    // use post id to get original information
    val rs: ResultSet = stmt.executeQuery("SELECT b.item_id as item_id, item_name" +
      ",item_description, item_price, item_condition, item_category, item_status from" +
        s"(SELECT * from post WHERE post_id ='$post_id') a join (SELECT * from item) b on a.item_id = b.item_id;")
    rs.next()

    val item_id = rs.getInt("item_id")
    var item_name = rs.getString("item_name")
    var item_description = rs.getString("item_description")
    var item_price = rs.getFloat("item_price")
    var item_condition = rs.getString("item_condition")
    var item_category = rs.getString("item_category")
    var item_status = rs.getByte("item_status")

    // item name
    println(s"Will you change the item name ?\n" + "- Yes: [1]\n" + "- No: [2]")
    val ans1: Int = readChar()
    ans1 match{
      case 49 =>
        item_name = readLine("Enter new item name: ")
      case _ =>
    }

    // item description
    println(s"Will you change the item description ?\n" + "- Yes: [1]\n" + "- No: [2]")
    val ans2: Int = readChar()
    ans2 match{
      case 49 =>
        item_description = readLine("Enter new item description: ")
      case _ =>
    }

    // item price
    println(s"Will you change the item price ?\n" + "- Yes: [1]\n" + "- No: [2]")
    val ans3: Int = readChar()
    ans3 match{
      case 49 =>
        item_price = readLine("Enter new item price: ").toFloat
      case _ =>
    }

    // item condition
    println(s"Will you change the item condition ?\n" + "- Yes: [1]\n" + "- No: [2]")
    val ans4: Int = readChar()
    ans4 match{
      case 49 =>
        item_condition = readLine("Enter new item condition: ")
      case _ =>
    }

    // item category
    println(s"Will you change the item category ?\n" + "- Yes: [1]\n" + "- No: [2]")
    val ans5: Int = readChar()
    ans5 match{
      case 49 =>
        item_category = readLine("Enter new item category: ")
      case _ =>
    }

    // item status
    println(s"Will you change the item status ?\n" + "- Yes: [1]\n" + "- No: [2]")
    val ans6: Int = readChar()
    ans6 match{
      case 49 =>
        item_status = readLine("Enter new item status: you can only input 0 (on shelf) or 1 (sold)").toByte
      case _ =>
    }

    // update new item info into Table item
    stmt.executeUpdate(s"UPDATE item SET item_name='$item_name', item_description='$item_description', item_price='$item_price', item_condition='$item_condition', item_category='$item_category', item_status='$item_status'" +
    s"WHERE item_id = '$item_id';")
    
    // get current date as post date
    val rs_curdate: ResultSet = stmt.executeQuery("SELECT CURDATE() as curdate;")
    rs_curdate.next()
    val post_date = rs_curdate.getDate("curdate")

    // update new post info into Table post
    stmt.executeUpdate(s"UPDATE post SET post_date='$post_date' WHERE post_id = '$post_id';")

    println("Successfully edit post.")
  }


  def DeleteOnePosting(user: User): Unit ={
    val stmt = connection.createStatement()
    val userid = user.id
    // First, choose one post to delete
    println("Please choose one post to delete")
    val post_id = readLine("Enter post id of the posting that you're going to delete: ")

    // use post id to get item id
    val rs: ResultSet = stmt.executeQuery( s"SELECT item_id from post WHERE post_id ='$post_id'")
    rs.next()
    val item_id = rs.getInt("item_id")

    // delete corresponding item in Table item
    stmt.executeUpdate(s"DELETE FROM item WHERE item_id = '$item_id';")

    // delete corresponding item in Table post
    stmt.executeUpdate(s"DELETE FROM post WHERE post_id = '$post_id';")
    
    // delete corresponding item in Table cart
    stmt.executeUpdate(s"DELETE FROM cart WHERE item_id = '$item_id';")

    println("Successfully delete post.")
  }
}
