package org.denigma.drugage.domain

import squants.mass.Kilograms
import squants.time.Hours

import org.denigma.drugage.UnitSpec

class DrugTest extends UnitSpec {

  val drug: Drug = Drug(id="1", compoundName = "compound name", synonyms = Set("synonym"),
      organism = Organism("big rat", Some("sick"), Some(MALE)), dosage = Kilograms(0.01) / Hours(1),
      lifespanChanges = Map(MEAN -> ChangesBy(1.5)), pubmedId = "pubmed article", notes = Some("notes"))

  it should "convert Drug to DrugView implicitly" in {
    val dv: DrugView = drug
    assertResult(DrugView(drug.id, drug.compoundName, drug.synonyms, drug.organism, "10.0 g/hr",
      drug.lifespanChanges, drug.pubmedId, drug.notes, drug.nonSignificant)) { dv }
  }
}
