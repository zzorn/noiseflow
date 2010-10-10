package org.noiseflow

import node._
import ui.graph.TableView
import ui.NoiseflowUi
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

    new NoiseflowUi(table, new Player(), createNodeFactories())
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

  def createNodeFactories(): List[NodeFactory] = {
    HardwiredNodeFactory("Sine", "Sine wave generator", null, () => new SineWave()) ::
    HardwiredNodeFactory("Square", "Square wave generator", null, () => new SquareWave()) ::
    HardwiredNodeFactory("White Noise", "White noise generator", null, () => new WhiteNoise()) ::
    HardwiredNodeFactory("Mixer", "Mixes two input lines with a selection line", null, () => new Mix()) :: 
    Nil

  }

}