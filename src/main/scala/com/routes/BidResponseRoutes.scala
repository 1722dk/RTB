package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.BidResponseRegistry._
import com.registry.{BidResponse, BidResponseRegistry, BidResponses}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#bidResponse-routes-class
class BidResponseRoutes(bidResponseRegistry: ActorRef[BidResponseRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getBidResponses(): Future[BidResponses] =
    bidResponseRegistry.ask(GetBidResponses)

  def getBidResponse(id: String): Future[GetBidResponseResponse] =
    bidResponseRegistry.ask(GetBidResponse(id, _))

  def createBidResponse(bidResponse: BidResponse): Future[ActionPerformed] =
    bidResponseRegistry.ask(CreateBidResponse(bidResponse, _))

  def deleteBidResponse(id: String): Future[ActionPerformed] =
    bidResponseRegistry.ask(DeleteBidResponse(id, _))

  //#all-bidResponse-routes
  val bidResponseRoutes: Route =
    pathPrefix("bidResponses") {
      concat(
        //#bidResponses-get-create
        pathEnd {
          concat(
            get {
              complete(getBidResponses())
            },
            post {
              entity(as[BidResponse]) { bidResponse =>
                onSuccess(createBidResponse(bidResponse)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#bidResponses-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getBidResponse(id)) { response =>
                  complete(response.maybeBidResponse)
                }
              }
            },
            delete {
              onSuccess(deleteBidResponse(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}