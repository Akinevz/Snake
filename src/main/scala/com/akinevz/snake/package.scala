package com.akinevz

import scala.util.Random

package object snake {

  
  implicit class ButOps[R](val r: R) extends AnyVal {
    @inline def but[Ignored <: Unit](op: Ignored): R = r

    @inline def :~[Ignored <: Unit](op: Ignored): R = r
  }

  implicit class AndOps[IgnoredResult <: Unit](val v: (IgnoredResult)) extends AnyVal {

    @inline def and[T](t2: => T): T = t2

    @inline def &[T](t2: => T): T = t2
  }

  implicit class PositionOps(val position: Position) extends AnyVal {
    @inline def x = position._1

    @inline def y = position._2

    @inline def offset(direction: Direction): Position =
      (position.x + direction.x, position.y + direction.y)
  }

  implicit class DimensionOps(val dimension: Dimension) extends AnyVal {

    def totalSpaces: Int = dimension.x * dimension.y

    def generate: Position = Random.nextInt(dimension.x) -> Random.nextInt(dimension.y)

    def contains(position: Position): Boolean =
      position.x > -1 && position.y > -1 && position.x < dimension.x && position.y < dimension.y

    def wrap(position: Position): Position =  {
      def mod(v: Int, bound: Int): Int = if (v < 0) bound + v % bound else v % bound

      mod(position.x, dimension.x) -> mod(position.y, dimension.y)
    }

    /*  def isOutOfBounds(position: Position): Boolean = {
        def outside(v: Int, lower: Int, upper: Int): Boolean = v < lower || upper < v

        outside(position._1, 0, dimensions._1 - 1) || outside(position._2, 0, dimensions._2 - 1)
      }

      def wrapAround(position: Position): Position = {
        def mod(v: Int, bound: Int): Int = if (v < 0) bound + v % bound else v % bound

        mod(position._1, dimensions._1) -> mod(position._2, dimensions._2)
      }
  */
  }

  type Position = (Int, Int)
  type Dimension = (Int, Int)
}
