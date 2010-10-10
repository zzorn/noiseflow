package org.noiseflow.node

import org.noiseflow.Time

/**
 * A composite node consisting of many internal nodes.
 */
class Table extends Node {

  private var _nodes: List[Node] = Nil
  private var addListeners: List[Node => Unit] = Nil
  private var removeListeners: List[Node => Unit] = Nil

  def nodes: List[Node] = _nodes

  def addNode(node: Node) {
    _nodes ::= node
    addListeners foreach (_(node))
  }

  def removeNode(node: Node) {
    _nodes = _nodes.filterNot(_ == node)
    removeListeners foreach (_(node))
  }

  def onNodeAdded(listener: Node => Unit) = addListeners ::= listener
  def onNodeRemoved(listener: Node => Unit) = removeListeners ::= listener

  def clearListeners() {
    addListeners = Nil
    removeListeners = Nil
  }

  def update(time: Time) {
    nodes foreach (_.update(time))
  }

  override def refresh() {
    super.updateBoundValues()

    nodes foreach (_.refresh())
  }

  override def stop() = nodes foreach (_.stop())
  override def start() = nodes foreach (_.start())
}
