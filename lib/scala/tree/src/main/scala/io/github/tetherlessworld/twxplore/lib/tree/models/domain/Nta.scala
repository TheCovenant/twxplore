package io.github.tetherlessworld.twxplore.lib.geo.models.domain

import edu.rpi.tw.twks.uri.Uri
import io.github.tetherlessworld.scena.{RdfReader, RdfWriter}
import io.github.tetherlessworld.twxplore.lib.base.models.domain._
import io.github.tetherlessworld.twxplore.lib.base.models.domain.vocabulary.TREE
import org.apache.jena.rdf.model.{Model, Resource, ResourceFactory}

//Nta
final case class Nta(nta: String, name: String, blocks: List[Uri], borough: Uri, postCode: Uri) extends Ordered[Nta] {
  val uri = Uri.parse(TREE.NTA_URI_PREFIX + nta)

  def compare(that: Nta) = this.nta compare that.nta

  def addBlock(block: Block): Nta = {
    this.copy(blocks = blocks :+ block.uri)
  }
}

object Nta {
  implicit class NTAResource(val resource: Resource)
    extends RdfProperties with RdfsProperties with SioProperties with TreeTermsProperties with SchemaProperties with DCTermsProperties


  implicit object NTARdfReader extends RdfReader[Nta] {
    override def read(resource: Resource): Nta = {
      Nta(
        nta = resource.identifier.get,
        name = resource.label.get,
        borough = resource.boroughUri.get,
        postCode = resource.postalCodeUri.get,
        blocks = resource.blocksUri,
      )
    }
  }

  implicit object NTARdfWriter extends RdfWriter[Nta] {
    override def write(model: Model, value: Nta): Resource = {
      val resource = Option(model.getResource(value.uri.toString))
        .getOrElse(ResourceFactory.createResource(value.uri.toString))

      resource.label = value.name
      resource.identifier = value.nta
      resource.postalCodeUri = value.postCode
      resource.blocksUri = value.blocks
      resource.boroughUri = value.borough
      resource
    }
  }
}