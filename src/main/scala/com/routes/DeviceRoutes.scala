package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.DeviceRegistry._
import com.registry.{Device, DeviceRegistry, Devices}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#device-routes-class
class DeviceRoutes(deviceRegistry: ActorRef[DeviceRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getDevices(): Future[Devices] =
    deviceRegistry.ask(GetDevices)

  def getDevice(id: String): Future[GetDeviceResponse] =
    deviceRegistry.ask(GetDevice(id, _))

  def createDevice(device: Device): Future[ActionPerformed] =
    deviceRegistry.ask(CreateDevice(device, _))

  def deleteDevice(id: String): Future[ActionPerformed] =
    deviceRegistry.ask(DeleteDevice(id, _))

  //#all-device-routes
  val deviceRoutes: Route =
    pathPrefix("devices") {
      concat(
        //#devices-get-create
        pathEnd {
          concat(
            get {
              complete(getDevices())
            },
            post {
              entity(as[Device]) { device =>
                onSuccess(createDevice(device)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#devices-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getDevice(id)) { response =>
                  complete(response.maybeDevice)
                }
              }
            },
            delete {
              onSuccess(deleteDevice(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}