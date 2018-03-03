package com.akinevz.snake

import javax.swing._

object Main {

  lazy val frame: GameFrame = new GameFrame()
  lazy val window: JFrame = new JFrame() {
    this setPreferredSize new java.awt.Dimension(800, 600)
    this setTitle "Snek"
    this setDefaultCloseOperation WindowConstants.EXIT_ON_CLOSE
    this setResizable false
    this addWindowListener frame
    this add frame
    this.pack()
  }

  def main(args: Array[String]): Unit = {
    window setVisible true
  }

}
