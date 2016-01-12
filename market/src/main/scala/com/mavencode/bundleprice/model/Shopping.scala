package com.mavencode.bundleprice.model

/**
  * Created by mavencodeapps on 08/01/16.
  */
case class Item(id: String, name: String, price: Money) {
  require(id.nonEmpty)
}

case class Money(currency: String, amount: Float) {
  require(amount > 0)
}

case class ShoppingItem(item: Item, quantity: Int)

case class Card(items: Set[ShoppingItem])

case class Bundle(card: Card, price: Money)