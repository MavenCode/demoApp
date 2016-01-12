package com.mavencode.bundleprice.service

import com.mavencode.bundleprice.model.{Bundle, Item, Money}

import scala.collection.immutable.TreeMap


/**
  * Created by mavencodeapps on 08/01/16.
  */
case class CatalogItem(id: String, name: String)

object Catalog {
  type CatalogBundle = Seq[CatalogItem]

  implicit val ordering = new Ordering[CatalogBundle] {
    override def compare(x: CatalogBundle, y: CatalogBundle): Int = {
      val len = x.lengthCompare(y.length)
      if (len == 0) {
        x.zip(y).collectFirst {
          case (i, j) if i.id != j.id => i.id compare j.id
        }.getOrElse(0)
      } else {
        len
      }
    }
  }

  def apply(items: Set[Item], bundles: Set[Bundle]): Catalog = new Catalog(items, bundles)
}

class Catalog(items: Set[Item], bundles: Set[Bundle]) {

  import Catalog._

  private val sorted: TreeMap[CatalogBundle, Money] = initCatalogTree(items, bundles)

  private def initCatalogTree(rawItems: Set[Item], rawBundles: Set[Bundle]): TreeMap[CatalogBundle, Money] = {
    val itms = rawItems.map(i => (List(CatalogItem(i.id, i.name)), i.price))
    val bndls = rawBundles.map(b => {
      (b.card.items.toSeq.flatMap(i => {
        Stream.continually(CatalogItem(i.item.id, i.item.name)).take(i.quantity).toSeq
      }).sortBy(_.id), b.price)
    })
    (itms ++ bndls).foldLeft(TreeMap[CatalogBundle, Money]()) { (c, e) =>
      c + e
    }
  }

  def price(b: CatalogBundle) = sorted.get(b)
}
