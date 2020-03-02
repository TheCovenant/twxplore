package stores

import edu.rpi.tw.twks.uri.Uri
import io.github.tetherlessworld.twxplore.lib.geo.models.domain._
import io.github.tetherlessworld.twxplore.lib.tree.TestData
import io.github.tetherlessworld.twxplore.lib.tree.models.selection.{SelectionInput, SelectionResults}
import io.github.tetherlessworld.twxplore.lib.tree.stores.TreeStore

object TestTreeStore extends TreeStore {

  implicit class TreeUri(uri: Uri) {
    def lastPart = uri.toString.substring(uri.toString.lastIndexOf(":") + 1)
  }

  override def getTrees(limit: Int, offset: Int): List[Tree] = if (offset == 0) TestData.treeList else List()

  override def getTreesBySelection(selection: SelectionInput): SelectionResults = ???
}
