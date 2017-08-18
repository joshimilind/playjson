package Package

import play.api.libs.json.Json

case class Location(lat: Double, long: Double)
object Location{
  implicit  val format = Json.format[Location]
}
case class Resident(name: String, age: Int, role: Option[String])
object Resident{
  implicit  val format = Json.format[Resident]
}
case class Place(name: String, location: Location, residents: Seq[Resident])
object Place{
  implicit  val format = Json.format[Place]
}

object Playjson extends App {

/*
//  val jData: String = "{\n  \"name\" : \"Watership Down\",\n  \"location\" : {\n    \"lat\" : 51.235685,\n    \"long\" : -1.309197\n  },\n  \"residents\" : [ {\n    \"name\" : \"Fiver\",\n    \"age\" : 4,\n    \"role\" : null\n  }, {\n    \"name\" : \"Bigwig\",\n    \"age\" : 6,\n    \"role\" : \"Owsla\"\n  } ]\n}"
  val json: JsValue = Json.parse("""
  {
    "name" : "Watership Down",
    "location" : {
      "lat" : 51.235685,
      "long" : -1.309197
    },
    "residents" : [ {
      "name" : "Fiver",
      "age" : 4,
      "role" : null
    }, {
      "name" : "Bigwig",
      "age" : 6,
      "role" : "Owsla"
    } ]
  }
  """)
  import play.api.libs.json._

  val json: JsValue = JsObject(Seq(
    "name" -> JsString("Watership Down"),
    "location" -> JsObject(Seq("lat" -> JsNumber(51.235685), "long" -> JsNumber(-1.309197))),
    "residents" -> JsArray(IndexedSeq(
      JsObject(Seq(
        "name" -> JsString("Fiver"),
        "age" -> JsNumber(4),
        "role" -> JsNull
      )),
      JsObject(Seq(
        "name" -> JsString("Bigwig"),
        "age" -> JsNumber(6),
        "role" -> JsString("Owsla")
      ))
    ))
  ))
  case class User (name: String)
*/

  import play.api.libs.json._

  val json: JsValue = Json.parse("""
  {
    "name" : "Watership Down | here json",
    "location" : {
      "lat" : 51.235685,
      "long" : -1.309197
    },
    "residents" : [ {
      "name" : "Fiver",
      "age" : 4,
      "role" : null
    }, {
      "name" : "Bigwig",
      "age" : 6,
      "role" : "Owsla"
    } ]
  }
  """)

//  val minifiedString: String = Json.stringify(json)

//  val readableString: String = Json.prettyPrint(json)
/*  val name = println((json \ "name").as[String])
  val location = println(json \ "location" \ "lat")*/

  val name = println((json \ "name").as[String])
  val names = println((json \\ "name").map(_.as[String]))

  println("****")

  val nameResult: JsResult[String] = (json \ "name").validate[String]

  // Pattern matching
  nameResult match {
    case s: JsSuccess[String] => println("Name: " + s.get)
    case e: JsError => println("Errors: " + JsError.toJson(e).toString())
  }
  // Fallback value
  val nameOrFallback = nameResult.getOrElse("Undefined")
  // map
  val nameUpperResult: JsResult[String] = nameResult.map(_.toUpperCase())
  // fold
  val nameOption: Option[String] = nameResult.fold(
    invalid = {
      fieldErrors =>
        fieldErrors.foreach(x => {
          println("field: " + x._1 + ", errors: " + x._2)
        })
        None
    },
    valid = {
      name => Some(name)
    }
  )

  println("*******************")

  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  implicit val locationReads: Reads[Location] = (
    (JsPath \ "lat").read[Double] and
      (JsPath \ "long").read[Double]
    )(Location.apply _)

  implicit val residentReads: Reads[Resident] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "age").read[Int] and
      (JsPath \ "role").readNullable[String]
    )(Resident.apply _)

//  implicit val placeReads: Reads[Place] = (
//    (JsPath \ "name").read[String] and
//      (JsPath \ "location").read[Location] and
//      (JsPath \ "residents").read[Seq[Resident]]
//    )(Place.apply _)

  val placeResult: Place = Json.fromJson[Place](json).get
  println(placeResult)
  // JsSuccess(Place(...),).

  val residentResult: JsResult[Resident] = (json \ "residents")(1).validate[Resident]
  println(residentResult)
  // JsSuccess(Resident(Bigwig,6,Some(Owsla)),)
}