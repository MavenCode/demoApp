package com.mavencode.bundleprice

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import com.mavencode.bundleprice.http.MarketHttpService
import com.mavencode.bundleprice.model._
import com.mavencode.bundleprice.service.Catalog
import com.typesafe.config.ConfigFactory

import scala.concurrent.{Future, ExecutionContext}

/**
  * Created by mavencodeapps on 11/01/16.
  */
object Boot extends App with MarketHttpService {

  implicit val system = ActorSystem("Bundle-Actor-System")
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val log: LoggingAdapter = Logging(system, getClass)

  val items = Set[Item](
    Item("111", "bread", Money("USD", 0.45f)),
    Item("112", "milk", Money("USD", 0.80f)),
    Item("113", "butter", Money("USD", 1.30f)))
  val bundles = Set[Bundle](
    Bundle(
      Card(Set[ShoppingItem](
        ShoppingItem(
          Item("111", "bread", Money("USD", 0.45f)), 1),
        ShoppingItem(
          Item("113", "butter", Money("USD", 1.30f)), 2))),
      Money("USD", 1.75f)
    ),
    Bundle(
      Card(Set[ShoppingItem](
        ShoppingItem(
          Item("111", "bread", Money("USD", 0.45f)), 1),
        ShoppingItem(
          Item("112", "milk", Money("USD", 0.80f)), 1))),
      Money("USD", 0.50f)
    ),
    Bundle(
      Card(Set[ShoppingItem](
        ShoppingItem(
          Item("113", "butter", Money("USD", 1.30f)), 5))),
      Money("USD", 0.70f)
    )
  )

  override val catalog: Catalog = Catalog(items, bundles)

  implicit val ec = system.dispatcher

  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)

  val (host, port) = (config.getString("http.interface"), config.getInt("http.port"))

  val binding = Http().bindAndHandle(handler, host, port)

  binding onFailure {
    case ex: Exception => log.error(ex, "Failed to bind to {}:{}!", host, port)
  }
}
