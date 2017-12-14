package ohnosequences.collections.test

import ohnosequences.collections._
import ohnosequences.stuff._
import WArray._
import scala.Int
import scala.Predef.String
import mon.Mon
import scala.Predef.println
import scala.StringContext

class Tags extends org.scalatest.FunSuite {

  def printTag[A]: A -> scala.Unit =
    λ { a =>
      println(s"value: ${a} tag: ${tagOf(a)}")
    }

  test("examples") {

    printTag("hola")
    printTag(2)
    printTag(singleton(1))
    printTag(unit[Int])

    val x1: Mon[Int] = WArray.unit[Int]
    printTag(x1)
    val x2 = concat(singleton(2) and concat(x1 and singleton(2)))
    printTag(x2)

    val x = fill(2)(
      λ { idx: Int =>
        if (idx == 0)
          WArray.unit[Int]
        else
          WArray.singleton(idx)
      }
    )
    printTag(x)
  }
}
