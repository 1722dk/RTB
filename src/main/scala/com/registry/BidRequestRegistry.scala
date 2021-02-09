package com.registry

//#bidRequest-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import com.datacontext.BidRequestDataContext

import scala.collection.immutable

//#bidRequest-case-classes
final case class BidRequest(id: String, auctionType: Int, currency: String,  siteId: Int, deviceId: Int, bannerId: Int, publisherId: Int, impressionId: Int)
final case class BidRequests(BidRequests: immutable.Seq[BidRequest])

object BidRequestRegistry {

  // actor protocol
  sealed trait Command

  final case class GetBidRequests(replyTo: ActorRef[BidRequests]) extends Command

  final case class CreateBidRequest(BidRequest: BidRequest, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetBidRequest(id: String, replyTo: ActorRef[GetBidRequestResponse]) extends Command

  final case class DeleteBidRequest(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetBidRequestResponse(maybeBidRequest: Option[BidRequest])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(bidRequests: Set[BidRequest]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetBidRequests(replyTo) =>
        val dbBidRequests = BidRequestDataContext.getBidRequests()
        replyTo ! BidRequests(dbBidRequests.toSeq)
        Behaviors.same
      case CreateBidRequest(bidRequest, replyTo) =>
        replyTo ! ActionPerformed(s"BidRequest ${bidRequest.id} created.")
        registry(bidRequests + bidRequest)
      case GetBidRequest(id, replyTo) =>
        val dbBidRequest = BidRequestDataContext.getBidRequest(id)
        replyTo ! GetBidRequestResponse(dbBidRequest)
        //replyTo ! GetBidRequestResponse(bidRequests.find(_.id == id))
        Behaviors.same
      case DeleteBidRequest(id, replyTo) =>
        replyTo ! ActionPerformed(s"BidRequest $id deleted.")
        registry(bidRequests.filterNot(_.id == id))
    }
}

