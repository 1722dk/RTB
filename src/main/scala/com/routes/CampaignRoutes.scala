package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.CampaignRegistry._
import com.registry.{Campaign, CampaignRegistry, Campaigns}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#campaign-routes-class
class CampaignRoutes(campaignRegistry: ActorRef[CampaignRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getCampaigns(): Future[Campaigns] =
    campaignRegistry.ask(GetCampaigns)

  def getCampaign(id: String): Future[GetCampaignResponse] =
    campaignRegistry.ask(GetCampaign(id, _))

  def createCampaign(campaign: Campaign): Future[ActionPerformed] =
    campaignRegistry.ask(CreateCampaign(campaign, _))

  def deleteCampaign(id: String): Future[ActionPerformed] =
    campaignRegistry.ask(DeleteCampaign(id, _))

  //#all-campaign-routes
  val campaignRoutes: Route =
    pathPrefix("campaigns") {
      concat(
        //#campaigns-get-create
        pathEnd {
          concat(
            get {
              complete(getCampaigns())
            },
            post {
              entity(as[Campaign]) { campaign =>
                onSuccess(createCampaign(campaign)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#campaigns-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getCampaign(id)) { response =>
                  complete(response.maybeCampaign)
                }
              }
            },
            delete {
              onSuccess(deleteCampaign(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}