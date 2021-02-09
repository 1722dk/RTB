package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.Pmp

object PmpDataContext {
  private var connection: Connection = _

  def getPmps(): Set[Pmp] = {
    var pmps: Set[Pmp] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM Pmps")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val privateAuction = resultSet.getString("privateAuction")
        val dealId = resultSet.getString("dealId")
        pmps += Pmp(id, privateAuction.toInt, dealId.toInt)
        //println("id = " + id + ", privateAuction = " + privateAuction + ", dealId = " + dealId)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    pmps
  }

  def getPmp(id: String): Option[Pmp] = {
    var pmp: Option[Pmp] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM Pmps WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val privateAuction = resultSet.getString("privateAuction")
        val dealId = resultSet.getString("dealId")
        pmp = Some(Pmp(id, privateAuction.toInt, dealId.toInt))
        //println("id = " + id + ", privateAuction = " + privateAuction + ", dealId = " + dealId)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    pmp
  }

  def savePmp(pmp: Pmp): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[Pmps]([PrivateAuction],[DealId])" +
        "VALUES (" + pmp.privateAuction + ", " + pmp.dealId + ")"
      val result = statement.execute(sql)
      if (result) {
        message = s"Pmp $pmp.name saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deletePmp(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM Pmps WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"Pmp $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}