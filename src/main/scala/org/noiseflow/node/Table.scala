package org.noiseflow.node

import org.noiseflow.Time

/**
 * A composite node consisting of many internal nodes.
 */
class Table extends Node {

  private var _nodes: List[Node] = Nil

  def nodes: List[Node] = _nodes
  def addNode(node: Node) = _nodes = node :: _nodes
  def removeNode(node: Node) = _nodes = _nodes.filterNot(_ == node)

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
