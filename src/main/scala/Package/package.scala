import scala.io.StdIn

object playjson2 extends App {
  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  println("enter new age")
  val newAge = StdIn readInt
  val json: JsValue = Json.parse("""{
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
                                    }""")

  val name = println((json \ "name").as[String])
  val names = (json \\ "name").map(_.as[String])

  case class Location(lat: Double, long: Double)
//  case class name(name: String)
  object Location {
    implicit val format = Json.format[Location]
  }
  println(names updated (2, "Hedwig"))

  case class Resident(name: String, age: Int, role: Option[String])
  object Resident {
    implicit val format = Json.format[Resident]
  }

//  val residentResult: JsResult[Resident] = (json \ "residents")(1).validate[Resident]
//  println(residentResult)

  case class Place(name: String, location: Location, residents: Seq[Resident])
  object Place{
    implicit  val format = Json.format[Place]
  }


  implicit val locationReads: Reads[Location] = (
    (JsPath \ "lat").read[Double] and
      (JsPath \ "long").read[Double]
    )(Location.apply _)

  implicit val residentReads: Reads[Resident] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "age").read[Int] and
      (JsPath \ "role").readNullable[String]
    )(Resident.apply _)

  implicit val placeReads: Reads[Place] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "location").read[Location] and
      (JsPath \ "residents").read[Seq[Resident]]
    )(Place.apply _)

  val placeResult: Place = Json.fromJson[Place](json).get
  println(placeResult)
  // JsSuccess(Place(...),).

  val residentResult: JsResult[Resident] = (json \ "residents")(1).validate[Resident]
//  println(residentResult)

}