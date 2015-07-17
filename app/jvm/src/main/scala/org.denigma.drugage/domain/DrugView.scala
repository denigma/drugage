package org.denigma.drugage.domain


case class DrugView(id: String, compoundName: String, synonyms: Set[String], organism: Organism, dosage: String,
  lifespanChanges: Map[Statistic, LifespanChange], pubmedId: String, notes: Option[String], nonSignificant: Boolean)
