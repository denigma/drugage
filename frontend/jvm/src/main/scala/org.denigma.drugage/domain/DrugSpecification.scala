package org.denigma.drugage.domain

case class DrugSpecification(compoundName: Option[String], synonyms: Set[String], organism: OrganismSpecification,
  pubmedId: Option[String], id: Option[DrugId])
