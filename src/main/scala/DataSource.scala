import org.apache.commons.dbcp2.*

import java.sql.{Connection, DriverManager, ResultSet, Statement}
import java.util.Properties

object DataSource {
  val dbUrl = "jdbc:mysql://localhost/2rd_hand_trading_sys"
  val connectionPool = new BasicDataSource()

  connectionPool.setUsername("root")
  // TODO: reset the password.
  connectionPool.setPassword("qwer1234")

  connectionPool.setDriverClassName("com.mysql.cj.jdbc.Driver")
  connectionPool.setUrl(dbUrl)
  connectionPool.setInitialSize(3)
}