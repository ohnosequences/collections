package ohnosequences.collections

import ohnosequences.stuff._
import mon._

sealed abstract class WArray {

  type Elements
  def elements: JVMArray[Elements]

  def isEmpty: Boolean
  def length: Size
}

final class NonEmpty[A](val elements: JVMArray[A]) extends WArray {

  type Elements = A

  @inline final def isEmpty: Boolean =
    false

  @inline override final def length: Size =
    elements.length
}

final class Empty[A] extends WArray {

  type Elements = A

  @inline final def isEmpty: Boolean =
    true

  def elements: JVMArray[Elements] =
    scala.Predef.???

  @inline final def length: Size =
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
            arrayFromValue(generator(0))(size)

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
      new NonEmpty(JVMArray[A](a)(tagOf(a)))
    }

  def unit[A]: Mon[A] =
    new Empty[A]

  @inline final def tagOf[A]: A -> Tag[A] =
    λ { a: A =>
      a.asInstanceOf[AnyRef] match {
        case _: java.lang.Boolean   => Tag.Boolean.asInstanceOf[Tag[A]]
        case _: java.lang.Byte      => Tag.Byte.asInstanceOf[Tag[A]]
        case _: java.lang.Character => Tag.Char.asInstanceOf[Tag[A]]
        case _: java.lang.Double    => Tag.Double.asInstanceOf[Tag[A]]
        case _: java.lang.Float     => Tag.Float.asInstanceOf[Tag[A]]
        case _: java.lang.Integer   => Tag.Int.asInstanceOf[Tag[A]]
        case _: java.lang.Long      => Tag.Long.asInstanceOf[Tag[A]]
        case _: java.lang.Short     => Tag.Short.asInstanceOf[Tag[A]]
        case _: NonEmpty[_]         => tag[WArray].asInstanceOf[Tag[A]]
        case _: Empty[_]            => tag[WArray].asInstanceOf[Tag[A]]
        case _                      => tag[AnyRef].asInstanceOf[Tag[A]]
      }
    }

  final def map[X, Y]: (X -> Y) -> (Mon[X] -> Mon[Y]) =
    λ { f =>
      λ { xs =>
        if (xs.isEmpty) unit[Y]
        else {

          val ys =
            arrayFromValue(f at xs.elements(0)) at xs.length

          var i = 1
          while (i < xs.length) {
            ys(i) = f(xs.elements(i))
            i = i + 1
          }

          new NonEmpty(ys)
        }
      }
    }

  def at[A]: Index -> (Mon[A] -> A) =
    λ { n =>
      λ { _.elements(n) }
    }

  private final def arrayFromTag[A]: Tag[A] -> (Size -> JVMArray[A]) =
    λ { tag =>
      λ { tag.newArray(_: Size) }
    }

  @inline private final def arrayFromValue[A]: A -> (Size -> JVMArray[A]) =
    tagOf[A] >-> arrayFromTag

  private final def joinNonEmptyArrays[A]
    : JVMArray[A] × JVMArray[A] -> NonEmpty[A] =
    λ { arrs =>
      val arr1 = arrs.left
      val arr2 = arrs.right

      val res =
        arrayFromValue(arr1(0))(arr1.length + arr2.length)

      new NonEmpty(
        arr2.copyTo(
          arr1.copyTo(res, 0, 0, arr1.length),
          0,
          arr1.length,
          arr2.length
        )
      )
    }

  def concat[Z]: Mon[Z] × Mon[Z] -> Mon[Z] =
    λ { xsys =>
      val xs: Mon[Z] = xsys.left
      val ys: Mon[Z] = xsys.right

      if ((!xs.isEmpty) && (!ys.isEmpty))
        joinNonEmptyArrays(
          xs.asInstanceOf[NonEmpty[Z]]
            .elements and ys.asInstanceOf[NonEmpty[Z]].elements)
      else if (xs.isEmpty) ys
      else xs
    }
}
