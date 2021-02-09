package com.registry

//#pmp-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable

//#pmp-case-classes
final case class Pmp(id: String, privateAuction: Int, dealId: Int)
final case class Pmps(Pmps: immutable.Seq[Pmp])

object PmpRegistry {

  // actor protocol
  sealed trait Command

  final case class GetPmps(replyTo: ActorRef[Pmps]) extends Command

  final case class CreatePmp(Pmp: Pmp, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetPmp(id: String, replyTo: ActorRef[GetPmpResponse]) extends Command

  final case class DeletePmp(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetPmpResponse(maybePmp: Option[Pmp])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(pmps: Set[Pmp]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetPmps(replyTo) =>
        replyTo ! Pmps(pmps.toSeq)
        Behaviors.same
      case CreatePmp(pmp, replyTo) =>
        replyTo ! ActionPerformed(s"Pmp ${pmp.id} created.")
        registry(pmps + pmp)
      case GetPmp(id, replyTo) =>
        replyTo ! GetPmpResponse(pmps.find(_.id == id))
        Behaviors.same
      case DeletePmp(id, replyTo) =>
        replyTo ! ActionPerformed(s"Pmp $id deleted.")
        registry(pmps.filterNot(_.id == id))
    }
}

