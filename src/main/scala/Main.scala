import cats.effect.*
import cats.syntax.all.*

import java.awt.Choice
import java.sql.ResultSet
import io.StdIn.*
import scala.annotation.tailrec

object Main extends IOApp {
  private val connection = DataSource.connectionPool.getConnection

  // initialize database
  private def initializeDatabase(): Unit = {
    println("Start Initializing the Database...")
    val stmt = connection.createStatement()
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS user( "+
      "user_id INT NOT NULL AUTO_INCREMENT, "+
      "user_name CHAR(64) NOT NULL, "+
      "user_email CHAR(64) NOT NULL, "+
      "user_password CHAR(32) NOT NULL, "+
      "PRIMARY KEY ( user_id ))ENGINE=InnoDB DEFAULT CHARSET=utf8;")
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS item( "+
      "item_id INT NOT NULL AUTO_INCREMENT, "+
      "item_name CHAR(64) NOT NULL, "+
      "item_description CHAR(128), "+
      "item_price FLOAT NOT NULL, "+
      "item_condition CHAR(32) NOT NULL, " +
      "item_category CHAR(32) NOT NULL, " +
      "item_status TINYINT NOT NULL, " +
      "PRIMARY KEY ( item_id ))ENGINE=InnoDB DEFAULT CHARSET=utf8;")
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS post( "+
      "post_id INT NOT NULL AUTO_INCREMENT, "+
      "user_id INT NOT NULL, "+
      "item_id INT NOT NULL, "+
      "post_date DATE NOT NULL, "+
      "PRIMARY KEY ( post_id ))ENGINE=InnoDB DEFAULT CHARSET=utf8;")
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cart( "+
      "cart_id INT NOT NULL AUTO_INCREMENT, "+
      "user_id INT NOT NULL, "+
      "item_id INT NOT NULL, "+
      "add_date Date NOT NULL, "+
      "PRIMARY KEY ( cart_id ))ENGINE=InnoDB DEFAULT CHARSET=utf8;")

    val rs: ResultSet = stmt.executeQuery("SELECT user_name from user WHERE user_name='admin';")
    var hasAdmin: Boolean = false
    if (rs.next()) {
      if (rs.getString("user_name") == "admin") {
        hasAdmin = true
      }
    }
    if (!hasAdmin) {
      stmt.executeUpdate("INSERT INTO user " +
        "(user_name, user_email, user_password) " +
        "VALUES "+
        "(\"admin\", \"admin@root.com\", \"123456\");")
      stmt.executeUpdate("INSERT INTO item " +
        "(item_name, item_description, item_price, item_condition, item_category, item_status) " +
        "VALUES "+
        "(\"Notebook\", \"Brand New Notebooks.\", 5.85, \"Like_New\", \"Education\", 0);")
      stmt.executeUpdate("INSERT INTO post " +
        "(user_id, item_id, post_date) " +
        "VALUES "+
        "(1, 1, '2021-12-21');")
    }
    println("Database Initialization Completed.")
  }

  // login or register
  @tailrec
  private def loginOrRegister(): User = {
    println("You could Login or Register Now.\n" + "- Login: [1]\n" + "- Register: [2]")
    val choice: Int = readChar()
    val stmt = connection.createStatement()
    choice match {
      case 49 =>
        val email = readLine("Enter your user email address: ")
        val password = readLine("Enter your password: ")
        val rs: ResultSet = stmt.executeQuery(s"SELECT * from user WHERE user_email='$email';")
        if (rs.next()) {
          if (rs.getString("user_password") == password) {
            val user: User = new User(rs.getInt("user_id"), rs.getString("user_name"), email, password)
            println("Login Successfully!")
            user
          } else {
            println("Error: Wrong Password!")
            loginOrRegister()
          }
        } else {
          println("Error: Invalid User Email or Password!")
          loginOrRegister()
        }
      case 50 =>
        val name = readLine("Enter your new user name: ")
        val email = readLine("Enter your email address: ")
        val rs_check_email = stmt.executeQuery(s"SELECT * from user WHERE user_email='$email';")
        if (rs_check_email.next()) {
          println("Error: Duplicate Email Address. This Email Address has been registered!")
          loginOrRegister()
        } else {
          val password = readLine("Enter your new password: ")
          stmt.executeUpdate("INSERT INTO user " +
            "(user_name, user_email, user_password) " +
            "VALUES "+
            s"(\"$name\", \"$email\", \"$password\");")
          val rs_get_user_id = stmt.executeQuery(s"SELECT * from user WHERE user_email='$email';")
          if (rs_get_user_id.next()) {
            val user = new User(rs_get_user_id.getInt("user_id"), name, email, password)
            println("Registered and Login Successfully!")
            user
          } else {
            println("Error: Error getting the created user ID!")
            loginOrRegister()
          }
        }
      case _ =>
        println("Invalid Input: You could enter a digit between [1] or [2] to make a choice.")
        loginOrRegister()
    }
  }

  // main logic loop
  @tailrec
  def mainLogic(user: User): Unit = {
    println("You could do the following Operations:\n" +
      "- View all the Postings of 2rd Hand Items: [1]\n" +
      "- Go to your Shopping Cart: [2]\n" +
      "- Go to your Posting Space: [3]\n" +
      "- Edit your Personal Information: [4]\n" +
      "- Exit: [0]"
    )
    val choice = readChar()
    choice match {
      case 49 =>
        // TODO: view all the postings
        PostingPage.PostingPageLoop(user)
        mainLogic(user)
      case 50 =>
        // TODO: go to your shopping cart
        mainLogic(user)
      case 51 =>
        // TODO: go to your posting space
        mainLogic(user)
      case 52 =>
        PersonalInfo.PersonalInfoLoop(user)
        mainLogic(user)
      case 48 =>
        println("Exit 2rd Hand Trading Backend System.")
      case _ =>
        println("Invalid Input: You could enter a digit from [0] or [4] to make a choice.")
        mainLogic(user)
    }
  }

  // main entrance
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO(println("Welcome to our IT5100A 2rd Hand Trading Backend System.\n"))
      // init database
      _ <- IO(initializeDatabase())
      // login or register
      user <- IO(loginOrRegister())
      _ <- IO(mainLogic(user))
      _ <- IO(println("GoodBye."))
    } yield ExitCode.Success
}
