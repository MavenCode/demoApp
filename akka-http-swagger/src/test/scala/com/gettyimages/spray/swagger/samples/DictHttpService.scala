package com.gettyimages.spray.swagger.samples

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.wordnik.swagger.annotations._
import javax.ws.rs.Path

import spray.json.DefaultJsonProtocol

import scala.concurrent.ExecutionContext

@Api(value = "/dict", description = "This is a dictionary api.")
trait DictHttpService extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val dictEntJson = jsonFormat3(DictEntry)
  protected implicit def executor: ExecutionContext
  protected implicit def materializer: ActorMaterializer

  var dict: Map[String, String] = Map[String, String]()

  @ApiOperation(value = "Add dictionary entry.", notes = "Will a new entry to the dictionary, indexed by key, with an optional expiration value.", httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "entry", value = "Key/Value pair of dictionary entry, with optional expiration time.", required = true, dataType = "DictEntry", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Client Error")
  ))
  def createRoute = post {
    path("/dict") {
      entity(as[DictEntry]) { e =>
        dict += e.key -> e.value
        complete("ok")
      }
    }
  }

  @ApiOperation(value = "Find entry by key.", notes = "Will look up the dictionary entry for the provided key.", response = classOf[DictEntry], httpMethod = "GET", nickname = "someothername")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "key", value = "Keyword for the dictionary entry.", required = true, dataType = "String", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Dictionary does not exist.")
  ))
  def readRoute = get {
    path("/dict" / Segment) { key =>
      complete(dict(key))
    }
  }

}

