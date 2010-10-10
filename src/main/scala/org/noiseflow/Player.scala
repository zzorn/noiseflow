package org.noiseflow

import node.Table

/**
 * 
 */
class Player(val sampleRate: Double = 44100) {

  def play(table: Table) {
    table.start()

    val playTimeSeconds = 2.0
    val samples = sampleRate * playTimeSeconds
    val time = new Time(0, 0, sampleRate)
    var i = 0
    while(i < samples) {
      table.refresh()
      table.update(time)
      time.advance(1)
      i += 1
    }

    table.stop()
  }

}