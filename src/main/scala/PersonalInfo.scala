import java.sql.ResultSet
import scala.annotation.tailrec
import scala.io.StdIn.{readChar, readLine}

object PersonalInfo {
  private val connection = DataSource.connectionPool.getConnection

  @tailrec
  def PersonalInfoLoop(user: User): Unit = {
    val stmt = connection.createStatement()
    println("You could do the following Operations:\n" +
      "- View Your Information: [1]\n" +
      "- Edit your User Name: [2]\n" +
      "- Edit your Password: [3]\n"+
      "- Go Back to Main Page: [0]"
    )
    val choice = readChar()
    choice match {
      case 49 =>
        val rs: ResultSet = stmt.executeQuery(s"SELECT * from user WHERE user_id='${user.id}';")
        if (rs.next()) {
          println(s"Your user name is: ${rs.getString("user_name")}")
          println(s"Your email address is: ${rs.getString("user_email")}")
        } else {
          println("Error getting the User Information.")
        }
        PersonalInfoLoop(user)
      case 50 =>
        val newUserName = readLine("Enter your new user name: ")
        stmt.executeUpdate(s"UPDATE user SET user_name='${newUserName}' WHERE user_id=${user.id};")
        user.name = newUserName
        println("Your user name changed successfully!")
        PersonalInfoLoop(user)
      case 51 =>
        val newPassword = readLine("Enter your new password: ")
        stmt.executeUpdate(s"UPDATE user SET user_password='${newPassword}' WHERE user_id=${user.id};")
        println("Your password changed successfully!")
        PersonalInfoLoop(user)
      case 48 =>
        println("Go Back to Main Page")
    }
  }
}
