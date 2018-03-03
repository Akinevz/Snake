package com.akinevz.snake

case class Snake private(travelling: Direction, elements: List[SnakeElement]) {

  def this(travelling:Direction,gridSize:Dimension) = {
    this(travelling,List(SnakeElement((gridSize.x/2,gridSize.y/2))))
  }

  object Head{
    def unapply(arg: Snake): Option[(Position)] = Some(arg.head.pos)


  }
  def head: SnakeElement = elements.head

  def tip: SnakeElement = elements.last

  def grow: Snake = {
    val next = head copy (head.pos offset travelling)
    this copy (elements = next +: elements)
  }

  def move: Snake = {
    val last = tip move (head.pos offset travelling)
    val updated = last +: elements.init
    this copy (elements = updated)
  }

  def body: List[SnakeElement] = elements drop 3 //dropping all above "neck"

}

