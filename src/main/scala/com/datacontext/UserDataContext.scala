package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.User

object UserDataContext {
  private var connection: Connection = _

  def getUsers(): Set[User] = {
    var users: Set[User] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM Users")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val buyerId = resultSet.getString("buyerId")
        val yearOfBirth = resultSet.getString("yearOfBirth")
        val gender = resultSet.getString("gender")
        val geoId = resultSet.getString("geoId")
        users += User(id, name, buyerId, yearOfBirth.toInt, gender, geoId.toInt)
        //println("id = " + id + ", name = " + name + ", buyerId = " + buyerId + ", yearOfBirth = " + yearOfBirth + ", gender = " + gender + ", geoId = " + geoId)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    users
  }

  def getUser(id: String): Option[User] = {
    var user: Option[User] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM Users WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val buyerId = resultSet.getString("buyerId")
        val yearOfBirth = resultSet.getString("yearOfBirth")
        val gender = resultSet.getString("gender")
        val geoId = resultSet.getString("geoId")
        user = Some(User(id, name, buyerId, yearOfBirth.toInt, gender, geoId.toInt))
        //println("id = " + id + ", name = " + name + ", buyerId = " + buyerId + ", yearOfBirth = " + yearOfBirth + ", gender = " + gender + ", geoId = " + geoId)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    user
  }

  def saveUser(user: User): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[Users]([Name],[BuyerId],[YearOfBirth],[Gender],[GeoId])" +
        "VALUES ('" + user.name + "', '" + user.buyerId + "', " + user.yearOfBirth + ", '" + user.gender + "', " + user.geoId + ")"
      val result = statement.execute(sql)
      if (result) {
        message = s"User $user.source saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteUser(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM Users WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"User $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}