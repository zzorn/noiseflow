package org.noiseflow.node

import org.noiseflow.Time

/**
 * A node that creates a square wave.
 */
class SquareWave extends Node {

  val frequency = property('frequency, 330.0)
  val amplitude = property('amplitude, 0.8)
  val shift = property('shift, 0.0)
  val out = property('out, 0.0)

  def update(time: Time) {
    val value = if (((shift() + time.secondsSinceStart) % frequency()) < frequency() * 0.5) -1 else 1  
    out := amplitude() * value
  }

}