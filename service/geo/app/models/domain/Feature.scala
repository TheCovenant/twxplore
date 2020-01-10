package models.domain

import edu.rpi.tw.twks.uri.Uri
import edu.rpi.tw.twxplore.lib.utils.rdf.{Rdf, RdfReader, RdfWriter}
import org.apache.jena.geosparql.implementation.datatype.WKTDatatype
import org.apache.jena.geosparql.implementation.vocabulary.Geo
import org.apache.jena.rdf.model.{Resource, ResourceFactory}
import org.apache.jena.vocabulary.{RDF, RDFS}

case class Feature(geometry: Geometry, label: Option[String], uri: Uri)

object Feature {
  implicit object FeatureRdfReader extends RdfReader[Feature] {
    override def read(resource: Resource): Feature =
      Feature(
        geometry = Rdf.read(resource.getProperty(Geo.HAS_DEFAULT_GEOMETRY_PROP).getObject.asResource()),
        label = Option(resource.getProperty(RDFS.label)).map(statement => statement.getObject.asLiteral().getString),
        uri = Uri.parse(resource.getURI)
      )
  }

  implicit object FeatureRdfWriter extends RdfWriter[Feature] {
    override def write(value: Feature): Resource = {
      val resource = ResourceFactory.createResource(value.uri.toString)
      resource.addProperty(RDF.`type`, resource.getModel.createResource("http://www.opengis.net/ont/sf#Feature"))
      value.label.foreach(label => resource.addProperty(RDFS.label, label))
      resource.addProperty(Geo.AS_WKT_PROP, ResourceFactory.createTypedLiteral(value.wkt, WKTDatatype.INSTANCE))
    }
  }
}
