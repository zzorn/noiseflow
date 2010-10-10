package org.noiseflow.ui.graph

import org.noiseflow.node.Node
import org.scalaprops.Property
import net.miginfocom.swing.MigLayout
import javax.swing.{BorderFactory, JLabel, JPanel}
import java.awt.Color
import java.awt.event.{MouseAdapter, MouseEvent, MouseMotionAdapter}

/**
 * 
 */
class NodeView(node: Node, tableView: TableView) extends JPanel {

  var xPos = 0
  var yPos = 0

  setLayout(new MigLayout("wrap 2"))

  setBorder(BorderFactory.createLineBorder(Color.BLACK))
  add(new JLabel(node.name()), "wrap")

  node.properties foreach (e => addPropertyView(e._1, e._2))

  var dragStartScreen: (Int, Int) = (0,0)
  var dragStartComponent: (Int, Int) = (0,0)
  addMouseListener(new MouseAdapter(){
    override def mousePressed(e: MouseEvent) = {
      dragStartScreen = (e.getXOnScreen, e.getYOnScreen)
      dragStartComponent = (getX, getY)
      tableView.bringToFront(NodeView.this)
    }
  })
  addMouseMotionListener(new MouseMotionAdapter() {
    override def mouseDragged(e: MouseEvent) = {
      xPos = dragStartComponent._1 + e.getXOnScreen - dragStartScreen._1
      yPos = dragStartComponent._2 + e.getYOnScreen - dragStartScreen._2
      tableView.reposition(NodeView.this, xPos, yPos)
    }
  })

  def addPropertyView(name: Symbol, property: Property[_]) {

    add(new JLabel(name.name))
    add(new JLabel(property.value.toString))

    //add(new PropertyView(name, property))
  }



}