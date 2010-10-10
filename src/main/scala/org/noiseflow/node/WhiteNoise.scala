package org.noiseflow.node

import org.noiseflow.Time
import util.Random

/**
 * Generates white noise at the sampling frequency.
 */
class WhiteNoise extends Node {
  val amplitude = property('amplitude, 0.8)
  val out = property('out, 0.0)

  private val random = new Random()

  def update(time: Time) {
    out := amplitude() * (random.nextDouble * 2.0 - 1.0)
  }

}