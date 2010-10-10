package org.noiseflow.ui

import graph.{NodePalette, TableView}
import org.noiseflow.util.SimpleFrame
import java.awt.BorderLayout
import net.miginfocom.swing.MigLayout
import java.awt.event.ActionEvent
import javax.swing.{AbstractAction, JButton, JComponent, JPanel}
import org.noiseflow.{Player, Time}
import org.noiseflow.node.{NodeFactory, Table}

/**
 * 
 */
class NoiseflowUi(table: Table, player: Player, nodeFactories: List[NodeFactory]) {

  val frame = new SimpleFrame("Noiseflow", createMainPanel())

  def createMainPanel(): JComponent = {
    val panel: JPanel = new JPanel(new BorderLayout())

    panel.add(new TableView(table))

    panel.add(new NodePalette(nodeFactories, table), BorderLayout.NORTH)

    panel.add(new JButton(new AbstractAction("Play"){
      def actionPerformed(e: ActionEvent) = {
        player.play(table)
      }
    }), BorderLayout.WEST)

    panel
  }

}