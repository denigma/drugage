package org.denigma.drugage.domain

import scala.util.Random
import squants.MassFlow


case class Drug(compoundName: String, synonyms: Set[String], organism: Organism, dosage: MassFlow,
  lifespanChanges: Map[Statistic, LifespanChange], pubmedId: String, notes: Option[String],
  nonSignificant: Boolean = false, id: String = new Random().nextLong().toString) {

  require(lifespanChanges.nonEmpty, "Lifespan changes info for drug can't be empty")
}
