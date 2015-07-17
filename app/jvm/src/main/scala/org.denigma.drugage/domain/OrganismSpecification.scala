package org.denigma.drugage.domain

case class OrganismSpecification(specie: Option[String] = None, strain: Option[String] = None,
  gender: Option[Gender] = None) {

  def satisfiedBy(o: Organism): Boolean = Seq(
    specie.map(_ == o.specie),
    for {s <- strain; os <- o.strain} yield s == os,
    for {g <- gender; og <- o.gender} yield g == og
  ).forall(_.getOrElse(true))
}
