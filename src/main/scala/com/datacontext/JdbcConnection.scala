package com.datacontext
import java.sql.DriverManager
import java.sql.Connection

object JdbcConnection {
  def getDbConnection() {
    // connect to the database named "mysql" on the localhost
    val driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
    val dbHostName = "localhost"
    val dbName = "rtb"
    val dbUserName = "SA"
    val dbPassword = "Darkhouse@441#"

    val connectionUrl = "jdbc:sqlserver://" + dbHostName + ":1433;" + "databaseName=" + dbName + ";user=" + dbUserName + ";password=" + dbPassword + ";"

    // there's probably a better way to do this
    var connection:Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(connectionUrl, dbUserName, dbPassword)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM test_table")
      while ( resultSet.next() ) {
        val id = resultSet.getString("id")
        val fname = resultSet.getString("fname")
        val lname = resultSet.getString("lname")
        println("id, host, user = " + id + ", " + fname + "," + lname)
      }
    } catch {
      case e => e.printStackTrace
    }
    connection.close()
  }
}
