package org.noiseflow.node

import javax.swing.Icon

/**
 * Node factory for builtin node types.
 */
case class HardwiredNodeFactory(name: String, 
                                description: String,
                                icon: Icon,
                                creator: () => Node) extends NodeFactory {
  def createInstance() = creator()
}