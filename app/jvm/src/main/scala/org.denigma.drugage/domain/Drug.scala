package org.denigma.drugage.domain

import scala.util.Random

import squants.MassFlow

import org.denigma.drugage.domain.aux.GramsPerHour

case class Drug(compoundName: String, synonyms: Set[String], organism: Organism, dosage: MassFlow,
  lifespanChanges: Map[Statistic, LifespanChange], pubmedId: String, notes: Option[String],
  nonSignificant: Boolean = false, id: String = new Random().nextLong().toString) {

  require(lifespanChanges.nonEmpty, "Lifespan changes info for drug can't be empty")
}

object Drug {
  implicit def drug2DrugView(d: Drug): DrugView = DrugView(d.id, d.compoundName, d.synonyms, d.organism,
    d.dosage.toString(GramsPerHour, "%.1f"), d.lifespanChanges, d.pubmedId, d.notes, d.nonSignificant)
}
