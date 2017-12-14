package ohnosequences

import scala.Int

package object collections {

  type Size =
    scala.Int

  type Index =
    scala.Int

  type Boolean =
    scala.Boolean

  type System =
    java.lang.System

  def copyJVMArray[X](from: JVMArray[X],
                      i1: Int,
                      to: JVMArray[X],
                      i2: Int,
                      i3: Int): JVMArray[X] = {

    java.lang.System.arraycopy(from, i1, to, i2, i3)
    to
  }

  implicit final class JVMArrayOps[X](val array: JVMArray[X])
      extends scala.AnyVal {

    def copyTo(
        target: JVMArray[X],
        sourceStart: Index,
        targetStart: Index,
        length: Size,
    ): JVMArray[X] =
      copyJVMArray(array, sourceStart, target, targetStart, length)
  }

  type Any =
    scala.Any

  type AnyRef =
    scala.AnyRef

  def tag[X](implicit t: Tag[X]): Tag[X] =
    scala.Predef.implicitly[Tag[X]]

  type Tag[X] =
    scala.reflect.ClassTag[X]

  val Tag: scala.reflect.ClassTag.type =
    scala.reflect.ClassTag

  type JVMArray[X] =
    scala.Array[X]

  val JVMArray: scala.Array.type =
    scala.Array
}
