package com.protocol.response

import com.protocol.campaign.Banner

case class BidResponse(id: String, bidRequestId: String, price: Double, adid: Option[String], banner: Option[Banner])
