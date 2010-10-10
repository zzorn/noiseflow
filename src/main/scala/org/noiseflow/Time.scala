package org.noiseflow

/**
 * Utility class to represent a point in time along a noise.
 */
case class Time(var samplesSinceLastUpdate: Int, var samplesSinceStart: Long, sampleRate: Double) {
  def secondsSinceStart: Double = samplesSinceStart / sampleRate
  def secondsSinceLastUpdate: Double = samplesSinceLastUpdate / sampleRate

  def advance(samples: Int) {
    samplesSinceLastUpdate = samples
    samplesSinceStart += samples
  }
}