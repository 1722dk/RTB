package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.BannerRegistry._
import com.registry.{Banner, BannerRegistry, Banners}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#banner-routes-class
class BannerRoutes(bannerRegistry: ActorRef[BannerRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getBanners(): Future[Banners] =
    bannerRegistry.ask(GetBanners)

  def getBanner(id: String): Future[GetBannerResponse] =
    bannerRegistry.ask(GetBanner(id, _))

  def createBanner(banner: Banner): Future[ActionPerformed] =
    bannerRegistry.ask(CreateBanner(banner, _))

  def deleteBanner(id: String): Future[ActionPerformed] =
    bannerRegistry.ask(DeleteBanner(id, _))

  //#all-banner-routes
  val  bannerRoutes: Route =
    pathPrefix("banners") {
      concat(
        //#banners-get-create
        pathEnd {
          concat(
            get {
              complete(getBanners())
            },
            post {
              entity(as[Banner]) { banner =>
                onSuccess(createBanner(banner)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#banners-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getBanner(id)) { response =>
                  complete(response.maybeBanner)
                }
              }
            },
            delete {
              onSuccess(deleteBanner(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}