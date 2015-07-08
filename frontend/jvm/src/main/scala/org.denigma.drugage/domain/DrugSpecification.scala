package org.denigma.drugage.domain

case class DrugSpecification(compoundName: Option[String] = None, organism: OrganismSpecification = OrganismSpecification(),
  pubmedId: Option[String] = None, id: Option[DrugId] = None) {

  def satisfiedBy(d: Drug): Boolean = Seq(
    compoundName.map(_ == d.compoundName),
    Some(organism.satisfiedBy(d.organism)),
    pubmedId.map(_ == d.pubmedId),
    id.map(_ == d.id)
  ).forall(_.getOrElse(true))
}
