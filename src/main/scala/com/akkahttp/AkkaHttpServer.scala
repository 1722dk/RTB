package com.akkahttp

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Directives, Route}
import com.registry._
import com.routes._

import scala.util.{Failure, Success}

//#AkkaHttp server object
object AkkaHttpServer {
  //#server-bootstrapping
  def bootstrappingServer(): Unit = {
    val rootBehavior = Behaviors.setup[Nothing] { context =>
      val bannerRegistryActor = context.spawn(BannerRegistry(), "BannerRegistryActor")
      val bidRequestRegistryActor = context.spawn(BidRequestRegistry(), "BidRequestRegistryActor")
      val bidResponseRegistryActor = context.spawn(BidResponseRegistry(), "BidResponseRegistryActor")
      val campaignRegistryActor = context.spawn(CampaignRegistry(), "CampaignRegistryActor")
      val cityRegistryActor = context.spawn(CityRegistry(), "CityActor")
      val countryRegistryActor = context.spawn(CountryRegistry(), "CountryRegistryActor")
      val dealRegistryActor = context.spawn(DealRegistry(), "DealRegistryActor")
      val deviceRegistryActor = context.spawn(DeviceRegistry(), "DeviceRegistryActor")
      val deviceTypeRegistryActor = context.spawn(DeviceTypeRegistry(), "DeviceTypeRegistryActor")
      val geoRegistryActor = context.spawn(GeoRegistry(), "GeoRegistryActor")
      val impressionRegistryActor = context.spawn(ImpressionRegistry(), "ImpressionRegistryActor")
      val pmpRegistryActor = context.spawn(PmpRegistry(), "PmpRegistryActor")
      val publisherRegistryActor = context.spawn(PublisherRegistry(), "PublisherRegistryActor")
      val siteRegistryActor = context.spawn(SiteRegistry(), "SiteRegistryActor")
      val targetedSiteRegistryActor = context.spawn(TargetedSiteRegistry(), "TargetedSiteRegistryActor")
      val userRegistryActor = context.spawn(UserRegistry(), "UserRegistryActor")

      context.watch(bannerRegistryActor)
      context.watch(bidRequestRegistryActor)
      context.watch(bidResponseRegistryActor)
      context.watch(campaignRegistryActor)
      context.watch(cityRegistryActor)
      context.watch(countryRegistryActor)
      context.watch(dealRegistryActor)
      context.watch(deviceRegistryActor)
      context.watch(deviceTypeRegistryActor)
      context.watch(geoRegistryActor)
      context.watch(impressionRegistryActor)
      context.watch(pmpRegistryActor)
      context.watch(publisherRegistryActor)
      context.watch(siteRegistryActor)
      context.watch(targetedSiteRegistryActor)
      context.watch(userRegistryActor)

      val bannerRoutes = new BannerRoutes(bannerRegistryActor)(context.system)
      val bidRequestRoutes = new BidRequestRoutes(bidRequestRegistryActor)(context.system)
      val bidResponseRoutes = new BidResponseRoutes(bidResponseRegistryActor)(context.system)
      val campaignRoutes = new CampaignRoutes(campaignRegistryActor)(context.system)
      val cityRoutes = new CityRoutes(cityRegistryActor)(context.system)
      val countryRoutes = new CountryRoutes(countryRegistryActor)(context.system)
      val dealRoutes = new DealRoutes(dealRegistryActor)(context.system)
      val deviceRoutes = new DeviceRoutes(deviceRegistryActor)(context.system)
      val deviceTypeRoutes = new DeviceTypeRoutes(deviceTypeRegistryActor)(context.system)
      val geoRoutes = new GeoRoutes(geoRegistryActor)(context.system)
      val impressionRoutes = new ImpressionRoutes(impressionRegistryActor)(context.system)
      val pmpRoutes = new PmpRoutes(pmpRegistryActor)(context.system)
      val publisherRoutes = new PublisherRoutes(publisherRegistryActor)(context.system)
      val siteRoutes = new SiteRoutes(siteRegistryActor)(context.system)
      val targetedSiteRoutes = new TargetedSiteRoutes(targetedSiteRegistryActor)(context.system)
      val usrRoutes = new UserRoutes(userRegistryActor)(context.system)

      val route = Directives.concat(
        bannerRoutes.bannerRoutes,
        bidRequestRoutes.bidRequestRoutes,
        bidResponseRoutes.bidResponseRoutes,
        campaignRoutes.campaignRoutes,
        cityRoutes.cityRoutes,
        countryRoutes.countryRoutes,
        dealRoutes.dealRoutes,
        deviceRoutes.deviceRoutes,
        deviceTypeRoutes.deviceTypeRoutes,
        geoRoutes.geoRoutes,
        impressionRoutes.impressionRoutes,
        pmpRoutes.pmpRoutes,
        publisherRoutes.publisherRoutes,
        siteRoutes.siteRoutes,
        targetedSiteRoutes.targetedSiteRoutes,
        usrRoutes.userRoutes,
      )

      this.startHttpServer(route)(context.system)
      //startHttpServer(routes.bannerRoutes)(context.system)

      Behaviors.empty
    }

    val system = ActorSystem[Nothing](rootBehavior, "RtbAkkaHttpServer")
  }

  //#start-http-server
  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    import system.executionContext

    val futureBinding = Http().newServerAt("localhost", 8080).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
}