package org.denigma.drugage.domain.aux

import org.w3.banana.{Prefix, RDFOps, RDF}

case class DrugsNS[Rdf <: RDF: RDFOps]() {
  private[this] val ns: Prefix[Rdf] = Prefix[Rdf]("drugsage", "https://denigma.org")

  def apply(name: String): Rdf#URI = ns(name)

  val drugs: Rdf#URI = apply("drugs")

  val specie = apply("specie")
  val strain = apply("strain")
  val gender = apply("gender")
  val statistic = apply("statistic")
  val changes_lifespan_by = apply("changes_lifespan_by")
  val increases_lifespan_at_least_by = apply("increases_lifespan_at_least_by")
  val increases_lifespan_up_to = apply("increases_lifespan_up_to")
  val compound_name = apply("compound_name")
  val synonym = apply("synonym")
  val organism = apply("organism")
  val dosage = apply("dosage")
  val lifespanChange = apply("lifespan_change")
  val pubmed_id = apply("pubmed_id")
  val notes = apply("notes")
  val nonSignificant = apply("non_significant")
  val drugId = apply("drug_id")
}
