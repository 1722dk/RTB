package com.registry

//#country-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable

//#country-case-classes
final case class Country(id: String, name: String, notes: String)
final case class Countries(Countries: immutable.Seq[Country])

object CountryRegistry {

  // actor protocol
  sealed trait Command

  final case class GetCountries(replyTo: ActorRef[Countries]) extends Command

  final case class CreateCountry(Country: Country, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetCountry(id: String, replyTo: ActorRef[GetCountryResponse]) extends Command

  final case class DeleteCountry(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetCountryResponse(maybeCountry: Option[Country])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(countries: Set[Country]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetCountries(replyTo) =>
        replyTo ! Countries(countries.toSeq)
        Behaviors.same
      case CreateCountry(country, replyTo) =>
        replyTo ! ActionPerformed(s"Country ${country.id} created.")
        registry(countries + country)
      case GetCountry(id, replyTo) =>
        replyTo ! GetCountryResponse(countries.find(_.id == id))
        Behaviors.same
      case DeleteCountry(id, replyTo) =>
        replyTo ! ActionPerformed(s"Country $id deleted.")
        registry(countries.filterNot(_.id == id))
    }
}

