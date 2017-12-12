package ohnosequences.collections

import ohnosequences.stuff._, NaturalTransformation._, Functor.{∘}

object mon {

  type Mon[Z] =
    WArray { type Elements = Z }

  type FreeMonoid =
    FreeMonoid.type

  object FreeMonoid extends Functor {

    type Source = Scala
    val source = Scala

    type Target = Scala
    val target = Scala

    type F[X] = Mon[X]

    def at[X, Y] =
      WArray.map
  }

  object FreeMonoidMonad extends Monad {

    type On =
      FreeMonoid

    val on: Functor.is[FreeMonoid.type] =
      FreeMonoid

    val ι: Functor.Identity[Scala] ~> FreeMonoid =
      new Between[Functor.Identity[Scala], FreeMonoid](Functor identity Scala, on) {

        @inline
        final def apply[X]: X -> Mon[X] =
          WArray.singleton
      }

    val μ: (FreeMonoid ∘ FreeMonoid) ~> FreeMonoid =
      new Between[FreeMonoid ∘ FreeMonoid, FreeMonoid](FreeMonoid ∘ FreeMonoid, FreeMonoid) {

        @inline
        final def apply[X]: Mon[Mon[X]] -> Mon[X] =
          scala.Predef.???
      }
  }
}
