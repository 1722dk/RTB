package com.protocol.utility

import com.protocol.campaign.Banner
import com.protocol.request.Impression

class SampleData {
  var banners: List[Banner] = List(
    Banner(1, "RCC", 1, 2),
    Banner(2, "MCC", 2, 4),
    Banner(3, "FCC", 3, 3),
    Banner(4, "JCC", 4, 4),
    Banner(5, "CCC", 3, 5),
    Banner(6, "CCR", 2, 2))

  val cities: List[String] = List("Dhaka", "Rajshahi", "Chittagong", "Rangpur", "Khulna", "Comilla")
  val targetedSiteIds: List[String] = List(
    "https://www.google.com/",
    "https://www.facebook.com/",
    "https://twitter.com/",
    "https://www.youtube.com/",
    "https://www.whatsapp.com/",
    "http://www.qq.com/",
    "http://www.wechat.com/",
    "http://qzone.qq.com/",
    "http://tumblr.com/",
    "http://www.instagram.com/",
    "http://tieba.baidu.com/",
    "http://skype.com/",
    "http://www.viber.com/",
    "http://weibo.com/",
    "http://line.me/",
    "https://www.snapchat.com/",
    "http://pinterest.com/",
    "http://www.linkedin.com/",
    "https://www.reddit.com/",
  )

  var impressions: List[Impression] = List(
    Impression("1", Some(1), Some(5), Some(1), Some(1), Some(10), Some(2), Some(25.50)),
    Impression("2", Some(2), Some(4), Some(2), Some(2), Some(8), Some(4), Some(20.50)),
    Impression("3", Some(3), Some(3), Some(3), Some(3), Some(6), Some(3), Some(30.50)),
    Impression("4", Some(4), Some(4), Some(4), Some(4), Some(4), Some(4), Some(15.50)),
    Impression("5", Some(3), Some(5), Some(3), Some(3), Some(6), Some(5), Some(10.50)),
    Impression("6", Some(2), Some(4), Some(2), Some(2), Some(4), Some(2), Some(35.50))
  )
}