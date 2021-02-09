package com.registry

//#banner-registry-actor
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import com.datacontext.BannerDataContext

import scala.collection.immutable

//#banner-case-classes
final case class Banner(id: String, source: String, description: String,  width: Int, height: Int, position: Int)
final case class Banners(Banners: immutable.Seq[Banner])

object BannerRegistry {

  // actor protocol
  sealed trait Command

  final case class GetBanners(replyTo: ActorRef[Banners]) extends Command

  final case class CreateBanner(Banner: Banner, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetBanner(id: String, replyTo: ActorRef[GetBannerResponse]) extends Command

  final case class DeleteBanner(id: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetBannerResponse(maybeBanner: Option[Banner])

  final case class ActionPerformed(description: String)

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(banners: Set[Banner]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetBanners(replyTo) =>
        val dbBanners = BannerDataContext.getBanners()
        replyTo ! Banners(dbBanners.toSeq)
        Behaviors.same
      case CreateBanner(banner, replyTo) =>
        replyTo ! ActionPerformed(s"Banner ${banner.id} created.")
        registry(banners + banner)
      case GetBanner(id, replyTo) =>
        val dbBanner = BannerDataContext.getBanner(id)
        replyTo ! GetBannerResponse(dbBanner)
        //replyTo ! GetBannerResponse(banners.find(_.id == id))
        Behaviors.same
      case DeleteBanner(id, replyTo) =>
        replyTo ! ActionPerformed(s"Banner $id deleted.")
        registry(banners.filterNot(_.id == id))
    }
}

