package com.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.format.JsonFormats._
import com.registry.PublisherRegistry._
import com.registry.{Publisher, PublisherRegistry, Publishers}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

//#publisher-routes-class
class PublisherRoutes(publisherRegistry: ActorRef[PublisherRegistry.Command])(implicit val system: ActorSystem[_]) {
  // If ask takes more time than this to complete the request is failed
  //private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val timeout = Timeout(5 seconds);

  def getPublishers(): Future[Publishers] =
    publisherRegistry.ask(GetPublishers)

  def getPublisher(name: String): Future[GetPublisherResponse] =
    publisherRegistry.ask(GetPublisher(name, _))

  def createPublisher(publisher: Publisher): Future[ActionPerformed] =
    publisherRegistry.ask(CreatePublisher(publisher, _))

  def deletePublisher(name: String): Future[ActionPerformed] =
    publisherRegistry.ask(DeletePublisher(name, _))

  //#all-publisher-routes
  val publisherRoutes: Route =
    pathPrefix("publishers") {
      concat(
        //#publishers-get-create
        pathEnd {
          concat(
            get {
              complete(getPublishers())
            },
            post {
              entity(as[Publisher]) { publisher =>
                onSuccess(createPublisher(publisher)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#publishers-get-delete
        path(Segment) { name =>
          concat(
            get {
              rejectEmptyResponse {
                onSuccess(getPublisher(name)) { response =>
                  complete(response.maybePublisher)
                }
              }
            },
            delete {
              onSuccess(deletePublisher(name)) { performed =>
                complete((StatusCodes.OK, performed))
              }
            })
        })
    }
}