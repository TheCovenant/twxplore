package models.domain

import java.util.{Calendar, Date}

import edu.rpi.tw.twks.uri.Uri
import io.github.tetherlessworld.scena._
import io.github.tetherlessworld.twxplore.lib.base.models.domain.SchemaProperties
import io.github.tetherlessworld.twxplore.lib.geo.models.domain.{ParsedGeometry, UnparsedGeometry}
import models.domain.vocabulary.LOCAL
import org.apache.jena.datatypes.xsd.XSDDateTime
import org.apache.jena.geosparql.implementation.vocabulary.Geo
import org.apache.jena.rdf.model.{Literal, Model, Resource, ResourceFactory}
import org.apache.jena.vocabulary.RDF

final case class Feature(
                          geometry: ParsedGeometry,
                          uri: Uri,
                          frequency: Option[Double] = None,
                          frequencyRange: Option[FrequencyRange] = None,
                          label: Option[String] = None,
                          locality: Option[String] = None,
                          postalCode: Option[String] = None,
                          regions: List[String] = List(),
                          timestamp: Option[Date] = None,
                          timestampRange: Option[TimestampRange] = None,
                          transmissionPower: Option[Int] = None,
                          `type`: Option[FeatureType] = None,
                        ) extends io.github.tetherlessworld.twxplore.lib.geo.models.domain.Feature

object Feature {

  // Mix in whichever enrichments we want
  implicit class FeatureResource(val resource: Resource)
    extends RdfProperties
      with RdfsProperties
      with SchemaProperties {
    private final def frequencyFromLiteral(literal: Literal): Double =
      literal.getFloat.asInstanceOf[Double]

    private final def frequencyToLiteral(frequency: Double): Literal =
      ResourceFactory.createTypedLiteral(frequency.floatValue())

    private final def timestampFromLiteral(literal: Literal): Date = {
      literal.getValue.asInstanceOf[XSDDateTime].asCalendar().getTime
    }

    private final def timestampToLiteral(timestamp: Date): Literal = {
      val calendar = Calendar.getInstance()
      calendar.setTime(timestamp)
      ResourceFactory.createTypedLiteral(new XSDDateTime(calendar))
    }

    final def frequency: Option[Double] = getPropertyObjectLiterals(LOCAL.frequency).headOption.map(literal => frequencyFromLiteral(literal))
    final def frequency_=(value: Double) = setProperty(LOCAL.frequency, List(frequencyToLiteral(value)))

    final def frequencyRange: Option[FrequencyRange] = {
      val frequencyMinimum = Option(resource.getProperty(LOCAL.frequencyMinimum)).map(statement => frequencyFromLiteral(statement.getObject.asLiteral()))
      val frequencyMaximum = Option(resource.getProperty(LOCAL.frequencyMaximum)).map(statement => frequencyFromLiteral(statement.getObject.asLiteral()))
      if (frequencyMinimum.isDefined && frequencyMaximum.isDefined) {
        Some(FrequencyRange(
          minimum = frequencyMinimum.get,
          maximum = frequencyMaximum.get
        ))
      } else {
        None
      }
    }
    final def frequencyRange_=(value: FrequencyRange) = {
      resource.addProperty(LOCAL.frequencyMinimum, frequencyToLiteral(value.minimum))
      resource.addProperty(LOCAL.frequencyMaximum, frequencyToLiteral(value.maximum))
    }

    final def timestamp: Option[Date] = getPropertyObjectLiterals(LOCAL.timestamp).headOption.map(literal => timestampFromLiteral(literal))
    final def timestamp_=(value: Date) = resource.setProperty(LOCAL.timestamp, List(timestampToLiteral(value)))

    final def timestampRange: Option[TimestampRange] = {
      val timestampMinimum = Option(resource.getProperty(LOCAL.timestampMinimum)).map(statement => timestampFromLiteral(statement.getObject.asLiteral()))
      val timestampMaximum = Option(resource.getProperty(LOCAL.timestampMaximum)).map(statement => timestampFromLiteral(statement.getObject.asLiteral()))
      if (timestampMinimum.isDefined && timestampMaximum.isDefined) {
        Some(TimestampRange(
          minimum = timestampMinimum.get,
          maximum = timestampMaximum.get
        ))
      } else {
        None
      }
    }
    final def timestampRange_=(value: TimestampRange) = {
      resource.addProperty(LOCAL.timestampMinimum, timestampToLiteral(value.minimum))
      resource.addProperty(LOCAL.timestampMaximum, timestampToLiteral(value.maximum))
    }

    final def transmissionPower: Option[Int] = getPropertyObjectLiterals(LOCAL.transmissionPower).headOption.map(literal => literal.getInt)
    final def transmissionPower_=(value: Int) = setPropertyLiterals(LOCAL.transmissionPower, List(value.asInstanceOf[Int]))
  }

  private final class UnparseableGeometryException extends Exception

  implicit object FeatureRdfReader extends RdfReader[Feature] {
    override def read(resource: Resource): Feature =
        Feature(
          frequency = resource.frequency,
          frequencyRange = resource.frequencyRange,
          geometry = Rdf.read[UnparsedGeometry](resource.getProperty(Geo.HAS_DEFAULT_GEOMETRY_PROP).getObject.asResource()).parse().getOrElse(throw new UnparseableGeometryException),
          label = resource.labels.headOption,
          locality = resource.addressLocality,
          postalCode = resource.postalCode,
          regions = resource.addressRegions,
          timestamp = resource.timestamp,
          timestampRange = resource.timestampRange,
          transmissionPower = resource.transmissionPower,
          `type` = resource.types.flatMap(typeResource => FeatureType.values.find(value => typeResource.getURI == value.uri.toString)).headOption,
          uri = Uri.parse(resource.getURI)
        )
  }

  implicit object FeatureRdfWriter extends RdfWriter[Feature] {
    override def write(model: Model, value: Feature): Resource = {
      val resource = model.createResource(value.uri.toString)
      resource.types = List(Geo.FEATURE_RES)
      //      resource.addProperty(RDF.`type`, Geo.FEATURE_RES)
      if (value.frequency.isDefined) resource.frequency = value.frequency.get
      if (value.frequencyRange.isDefined) resource.frequencyRange = value.frequencyRange.get
      if (value.label.isDefined) resource.labels = List(value.label.get)
      if (value.locality.isDefined) resource.addressLocality = value.locality.get
      if (value.postalCode.isDefined) resource.postalCode = value.postalCode.get
      value.regions.foreach(region => resource.addAddressRegion(region))
      if (value.timestamp.isDefined) resource.timestamp = value.timestamp.get
      if (value.timestampRange.isDefined) resource.timestampRange = value.timestampRange.get
      if (value.transmissionPower.isDefined) resource.transmissionPower = value.transmissionPower.get
      if (value.`type`.isDefined) resource.addProperty(RDF.`type`, model.createResource(value.`type`.get.uri.toString))
      resource.addProperty(Geo.HAS_DEFAULT_GEOMETRY_PROP, Rdf.write[UnparsedGeometry](model, UnparsedGeometry(label=value.geometry.label, uri=value.geometry.uri, wkt=value.geometry.wkt)))
    }
  }

}
