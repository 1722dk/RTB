package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.DealRegistry._
import com.registry.{Deal, DealRegistry, Deals}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#deal-routes-class
class DealRoutes(dealRegistry: ActorRef[DealRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getDeals(): Future[Deals] =
    dealRegistry.ask(GetDeals)

  def getDeal(id: String): Future[GetDealResponse] =
    dealRegistry.ask(GetDeal(id, _))

  def createDeal(deal: Deal): Future[ActionPerformed] =
    dealRegistry.ask(CreateDeal(deal, _))

  def deleteDeal(id: String): Future[ActionPerformed] =
    dealRegistry.ask(DeleteDeal(id, _))

  //#all-deal-routes
  val dealRoutes: Route =
    pathPrefix("deals") {
      concat(
        //#deals-get-create
        pathEnd {
          concat(
            get {
              complete(getDeals())
            },
            post {
              entity(as[Deal]) { deal =>
                onSuccess(createDeal(deal)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#deals-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getDeal(id)) { response =>
                  complete(response.maybeDeal)
                }
              }
            },
            delete {
              onSuccess(deleteDeal(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}