package com.akinevz.snake

import java.awt.event.{WindowEvent, WindowListener}
import java.awt.{Color, Graphics, Graphics2D}
import javax.swing.JComponent

import scala.annotation.tailrec

trait WindowAdapter extends WindowListener {
  override def windowActivated(e: WindowEvent): Unit = ()

  override def windowClosed(e: WindowEvent): Unit = ()

  override def windowClosing(e: WindowEvent): Unit = ()

  override def windowOpened(e: WindowEvent): Unit = ()

  override def windowIconified(e: WindowEvent): Unit = ()

  override def windowDeiconified(e: WindowEvent): Unit = ()

  override def windowDeactivated(e: WindowEvent): Unit = ()
}

class GameFrame(var game: GameState = GameState.start) extends JComponent with WindowAdapter {
  val gameSpeed = 0.5f
  val timer: Thread = new Thread(() => tick())
  var state: State = Init
  var lastTime: Long = System.currentTimeMillis()
  def redraw(): Unit = {
    val newTime = System.currentTimeMillis()
    println(newTime - lastTime) and (lastTime = newTime)
    val g = getGraphics
    g.clearRect(0,0,getWidth,getHeight)
    paint(g)
  }

  @tailrec
  private final def tick(): Unit = {
    try {
      state match {
        case Init | Paused =>
          Thread.sleep(80600)
        case Running =>
          game = game.update
          redraw()
          Thread sleep (gameSpeed * 1000).toInt
        case Stopped => return
      }
    }
    catch {
      case _: InterruptedException =>
    }
    tick()
  }

  def resume(): Unit = println("resume") and {
    state = Running
  } and timer.interrupt()

  def pause(): Unit = println("pause") and {
    state = Paused
  }

  def start(): Unit = println("start") and {
    state = Running
    timer.start()
  }

  def stop(): Unit = println("stop") and {
    state = Stopped
  } and timer.interrupt() and timer.join()


  def fruitColour: Color = Color.pink

  def blockColour: Color = Color.darkGray

  def snakeColour: Color = Color.green

  {
    Keybindings register this
    this setIgnoreRepaint true
  }
  override def paint(g: Graphics): Unit = {
    val g2 = g.asInstanceOf[Graphics2D]
    game match {
      case s@PlayingState(snake, size, _) =>
        val windowSize = this.getSize()
        val scale = math.min(windowSize.width / size.x, windowSize.height / size.y)
        val realSize = (size.x * scale, size.y * scale)
        val offset = (windowSize.width - realSize.x, windowSize.height - realSize.y)
        g2.translate(offset.x / 2, offset.y / 2)
        //        g2.scale(scale, scale)
        g2.setColor(blockColour) and g2.fillRect(0, 0, size.x * scale, size.y * scale)
        g2.setColor(snakeColour)
        snake.elements foreach { case SnakeElement(pos) => g2.fillRect(pos.x * scale, pos.y * scale, scale, scale) }
        s.fruit.collect {
          case (FruitElement((x, y))) =>
            g2 setColor fruitColour and (g2 drawOval (x * scale, y * scale, scale, scale))
        }
      case Win | Lost => g2.drawString("Game end!",
        this.getSize().getWidth.toInt / 2,
        this.getSize().getHeight.toInt / 2)
    }

    //acquire drawn image
    //scale up, draw to self
    //draw score?
  }

  override def windowActivated(e: WindowEvent): Unit =
    super.windowActivated(e) and resume()

  override def windowDeactivated(e: WindowEvent): Unit =
    super.windowDeactivated(e) and pause()

  override def windowClosing(e: WindowEvent): Unit =
    super.windowClosing(e) and stop()

  override def windowOpened(e: WindowEvent): Unit =
    super.windowOpened(e) and start()
}
