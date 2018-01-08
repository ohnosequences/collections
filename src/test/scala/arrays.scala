package ohnosequences.collections.test

import ohnosequences.stuff._
import ohnosequences.collections._

class arrays extends org.scalatest.FunSuite {

  test("singletons") {

    WArray singleton 2

    val ups =
      WArray.fill(10000000) {
        λ { n: Index =>
          n
        }
      }

    val plus1 =
      λ { (_: scala.Int) + 1 }

    val argh =
      (WArray map plus1) at ups
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
