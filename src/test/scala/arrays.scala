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
    val x3 = singleton(x2)
    printTag(x2)
    println(Tag(x2.elements.getClass))

    val z = fill(2) {
      λ { pos: Int =>
        pos
      }
    }
    println(Tag(z.elements.getClass))

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

class Arrays extends org.scalatest.FunSuite {

  /**
    * Tests whether the array generated with flatten has the correct length,
    * in the case where the shape of the original array is
    * ((), (0), (0, 1), ..., (0, 1, ..., n-1))
    */
  test("flatten") {
    val nestedLength: Size     = 1000
    val numberOfElements: Size = nestedLength * (nestedLength - 1) / 2

    println(numberOfElements)

    val nested = fill(nestedLength)(
      λ { idx: Index =>
        fill(idx)(
          λ { innerIdx: Index =>
            innerIdx
          }
        )
      }
    )

    val unnested = WArray.flatten(nested)

    assert(numberOfElements == unnested.length)
  }
}
