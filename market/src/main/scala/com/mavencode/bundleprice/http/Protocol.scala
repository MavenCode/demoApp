package com.mavencode.bundleprice.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.mavencode.bundleprice.model.{Card, ShoppingItem, Item, Money}
import spray.json.DefaultJsonProtocol

/**
  * Created by mavencodeapps on 11/01/16.
  */
object Protocol extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val moneyFormat = jsonFormat2(Money)
  implicit val itemFormat = jsonFormat3(Item)
  implicit val shoppingItemFormat = jsonFormat2(ShoppingItem)
  implicit val cardFormat = jsonFormat1(Card)
  implicit val ackFormat = jsonFormat2(Acknowledge)

}
