import scala.io.StdIn

object change2 extends App {

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  //  println("enter new age")
  //  val newAge = StdIn readInt
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

  case class Residents(name: String, age: Int, role: Option[String]) {
    implicit val format = Json.format[Residents]
  }

  object Residents {
    implicit val ResidentReads: Reads[Residents] = (
      (JsPath \ "name").read[String] and
        (JsPath \ "age").read[Int] and
        (JsPath \ "role").readNullable[String]
    )(Residents.apply _)
  }

  val readableString: String = Json.prettyPrint(json1)
  val minifiedString: String = Json.stringify(json1)

//  println(minifiedString)
  println(readableString)

  val name = (json1 \ "name").as[String]

  val names = (json1 \\ "name").map(_.as[String])

  val nameOption = (json1 \ "name").asOpt[String]

  val bogusOption = (json1 \ "bogus").asOpt[String]
/*
  println(name)
  println(names)
  println(nameOption)
  println(bogusOption)*/

  val newNames = names updated (1, "DELL")

  println(newNames)

  println("new age  = ")
  val newAge = StdIn readInt

  import play.api.libs.json._

  val ChangedJson: JsValue = JsObject(
    Seq(
      "name" -> JsString("Watership Down"),
      "location" -> JsObject(
        Seq("lat" -> JsNumber(51.235685), "long" -> JsNumber(-1.309197))),
      "residents" -> JsArray(IndexedSeq(
        JsObject(
          Seq(
            "name" -> JsString("Fiver"),
            "age" -> JsNumber(newAge),
            "role" -> JsNull
          )),
        JsObject(
          Seq(
            "name" -> JsString("Bigwig"),
            "age" -> JsNumber(6),
            "role" -> JsString("Owsla")
          ))
      ))
    ))


  val changedbuffer: String = Json.prettyPrint(ChangedJson)
  println("****")

  println(changedbuffer)
}
