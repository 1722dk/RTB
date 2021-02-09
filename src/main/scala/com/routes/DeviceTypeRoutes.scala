package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.DeviceTypeRegistry._
import com.registry.{DeviceType, DeviceTypeRegistry, DeviceTypes}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#deviceType-routes-class
class DeviceTypeRoutes(deviceTypeRegistry: ActorRef[DeviceTypeRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getDeviceTypes(): Future[DeviceTypes] =
    deviceTypeRegistry.ask(GetDeviceTypes)

  def getDeviceType(id: String): Future[GetDeviceTypeResponse] =
    deviceTypeRegistry.ask(GetDeviceType(id, _))

  def createDeviceType(deviceType: DeviceType): Future[ActionPerformed] =
    deviceTypeRegistry.ask(CreateDeviceType(deviceType, _))

  def deleteDeviceType(id: String): Future[ActionPerformed] =
    deviceTypeRegistry.ask(DeleteDeviceType(id, _))

  //#all-deviceType-routes
  val deviceTypeRoutes: Route =
    pathPrefix("deviceTypes") {
      concat(
        //#deviceTypes-get-create
        pathEnd {
          concat(
            get {
              complete(getDeviceTypes())
            },
            post {
              entity(as[DeviceType]) { deviceType =>
                onSuccess(createDeviceType(deviceType)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#deviceTypes-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getDeviceType(id)) { response =>
                  complete(response.maybeDeviceType)
                }
              }
            },
            delete {
              onSuccess(deleteDeviceType(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}