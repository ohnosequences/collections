package ohnosequences.collections

object tag {

  def ofType[X](implicit t: Tag[X]): Tag[X] =
    t
}
