package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.BidRequest

object BidRequestDataContext {
  private var connection: Connection = _

  def getBidRequests(): Set[BidRequest] = {
    var bidRequests: Set[BidRequest] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM BidRequests")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val auctionType = resultSet.getString("auctionType")
        val currency = resultSet.getString("currency")
        val siteId = resultSet.getString("siteId")
        val deviceId = resultSet.getString("deviceId")
        val bannerId = resultSet.getString("bannerId")
        val publisherId = resultSet.getString("publisherId")
        val impressionId = resultSet.getString("impressionId")
        bidRequests += BidRequest(id, auctionType.toInt, currency, siteId.toInt, deviceId.toInt, bannerId.toInt, publisherId.toInt, impressionId.toInt)
        //println("id = " + id + ", auctionType = " + auctionType + ", currency = " + currency + ", siteId = " + siteId + ", deviceId = " + deviceId + ", bannerId = " + bannerId)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    bidRequests
  }

  def getBidRequest(id: String): Option[BidRequest] = {
    var bidRequest: Option[BidRequest] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM BidRequests WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val auctionType = resultSet.getString("auctionType")
        val currency = resultSet.getString("currency")
        val siteId = resultSet.getString("siteId")
        val deviceId = resultSet.getString("deviceId")
        val bannerId = resultSet.getString("bannerId")
        val publisherId = resultSet.getString("publisherId")
        val impressionId = resultSet.getString("impressionId")
        bidRequest = Some(BidRequest(id, auctionType.toInt, currency, siteId.toInt, deviceId.toInt, bannerId.toInt, publisherId.toInt, impressionId.toInt))
        //println("id = " + id + ", auctionType = " + auctionType + ", currency = " + currency + ", siteId = " + siteId + ", deviceId = " + deviceId + ", bannerId = " + bannerId)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    bidRequest
  }

  def saveBidRequest(bidRequest: BidRequest): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[BidRequests]([AuctionType],[Currency],[SiteId],[DeviceId],[BannerId],[PublisherId],[ImpressionId])" +
        "VALUES (" + bidRequest.auctionType + ", '" + bidRequest.currency + "', " + bidRequest.siteId + ", " + bidRequest.deviceId + ", " + bidRequest.bannerId + ", " + bidRequest.publisherId + ", " + bidRequest.impressionId + ")"
      val result = statement.execute(sql)
      if (result) {
        message = s"BidRequest $bidRequest.source saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteBidRequest(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM BidRequests WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"BidRequest $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}