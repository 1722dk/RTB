package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.SiteRegistry._
import com.registry.{Site, SiteRegistry, Sites}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#site-routes-class
class SiteRoutes(siteRegistry: ActorRef[SiteRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getSites(): Future[Sites] =
    siteRegistry.ask(GetSites)

  def getSite(id: String): Future[GetSiteResponse] =
    siteRegistry.ask(GetSite(id, _))

  def createSite(site: Site): Future[ActionPerformed] =
    siteRegistry.ask(CreateSite(site, _))

  def deleteSite(id: String): Future[ActionPerformed] =
    siteRegistry.ask(DeleteSite(id, _))

  //#all-site-routes
  val siteRoutes: Route =
    pathPrefix("sites") {
      concat(
        //#sites-get-create
        pathEnd {
          concat(
            get {
              complete(getSites())
            },
            post {
              entity(as[Site]) { site =>
                onSuccess(createSite(site)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#sites-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getSite(id)) { response =>
                  complete(response.maybeSite)
                }
              }
            },
            delete {
              onSuccess(deleteSite(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}