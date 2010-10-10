package org.noiseflow.node

import javax.sound.sampled.{SourceDataLine, AudioSystem, AudioFormat}
import org.noiseflow.Time

/**
 * Node that plays incoming audio data.
 */
class PlayOutput extends Node {

  val bufferSize = property('bufferSize, 1024) onChange init _
  val sampleRate = property('sampleRate, 44100) onChange init _
  val volume = property('volume, 0.3)
  val input = property('input, 0.0)

  private var audioFormat: AudioFormat = null
  private var outputLine: SourceDataLine = null
  private var buffer: Array[Byte] = null
  private var bufferPos = 0

  init()

  def init() {
    audioFormat = new AudioFormat(sampleRate(), 8, 1, true, false)
    outputLine = AudioSystem.getSourceDataLine(audioFormat)
    buffer = new Array[Byte](bufferSize())
    bufferPos = 0
  }

  override def start() {
    outputLine.open(audioFormat)
    outputLine.start()
  }

  def update(time: Time) {

    // Get current input value
    val value = input()

    var i = 0
    while(i < time.samplesSinceLastUpdate) {

      // Copy value to buffer
      writeToBuffer(value * volume())

      i += 1
    }
  }

  override def stop() {
    flushBuffer()
    outputLine.drain()
    outputLine.stop()
    outputLine.close()
  }

  def abort() {
    outputLine.stop()
    outputLine.close()
  }

  private def writeToBuffer(value: Double) {
    def clamp(v: Double): Double = if (v > 1.0) 1.0 else if (v < -1.0) -1.0 else v

    // Copy value to buffer
    buffer(bufferPos) = (clamp(value) * 127).toByte

    // Flush if buffer wrap around
    bufferPos += 1
    if (bufferPos >= bufferSize()) flushBuffer()
  }

  private def flushBuffer() {
    if (bufferPos > 0) {
      outputLine.write(buffer, 0, bufferPos)
      bufferPos = 0
    }
  }



}