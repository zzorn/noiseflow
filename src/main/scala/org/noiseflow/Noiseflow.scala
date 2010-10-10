package org.noiseflow

import node.{PlayOutput, SineWave, Table}
import util.SimpleFrame
import javax.swing.{AbstractAction, JButton, JPanel}
import java.awt.event.ActionEvent

/**
 * Main entry point.
 */
object Noiseflow {
  def main(args: Array[String]) {

    val panel: JPanel = new JPanel()
    panel.add(new JButton(new AbstractAction("Play"){
      def actionPerformed(e: ActionEvent) = {

        val table = new Table()
        val sineWave = new SineWave()
        val playOutput = new PlayOutput()
        table.addNode(sineWave)
        table.addNode(playOutput)

        playOutput.input.bind(sineWave.out, automaticUpdate=false)

        table.start()

        var i = 0
        val samples = playOutput.sampleRate() * 2
        val time = new Time(0, 0, playOutput.sampleRate())
        while(i < samples) {
          table.refresh()
          table.update(time)
          time.advance(1)
          i += 1
        }

        table.stop()

      }
    }))

    val frame = new SimpleFrame("Noiseflow", panel)
  }
  
}