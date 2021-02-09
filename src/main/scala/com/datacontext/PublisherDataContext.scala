package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.Publisher

object PublisherDataContext {
  private var connection: Connection = _

  def getPublishers(): Set[Publisher] = {
    var publishers: Set[Publisher] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM Publishers")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val domain = resultSet.getString("domain")
        val category = resultSet.getString("category")
        publishers += Publisher(id.toInt, name, domain, category)
        //println("id = " + id + ", name = " + name + ", domain = " + domain + ", category = " + category)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    publishers
  }

  def getPublisher(id: String): Option[Publisher] = {
    var publisher: Option[Publisher] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM Publishers WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val domain = resultSet.getString("domain")
        val category = resultSet.getString("category")
        publisher = Some(Publisher(id.toInt, name, domain, category))
        //println("id = " + id + ", name = " + name + ", domain = " + domain + ", category = " + category)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    publisher
  }

  def savePublisher(publisher: Publisher): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[Publishers]([Name],[Domain],[Category])" +
        "VALUES ('" + publisher.name + "', '" + publisher.domain + "', '" + publisher.category + "')"
      val result = statement.execute(sql)
      if (result) {
        message = s"Publisher $publisher.name saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deletePublisher(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM Publishers WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"Publisher $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}