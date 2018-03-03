package com.akinevz.snake

import java.awt.event.ActionEvent
import javax.swing.{AbstractAction, JComponent, KeyStroke}

object Keybindings {
  type Keycode = Int
  type GameEvent = (GameState) => GameState

  import java.awt.event.KeyEvent._

  def changeDirection(newDirection: Direction): GameEvent = {
    case e@PlayingState(Snake(travelling,_),_,_) if travelling opposes newDirection=>e
    case g: PlayingState => g copy (g.player copy newDirection)
    case e => e
  }

  def keybinds: Map[Keycode, GameEvent] = Map(
    VK_UP -> changeDirection(North),
    VK_DOWN -> changeDirection(South),
    VK_RIGHT -> changeDirection(East),
    VK_LEFT -> changeDirection(West)
  )

  def register(component: GameFrame): Unit = {
    val inputMap = component.getInputMap
    val actionMap = component.getActionMap
    keybinds foreach {
      case (kc, ge) =>
        actionMap put(kc, new AbstractAction() {
          override def actionPerformed(e: ActionEvent): Unit = component.game = ge(component.game)
        })
        inputMap.put(KeyStroke.getKeyStroke(kc, 0), kc)
    }
  }
}
