package org.noiseflow.ui.graph

import org.noiseflow.node.Node
import org.scalaprops.Property
import net.miginfocom.swing.MigLayout
import javax.swing._
import event.{ChangeEvent, ChangeListener}
import java.awt.event._
import java.awt.{Dimension, Graphics, Color}

/**
 * 
 */
// TODO: Extract property view
class NodeView(val node: Node, tableView: TableView) extends JPanel {

  var xPos = 0
  var yPos = 0

  private var propertySources: Map[Property[_], PropertySource] = Map()
  private var propertyTargets: Map[Property[_], PropertyTarget] = Map()

  setLayout(new MigLayout("wrap 3, insets null null null 0"))

  setBorder(BorderFactory.createLineBorder(Color.BLACK))
  add(new JLabel(node.name()), "wrap")

  node.properties.filterNot(_._1 == 'name) foreach (e => addPropertyView(e._1, e._2))

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

    // Name label
    val propertySource = new PropertySource(property)
    propertySources += property -> propertySource
    add(propertySource)

    // Value view / editor
    // TODO: Add type to properties - probably name also, and a ref to the parent bean - a bit heavier, but much more useful
    property.value match {
      case v: Double => add(createNumberSpinner(property))
      case _         => add(createReadOnlyLabel(property))
    }

    // Binding widget
    val propertyTarget = new PropertyTarget(property, tableView)
    propertyTargets += property -> propertyTarget
    add(propertyTarget)
  }

  def hasProperty(p: Property[_]): Boolean = propertyTargets.contains(p)

  def getPropertyInPos(p: Property[_]): (Int, Int) = propertyTargets(p).inPoint

  def getPropertyOutPos(p: Property[_]): (Int, Int) = propertySources(p).outputPos

  class PropertySource(val property: Property[_]) extends JLabel(property.name.name) {
    def outputPos = (getLocationOnScreen.x - tableView.getLocationOnScreen.x,
                     getLocationOnScreen.y - tableView.getLocationOnScreen.y + getHeight / 2 )
  }

  def getPropertyAt(x: Int, y: Int): Option[Property[_]] = {
    getComponentAt(x, y) match {
      case null => None
      case ps: PropertySource => if (ps.property.value.isInstanceOf[Double]) Some(ps.property) else None
      case _ => None
    }
  }

  class PropertyTarget(val property: Property[_], tableView: TableView) extends JPanel()  {
    setPreferredSize(new Dimension(16, 16))

    def inPoint = (bindPointX, bindPointY)

    def bindPointX = getLocationOnScreen.x - tableView.getLocationOnScreen.x + getWidth
    def bindPointY = getLocationOnScreen.y - tableView.getLocationOnScreen.y + getHeight / 2

    var bindingDragActive = false

    if (property.value.isInstanceOf[Double]) {
      addMouseMotionListener(new MouseMotionAdapter(){
        override def mouseDragged(e: MouseEvent) = {
          tableView.setBindingPreviewLine(bindPointX, bindPointY,
            e.getXOnScreen - tableView.getLocationOnScreen.x,
            e.getYOnScreen - tableView.getLocationOnScreen.y)
        }
      })
      addMouseListener(new MouseAdapter(){
        override def mousePressed(e: MouseEvent) = {
          bindingDragActive = true
        }

        override def mouseReleased(e: MouseEvent) = {
          if (bindingDragActive) {
            bindingDragActive = false
            tableView.getPropertyAt(e.getXOnScreen - tableView.getLocationOnScreen.x,
                                    e.getYOnScreen - tableView.getLocationOnScreen.y) match {
              case None => property.unbind
              case Some(other: Property[_]) =>
                // TODO: Check type compability
                property.asInstanceOf[Property[Double]].bind(other.asInstanceOf[Property[Double]], automaticUpdate = false)
            }

            tableView.hideBindingPreviewLine()
          }
        }
      })
    }

    override def paintComponent(g: Graphics) = {
      g.setColor(Color.BLACK)
      g.fillOval(0,0,getWidth(), getHeight())
    }
  }


  private def createNumberSpinner(property: Property[_]): JSpinner = {
    val initialValue: Double = property.value.asInstanceOf[Double]
    val model = new SpinnerNumberModel(initialValue, null, null, initialValue.abs.floor * 0.1 + 0.1)
    model.addChangeListener(new ChangeListener() {
      def stateChanged(e: ChangeEvent) = {
        val v = model.getValue.asInstanceOf[Double]
        model.setStepSize(v.abs.floor * 0.1 + 0.1)
        property.asInstanceOf[Property[Double]].set(v)
      }
    })

    // NOTE: Updates too fast, eats performance - what do?
    //property.onChange((p: Property[_]) => model.setValue(p.value.asInstanceOf[Double]))

    val spinner = new JSpinner(model)
    spinner.setPreferredSize(new Dimension(80, 24))
    spinner.addMouseWheelListener(new MouseWheelListener() {
      def mouseWheelMoved(e: MouseWheelEvent) = {
        val scroll = -e.getWheelRotation
        var i = 0
        while (i < scroll) {
          model.setValue(model.getNextValue)
          i += 1
        }
        while (i > scroll) {
          model.setValue(model.getPreviousValue)
          i -= 1
        }
      }
    })

    spinner
  }

  def createReadOnlyLabel(property: Property[_]): JLabel = {
    val label = new JLabel(property.value.toString)
    property.onChange((p: Property[_]) => label.setText(p.value.toString))
    label
  }



}