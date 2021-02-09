package com.registry

//#deal-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable

//#deal-case-classes
final case class Deal(id: String, bidFloor: Double, currency: String,  auctionType: Int, wSeat: String, waDomain: String)
final case class Deals(Deals: immutable.Seq[Deal])

object DealRegistry {

  // actor protocol
  sealed trait Command

  final case class GetDeals(replyTo: ActorRef[Deals]) extends Command

  final case class CreateDeal(Deal: Deal, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetDeal(id: String, replyTo: ActorRef[GetDealResponse]) extends Command

  final case class DeleteDeal(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetDealResponse(maybeDeal: Option[Deal])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(deals: Set[Deal]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetDeals(replyTo) =>
        replyTo ! Deals(deals.toSeq)
        Behaviors.same
      case CreateDeal(deal, replyTo) =>
        replyTo ! ActionPerformed(s"Deal ${deal.id} created.")
        registry(deals + deal)
      case GetDeal(id, replyTo) =>
        replyTo ! GetDealResponse(deals.find(_.id == id))
        Behaviors.same
      case DeleteDeal(id, replyTo) =>
        replyTo ! ActionPerformed(s"Deal $id deleted.")
        registry(deals.filterNot(_.id == id))
    }
}

