package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.BidResponse

object BidResponseDataContext {
  private var connection: Connection = _

  def getBidResponses(): Set[BidResponse] = {
    var bidResponses: Set[BidResponse] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM BidResponses")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val price = resultSet.getString("price")
        val currency = resultSet.getString("currency")
        val bidRequestId = resultSet.getString("bidRequestId")
        val adId = resultSet.getString("adId")
        val bannerId = resultSet.getString("bannerId")
        bidResponses += BidResponse(id, price.toDouble, currency, bidRequestId, adId.toInt, bannerId.toInt)
        //println("id = " + id + ", price = " + price + ", currency = " + currency + ", bidRequestId = " + bidRequestId + ", adId = " + adId + ", bannerId = " + bannerId)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    bidResponses
  }

  def getBidResponse(id: String): Option[BidResponse] = {
    var bidResponse: Option[BidResponse] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM BidResponses WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val price = resultSet.getString("price")
        val currency = resultSet.getString("currency")
        val bidRequestId = resultSet.getString("bidRequestId")
        val adId = resultSet.getString("adId")
        val bannerId = resultSet.getString("bannerId")
        bidResponse = Some(BidResponse(id, price.toDouble, currency, bidRequestId, adId.toInt, bannerId.toInt))
        //println("id = " + id + ", price = " + price + ", currency = " + currency + ", bidRequestId = " + bidRequestId + ", adId = " + adId + ", bannerId = " + bannerId)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    bidResponse
  }

  def saveBidResponse(bidResponse: BidResponse): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[BidResponses]([Price],[Currency],[BidRequestId],[AdId],[BannerId])" +
        "VALUES (" + bidResponse.price + ", '" + bidResponse.currency + "', " + bidResponse.bidRequestId + ", " + bidResponse.adId + ", " + bidResponse.bannerId + ")"
      val result = statement.execute(sql)
      if (result) {
        message = s"BidResponse $bidResponse.price saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteBidResponse(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM BidResponses WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"BidResponse $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}