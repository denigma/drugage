package org.denigma.drugage.domain

import org.openrdf.repository.RepositoryConnection
import org.openrdf.repository.sail.SailRepository
import org.openrdf.sail.memory.MemoryStore
import org.w3.banana.sesame.Sesame
import squants.mass.Kilograms
import squants.time.Hours


import org.denigma.drugage.UnitSpec

class BananaDrugRepositoryTest extends UnitSpec {

  val drug: Drug = Drug(id="1", compoundName = "compound name", synonyms = Set("synonym"),
    organism = Organism("big rat", Some("sick"), Some(MALE)), dosage = Kilograms(10) / Hours(1),
    lifespanChanges = Map(MEAN -> ChangesBy(1.5)), pubmedId = "pubmed article", notes = Some("notes"))

  trait Repository {
    private[this] val sesameRepo = new SailRepository(new MemoryStore())
    sesameRepo.initialize()
    val repository = new BananaDrugRepository[Sesame, RepositoryConnection](sesameRepo.getConnection)
  }

  it should "remove drug by id" in new Repository {
    repository.upsert(drug)
    repository.delete(drug.id)
    assertResult(None) { repository.getById(drug.id) }
  }

  it should "persist and retrieve drug" in new Repository {
    repository.upsert(drug)
    repository.getById(drug.id).value shouldBe drug
  }

  it should "return None for wrong ids" in new Repository {
    assertResult(None) { repository.getById("absent") }
  }

  it should "return drugs matching to specifications with compound name" in new Repository {
    val spec = DrugSpecification(compoundName = Some(drug.compoundName))

    repository.upsert(drug)
    repository.upsert(drug.copy(id="2", compoundName = "other name"))
    repository.upsert(drug.copy(id="3", compoundName = "one more name"))

    assertResult(Seq(drug)) { repository.get(spec) }
  }

  it should "return drugs matching to specifications with id" in new Repository {
    val spec = DrugSpecification(id = Some("3"))

    val drug3 = drug.copy(id="3", compoundName = "one more name")

    repository.upsert(drug)
    repository.upsert(drug.copy(id="2", compoundName = "other name"))
    repository.upsert(drug3)

    assertResult(Seq(drug3)) { repository.get(spec) }
  }

  it should "return drugs matching to specifications with pubmed id" in new Repository {
    val spec = DrugSpecification(pubmedId = Some(drug.pubmedId))

    repository.upsert(drug)
    repository.upsert(drug.copy(id="2", pubmedId = "other pubmed id"))
    repository.upsert(drug.copy(id="3", pubmedId = "some other pubmed id"))

    assertResult(Seq(drug)) { repository.get(spec) }
  }

  it should "return drugs matching to specifications with organism specie" in new Repository {
    val spec = DrugSpecification(organism = OrganismSpecification(specie=Some("big rat")))

    val drug2 = drug.copy(id="2", organism = Organism("big rat", Some("healthy"), Some(MALE)))
    val drug3 = drug.copy(id="3", organism = Organism("big vugluskr", Some("healthy"), Some(MALE)))

    repository.upsert(drug)
    repository.upsert(drug2)
    repository.upsert(drug3)

    assertResult(Seq(drug, drug2)) { repository.get(spec) }
  }

  it should "return drugs matching to specifications with organism strain" in new Repository {
    val spec = DrugSpecification(organism = OrganismSpecification(strain=Some("sick")))

    val drug2 = drug.copy(id="2", organism = Organism("big rat", Some("healthy"), Some(MALE)))
    val drug3 = drug.copy(id="3", organism = Organism("big vugluskr", Some("sick"), Some(MALE)))

    repository.upsert(drug)
    repository.upsert(drug2)
    repository.upsert(drug3)

    assertResult(Seq(drug, drug3)) { repository.get(spec) }
  }

  it should "return drugs matching to specifications with organism gender" in new Repository {
    val spec = DrugSpecification(organism = OrganismSpecification(gender=Some(MALE)))

    val drug2 = drug.copy(id="2", organism = Organism("big rat", Some("healthy"), Some(MALE)))
    val drug3 = drug.copy(id="3", organism = Organism("big vugluskr", Some("sick"), Some(FEMALE)))

    repository.upsert(drug)
    repository.upsert(drug2)
    repository.upsert(drug3)

    assertResult(Seq(drug, drug2)) { repository.get(spec) }
  }

  it should "return drugs matching to specifications with combination of criteria" in new Repository {
    val spec = DrugSpecification(compoundName = Some(drug.compoundName),
      organism = OrganismSpecification(gender=Some(MALE)))

    val drug2 = drug.copy(id="2", organism = Organism("big rat", Some("healthy"), Some(FEMALE)))
    val drug3 = drug.copy(id="3", compoundName = "other name")

    repository.upsert(drug)
    repository.upsert(drug2)
    repository.upsert(drug3)

    assertResult(Seq(drug)) { repository.get(spec) }
  }
}
