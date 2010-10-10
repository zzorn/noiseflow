package org.noiseflow.node

import org.scalaprops.Bean
import org.noiseflow.Time

/**
 * 
 */
trait Node extends Bean {

  def name = property('name, getClass.getSimpleName)

  def start() {}
  def stop() {}

  def update(time: Time)

  def refresh() = updateBoundValues()

}