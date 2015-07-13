package org.denigma.drugage.domain

object DrugSpecificationOps {
  implicit def pimpDrugSpecification(spec: DrugSpecification): DrugSpecificationOps = new DrugSpecificationOps(spec)
}

class DrugSpecificationOps(spec: DrugSpecification) {

  def satisfiedBy(d: Drug): Boolean = Seq(
    spec.compoundName.map(_ == d.compoundName),
    Some(spec.organism.satisfiedBy(d.organism)),
    spec.pubmedId.map(_ == d.pubmedId),
    spec.id.map(_ == d.id)
  ).forall(_.getOrElse(true))
}
