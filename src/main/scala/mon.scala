package ohnosequences.collections

import ohnosequences.stuff._

object mon {

  type Mon[Z] =
    WArray { type Elements = Z }

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

    type On = FreeMonoid.type
    val on = FreeMonoid

    object ι extends NaturalTransformation {

      type SourceCategory = Scala
      val sourceCategory = on.source

      type TargetCategory = Scala
      val targetCategory = on.target

      type SourceFunctor = Functor.is[Functor.Identity[OnCat]]
      val sourceFunctor: SourceFunctor = Functor.identity(Scala)

      type TargetFunctor = Functor.is[On]
      val targetFunctor = on

      def apply[X]: X -> Mon[X] =
        WArray.singleton
    }

    object μ extends NaturalTransformation {

      type SourceCategory = Scala
      val sourceCategory = on.source

      type TargetCategory = Scala
      val targetCategory = on.target

      type SourceFunctor = (Functor.is[On] ∘ Functor.is[On])
      val sourceFunctor: Functor.is[SourceFunctor] =
        Functor.composition { FreeMonoid and FreeMonoid }

      type TargetFunctor = Functor.is[On]
      val targetFunctor = on

      def apply[X]: Mon[Mon[X]] -> Mon[X] =
        // TODO flatMap/flatten
        scala.Predef.???
    }

  }
}
