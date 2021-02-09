package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.City

object CityDataContext {
  private var connection: Connection = _

  def getCities(): Set[City] = {
    var cities: Set[City] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM Cities")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val notes = resultSet.getString("notes")
        cities += City(id, name, notes)
        //println("id = " + id + ", name = " + name + ", notes = " + notes)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    cities
  }

  def getCity(id: String): Option[City] = {
    var city: Option[City] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM Cities WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val notes = resultSet.getString("notes")
        city = Some(City(id, name, notes))
        //println("id = " + id + ", name = " + name + ", notes = " + notes)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    city
  }

  def saveCity(city: City): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[Cities]([Name],[Notes])" +
        "VALUES ('" + city.name + "', '" + city.notes + "')"
      val result = statement.execute(sql)
      if (result) {
        message = s"City $city.name saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteCity(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM Cities WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"City $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}