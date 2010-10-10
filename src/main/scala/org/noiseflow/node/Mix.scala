package org.noiseflow.node

import org.noiseflow.Time

/**
 * Blends together two input lines.
 */
class Mix extends Node {

  val inA = property('inB, 0.0)
  val inB = property('inA, 0.0)
  val select = property('select, 0.5)
  val out = property('out, 0.0)

  def update(time: Time) {
    out := inA() * (1.0 - select()) + inB() * select()
  }
}