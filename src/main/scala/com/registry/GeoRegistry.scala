package com.registry

//#geo-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable

//#geo-case-classes
final case class Geo(id: String, country: String, city: String,  lat: Double, lon: Double)
final case class Geos(Geos: immutable.Seq[Geo])

object GeoRegistry {

  // actor protocol
  sealed trait Command

  final case class GetGeos(replyTo: ActorRef[Geos]) extends Command

  final case class CreateGeo(Geo: Geo, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetGeo(id: String, replyTo: ActorRef[GetGeoResponse]) extends Command

  final case class DeleteGeo(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetGeoResponse(maybeGeo: Option[Geo])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(geos: Set[Geo]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetGeos(replyTo) =>
        replyTo ! Geos(geos.toSeq)
        Behaviors.same
      case CreateGeo(geo, replyTo) =>
        replyTo ! ActionPerformed(s"Geo ${geo.id} created.")
        registry(geos + geo)
      case GetGeo(id, replyTo) =>
        replyTo ! GetGeoResponse(geos.find(_.id == id))
        Behaviors.same
      case DeleteGeo(id, replyTo) =>
        replyTo ! ActionPerformed(s"Geo $id deleted.")
        registry(geos.filterNot(_.id == id))
    }
}

