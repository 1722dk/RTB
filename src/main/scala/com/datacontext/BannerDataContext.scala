package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.Banner

object BannerDataContext {
  private var connection: Connection = _

  def getBanners(): Set[Banner] = {
    var banners: Set[Banner] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM Banners")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val source = resultSet.getString("source")
        val description = resultSet.getString("description")
        val width = resultSet.getString("width")
        val height = resultSet.getString("height")
        val position = resultSet.getString("position")
        banners += Banner(id, source, description, width.toInt, height.toInt, position.toInt)
        //println("id = " + id + ", source = " + source + ", description = " + description + ", width = " + width + ", height = " + height + ", position = " + position)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    banners
  }

  def getBanner(id: String): Option[Banner] = {
    var banner: Option[Banner] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM Banners WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val source = resultSet.getString("source")
        val description = resultSet.getString("description")
        val width = resultSet.getString("width")
        val height = resultSet.getString("height")
        val position = resultSet.getString("position")
        banner = Some(Banner(id, source, description, width.toInt, height.toInt, position.toInt))
        //println("id = " + id + ", source = " + source + ", description = " + description + ", width = " + width + ", height = " + height + ", position = " + position)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    banner
  }

  def saveBanner(banner: Banner): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[Banners]([Source],[Description],[Width],[Height],[Position])" +
        "VALUES ('" + banner.source + "', '" + banner.description + "', " + banner.width + ", " + banner.height + ", " + banner.position + ")"
      val result = statement.execute(sql)
      if (result) {
        message = s"Banner $banner.source saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteBanner(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM Banners WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"Banner $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}