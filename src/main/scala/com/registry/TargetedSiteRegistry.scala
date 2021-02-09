package com.registry

//#targetedSite-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable

//#targetedSite-case-classes
final case class TargetedSite(id: String, name: String, domain: String,  url: String)
final case class TargetedSites(TargetedSites: immutable.Seq[TargetedSite])

object TargetedSiteRegistry {

  // actor protocol
  sealed trait Command

  final case class GetTargetedSites(replyTo: ActorRef[TargetedSites]) extends Command

  final case class CreateTargetedSite(TargetedSite: TargetedSite, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetTargetedSite(id: String, replyTo: ActorRef[GetTargetedSiteResponse]) extends Command

  final case class DeleteTargetedSite(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetTargetedSiteResponse(maybeTargetedSite: Option[TargetedSite])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(targetedSites: Set[TargetedSite]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetTargetedSites(replyTo) =>
        replyTo ! TargetedSites(targetedSites.toSeq)
        Behaviors.same
      case CreateTargetedSite(targetedSite, replyTo) =>
        replyTo ! ActionPerformed(s"TargetedSite ${targetedSite.id} created.")
        registry(targetedSites + targetedSite)
      case GetTargetedSite(id, replyTo) =>
        replyTo ! GetTargetedSiteResponse(targetedSites.find(_.id == id))
        Behaviors.same
      case DeleteTargetedSite(id, replyTo) =>
        replyTo ! ActionPerformed(s"TargetedSite $id deleted.")
        registry(targetedSites.filterNot(_.id == id))
    }
}

