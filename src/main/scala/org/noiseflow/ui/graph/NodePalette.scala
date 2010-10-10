package org.noiseflow.ui.graph

import net.miginfocom.swing.MigLayout
import javax.swing.{AbstractAction, JButton, JPanel}
import java.awt.event.ActionEvent
import org.noiseflow.node.{Table, NodeFactory}

/**
 * A toolbar / palette with the available node types.
 */
class NodePalette(nodeFactories: List[NodeFactory], table: Table) extends JPanel {

  setLayout(new MigLayout())

  nodeFactories foreach {f =>
    add(new JButton(new AbstractAction(f.name, f.icon){
      setToolTipText(f.description)

      def actionPerformed(e: ActionEvent) = {
        table.addNode(f.createInstance)
      }
    }))
  }
}
