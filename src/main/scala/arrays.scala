package ohnosequences.collections

import ohnosequences.stuff._
import mon._

sealed abstract class WArray {

  type Elements

  def isEmpty: Boolean
  def length: Size
}

final class NonEmpty[A](val elements: JVMArray[A]) extends WArray {

  type Elements = A

  @inline
  final def isEmpty: Boolean =
    false

  @inline
  final def length: Size =
    elements.length
}

final class Empty[A] extends WArray {

  type Elements = A

  @inline
  final def isEmpty: Boolean =
    true

  @inline
  final def length: Size =
    0
}

object WArray {

  def fill[A]: Size -> ((Index -> A) -> Mon[A]) =
    λ { size =>
      λ { generator =>
        if (size == 0)
          unit[A]
        else {
          val arr: JVMArray[A] =
            unsafe.array.fromValue(generator(0))(size)

          var idx = 0
          while (idx < arr.length) {
            arr(idx) = generator(idx)
            idx = idx + 1
          }

          new NonEmpty(arr)
        }
      }
    }

  def singleton[A]: A -> Mon[A] =
    λ { a: A =>
      val elems = unsafe.array.fromValue(a)(1)
      elems(0) = a
      new NonEmpty(elems)
    }

  def unit[A]: Mon[A] =
    new Empty[A]

  final def map[X, Y]: (X -> Y) -> (Mon[X] -> Mon[Y]) =
    λ { f =>
      λ { xs =>
        xs match {

          case xss: NonEmpty[X] => {

            val ys =
              unsafe.array.fromValue(f at xss.elements(0)) at xss.length

            var i = 1
            while (i < xss.length) {
              ys(i) = f(xss.elements(i))
              i = i + 1
            }

            new NonEmpty(ys)
          }

          case _ => unit[Y]
        }
      }
    }

  def concat[Z]: Mon[Z] × Mon[Z] -> Mon[Z] =
    λ { xsys =>
      val xs: Mon[Z] = xsys.left
      val ys: Mon[Z] = xsys.right

      xs match {

        case xss: NonEmpty[Z] =>
          ys match {

            case yss: NonEmpty[Z] =>
              new NonEmpty(
                unsafe.array.joinNonEmpty(xss.elements and yss.elements)
              )

            case _ => xs
          }

        case _ => ys
      }
    }
}
