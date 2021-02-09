package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.Device

object DeviceDataContext {
  private var connection: Connection = _

  def getDevices(): Set[Device] = {
    var devices: Set[Device] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM Devices")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val make = resultSet.getString("make")
        val model = resultSet.getString("model")
        val os = resultSet.getString("os")
        val language = resultSet.getString("language")
        val deviceTypeId = resultSet.getString("deviceTypeId")
        val geoId = resultSet.getString("geoId")
        devices += Device(id, make, model, os, language, deviceTypeId.toInt, geoId.toInt)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    devices
  }

  def getDevice(id: String): Option[Device] = {
    var device: Option[Device] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM Devices WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val make = resultSet.getString("make")
        val model = resultSet.getString("model")
        val os = resultSet.getString("os")
        val language = resultSet.getString("language")
        val deviceTypeId = resultSet.getString("deviceTypeId")
        val geoId = resultSet.getString("geoId")
        device = Some(Device(id, make, model, os, language, deviceTypeId.toInt, geoId.toInt))
        //println("id = " + id + ", make = " + make + ", model = " + model + ", os = " + os + ", language = " + language + ", deviceTypeId = " + deviceTypeId+ ", geoId = " + geoId)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    device
  }

  def saveDevice(device: Device): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[Devices]([Make],[Model],[OS],[Language],[DeviceTypeId],[GeoId])" +
        "VALUES ('" + device.make + "', '" + device.model + "', '" + device.os + "', '" + device.language + "', " + device.deviceTypeId + ", " + device.geoId + ")"
      val result = statement.execute(sql)
      if (result) {
        message = s"Device $device.model saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteDevice(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM Devices WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"Device $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}