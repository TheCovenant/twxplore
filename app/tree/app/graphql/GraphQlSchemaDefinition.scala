package models.graphql

import java.util.Date

import edu.rpi.tw.twks.uri.Uri
import io.github.tetherlessworld.twxplore.lib.base.models.graphql.AbstractGraphQlSchemaDefinition
import io.github.tetherlessworld.twxplore.lib.geo.models.domain._
import play.api.libs.json
import play.api.libs.json.{JsResult, JsString, JsSuccess, JsValue}
import sangria.macros.derive._
import sangria.marshalling.{CoercedScalaResultMarshaller, FromInput}
import sangria.schema.{Argument, Field, FloatType, InputField, ListInputType, ListType, ScalarAlias, Schema, StringType, fields}

object GraphQlSchemaDefinition extends AbstractGraphQlSchemaDefinition{
  // Scalar Formats
  implicit val uriFormat = new json.Format[Uri] {
    override def reads(json: JsValue): JsResult[Uri] = JsSuccess(Uri.parse(json.asInstanceOf[JsString].value))
    override def writes(o: Uri): JsValue = JsString(o.toString)
  }


  implicit val ScalaFloatType = ScalarAlias[Float, Double](
    FloatType, _.toDouble, value => {
      Right(value.toFloat)
    }
  )

  implicit val DateType = ScalarAlias[Date, String](
    StringType, _.toString, date => {
      val dateFormatter = new java.text.SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy")
      Right(dateFormatter.parse(date))
    }
  )
  implicit val CurbLocType = ScalarAlias[CurbLoc, String](
    StringType, _.label, curbLoc => {
      val result = curbLoc match {
        case "OffsetFromCurb" => OffsetFromCurb
        case "OnCurb" => OnCurb
      }
      Right(result)
    }
  )
  implicit val StatusType = ScalarAlias[Status, String](
    StringType, _.label, status => {
      val result = status match {
        case "Alive" => Alive
        case "Dead" => Dead
        case "Stump" => Stump
        case _ => Dead
      }
      Right(result)
    }
  )
  implicit val GuardsType = ScalarAlias[Guards, String](
    StringType, _.label, guards => {
      val result = guards match {
        case "Helpful" => Helpful
        case "Harmful" => Harmful
        case "Unsure" => Unsure
      }
      Right(result)
    }
  )
  implicit val HealthType = ScalarAlias[Health, String](
    StringType, _.label, health => {
      val result = health match {
        case "Fair" => Fair
        case "Good" => Good
        case "Poor" => Poor
      }
      Right(result)
    }
  )
  implicit val ProblemType = ScalarAlias[Problems, String](
    StringType, _.label, problem => {
      val result = problem match {
        case "BranchLights" => BranchLights
        case "BranchOther" => BranchOther
        case "BranchShoe" => BranchShoe
        case "MetalGrates" => MetalGrates
        case "Stones" => Stones
        case "TrunkLights" => TrunkLights
        case "TrunkOther" => TrunkOther
        case "TrunkWire" => TrunkWire
        case "RootGrate" => RootGrate
        case "RootLights" => RootLights
        case "RootStone" => RootStone
        case "Sneakers" => Sneakers
        case "WiresRope" => WiresRope
      }
      Right(result)
    }
  )
  implicit val SidewalkType = ScalarAlias[Sidewalk, String](
    StringType, _.label, sidewalk => {
      val result = sidewalk match {
        case "NoDamage" => NoDamage
        case "Damage" => Damage
      }
      Right(result)
    }
  )
  implicit val StewardType = ScalarAlias[Steward, String](
    StringType, _.label, steward => {
      val result = steward match {
        case "OneOrTwo" => OneOrTwo
        case "ThreeOrFour" => ThreeOrFour
      }
      Right(result)
    }
  )
  implicit val UserType = ScalarAlias[UserType, String](
    StringType, _.label, user => {
      val result = user match {
        case "TreesCountStaff" => TreesCountStaff
        case "NYCParksStaff" => NYCParksStaff
        case "Volunteer" => Volunteer
      }
      Right(result)
    }
  )
//  // Scalar argument types
//  val LimitArgument = Argument("limit", IntType, description = "Limit")
//  val OffsetArgument = Argument("offset", IntType, description = "Offset")
//  val UriArgument = Argument("uri", UriType, description= "URI")

  // Domain model types, in dependence order


  implicit val CensusTractInputType = deriveInputObjectType[CensusTract](
    InputObjectTypeName("CensusTractFieldsInput"),
    ReplaceInputField("uri", InputField("uri", UriType))
  )

  implicit val PostCodeInputType = deriveInputObjectType[Postcode](
    InputObjectTypeName("PostcodeFieldsInput"),
    ReplaceInputField("uri", InputField("uri", UriType))
  )

  implicit val SpeciesInputType = deriveInputObjectType[TreeSpecies](
    InputObjectTypeName("SpeciesFieldsInput"),
    ReplaceInputField("uri", InputField("uri", UriType))
  )

  implicit val ZipCityInputType = deriveInputObjectType[ZipCity](
    InputObjectTypeName("ZipCityFieldsInput"),
    ReplaceInputField("uri", InputField("uri", UriType))
  )

  implicit val BlockInputType = deriveInputObjectType[Block](
    InputObjectTypeName("BlockFieldsInput"),
    ReplaceInputField("uri", InputField("uri", UriType))
  )

  implicit val NtaInputType = deriveInputObjectType[Nta](
    InputObjectTypeName("NtaFieldsInput"),
    ReplaceInputField("uri", InputField("uri", UriType))
  )

  implicit val BoroughInputType = deriveInputObjectType[Borough](
    InputObjectTypeName("BoroughFieldsInput"),
    ReplaceInputField("uri", InputField("uri", UriType))
  )

  implicit val CityInputType = deriveInputObjectType[City](
    InputObjectTypeName("CityFieldsInput"),
    ReplaceInputField("uri", InputField("uri", UriType))
  )

  implicit val StateInputType = deriveInputObjectType[State](
    InputObjectTypeName("StateFieldsInput"),
    ReplaceInputField("uri", InputField("uri", UriType))
  )

  implicit val GeometryInputType = deriveInputObjectType[Geometry](
    InputObjectTypeName("GeometryFieldsInput"),
    ReplaceInputField("uri", InputField("uri", UriType))

  )

//  implicit val TreeInputType = deriveInputObjectType[Tree](
//    InputObjectTypeName("TreeFieldsInput"),
//    ReplaceInputField("uri", InputField("uri", UriType)),
//    ReplaceInputField("createdAt", InputField("createdAt", DateType)),
//    ReplaceInputField("curbLoc", InputField("curbLoc", CurbLocType)),
//  )

  implicit val GeometryType = deriveObjectType[GraphQlSchemaContext, Geometry](
    ReplaceField("uri", Field("uri", UriType, resolve = _.value.uri)),
    ReplaceField("wkt", Field("wkt", StringType, resolve = _.value.wkt))
  )

  implicit val FeatureType = deriveObjectType[GraphQlSchemaContext, Feature](
    ReplaceField("uri", Field("uri", UriType, resolve = _.value.uri))
  )

  implicit val StateType = deriveObjectType[GraphQlSchemaContext, State](
    ReplaceField("uri", Field("uri", UriType, resolve = _.value.uri))
  )

  implicit val CityType = deriveObjectType[GraphQlSchemaContext, City](
    ReplaceField("uri", Field("uri", UriType, resolve = _.value.uri))
  )

  implicit val BoroughType = deriveObjectType[GraphQlSchemaContext, Borough](
    ReplaceField("uri", Field("uri", UriType, resolve = _.value.uri))
  )

  implicit val NtaType = deriveObjectType[GraphQlSchemaContext, Nta](
    ReplaceField("uri", Field("uri", UriType, resolve = _.value.uri))
  )

  implicit val BlockType = deriveObjectType[GraphQlSchemaContext, Block](
    ReplaceField("uri", Field("uri", UriType, resolve = _.value.uri)),
  )

  implicit val CensusTractType = deriveObjectType[GraphQlSchemaContext, CensusTract](
    ReplaceField("uri", Field("uri", UriType, resolve = _.value.uri))
  )

  implicit val PostCodeType = deriveObjectType[GraphQlSchemaContext, Postcode](
    ReplaceField("uri", Field("uri", UriType, resolve = _.value.uri))
  )

  implicit val SpeciesType = deriveObjectType[GraphQlSchemaContext, TreeSpecies](
    ReplaceField("uri", Field("uri", UriType, resolve = _.value.uri))
  )

  implicit val ZipCityType = deriveObjectType[GraphQlSchemaContext, ZipCity](
    ReplaceField("uri", Field("uri", UriType, resolve = _.value.uri))
  )


  implicit val TreeType = deriveObjectType[GraphQlSchemaContext, Tree](
    ReplaceField("uri", Field("uri", UriType, resolve = _.value.uri)),
  )


  implicit val geo = new FromInput[Geometry] {
    val marshaller = CoercedScalaResultMarshaller.default
    def fromResult(node: marshaller.Node) = {
      val ad = node.asInstanceOf[Map[String, Any]]

      Geometry(
        label = ad.get("label").flatMap(_.asInstanceOf[Option[String]]),
        uri = ad("uri").asInstanceOf[Uri],
        wkt = ad("wkt").asInstanceOf[String]
      )
    }
  }

  implicit val feature = new FromInput[Feature] {
    val marshaller = CoercedScalaResultMarshaller.default

    def fromResult(node: marshaller.Node) = {
      val ad = node.asInstanceOf[Map[String, Any]]

      Feature(
        geometry = ad("geometry").asInstanceOf[Geometry],
        label = ad("label").asInstanceOf[Option[String]],
        uri = ad("uri").asInstanceOf[Uri]
      )
    }
  }

  implicit val city = new FromInput[City] {
    val marshaller = CoercedScalaResultMarshaller.default
    def fromResult(node: marshaller.Node) = {
      val ad = node.asInstanceOf[Map[String, Any]]

      City(
        name = ad("name").asInstanceOf[String],
        boroughs = ad("boroughs").asInstanceOf[Vector[Uri]].toList,
        postcodes = ad("postcodes").asInstanceOf[Vector[Uri]].toList,
        state = ad("state").asInstanceOf[Uri],
        uri = ad("uri").asInstanceOf[Uri],
        feature = ad("feature").asInstanceOf[Uri]
      )
    }
  }

  implicit val manualblock = new FromInput[Block] {
    val marshaller = CoercedScalaResultMarshaller.default
    def fromResult(node: marshaller.Node) = {
      val ad = node.asInstanceOf[Map[String, Any]]

      Block(
        id = ad("id").asInstanceOf[Int],
        nta = ad("nta").asInstanceOf[Uri],
        feature = ad("feature").asInstanceOf[Uri],
        uri = ad("uri").asInstanceOf[Uri]
      )
    }
  }

  implicit val nta = new FromInput[Nta] {
    val marshaller = CoercedScalaResultMarshaller.default
    def fromResult(node: marshaller.Node) = {
      val ad = node.asInstanceOf[Map[String, Any]]

      Nta(
        name = ad("name").asInstanceOf[String],
        nta = ad("nta").asInstanceOf[String],
        blocks = ad("blocks").asInstanceOf[Vector[Uri]].toList,
        borough = ad("borough").asInstanceOf[Uri],
        postCode = ad("postCode").asInstanceOf[Uri],
        feature = ad("feature").asInstanceOf[Uri],
        uri = ad("uri").asInstanceOf[Uri]
      )
    }
  }

  implicit val borough = new FromInput[Borough] {
    val marshaller = CoercedScalaResultMarshaller.default
    def fromResult(node: marshaller.Node) = {
      val ad = node.asInstanceOf[Map[String, Any]]

      Borough(
        name = ad("name").asInstanceOf[String],
        borocode = ad("borocode").asInstanceOf[Int],
        city = ad("city").asInstanceOf[Uri],
        ntaList = ad("ntaList").asInstanceOf[Vector[Uri]].toList,
        feature = ad("feature").asInstanceOf[Uri],
        uri = ad("uri").asInstanceOf[Uri]
      )
    }
  }

  implicit val manualTree = new FromInput[Tree] {
    val marshaller = CoercedScalaResultMarshaller.default
    def fromResult(node: marshaller.Node) = {
      val ad = node.asInstanceOf[Map[String, Any]]

      Tree(
        id = ad("id").asInstanceOf[Int],
        createdAt = ad("createdAt").asInstanceOf[Date],
        dbh = ad.get("dbh").asInstanceOf[Int],
        stump = ad.get("stump").asInstanceOf[Int],
        block = ad.get("block").asInstanceOf[Uri],
        curbLoc = ad.get("curbLoc").asInstanceOf[CurbLoc],
        status = ad.get("status").asInstanceOf[Status],
        health = ad.get("health").asInstanceOf[Option[Health]],
        species = ad.get("species").asInstanceOf[Option[Uri]],
        steward = ad.get("steward").asInstanceOf[Option[Steward]],
        guards = ad.get("guards").asInstanceOf[Option[Guards]],
        sidewalk = ad.get("sidewalk").asInstanceOf[Option[Sidewalk]],
        userType = ad.get("userType").asInstanceOf[UserType],
        problems = ad.get("problems").toList.map( problem => problem.asInstanceOf[Problems]),
        address = ad.get("address").asInstanceOf[String],
        postcode = ad.get("postcode").asInstanceOf[Uri],
        city = ad.get("city").asInstanceOf[Uri],
        zipCity = ad.get("zipCity").asInstanceOf[Uri],
        community = ad.get("community").asInstanceOf[Int],
        borough = ad.get("borough").asInstanceOf[Uri],
        cncldist = ad.get("cncldist").asInstanceOf[Int],
        stateAssembly = ad.get("stateAssembly").asInstanceOf[Int],
        stateSenate = ad.get("stateSenate").asInstanceOf[Int],
        NTA = ad.get("NTA").asInstanceOf[Uri],
        boroughCount = ad.get("boroughCount").asInstanceOf[Int],
        state = ad.get("state").asInstanceOf[Uri],
        latitude = ad.get("latitude").asInstanceOf[Float],
        longitude = ad.get("longitude").asInstanceOf[Float],
        x_sp = ad.get("x_sp").asInstanceOf[Float],
        y_sp = ad.get("y_sp").asInstanceOf[Float],
        censusTract = ad.get("censusTract").asInstanceOf[Option[Uri]],
        bin = ad.get("bin").asInstanceOf[Option[Int]],
        bbl = ad.get("bbl").asInstanceOf[Option[Long]],
        uri = ad.get("uri").asInstanceOf[Uri]
      )
    }
  }


  // Argument types
  val GeometryArgument = Argument("geometry", GeometryInputType, description="Geometry Input")
  val BoroughArgument = Argument("borough", BoroughInputType, description="Borough Input")
  val BoroughsArgument = Argument("boroughs", ListInputType(BoroughInputType), description="Boroughs Input")
  val NtaArgument = Argument("nta", NtaInputType, description = "NTA Input")
  val NtasArgument = Argument("ntas", ListInputType(NtaInputType), description = "NTAs Input")
  val BlockArgument = Argument("block", BlockInputType, description = "Block Input")
  val BlocksArgument = Argument("blocks", ListInputType(BlockInputType), description = "Blocks Input")
  val CityArgument = Argument("city", CityInputType, description = "City Input")

  // Query types
  val RootQueryType = sangria.schema.ObjectType("RootQuery", fields[GraphQlSchemaContext, Unit](
    Field("trees", ListType(TreeType), arguments = LimitArgument :: OffsetArgument :: Nil, resolve = (ctx) => ctx.ctx.store.getTrees(limit = ctx.args.arg("limit"), offset = ctx.args.arg("offset"))),
    Field("getNtasByBorough", ListType(NtaType), arguments = BoroughArgument :: Nil, resolve = (ctx) => ctx.ctx.store.getNtasByBorough(borough = ctx.args.arg("borough"))),
    Field("getBlocksByNta", ListType(BlockType), arguments= NtaArgument :: Nil, resolve = (ctx) => ctx.ctx.store.getBlocksByNta(nta = ctx.args.arg("nta"))),
    Field("getBoroughsByCity", ListType(BoroughType), arguments= CityArgument :: Nil, resolve = (ctx) => ctx.ctx.store.getBoroughsByCity(city = ctx.args.arg("city"))),
    Field("getGeometryOfCity", GeometryType, arguments= CityArgument :: Nil, resolve = (ctx) => ctx.ctx.store.getGeometryOfCity(city = ctx.args.arg("city"))),
    Field("getGeometryOfBoroughs", ListType(GeometryType), arguments= BoroughsArgument :: Nil, resolve = (ctx) => ctx.ctx.store.getGeometryOfBoroughs(boroughs = ctx.args.arg("boroughs"))),
    Field("getGeometryOfBorough", GeometryType, arguments= BoroughArgument :: Nil, resolve = (ctx) => ctx.ctx.store.getGeometryOfBorough(borough = ctx.args.arg("borough"))),
    Field("getGeometryOfNtas", ListType(GeometryType), arguments= NtasArgument :: Nil, resolve = (ctx) => ctx.ctx.store.getGeometryOfNtas(ntas = ctx.args.arg("ntas"))),
    Field("getGeometryOfNta", GeometryType, arguments= NtaArgument :: Nil, resolve = (ctx) => ctx.ctx.store.getGeometryOfNta(nta = ctx.args.arg("nta"))),
    Field("getGeometryOfBlocks", ListType(GeometryType), arguments= BlocksArgument :: Nil, resolve = (ctx) => ctx.ctx.store.getGeometryOfBlocks(blocks = ctx.args.arg("blocks"))),
    Field("getGeometryOfBlock", GeometryType, arguments= BlockArgument :: Nil, resolve = (ctx) => ctx.ctx.store.getGeometryOfBlock(block = ctx.args.arg("block"))),
  ))

  // Schema
  val schema = Schema(
    RootQueryType
  )
}
