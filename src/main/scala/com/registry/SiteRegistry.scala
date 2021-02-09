package com.registry

//#site-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable

//#site-case-classes
final case class Site(id: String, name: String, domain: String,  page: String, category: String)
final case class Sites(Sites: immutable.Seq[Site])

object SiteRegistry {

  // actor protocol
  sealed trait Command

  final case class GetSites(replyTo: ActorRef[Sites]) extends Command

  final case class CreateSite(Site: Site, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetSite(id: String, replyTo: ActorRef[GetSiteResponse]) extends Command

  final case class DeleteSite(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetSiteResponse(maybeSite: Option[Site])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(sites: Set[Site]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetSites(replyTo) =>
        replyTo ! Sites(sites.toSeq)
        Behaviors.same
      case CreateSite(site, replyTo) =>
        replyTo ! ActionPerformed(s"Site ${site.id} created.")
        registry(sites + site)
      case GetSite(id, replyTo) =>
        replyTo ! GetSiteResponse(sites.find(_.id == id))
        Behaviors.same
      case DeleteSite(id, replyTo) =>
        replyTo ! ActionPerformed(s"Site $id deleted.")
        registry(sites.filterNot(_.id == id))
    }
}

