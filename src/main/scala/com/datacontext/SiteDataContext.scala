package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.Site

object SiteDataContext {
  private var connection: Connection = _

  def getSites(): Set[Site] = {
    var sites: Set[Site] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM Sites")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val domain = resultSet.getString("domain")
        val page = resultSet.getString("page")
        val category = resultSet.getString("category")
        sites += Site(id, name, domain, page, category)
        //println("id = " + id + ", name = " + name + ", domain = " + domain + ", page = " + page + ", category = " + category)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    sites
  }

  def getSite(id: String): Option[Site] = {
    var site: Option[Site] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM Sites WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val domain = resultSet.getString("domain")
        val page = resultSet.getString("page")
        val category = resultSet.getString("category")
        site = Some(Site(id, name, domain, page, category))
        //println("id = " + id + ", name = " + name + ", domain = " + domain + ", page = " + page + ", category = " + category)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    site
  }

  def saveSite(site: Site): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[Sites]([Name],[Domain],[Page],[Category])" +
        "VALUES ('" + site.name + "', '" + site.domain + "', '" + site.page + "', '" + site.category + "')"
      val result = statement.execute(sql)
      if (result) {
        message = s"Site $site.name saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteSite(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM Sites WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"Site $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}