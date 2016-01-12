package com.mavencode.bundleprice.service

import com.mavencode.bundleprice.model._
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by mavencodeapps on 11/01/16.
  */
class CatalogTest extends WordSpec with Matchers {

  "The Catalog object" when {
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
    "pass items and bundle" should {
      val catalog = Catalog(items, bundles)

      "provide price for item" in {
        val pr = catalog.price(Seq[CatalogItem](CatalogItem("111", "bread")))
        pr should be(Some(Money("USD", 0.45f)))
      }
      "provide price for bundle" in {
        val pr = catalog.price(Seq[CatalogItem](CatalogItem("111", "bread"), CatalogItem("112", "milk")))
        pr should be(Some(Money("USD", 0.5f)))
      }
      "provide None if not in the catalog" in {
        val pr = catalog.price(Seq[CatalogItem](CatalogItem("115", "bread"), CatalogItem("112", "milk")))
        pr should be(None)
      }
    }
  }

}
