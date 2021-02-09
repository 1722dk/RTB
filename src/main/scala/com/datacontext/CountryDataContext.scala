package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.Country

object CountryDataContext {
  private var connection: Connection = _

  def getCountries(): Set[Country] = {
    var countries: Set[Country] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM Countries")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val notes = resultSet.getString("notes")
        countries += Country(id, name, notes)
        //println("id = " + id + ", name = " + name + ", notes = " + notes)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    countries
  }

  def getCountry(id: String): Option[Country] = {
    var country: Option[Country] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM Countries WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val notes = resultSet.getString("notes")
        country = Some(Country(id, name, notes))
        //println("id = " + id + ", name = " + name + ", notes = " + notes)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    country
  }

  def saveCountry(country: Country): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[Countries]([Name],[Notes])" +
        "VALUES ('" + country.name + "', '" + country.notes + "')"
      val result = statement.execute(sql)
      if (result) {
        message = s"Country $country.name saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteCountry(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM Countries WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"Country $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}