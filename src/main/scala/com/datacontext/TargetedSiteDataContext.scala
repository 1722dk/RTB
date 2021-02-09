package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.TargetedSite

object TargetedSiteDataContext {
  private var connection: Connection = _

  def getTargetedSites(): Set[TargetedSite] = {
    var targetedSites: Set[TargetedSite] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM TargetedSites")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val domain = resultSet.getString("domain")
        val url = resultSet.getString("url")
        targetedSites += TargetedSite(id, name, domain, url)
        //println("id = " + id + ", name = " + name + ", domain = " + domain + ", url = " + url)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    targetedSites
  }

  def getTargetedSite(id: String): Option[TargetedSite] = {
    var targetedSite: Option[TargetedSite] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM TargetedSites WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val domain = resultSet.getString("domain")
        val url = resultSet.getString("url")
        targetedSite = Some(TargetedSite(id, name, domain, url))
        //println("id = " + id + ", name = " + name + ", domain = " + domain + ", url = " + url)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    targetedSite
  }

  def saveTargetedSite(targetedSite: TargetedSite): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[TargetedSites]([Name],[Domain],[Url])" +
        "VALUES ('" + targetedSite.name + "', '" + targetedSite.domain + "', '" + targetedSite.url + "')"
      val result = statement.execute(sql)
      if (result) {
        message = s"TargetedSite $targetedSite.name saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteTargetedSite(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM TargetedSites WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"TargetedSite $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}