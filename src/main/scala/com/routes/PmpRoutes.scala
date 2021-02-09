package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.PmpRegistry._
import com.registry.{Pmp, PmpRegistry, Pmps}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#pmp-routes-class
class PmpRoutes(pmpRegistry: ActorRef[PmpRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getPmps(): Future[Pmps] =
    pmpRegistry.ask(GetPmps)

  def getPmp(id: String): Future[GetPmpResponse] =
    pmpRegistry.ask(GetPmp(id, _))

  def createPmp(pmp: Pmp): Future[ActionPerformed] =
    pmpRegistry.ask(CreatePmp(pmp, _))

  def deletePmp(id: String): Future[ActionPerformed] =
    pmpRegistry.ask(DeletePmp(id, _))

  //#all-pmp-routes
  val pmpRoutes: Route =
    pathPrefix("pmps") {
      concat(
        //#pmps-get-create
        pathEnd {
          concat(
            get {
              complete(getPmps())
            },
            post {
              entity(as[Pmp]) { pmp =>
                onSuccess(createPmp(pmp)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#pmps-get-delete
        path(Segment) { id =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getPmp(id)) { response =>
                  complete(response.maybePmp)
                }
              }
            },
            delete {
              onSuccess(deletePmp(id)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}