package com.rtb.agent

import com.protocol.campaign.{Banner, Campaign, Targeting}
import com.protocol.request.{BidRequest, Device, Geo, Site, User}
import com.protocol.response.BidResponse
import com.protocol.utility.{BidCompare, SampleData}
import net.liftweb.json.DefaultFormats
import net.liftweb.json.Serialization.write
import scala.collection.mutable.ListBuffer

class BidProcessor {

  def getTopBidder(bidPrice: Double, addHeight: Int, addWidth: Int) = {
    var data = new SampleData()
    var site = Site(1, "https://www.google.com/")
    var geo = Geo(Some("Bangladesh"), Some("Dhaka"), Some(23.777176), Some(90.399452))
    var user = User("1", Some(geo))
    var device = Device("1", Some(geo))
    var bidRequest = BidRequest("1", Some(data.impressions), site, Some(user), Some(device))
    var targeting = Targeting(data.cities, data.targetedSiteIds)
    var campaign = Campaign(1, 1, "Bangladesh", targeting, data.banners, 20.50)
    var bidResponse = BidResponse("1", "1", 20.50, Some("1"), Some(data.banners.head))

    var meetTheBidFloor = false
    for (impression <- data.impressions) {
      var bidFloor = impression.bidFloor.getOrElse(0.00)
      if (bidPrice >= bidFloor) {
        meetTheBidFloor = true
      }
    }

    if (!meetTheBidFloor) {
      println("Your bid price lower than bid floor. Please raise your bid price...")
    } else {
      var topBidders = new ListBuffer[BidCompare]()
      for (impression <- data.impressions) {
        var bidFloor = impression.bidFloor.getOrElse(0.00)

        var minHeight = impression.hmin.getOrElse(0)
        var maxHeight = impression.hmax.getOrElse(0)
        var height = impression.h.getOrElse(0)

        var minWidth = impression.wmin.getOrElse(0)
        var maxWidth = impression.wmax.getOrElse(0)
        var width = impression.w.getOrElse(0)

        if (bidPrice >= bidFloor) {
          if (addHeight <= maxHeight && addHeight >= minHeight && addWidth <= maxWidth && addWidth >= minWidth) {
            var topBidder = BidCompare(impression.id, bidPrice)
            topBidders += topBidder
          }
        }
      }

      var topBidPrice: Double = 0.0
      for (topBidder <- topBidders) {
        if (topBidder.bidPrice > topBidPrice) {
          topBidPrice = topBidder.bidPrice
        }
      }

      if (topBidders.size == 0) {
        println("No content found.")
      } else {
        var bidObject = topBidders.find(tb => tb.bidPrice == topBidPrice).head
        if (bidObject != null) {
          var bidWinner = data.impressions.find(imp => imp.id == bidObject.id).head
          println()
          println("Real Time Bid Winner's details")
          //println(s"Add Id: ${bidWinner.id}")
          //println(s"Add Height: ${bidWinner.h.getOrElse(0)}, Add Width: ${bidWinner.w.getOrElse(0)}")
          //println(s"Bid Price: ${bidObject.bidPrice}")

          var banner = data.banners.find(b => b.id == bidWinner.id.toInt).head
          var response = BidResponse(bidWinner.id, "BRI_001", bidObject.bidPrice, Some("RTB_AABB"), Some(banner))
          implicit val formats = DefaultFormats
          val jsonResponse = write(response)
          //println()
          println("JSON Response:")
          println(jsonResponse)
        }
      }
    }
  }
}