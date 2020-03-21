package com.rtb.agent

import akka.actor.{ActorSystem, Props}
import com.protocol.utility.UserInput

object Program {
  def main(args: Array[String]): Unit = {
    println("Used Tools: Java SDK-1.8, Scala SDK-2.13.1, AkkaVersion = 2.6.1, Lift-JSON - 3.4.1")
    println("Real Time Biding Agent")

    var counter = 0
    do {
      print("Enter your bid price: ")
      val userBidPrice = scala.io.StdIn.readDouble()
      print("Enter your add height: ")
      val userAddHeight = scala.io.StdIn.readInt()
      print("Enter your add width: ")
      val userAddWidth = scala.io.StdIn.readInt()
      println()

      var userInput = UserInput(userBidPrice, userAddHeight, userAddWidth, Some("https://www.google.com/"))
      var actorSystem = ActorSystem("ActorSystem"); // Creating ActorSystem
      var actor = actorSystem.actorOf(Props[AgentActor], "AgentActor") //Creating actor
      actor ! userInput // Sending messages by using !
      counter += 1
      Thread.sleep(1000)
      println()
      println("Try Again")
    } while ({
      counter < 101
    })
  }
}