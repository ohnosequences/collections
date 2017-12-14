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
