package com.format

import com.registry._
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

object JsonFormats  {
  // import the default encoders for primitive types (Int, String, Lists etc)
  implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat6(User)
  implicit val usersJsonFormat: RootJsonFormat[Users] = jsonFormat1(Users)
  implicit val userActionPerformedJsonFormat: RootJsonFormat[UserRegistry.ActionPerformed] = jsonFormat1(UserRegistry.ActionPerformed)

  implicit val bannerJsonFormat: RootJsonFormat[Banner] = jsonFormat6(Banner)
  implicit val bannersJsonFormat: RootJsonFormat[Banners] = jsonFormat1(Banners)
  implicit val bannerActionPerformedJsonFormat: RootJsonFormat[BannerRegistry.ActionPerformed] = jsonFormat1(BannerRegistry.ActionPerformed)

  implicit val publisherJsonFormat: RootJsonFormat[Publisher] = jsonFormat4(Publisher)
  implicit val publishersJsonFormat: RootJsonFormat[Publishers] = jsonFormat1(Publishers)
  implicit val publisherActionPerformedJsonFormat: RootJsonFormat[PublisherRegistry.ActionPerformed] = jsonFormat1(PublisherRegistry.ActionPerformed)

  implicit val siteJsonFormat: RootJsonFormat[Site] = jsonFormat5(Site)
  implicit val sitesJsonFormat: RootJsonFormat[Sites] = jsonFormat1(Sites)
  implicit val siteActionPerformedJsonFormat: RootJsonFormat[SiteRegistry.ActionPerformed] = jsonFormat1(SiteRegistry.ActionPerformed)

  implicit val geoJsonFormat: RootJsonFormat[Geo] = jsonFormat5(Geo)
  implicit val geosJsonFormat: RootJsonFormat[Geos] = jsonFormat1(Geos)
  implicit val geoActionPerformedJsonFormat: RootJsonFormat[GeoRegistry.ActionPerformed] = jsonFormat1(GeoRegistry.ActionPerformed)

  implicit val impressionJsonFormat: RootJsonFormat[Impression] = jsonFormat8(Impression)
  implicit val impressionsJsonFormat: RootJsonFormat[Impressions] = jsonFormat1(Impressions)
  implicit val impressionActionPerformedJsonFormat: RootJsonFormat[ImpressionRegistry.ActionPerformed] = jsonFormat1(ImpressionRegistry.ActionPerformed)

  implicit val dealJsonFormat: RootJsonFormat[Deal] = jsonFormat6(Deal)
  implicit val dealsJsonFormat: RootJsonFormat[Deals] = jsonFormat1(Deals)
  implicit val dealActionPerformedJsonFormat: RootJsonFormat[DealRegistry.ActionPerformed] = jsonFormat1(DealRegistry.ActionPerformed)

  implicit val deviceTypeJsonFormat: RootJsonFormat[DeviceType] = jsonFormat4(DeviceType)
  implicit val deviceTypesJsonFormat: RootJsonFormat[DeviceTypes] = jsonFormat1(DeviceTypes)
  implicit val deviceTypeActionPerformedJsonFormat: RootJsonFormat[DeviceTypeRegistry.ActionPerformed] = jsonFormat1(DeviceTypeRegistry.ActionPerformed)

  implicit val pmpJsonFormat: RootJsonFormat[Pmp] = jsonFormat3(Pmp)
  implicit val pmpsJsonFormat: RootJsonFormat[Pmps] = jsonFormat1(Pmps)
  implicit val pmpActionPerformedJsonFormat: RootJsonFormat[PmpRegistry.ActionPerformed] = jsonFormat1(PmpRegistry.ActionPerformed)

  implicit val deviceJsonFormat: RootJsonFormat[Device] = jsonFormat7(Device)
  implicit val devicesJsonFormat: RootJsonFormat[Devices] = jsonFormat1(Devices)
  implicit val deviceActionPerformedJsonFormat: RootJsonFormat[DeviceRegistry.ActionPerformed] = jsonFormat1(DeviceRegistry.ActionPerformed)

  implicit val countryJsonFormat: RootJsonFormat[Country] = jsonFormat3(Country)
  implicit val countriesJsonFormat: RootJsonFormat[Countries] = jsonFormat1(Countries)
  implicit val countryActionPerformedJsonFormat: RootJsonFormat[CountryRegistry.ActionPerformed] = jsonFormat1(CountryRegistry.ActionPerformed)

  implicit val cityJsonFormat: RootJsonFormat[City] = jsonFormat3(City)
  implicit val citiesJsonFormat: RootJsonFormat[Cities] = jsonFormat1(Cities)
  implicit val cityActionPerformedJsonFormat: RootJsonFormat[CityRegistry.ActionPerformed] = jsonFormat1(CityRegistry.ActionPerformed)

  implicit val targetedSiteJsonFormat: RootJsonFormat[TargetedSite] = jsonFormat4(TargetedSite)
  implicit val targetedSitesJsonFormat: RootJsonFormat[TargetedSites] = jsonFormat1(TargetedSites)
  implicit val targetedSiteActionPerformedJsonFormat: RootJsonFormat[TargetedSiteRegistry.ActionPerformed] = jsonFormat1(TargetedSiteRegistry.ActionPerformed)

  implicit val campaignJsonFormat: RootJsonFormat[Campaign] = jsonFormat8(Campaign)
  implicit val campaignsJsonFormat: RootJsonFormat[Campaigns] = jsonFormat1(Campaigns)
  implicit val campaignActionPerformedJsonFormat: RootJsonFormat[CampaignRegistry.ActionPerformed] = jsonFormat1(CampaignRegistry.ActionPerformed)

  implicit val bidRequestJsonFormat: RootJsonFormat[BidRequest] = jsonFormat8(BidRequest)
  implicit val bidRequestsJsonFormat: RootJsonFormat[BidRequests] = jsonFormat1(BidRequests)
  implicit val bidRequestActionPerformedJsonFormat: RootJsonFormat[BidRequestRegistry.ActionPerformed] = jsonFormat1(BidRequestRegistry.ActionPerformed)

  implicit val bidResponseJsonFormat: RootJsonFormat[BidResponse] = jsonFormat6(BidResponse)
  implicit val bidResponsesJsonFormat: RootJsonFormat[BidResponses] = jsonFormat1(BidResponses)
  implicit val bidResponseActionPerformedJsonFormat: RootJsonFormat[BidResponseRegistry.ActionPerformed] = jsonFormat1(BidResponseRegistry.ActionPerformed)
}
//#json-formats
