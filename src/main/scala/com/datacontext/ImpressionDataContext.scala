package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.Impression

object ImpressionDataContext {
  private var connection: Connection = _

  def getImpressions(): Set[Impression] = {
    var impressions: Set[Impression] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM Impressions")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val widthMin = resultSet.getString("widthMin")
        val widthMax = resultSet.getString("widthMax")
        val width = resultSet.getString("width")
        val heightMin = resultSet.getString("heightMin")
        val heightMax = resultSet.getString("heightMax")
        val height = resultSet.getString("height")
        val bidFloor = resultSet.getString("bidFloor")
        impressions += Impression(id, widthMin.toInt, widthMax.toInt, width.toInt, heightMin.toInt, heightMax.toInt, height.toInt, bidFloor.toDouble)
        //println("id = " + id + ", widthMin = " + widthMin + ", widthMax = " + widthMax + ", width = " + width + ", heightMin = " + heightMin + ", heightMax = " + heightMax + ", height = " + height + ", bidFloor = " + bidFloor)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    impressions
  }

  def getImpression(id: String): Option[Impression] = {
    var impression: Option[Impression] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM Impressions WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val widthMin = resultSet.getString("widthMin")
        val widthMax = resultSet.getString("widthMax")
        val width = resultSet.getString("width")
        val heightMin = resultSet.getString("heightMin")
        val heightMax = resultSet.getString("heightMax")
        val height = resultSet.getString("height")
        val bidFloor = resultSet.getString("bidFloor")
        impression = Some(Impression(id, widthMin.toInt, widthMax.toInt, width.toInt, heightMin.toInt, heightMax.toInt, height.toInt, bidFloor.toDouble))
        //println("id = " + id + ", widthMin = " + widthMin + ", widthMax = " + widthMax + ", width = " + width + ", heightMin = " + heightMin + ", heightMax = " + heightMax + ", height = " + height + ", bidFloor = " + bidFloor)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    impression
  }

  def saveImpression(impression: Impression): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[Impressions]([WidthMin],[WidthMax],[Width],[HeightMin],[HeightMax],[Height],[BidFloor])" +
        "VALUES (" + impression.widthMin + ", " + impression.widthMax + ", " + impression.width + ", " + impression.heightMin + ", " + impression.heightMax + "," + impression.height + ", " + impression.bidFloor + ")"
      val result = statement.execute(sql)
      if (result) {
        message = s"Impression $impression.widthMin saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteImpression(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM Impressions WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"Impression $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}