package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.Campaign

object CampaignDataContext {
  private var connection: Connection = _

  def getCampaigns(): Set[Campaign] = {
    var campaigns: Set[Campaign] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM Campaigns")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val bidPrice = resultSet.getString("bidPrice")
        val currency = resultSet.getString("currency")
        val cityId = resultSet.getString("cityId")
        val countryId = resultSet.getString("countryId")
        val userId = resultSet.getString("userId")
        val targetSiteId = resultSet.getString("targetSiteId")
        val bannerId = resultSet.getString("bannerId")
        campaigns += Campaign(id, bidPrice.toDouble, currency, cityId.toInt, countryId.toInt, userId.toInt, targetSiteId.toInt, bannerId.toInt)
        //println("id = " + id + ", bidPrice = " + bidPrice + ", currency = " + currency + ", cityId = " + cityId + ", countryId = " + countryId + ", userId = " + userId + ", targetSiteId = " + targetSiteId + ", bannerId = " + bannerId + ")
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    campaigns
  }

  def getCampaign(id: String): Option[Campaign] = {
    var campaign: Option[Campaign] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM Campaigns WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val bidPrice = resultSet.getString("bidPrice")
        val currency = resultSet.getString("currency")
        val cityId = resultSet.getString("cityId")
        val countryId = resultSet.getString("countryId")
        val userId = resultSet.getString("userId")
        val targetSiteId = resultSet.getString("targetSiteId")
        val bannerId = resultSet.getString("bannerId")
        campaign = Some(Campaign(id, bidPrice.toDouble, currency, cityId.toInt, countryId.toInt, userId.toInt, targetSiteId.toInt, bannerId.toInt))
        //println("id = " + id + ", bidPrice = " + bidPrice + ", currency = " + currency + ", cityId = " + cityId + ", countryId = " + countryId + ", userId = " + userId + ", targetSiteId = " + targetSiteId + ", bannerId = " + bannerId + ")
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    campaign
  }

  def saveCampaign(campaign: Campaign): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[Campaigns]([BidPrice],[Currency],[CityId],[CountryId],[UserId],[TargetSiteId],[BannerId])" +
        "VALUES (" + campaign.bidPrice + ", '" + campaign.currency + "', " + campaign.cityId + ", " + campaign.countryId + ", " + campaign.userId + ", " + campaign.targetSiteId + ", " + campaign.bannerId + ")"
      val result = statement.execute(sql)
      if (result) {
        message = s"Campaign $campaign.source saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteCampaign(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM Campaigns WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"Campaign $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}