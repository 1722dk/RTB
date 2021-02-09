package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.ImpressionRegistry._
import com.registry.{Impression, ImpressionRegistry, Impressions}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#impression-routes-class
class ImpressionRoutes(impressionRegistry: ActorRef[ImpressionRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getImpressions(): Future[Impressions] =
    impressionRegistry.ask(GetImpressions)

  def getImpression(id: String): Future[GetImpressionResponse] =
    impressionRegistry.ask(GetImpression(id, _))

  def createImpression(impression: Impression): Future[ActionPerformed] =
    impressionRegistry.ask(CreateImpression(impression, _))

  def deleteImpression(id: String): Future[ActionPerformed] =
    impressionRegistry.ask(DeleteImpression(id, _))

  //#all-impression-routes
  val impressionRoutes: Route =
    pathPrefix("impressions") {
      concat(
        //#impressions-get-create
        pathEnd {
          concat(
            get {
              complete(getImpressions())
            },
            post {
              entity(as[Impression]) { impression =>
                onSuccess(createImpression(impression)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#impressions-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getImpression(id)) { response =>
                  complete(response.maybeImpression)
                }
              }
            },
            delete {
              onSuccess(deleteImpression(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}