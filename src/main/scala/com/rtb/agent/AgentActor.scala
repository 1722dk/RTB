package com.rtb.agent

import akka.actor.Actor
import com.protocol.utility.UserInput

class AgentActor extends Actor {
  def receive() = {
    case userInput: UserInput => {
      var agent = new BidProcessor()
      agent.getTopBidder(userInput.bidPrice, userInput.addHeight, userInput.addWidth)
    }
    case _ => println("Invalid User Input")
  }
}