package ohnosequences.collections

import ohnosequences.stuff._

object unsafe {

  object array {

    @inline
    final def fromTag[A]: Tag[A] -> (Size -> JVMArray[A]) =
      λ { tag =>
        λ { tag.newArray(_: Size) }
      }

    @inline
    final def fromValue[A]: A -> (Size -> JVMArray[A]) =
      tagForArray[A] >-> fromTag

    @inline
    final def tagForArray[A]: A -> Tag[A] =
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
          case _: NonEmpty[_]         => tag.ofType[WArray].asInstanceOf[Tag[A]]
          case _: Empty[_]            => tag.ofType[WArray].asInstanceOf[Tag[A]]
          case _                      => tag.ofType[AnyRef].asInstanceOf[Tag[A]]
        }
      }

    @inline
    final def joinNonEmpty[A]: JVMArray[A] × JVMArray[A] -> JVMArray[A] =
      λ { arrs =>
        val arr1 = arrs.left
        val arr2 = arrs.right

        val res =
          fromValue(arr1(0))(arr1.length + arr2.length)

        arr2.copyTo(
          arr1.copyTo(res, 0, 0, arr1.length),
          0,
          arr1.length,
          arr2.length
        )
      }
  }
}
