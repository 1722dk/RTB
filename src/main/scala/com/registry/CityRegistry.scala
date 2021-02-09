package com.registry

//#city-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable

//#city-case-classes
final case class City(id: String, name: String, notes: String)
final case class Cities(Cities: immutable.Seq[City])

object CityRegistry {

  // actor protocol
  sealed trait Command

  final case class GetCities(replyTo: ActorRef[Cities]) extends Command

  final case class CreateCity(City: City, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetCity(id: String, replyTo: ActorRef[GetCityResponse]) extends Command

  final case class DeleteCity(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetCityResponse(maybeCity: Option[City])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(cities: Set[City]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetCities(replyTo) =>
        replyTo ! Cities(cities.toSeq)
        Behaviors.same
      case CreateCity(city, replyTo) =>
        replyTo ! ActionPerformed(s"City ${city.id} created.")
        registry(cities + city)
      case GetCity(id, replyTo) =>
        replyTo ! GetCityResponse(cities.find(_.id == id))
        Behaviors.same
      case DeleteCity(id, replyTo) =>
        replyTo ! ActionPerformed(s"City $id deleted.")
        registry(cities.filterNot(_.id == id))
    }
}

