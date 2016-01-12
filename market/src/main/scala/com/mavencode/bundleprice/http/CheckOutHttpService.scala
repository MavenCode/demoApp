package com.mavencode.bundleprice.http

import com.mavencode.bundleprice.model.Card
import akka.http.scaladsl.server.Directives._
import com.mavencode.bundleprice.service.{Bill, Catalog}
import com.wordnik.swagger.annotations._
import spray.json.RootJsonFormat

/**
  * Created by mavencodeapps on 11/01/16.
  */
@Api(value = "/checkout", description = "Checkout shopping items and gives the lowest price",
  produces = "text/plain", consumes = "application/json")
trait CheckOutHttpService {

  import Protocol._

  val catalog: Catalog

  val checkoutRoute = checkout

  @ApiOperation(httpMethod = "POST", response = classOf[String],
    value = "Price amount", notes = "Return the lowest price")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value = "Shopping cart",
      dataType = "ca.mavencode.bundleprice.model.Card", required = true, paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Password changed", response = classOf[String])
  ))
  def checkout = path ( "checkout" ) {
    post {
      entity(as[Card]) { en =>
        val sum = Bill(en, catalog).checkout
        complete {s"{checkout: $sum}"}
      }
    }
  }

}
