package com.mavencode.bundleprice.service

import com.mavencode.bundleprice.model._
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by mavencodeapps on 11/01/16.
  */
class BillTest extends WordSpec with Matchers {

  "The Bill object" when {

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
    val catalog = Catalog(items, bundles)

    "pass shopping items" should {
      "provide the lowest price" in {
        val shoppingCard = Card(Set[ShoppingItem](
          ShoppingItem(
            Item("111", "bread", Money("USD", 0.45f)), 2),
          ShoppingItem(
            Item("113", "butter", Money("USD", 1.30f)), 4),
          ShoppingItem(
            Item("112", "milk", Money("USD", 0.80f)), 2)))

        val fin = Bill(shoppingCard, catalog).checkout
        fin should be(5.10f)
      }

      "choose the lowest if few bundles" in {
        val shoppingCard = Card(Set[ShoppingItem](
          ShoppingItem(
            Item("111", "bread", Money("USD", 0.45f)), 2),
          ShoppingItem(
            Item("113", "butter", Money("USD", 1.30f)), 5),
          ShoppingItem(
            Item("112", "milk", Money("USD", 0.80f)), 2)))

        val fin = Bill(shoppingCard, catalog).checkout
        fin should be(1.70f)
      }
    }
  }

}
