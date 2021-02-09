package com.registry

//#deviceType-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable

//#deviceType-case-classes
final case class DeviceType(id: String, name: String, description: String,  notes: String)
final case class DeviceTypes(DeviceTypes: immutable.Seq[DeviceType])

object DeviceTypeRegistry {

  // actor protocol
  sealed trait Command

  final case class GetDeviceTypes(replyTo: ActorRef[DeviceTypes]) extends Command

  final case class CreateDeviceType(DeviceType: DeviceType, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetDeviceType(id: String, replyTo: ActorRef[GetDeviceTypeResponse]) extends Command

  final case class DeleteDeviceType(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetDeviceTypeResponse(maybeDeviceType: Option[DeviceType])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(deviceTypes: Set[DeviceType]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetDeviceTypes(replyTo) =>
        replyTo ! DeviceTypes(deviceTypes.toSeq)
        Behaviors.same
      case CreateDeviceType(deviceType, replyTo) =>
        replyTo ! ActionPerformed(s"DeviceType ${deviceType.id} created.")
        registry(deviceTypes + deviceType)
      case GetDeviceType(id, replyTo) =>
        replyTo ! GetDeviceTypeResponse(deviceTypes.find(_.id == id))
        Behaviors.same
      case DeleteDeviceType(id, replyTo) =>
        replyTo ! ActionPerformed(s"DeviceType $id deleted.")
        registry(deviceTypes.filterNot(_.id == id))
    }
}

