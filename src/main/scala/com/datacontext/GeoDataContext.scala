package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.Geo

object GeoDataContext {
  private var connection: Connection = _

  def getGeos(): Set[Geo] = {
    var geos: Set[Geo] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM Geos")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val country = resultSet.getString("country")
        val city = resultSet.getString("city")
        val lat = resultSet.getString("lat")
        val lon = resultSet.getString("lon")
        val position = resultSet.getString("position")
        geos += Geo(id, country, city, lat.toDouble, lon.toDouble)
        //println("id = " + id + ", country = " + country + ", city = " + city + ", lat = " + lat + ", lon = " + lon + ")
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    geos
  }

  def getGeo(id: String): Option[Geo] = {
    var geo: Option[Geo] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM Geos WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val country = resultSet.getString("country")
        val city = resultSet.getString("city")
        val lat = resultSet.getString("lat")
        val lon = resultSet.getString("lon")
        val position = resultSet.getString("position")
        geo = Some(Geo(id, country, city, lat.toDouble, lon.toDouble))
        //println("id = " + id + ", country = " + country + ", city = " + city + ", lat = " + lat + ", lon = " + lon + ")
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    geo
  }

  def saveGeo(geo: Geo): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[Geos]([Country],[City],[Lat],[Lon])" +
        "VALUES ('" + geo.country + "', '" + geo.city + "', " + geo.lat + ", " + geo.lon + ")"
      val result = statement.execute(sql)
      if (result) {
        message = s"Geo $geo.source saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteGeo(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM Geos WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"Geo $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}