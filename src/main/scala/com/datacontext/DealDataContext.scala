package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.Deal

object DealDataContext {
  private var connection: Connection = _

  def getDeals(): Set[Deal] = {
    var deals: Set[Deal] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM Deals")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val bidFloor = resultSet.getString("bidFloor")
        val currency = resultSet.getString("currency")
        val auctionType = resultSet.getString("auctionType")
        val wSeat = resultSet.getString("wSeat")
        val waDomain = resultSet.getString("waDomain")
        deals += Deal(id, bidFloor.toDouble, currency, auctionType.toInt, wSeat, waDomain)
        //println("id = " + id + ", bidFloor = " + bidFloor + ", currency = " + currency + ", auctionType = " + auctionType + ", wSeat = " + wSeat + ", waDomain = " + waDomain)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    deals
  }

  def getDeal(id: String): Option[Deal] = {
    var deal: Option[Deal] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM Deals WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val bidFloor = resultSet.getString("bidFloor")
        val currency = resultSet.getString("currency")
        val auctionType = resultSet.getString("auctionType")
        val wSeat = resultSet.getString("wSeat")
        val waDomain = resultSet.getString("waDomain")
        deal = Some(Deal(id, bidFloor.toDouble, currency, auctionType.toInt, wSeat, waDomain))
        //println("id = " + id + ", bidFloor = " + bidFloor + ", currency = " + currency + ", auctionType = " + auctionType + ", wSeat = " + wSeat + ", waDomain = " + waDomain)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    deal
  }

  def saveDeal(deal: Deal): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[Deals]([BidFloor],[Currency],[AuctionType],[WSeat],[WaDomain])" +
        "VALUES (" + deal.bidFloor + ", '" + deal.currency + "', " + deal.auctionType + ", '" + deal.wSeat + "', '" + deal.waDomain + "')"
      val result = statement.execute(sql)
      if (result) {
        message = s"Deal $deal.source saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteDeal(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM Deals WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"Deal $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}