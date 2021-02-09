package com.registry

//#impression-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable

//#impression-case-classes
final case class Impression(id: String, widthMin: Int, widthMax: Int,  width: Int, heightMin: Int, heightMax: Int, height: Int, bidFloor: Double)
final case class Impressions(Impressions: immutable.Seq[Impression])

object ImpressionRegistry {

  // actor protocol
  sealed trait Command

  final case class GetImpressions(replyTo: ActorRef[Impressions]) extends Command

  final case class CreateImpression(Impression: Impression, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetImpression(id: String, replyTo: ActorRef[GetImpressionResponse]) extends Command

  final case class DeleteImpression(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetImpressionResponse(maybeImpression: Option[Impression])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(impressions: Set[Impression]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetImpressions(replyTo) =>
        replyTo ! Impressions(impressions.toSeq)
        Behaviors.same
      case CreateImpression(impression, replyTo) =>
        replyTo ! ActionPerformed(s"Impression ${impression.id} created.")
        registry(impressions + impression)
      case GetImpression(id, replyTo) =>
        replyTo ! GetImpressionResponse(impressions.find(_.id == id))
        Behaviors.same
      case DeleteImpression(id, replyTo) =>
        replyTo ! ActionPerformed(s"Impression $id deleted.")
        registry(impressions.filterNot(_.id == id))
    }
}

