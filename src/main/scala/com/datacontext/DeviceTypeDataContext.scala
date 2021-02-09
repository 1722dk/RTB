package com.datacontext

import java.sql.Connection

import com.jdbc.JdbcConnection
import com.registry.DeviceType

object DeviceTypeDataContext {
  private var connection: Connection = _

  def getDeviceTypes(): Set[DeviceType] = {
    var deviceTypes: Set[DeviceType] = Set()
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM DeviceTypes")

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val description = resultSet.getString("description")
        val note = resultSet.getString("note")
        deviceTypes += DeviceType(id, name, description, note)
        //println("id = " + id + ", name = " + name + ", description = " + description + ", note = " + note)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    deviceTypes
  }

  def getDeviceType(id: String): Option[DeviceType] = {
    var deviceType: Option[DeviceType] = null
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "SELECT * FROM DeviceTypes WHERE Id = " + id.toInt + ""
      val resultSet = statement.executeQuery(sql)

      while (resultSet.next()) {
        val id = resultSet.getString("id")
        val name = resultSet.getString("name")
        val description = resultSet.getString("description")
        val note = resultSet.getString("note")
        deviceType = Some(DeviceType(id, name, description, note))
        //println("id = " + id + ", name = " + name + ", description = " + description + ", note = " + note)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    deviceType
  }

  def saveDeviceType(deviceType: DeviceType): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "INSERT INTO [dbo].[DeviceTypes]([Name],[Description],[Notes])" +
        "VALUES ('" + deviceType.name + "', '" + deviceType.description + "', '" + deviceType.notes + "')"
      val result = statement.execute(sql)
      if (result) {
        message = s"DeviceType $deviceType.name saved successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }

  def deleteDeviceType(id: String): String = {
    var message: String = ""
    try {
      this.connection = JdbcConnection.getDbConnection()
      val statement = this.connection.createStatement()
      val sql = "Delete * FROM DeviceTypes WHERE Id = " + id.toInt + ""
      val result = statement.execute(sql)
      if (result) {
        message = s"DeviceType $id deleted successfully."
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally if (this.connection != null) {
      this.connection.close()
    }
    message
  }
}