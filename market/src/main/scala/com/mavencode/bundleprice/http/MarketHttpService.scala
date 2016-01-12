package com.mavencode.bundleprice.http

import akka.http.scaladsl.server.Directives._
import com.gettyimages.spray.swagger.SwaggerHttpService

/**
  * Created by mavencodeapps on 11/01/16.
  */
trait MarketHttpService extends CheckOutHttpService {

  import scala.reflect.runtime.universe._

  val swaggerService = new SwaggerHttpService {
    override def apiTypes = Seq(typeOf[CheckOutHttpService])

    override def apiVersion = "2.0"

    override def baseUrl = "/v1"

    override def docsPath = "docs"
  }

  implicit val formats = org.json4s.DefaultFormats

  val handler = {
    path("swagger") {
      getFromResource("swagger/index.html")
    } ~
      getFromResourceDirectory("swagger") ~
      swaggerService.swaggerRoutes ~
      pathPrefix("v1") {
        checkoutRoute
      }
  }

}
