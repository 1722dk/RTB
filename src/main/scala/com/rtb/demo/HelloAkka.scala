package com.rtb.demo

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps


class HelloAkka extends Actor { // Extending actor trait
  def receive = { //  Receiving message
    case msg: String => println(msg)
    case _ => println("Unknown message") // Default case
  }
}

class CreateActor extends Actor{
  def receive = {
    case msg: String => println(msg+" "+self.path.name)
    case _ => println("Unknown Message")
  }
}

class RootActor extends Actor {
  def receive = {
    case msg: String => println(msg + " " + self.path.name)
      var childActor = context.actorOf(Props[ChildActor], "ChildActor")
      childActor ! "Hello"
      println("Sender: "+ sender())
  }
}

class ChildActor extends Actor {
  def receive = {
    case msg: String => println(msg + " " + self.path.name)
  }
}

class ActorExample extends Actor{
  def receive = {
    case message:String => println("Message recieved: "+message);
  }
}

class ActorReplyExample extends Actor{
  def receive = {
    case message:String => println("Message recieved from "+sender.path.name+" massage: "+message);
      val child = context.actorOf(Props[ActorChildReplyExample],"ActorChild");
      child ! "Hello Child"
  }
}

class ActorChildReplyExample extends Actor{
  def receive ={
    case message:String => println("Message recieved from "+sender.path.name+" massage: "+message);
      println("Replying to "+sender().path.name);
      sender()! "I got you message";
  }
}

object Main {
  def main(args: Array[String]) {
    var actorSystem = ActorSystem("ActorSystem"); // Creating ActorSystem
    var actor = actorSystem.actorOf(Props[HelloAkka], "HelloAkka") //Creating actor
    actor ! "Hello Akka" // Sending messages by using !
    actor ! 100.52

    var actorSystem2 = ActorSystem("ActorSystem")
    var actor2 = actorSystem2.actorOf(Props[CreateActor], name = "CreateActor")
    actor2 ! "I'm new to Scala & Akka"

    var actorSystem3 = ActorSystem()
    var actor3 = actorSystem3.actorOf(Props[RootActor], "RootActor")
    actor3 ! "Hello"

    val actorSystem4 = ActorSystem("ActorSystem");
    val actor4 = actorSystem4.actorOf(Props[ActorExample], "RootActor");
    implicit val timeout = Timeout(2 seconds);
    val future = actor4 ? "Hello";
    val result = Await.result(future, timeout.duration);
    println("Message received: "+result);

    val actorSystem5 = ActorSystem("ActorSystem");
    val actor5 = actorSystem5.actorOf(Props[ActorReplyExample], "RootActor");
    actor5 ! "Hello";
  }
}