package org.noiseflow.ui.graph

import javax.swing.JPanel
import org.scalaprops.Bean
import org.noiseflow.node.{Node, Table}
import java.awt.Graphics
import net.miginfocom.swing.MigLayout

/**
 * View of a composite node consisting of other nodes.
 */
class TableView(table: Table) extends JPanel {

  private var views: Map[Node, NodeView] = Map()

  private var insertX = 10
  private var insertY = 10

  val migLayout: MigLayout = new MigLayout()
  setLayout(migLayout)

  table.nodes foreach addView _
  table onNodeAdded addView _
  table onNodeRemoved removeView _

  def addView(node: Node) {
    val view = new NodeView(node, this)
    view.xPos = insertX
    view.yPos = insertY
    add(view, "pos " + view.xPos + " " + view.yPos)
    insertX += 200
    insertY += 200

    views += node -> view
  }

  def removeView(node: Node) {
    if (views.contains(node)) {
      remove(views(node))
      views -= node
    }
  }

  def reposition(nodeView: NodeView, x: Int, y: Int) {
    //remove(nodeView)
    migLayout.setComponentConstraints(nodeView, "pos " + x + " " + y)
    //nodeView.setBounds(x, y, nodeView.getWidth, nodeView.getHeight)
    invalidate()
    validate()
    repaint()
  }

  def bringToFront(nodeView: NodeView) {
    val x = nodeView.xPos
    val y = nodeView.yPos
    remove(nodeView)
    add(nodeView, "pos " + x + " " + y, 0)
    invalidate()
    validate()
    repaint()
  }

  override def paintComponent(g: Graphics) = {
    // TODO: Draw connection lines
    g.clearRect(0,0, getWidth, getHeight)
  }
}