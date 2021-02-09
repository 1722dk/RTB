package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.GeoRegistry._
import com.registry.{Geo, GeoRegistry, Geos}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#geo-routes-class
class GeoRoutes(geoRegistry: ActorRef[GeoRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getGeos(): Future[Geos] =
    geoRegistry.ask(GetGeos)

  def getGeo(id: String): Future[GetGeoResponse] =
    geoRegistry.ask(GetGeo(id, _))

  def createGeo(geo: Geo): Future[ActionPerformed] =
    geoRegistry.ask(CreateGeo(geo, _))

  def deleteGeo(id: String): Future[ActionPerformed] =
    geoRegistry.ask(DeleteGeo(id, _))

  //#all-geo-routes
  val geoRoutes: Route =
    pathPrefix("geos") {
      concat(
        //#geos-get-create
        pathEnd {
          concat(
            get {
              complete(getGeos())
            },
            post {
              entity(as[Geo]) { geo =>
                onSuccess(createGeo(geo)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#geos-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getGeo(id)) { response =>
                  complete(response.maybeGeo)
                }
              }
            },
            delete {
              onSuccess(deleteGeo(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}