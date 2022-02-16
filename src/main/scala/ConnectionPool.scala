import java.util.Properties
import java.sql.{Connection, DriverManager, Statement, ResultSet}
import org.apache.commons.dbcp2._

object Datasource {
  val dbUrl = "jdbc:mysql://localhost/mysql";
  val connectionPool = new BasicDataSource()

  connectionPool.setUsername("root")
  connectionPool.setPassword("zjk363506")

  connectionPool.setDriverClassName("com.mysql.cj.jdbc.Driver")
  connectionPool.setUrl(dbUrl)
  connectionPool.setInitialSize(3)
}

object ConnectionPool {
  def main(args: Array[String]) = {
    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://localhost/mysql"
    val username = "root"
    val password = "zjk363506"

    var connection:Connection = null

    try {
      // make the connection
      // connection = DriverManager.getConnection(url, username, password)
      val connection = Datasource.connectionPool.getConnection
    } catch {
      case e => e.printStackTrace
    }
    // connection.close()
  }
}
