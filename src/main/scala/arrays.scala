package ohnosequences.collections

import ohnosequences.stuff._

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

object Empty extends WArray {

  @inline final def isEmpty: Boolean =
    true

  def elements: JVMArray[Elements] =
    scala.Predef.???

  @inline final def length: Size =
    0
}

object WArray {

  def singleton[A]: A -> Mon[A] =
    λ { a =>
      new NonEmpty(JVMArray(a)(tagOf(a)))
    }

  type Mon[Z] =
    WArray { type Elements = Z }

  def unit[A]: Mon[A] =
    Empty.asInstanceOf[Mon[A]]

  @inline final private[collections] def tagOf[A]: A -> Tag[A] =
    λ { a =>
      a.asInstanceOf[AnyRef] match {
        case _: java.lang.Boolean   => Tag.Boolean.asInstanceOf[Tag[A]]
        case _: java.lang.Byte      => Tag.Byte.asInstanceOf[Tag[A]]
        case _: java.lang.Character => Tag.Char.asInstanceOf[Tag[A]]
        case _: java.lang.Double    => Tag.Double.asInstanceOf[Tag[A]]
        case _: java.lang.Float     => Tag.Float.asInstanceOf[Tag[A]]
        case _: java.lang.Integer   => Tag.Int.asInstanceOf[Tag[A]]
        case _: java.lang.Long      => Tag.Long.asInstanceOf[Tag[A]]
        case _: java.lang.Short     => Tag.Short.asInstanceOf[Tag[A]]
        case _                      => Tag(a.getClass)
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