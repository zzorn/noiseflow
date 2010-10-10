package org.noiseflow.node

import javax.swing.Icon

/**
 * Creates instances of a specific kind of node, and provides general info about it.
 */
trait NodeFactory {

  def name: String
  def description: String
  def icon: Icon

  def createInstance(): Node

}