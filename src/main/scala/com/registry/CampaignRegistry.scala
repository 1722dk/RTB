package com.registry

//#campaign-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable

//#campaign-case-classes
final case class Campaign(id: String, bidPrice: Double, currency: String,  cityId: Int, countryId: Int, userId: Int, targetSiteId: Int, bannerId: Int)
final case class Campaigns(Campaigns: immutable.Seq[Campaign])

object CampaignRegistry {

  // actor protocol
  sealed trait Command

  final case class GetCampaigns(replyTo: ActorRef[Campaigns]) extends Command

  final case class CreateCampaign(Campaign: Campaign, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetCampaign(id: String, replyTo: ActorRef[GetCampaignResponse]) extends Command

  final case class DeleteCampaign(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetCampaignResponse(maybeCampaign: Option[Campaign])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(campaigns: Set[Campaign]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetCampaigns(replyTo) =>
        replyTo ! Campaigns(campaigns.toSeq)
        Behaviors.same
      case CreateCampaign(campaign, replyTo) =>
        replyTo ! ActionPerformed(s"Campaign ${campaign.id} created.")
        registry(campaigns + campaign)
      case GetCampaign(id, replyTo) =>
        replyTo ! GetCampaignResponse(campaigns.find(_.id == id))
        Behaviors.same
      case DeleteCampaign(id, replyTo) =>
        replyTo ! ActionPerformed(s"Campaign $id deleted.")
        registry(campaigns.filterNot(_.id == id))
    }
}

