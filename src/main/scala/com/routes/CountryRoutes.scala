package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.CountryRegistry._
import com.registry.{Country, CountryRegistry, Countries}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#country-routes-class
class CountryRoutes(countryRegistry: ActorRef[CountryRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getCountries(): Future[Countries] =
    countryRegistry.ask(GetCountries)

  def getCountry(id: String): Future[GetCountryResponse] =
    countryRegistry.ask(GetCountry(id, _))

  def createCountry(country: Country): Future[ActionPerformed] =
    countryRegistry.ask(CreateCountry(country, _))

  def deleteCountry(id: String): Future[ActionPerformed] =
    countryRegistry.ask(DeleteCountry(id, _))

  //#all-country-routes
  val countryRoutes: Route =
    pathPrefix("countries") {
      concat(
        //#countries-get-create
        pathEnd {
          concat(
            get {
              complete(getCountries())
            },
            post {
              entity(as[Country]) { country =>
                onSuccess(createCountry(country)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#countries-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getCountry(id)) { response =>
                  complete(response.maybeCountry)
                }
              }
            },
            delete {
              onSuccess(deleteCountry(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}