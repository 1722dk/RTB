package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.TargetedSiteRegistry._
import com.registry.{TargetedSite, TargetedSiteRegistry, TargetedSites}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#targetedSite-routes-class
class TargetedSiteRoutes(targetedSiteRegistry: ActorRef[TargetedSiteRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getTargetedSites(): Future[TargetedSites] =
    targetedSiteRegistry.ask(GetTargetedSites)

  def getTargetedSite(id: String): Future[GetTargetedSiteResponse] =
    targetedSiteRegistry.ask(GetTargetedSite(id, _))

  def createTargetedSite(targetedSite: TargetedSite): Future[ActionPerformed] =
    targetedSiteRegistry.ask(CreateTargetedSite(targetedSite, _))

  def deleteTargetedSite(id: String): Future[ActionPerformed] =
    targetedSiteRegistry.ask(DeleteTargetedSite(id, _))

  //#all-targetedSite-routes
  val targetedSiteRoutes: Route =
    pathPrefix("targetedSites") {
      concat(
        //#targetedSites-get-create
        pathEnd {
          concat(
            get {
              complete(getTargetedSites())
            },
            post {
              entity(as[TargetedSite]) { targetedSite =>
                onSuccess(createTargetedSite(targetedSite)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#targetedSites-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getTargetedSite(id)) { response =>
                  complete(response.maybeTargetedSite)
                }
              }
            },
            delete {
              onSuccess(deleteTargetedSite(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}