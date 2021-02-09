package com.registry

//#bidResponse-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable

//#bidResponse-case-classes
final case class BidResponse(id: String, price: Double, currency: String,  bidRequestId: String, adId: Int, bannerId: Int)
final case class BidResponses(BidResponses: immutable.Seq[BidResponse])

object BidResponseRegistry {

  // actor protocol
  sealed trait Command

  final case class GetBidResponses(replyTo: ActorRef[BidResponses]) extends Command

  final case class CreateBidResponse(BidResponse: BidResponse, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetBidResponse(id: String, replyTo: ActorRef[GetBidResponseResponse]) extends Command

  final case class DeleteBidResponse(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetBidResponseResponse(maybeBidResponse: Option[BidResponse])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(bidResponses: Set[BidResponse]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetBidResponses(replyTo) =>
        replyTo ! BidResponses(bidResponses.toSeq)
        Behaviors.same
      case CreateBidResponse(bidResponse, replyTo) =>
        replyTo ! ActionPerformed(s"BidResponse ${bidResponse.id} created.")
        registry(bidResponses + bidResponse)
      case GetBidResponse(id, replyTo) =>
        replyTo ! GetBidResponseResponse(bidResponses.find(_.id == id))
        Behaviors.same
      case DeleteBidResponse(id, replyTo) =>
        replyTo ! ActionPerformed(s"BidResponse $id deleted.")
        registry(bidResponses.filterNot(_.id == id))
    }
}

