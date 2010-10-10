package org.noiseflow.util

import java.awt.Dimension
import javax.swing.{JComponent, JFrame}

/**
 * A Swing JFrame with sensible default settings.
 * Makes UI prototyping easier.
 *
 * @author Hans Haggstrom
 */
class SimpleFrame(title : String, content : JComponent) extends JFrame {

  setTitle(title)
  setContentPane( content )
  setPreferredSize( new Dimension(800, 600) )

  setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE )

  pack
  setVisible(true)
}


