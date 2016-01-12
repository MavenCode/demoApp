package com.mavencode.bundleprice

import com.mavencode.bundleprice.model._
import com.mavencode.bundleprice.service.{Bill, Catalog}

/**
  * Created by mavencodeapps on 08/01/16.
  */
object BundleApp {

  def main(args: Array[String]): Unit = {
    val items = Set[Item](Item("111", "bread", Money("USD", 0.45f)), Item("112", "milk", Money("USD", 0.80f)), Item("113", "butter", Money("USD", 1.30f)))
    val bundles = Set[Bundle](
      Bundle(
        Card(Set[ShoppingItem](
          ShoppingItem(
            Item("111", "bread", Money("USD", 0.45f)), 1),
          ShoppingItem(
            Item("113", "butter", Money("USD", 1.30f)), 2))),
        Money("USD", 1.75f)
      )
    )
    val catalog = Catalog(items, bundles)
    val shoppingCard = Card(Set[ShoppingItem](
      ShoppingItem(
        Item("111", "bread", Money("USD", 0.45f)), 2),
      ShoppingItem(
        Item("113", "butter", Money("USD", 1.30f)), 4),
      ShoppingItem(
        Item("112", "milk", Money("USD", 0.80f)), 2)))

    val fin = Bill(shoppingCard, catalog).checkout
    println(fin)
  }

}
