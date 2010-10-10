package org.noiseflow

import node.{PlayOutput, SineWave, Table}
import ui.graph.TableView
import util.SimpleFrame
import javax.swing.{AbstractAction, JButton, JPanel}
import java.awt.event.ActionEvent
import java.awt.BorderLayout

/**
 * Main entry point.
 */
object Noiseflow {
  def main(args: Array[String]) {

    val sampleRate = 44100
    val table = createTable(sampleRate)

    val panel: JPanel = new JPanel(new BorderLayout())
    panel.add(new JButton(new AbstractAction("Play"){
      def actionPerformed(e: ActionEvent) = {

        table.start()

        var i = 0
        val samples = sampleRate * 2
        val time = new Time(0, 0, sampleRate)
        while(i < samples) {
          table.refresh()
          table.update(time)
          time.advance(1)
          i += 1
        }

        table.stop()

      }
    }), BorderLayout.NORTH)
    
    panel.add(new TableView(table), BorderLayout.CENTER)

    val frame = new SimpleFrame("Noiseflow", panel)
  }

  def createTable(sampleRate: Double): Table = {
    val table = new Table()
    val sineWave = new SineWave()
    val playOutput = new PlayOutput()
    playOutput.sampleRate := sampleRate
    table.addNode(sineWave)
    table.addNode(playOutput)

    playOutput.input.bind(sineWave.out, automaticUpdate=false)

    table
  }

}