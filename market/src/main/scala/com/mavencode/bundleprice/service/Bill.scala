package com.mavencode.bundleprice.service

import com.mavencode.bundleprice.model.Card

import scala.math.BigDecimal.RoundingMode

/**
  * Created by mavencodeapps on 08/01/16.
  */
object Bill {
  def apply(card: Card, catalog: Catalog): Bill = {
    val itms = card.items.toSeq
      .flatMap {
        i => Stream.continually(CatalogItem(i.item.id, i.item.name)).take(i.quantity).toSeq
      }.sortBy(_.id)
    new Bill(itms, catalog)
  }
}

class Bill private(items: Seq[CatalogItem], catalog: Catalog) {

  import Catalog._

  val itemBundled: Iterator[Seq[CatalogBundle]] = breakByBundles(items)

  private def combinations(itms: Seq[CatalogItem]) = for {
    len <- 1 to itms.length
    comb <- itms combinations len
  } yield comb

  private def breakByBundles(items: Seq[CatalogItem]): Iterator[Seq[CatalogBundle]] = {
    def dropInner(prev: Seq[CatalogBundle], cur: Seq[CatalogItem]): Iterator[Seq[CatalogBundle]] = {
      cur match {
        case Nil => Set(prev.sorted).toIterator
        case e => combinations(e).flatMap { i =>
          dropInner(prev :+ i.sortBy(_.id), cur.diff(i))
        }.toSet.toIterator
      }
    }
    dropInner(Seq[Seq[CatalogItem]](), items)
  }

  def checkout = {
    val raw = itemBundled.map {
      b => b.foldRight(0.0) {
        case (bun, sum) => catalog.price(bun) match {
          case Some(p) => sum + p.amount
          case None => Float.MaxValue
        }
      }
    }.min
    BigDecimal(raw).setScale(2, RoundingMode.HALF_UP).toFloat
  }
}
