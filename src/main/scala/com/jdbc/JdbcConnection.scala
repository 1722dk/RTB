package com.jdbc
import java.sql.{Connection, DriverManager}

object JdbcConnection {
  private var connection: Connection = _

  def getDbConnection(): Connection = {
    if (this.connection == null || this.connection.isClosed) {
      this.createDbConnection()
    } else {
      this.connection
    }
  }

  private def createDbConnection(): Connection = {
    val driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
    val dbHostName = "localhost"
    val dbName = "rtb"
    val dbUserName = "sa"
    val dbPassword = "P@@$w0rd"
    val connectionUrl = "jdbc:sqlserver://" + dbHostName + ":1433" + ";databaseName=" + dbName + ";user=" + dbUserName + ";password=" + dbPassword + ";"

    try {
      Class.forName(driver)
      this.connection = DriverManager.getConnection(connectionUrl)
    } catch {
      case e: Exception => e.printStackTrace()
    }
    this.connection
  }
}