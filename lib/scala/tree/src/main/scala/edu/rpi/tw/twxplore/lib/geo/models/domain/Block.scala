package edu.rpi.tw.twxplore.lib.geo.models.domain

import edu.rpi.tw.twxplore.lib.base.models.domain.vocabulary.{SIO, TREE}
import io.github.tetherlessworld.scena.{RdfReader, RdfWriter}
import org.apache.jena.rdf.model.{Resource, ResourceFactory}
import org.apache.jena.vocabulary.DCTerms

final case class Block(id: Int, nta: String)

object Block {
  implicit object BlockRdfReader extends RdfReader[Block] {
    override def read(resource: Resource): Block = {
      Block(
        id = resource.getProperty(DCTerms.identifier).getObject.asLiteral().getInt,
        nta  = resource.getProperty(SIO.isLocationIn).getResource().getProperty(DCTerms.identifier).getObject.asLiteral().toString
      )
    }
  }

  implicit object BlockRdfWriter extends RdfWriter[Block] {
    override def write(value: Block): Resource = {
      val resource = ResourceFactory.createResource(TREE.URI + "block")
      resource.addProperty(DCTerms.identifier, value.id.toString)
      resource.addProperty(SIO.isLocationIn, ResourceFactory.createResource(TREE.URI + "NTA").addProperty(DCTerms.identifier, value.nta))
    }
  }
}