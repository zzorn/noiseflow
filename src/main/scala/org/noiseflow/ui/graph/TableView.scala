package org.noiseflow.ui.graph

import javax.swing.JPanel
import org.noiseflow.node.{Node, Table}
import net.miginfocom.swing.MigLayout
import org.scalaprops.{Property, Bean}
import java.awt.{Color, Component, Graphics}

/**
 * View of a composite node consisting of other nodes.
 */
class TableView(table: Table) extends JPanel {

  private var views: Map[Node, NodeView] = Map()

  private var insertX = 100
  private var insertY = 100

  private var bindingPreviewLineVisible = false
  private var bindingPreviewLine = (0,0,0,0)

  val migLayout: MigLayout = new MigLayout()
  setLayout(migLayout)

  table.nodes foreach addView _
  table onNodeAdded addView _
  table onNodeRemoved removeView _

  def addView(node: Node) {
    val view = new NodeView(node, this)
    view.xPos = insertX
    view.yPos = insertY
    add(view, "pos " + view.xPos + " " + view.yPos, 0)

    views += node -> view
    invalidate()
    validate()
    repaint()
  }

  def removeView(node: Node) {
    if (views.contains(node)) {
      remove(views(node))
      views -= node
    }
    invalidate()
    validate()
    repaint()
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

  def getPropertyAt(x: Int, y: Int): Option[Property[_]] = {
    getComponentAt(x, y) match {
      case null => None
      case n: NodeView => n.getPropertyAt(x - n.getX, y - n.getY)
      case _ => None
    }
  }

  def setBindingPreviewLine(x1: Int, y1: Int, x2: Int, y2: Int) {
    bindingPreviewLine = (x1, y1, x2, y2)
    bindingPreviewLineVisible = true
    repaint()
  }

  def hideBindingPreviewLine() {
    bindingPreviewLineVisible = false
    repaint()
  }

  override def paintComponent(g: Graphics) = {
    // TODO: Draw connection lines
    g.clearRect(0,0, getWidth, getHeight)
    
    views.values foreach {nv =>
      nv.node.properties.values foreach { p =>
        val bound = p.boundProperty
        if (bound != null) {
          val from = nv.getPropertyInPos(p)
          views.values.find( nv2 => nv2.hasProperty(bound)) match {
            case None =>
            case Some(nv3: NodeView) =>
              val to = nv3.getPropertyOutPos(bound)
              g.setColor(Color.BLACK)
              g.drawLine(from._1, from._2,
                         to._1,   to._2)
          }
        }
      }
    }
  }


  override def paint(g: Graphics) = {
    super.paint(g)

    if (bindingPreviewLineVisible) {
      g.setColor(Color.BLACK)
      g.drawLine(bindingPreviewLine._1, bindingPreviewLine._2,
                 bindingPreviewLine._3, bindingPreviewLine._4)
    }

  }
}