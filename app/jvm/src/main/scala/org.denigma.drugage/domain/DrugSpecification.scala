package org.denigma.drugage.domain

case class DrugSpecification(compoundName: Option[String] = None, organism: OrganismSpecification = OrganismSpecification(),
  pubmedId: Option[String] = None, id: Option[DrugId] = None)
