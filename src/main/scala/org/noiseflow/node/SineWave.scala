package org.noiseflow.node

import org.noiseflow.Time

/**
 * A node that creates a sine wave.
 */
class SineWave extends Node {

  val frequency = property('frequency, 330.0)
  val amplitude = property('amplitude, 0.8)
  val shift = property('shift, 0.0)
  val out = property('out, 0.0)

  private val Tau = math.Pi * 2.0

  def update(time: Time) {
    val value = math.sin((shift() + time.secondsSinceStart) * frequency() * Tau)
    out := amplitude() * value 
  }

}