object change extends App {
  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  /* println("enter new age")
 val newAge = StdIn readInt*/

  val json1: JsValue = Json.parse("""{
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

  case class AllData(name: String,
                     location: Location,
                     residents: Seq[Residents])
  case class Location(lat: Double, Long: Double)

  case class Residents(name: String, var age: Int, role: Option[String])

  object Residents {
    implicit val ResidentReads: Reads[Residents] = (
      (JsPath \ "name").read[String] and
        (JsPath \ "age").read[Int] and
        (JsPath \ "role").readNullable[String]
    )(Residents.apply _)

  }

  object Location {
    implicit val LatLongReads: Reads[Location] = (
      (JsPath \ "lat").read[Double] and
        (JsPath \ "long").read[Double]
    )(Location.apply _)
  }

  object AllData {
    implicit val allDataReads: Reads[AllData] = (
      (JsPath \ "name").read[String] and
        (JsPath \ "location").read[Location] and
        (JsPath \ "residents").read[Seq[Residents]]
    )(AllData.apply _)
  }

  println("is old")

  println(
    json1
      .validateOpt[AllData]
      .map {
        case Some(data: AllData) =>
          val data1 = .copy
//          data.residents(1).copy(age = 555)

//        data.copy(Seq(Residents(age = 12),Residents(name = )))
        /*case Some(data: AllData) =>
         data.copy(_,residents = data.residents(1).age =12,_,_)
       */

      }
      .get)

  println("is new")

  val temp1 = temp.copy(name = "LL")
  println("temp1")
  println(temp1)

  val temp = AllData("Watership Down",
                     Location(51.235685, -1.309197),
                     Seq(
                       Residents("Fiver", 23, None),
                       Residents("Bigwig", 6, Some("Owsla"))
                     ))
  val t1 = temp.copy()
  println(t1)
  println(temp)
  //  val tempShow: AllData = Json.fromJson[AllData](json1).get
//  println(tempShow)
//  data.residents(1)
//  data.residents(1).age ->
  //json1.as[AllData]

//  JsSuccess(Residents)
//  println("newVal is ")
  /*val newValue = StdIn readInt
  val updatedJson = json1.as[JsObject] ++ Json.obj("newValue" -> newValue)
  println(newValue)*/
  /*val residentResult: JsResult[Residents] =
    (json1 \ "residents")(1).validate[Residents]

  println(residentResult)*/
//  val nameUpdate = json1.as[JsObject]

  //  AllData
}

object Test extends App {
  case class Person(name: String, id: Int)

  val p1 = new Person("abc", 1)

  val p = p1.copy(name = p1.name.reverse)
  println(p)
  val p2 = new Person(p1.name.reverse, p1.id)
  println(p2)

}
