package com.protocol.campaign

case class Campaign(id: Int, userId: Int, country: String, targeting: Targeting, banners: List[Banner], bid: Double)
