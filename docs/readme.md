# Collections

## Monoids

We are wrapping arrays, and using some tricks to make them work without any implicit [ClassTag](http://www.scala-lang.org/api/2.12.x/scala/reflect/ClassTag.html)s. The approach is equivalent to that of [rklaehn/abc](https://rklaehn.github.io/2015/12/18/array-based-immutable-collections/).

## Equality-based collections

> See [TomasMikula/hasheq](https://github.com/TomasMikula/hasheq) and this [post](http://typelevel.org/blog/2017/04/02/equivalence-vs-equality.html)

The idea is that each notion of equality represents a *full* subcategory of `Scala`, on which we can (hopefully) define (idempotent) commutative monoids. An object of these categories will keep track of its own equality, either directly as a value (so we are working with a category of setoids) or through implicits associated with them.

At any rate, these categories have products and coproducts, defined in the obvious way. They're not cartesian-closed in general in any expressable way.

#### Implicits

The objects bound could imply a non-constructive proof of the availability of an equality instance for it, if the only way of building values is through one such instance. Products, sums, etc could be implemented based on this in the only logical way.

``` scala
trait AnySetoid {

  type On
  def a: On
  // equivalence relation etc
}
type Setoid[A] = AnySetoid { type On = A }

type Objects = AnySetoid

def asObject[A](a: A)(implicit eq: Equiv[A]): Setoid[A] =
  new Setoid[A] {
    // ...
  }
```

Happily, we can use fastutil concept of [HashStrategy](http://fastutil.di.unimi.it/docs/it/unimi/dsi/fastutil/Hash.Strategy.html) to build (idempotent) commutative monoids on top of this.

### Commutative Monoids

Right now we are wrapping a fastutil `OpenHashMap[A,Long]`. We will incorporate equality through a custom hash strategy.

## Idempotent Commutative Monoids

Same: use [ObjectOpenCustomHashSet](http://fastutil.di.unimi.it/docs/it/unimi/dsi/fastutil/objects/ObjectOpenCustomHashSet.html).
