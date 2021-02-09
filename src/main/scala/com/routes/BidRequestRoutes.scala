package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.BidRequestRegistry._
import com.registry.{BidRequest, BidRequestRegistry, BidRequests}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#bidRequest-routes-class
class BidRequestRoutes(bidRequestRegistry: ActorRef[BidRequestRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout: Timeout = Timeout(5 seconds)

  def getBidRequests(): Future[BidRequests] =
    bidRequestRegistry.ask(GetBidRequests)

  def getBidRequest(id: String): Future[GetBidRequestResponse] =
    bidRequestRegistry.ask(GetBidRequest(id, _))

  def createBidRequest(bidRequest: BidRequest): Future[ActionPerformed] =
    bidRequestRegistry.ask(CreateBidRequest(bidRequest, _))

  def deleteBidRequest(id: String): Future[ActionPerformed] =
    bidRequestRegistry.ask(DeleteBidRequest(id, _))

  //#all-bidRequest-routes
  val bidRequestRoutes: Route =
    pathPrefix("bidRequests") {
      concat(
        //#bidRequests-get-create
        pathEnd {
          concat(
            get {
              complete(getBidRequests())
            },
            post {
              entity(as[BidRequest]) { bidRequest =>
                onSuccess(createBidRequest(bidRequest)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#bidRequests-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getBidRequest(id)) { response =>
                  complete(response.maybeBidRequest)
                }
              }
            },
            delete {
              onSuccess(deleteBidRequest(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}