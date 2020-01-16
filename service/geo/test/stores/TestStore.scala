package stores

import edu.rpi.tw.twks.uri.Uri
import models.domain.Feature

object TestStore extends Store {
  override def getFeatures(limit: Int, offset: Int): List[Feature] = if (offset == 0) List(TestData.feature) else List()
  override def getFeaturesCount() = 1
  override def getFeatureByUri(featureUri: Uri): Feature = if (featureUri == TestData.feature.uri) TestData.feature else throw new NoSuchElementException()
}
