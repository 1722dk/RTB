package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.CityRegistry._
import com.registry.{Cities, City, CityRegistry}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#city-routes-class
class CityRoutes(cityRegistry: ActorRef[CityRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getCities(): Future[Cities] =
    cityRegistry.ask(GetCities)

  def getCity(id: String): Future[GetCityResponse] =
    cityRegistry.ask(GetCity(id, _))

  def createCity(city: City): Future[ActionPerformed] =
    cityRegistry.ask(CreateCity(city, _))

  def deleteCity(id: String): Future[ActionPerformed] =
    cityRegistry.ask(DeleteCity(id, _))

  //#all-city-routes
  val cityRoutes: Route =
    pathPrefix("cities") {
      concat(
        //#cities-get-create
        pathEnd {
          concat(
            get {
              complete(getCities())
            },
            post {
              entity(as[City]) { city =>
                onSuccess(createCity(city)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#cities-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getCity(id)) { response =>
                  complete(response.maybeCity)
                }
              }
            },
            delete {
              onSuccess(deleteCity(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}