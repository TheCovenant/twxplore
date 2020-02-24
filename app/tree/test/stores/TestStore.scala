package stores

import edu.rpi.tw.twks.uri.Uri
import io.github.tetherlessworld.twxplore.lib.geo.models.domain._
import io.github.tetherlessworld.twxplore.lib.tree.TestData
import io.github.tetherlessworld.twxplore.lib.tree.stores.Store

object TestStore extends Store {
  implicit class TreeUri(uri: Uri) { def lastPart = uri.toString.substring(uri.toString.lastIndexOf(":") + 1) }

  override def getTrees(limit: Int, offset: Int): List[Tree] = if (offset == 0) TestData.treeList else List()

  override def getNtasByBorough(borough: Borough): List[Nta] = if (borough != null && borough == TestData.boroughMap(1)) TestData.boroughMap(1).ntaList.map(nta => TestData.ntaMap(nta.lastPart)) else List()

  override def getBlocksByNta(nta: Nta): List[Block] = if (nta != null && nta == TestData.ntaMap("MN14")) TestData.ntaMap("MN14").blocks.map(block => TestData.blockMap(block.lastPart.toInt)) else List()

  override def getBoroughsByCity(city: City): List[Borough] = if (city != null && city == TestData.city) TestData.city.boroughs.map(borough => TestData.boroughMap(borough.lastPart.toInt)) else List()

  override def getGeometryOfCity(city: City): Geometry = if (city != null && city == TestData.city) TestData.cityGeoMap("New York").geometry else null

  override def getGeometryOfBoroughs(boroughs: Vector[Borough]): List[Geometry] = {
    boroughs.map(borough => {
      if (borough != null && borough == TestData.boroughMap(borough.borocode))
        TestData.boroughGeoMap(borough.name).geometry
    }).toList.asInstanceOf[List[Geometry]]
  }

  //override def getGeometryOfBoroughs(boroughs: List[Borough]): List[Geometry] = ???

  override def getGeometryOfBorough(borough: Borough): Geometry = getGeometryOfBoroughs(Vector(borough)).head

  override def getGeometryOfNtas(ntas: Vector[Nta]): List[Geometry] = {
    ntas.map(nta => {
      if (nta != null && nta == TestData.ntaMap(nta.nta))
        TestData.ntaGeoMap(nta.name).geometry
    }).toList.asInstanceOf[List[Geometry]]
  }

  override def getGeometryOfNta(nta: Nta): Geometry = getGeometryOfNtas(Vector(nta)).head

  override def getGeometryOfBlocks(blocks: Vector[Block]): List[Geometry] = {
    blocks.map(block => {
      if (block != null && block == TestData.blockMap(block.id))
        TestData.blockGeoMap(block.id.toString).geometry
    }).toList.asInstanceOf[List[Geometry]]
  }

  override def getGeometryOfBlock(block: Block): Geometry = getGeometryOfBlocks(Vector(block)).head
}
