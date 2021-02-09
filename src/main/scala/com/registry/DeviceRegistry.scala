package com.registry

//#device-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable

//#device-case-classes
final case class Device(id: String, make: String, model: String,  os: String, language: String, deviceTypeId: Int, geoId: Int)
final case class Devices(Devices: immutable.Seq[Device])

object DeviceRegistry {

  // actor protocol
  sealed trait Command

  final case class GetDevices(replyTo: ActorRef[Devices]) extends Command

  final case class CreateDevice(Device: Device, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetDevice(id: String, replyTo: ActorRef[GetDeviceResponse]) extends Command

  final case class DeleteDevice(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetDeviceResponse(maybeDevice: Option[Device])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(devices: Set[Device]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetDevices(replyTo) =>
        replyTo ! Devices(devices.toSeq)
        Behaviors.same
      case CreateDevice(device, replyTo) =>
        replyTo ! ActionPerformed(s"Device ${device.id} created.")
        registry(devices + device)
      case GetDevice(id, replyTo) =>
        replyTo ! GetDeviceResponse(devices.find(_.id == id))
        Behaviors.same
      case DeleteDevice(id, replyTo) =>
        replyTo ! ActionPerformed(s"Device $id deleted.")
        registry(devices.filterNot(_.id == id))
    }
}

