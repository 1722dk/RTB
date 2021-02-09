package com.registry

//#publisher-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable

//#publisher-case-classes
final case class Publisher(id: Int, name: String, domain: String, category: String)
final case class Publishers(publishers: immutable.Seq[Publisher])

object PublisherRegistry {

  // actor protocol
  sealed trait Command

  final case class GetPublishers(replyTo: ActorRef[Publishers]) extends Command

  final case class CreatePublisher(publisher: Publisher, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetPublisher(name: String, replyTo: ActorRef[GetPublisherResponse]) extends Command

  final case class DeletePublisher(name: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetPublisherResponse(maybePublisher: Option[Publisher])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(publishers: Set[Publisher]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetPublishers(replyTo) =>
        replyTo ! Publishers(publishers.toSeq)
        Behaviors.same
      case CreatePublisher(publisher, replyTo) =>
        replyTo ! ActionPerformed(s"Publisher ${publisher.name} created.")
        registry(publishers + publisher)
      case GetPublisher(name, replyTo) =>
        replyTo ! GetPublisherResponse(publishers.find(_.name == name))
        Behaviors.same
      case DeletePublisher(name, replyTo) =>
        replyTo ! ActionPerformed(s"Publisher $name deleted.")
        registry(publishers.filterNot(_.name == name))
    }
}

