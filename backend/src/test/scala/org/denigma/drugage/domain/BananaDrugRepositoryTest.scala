package org.denigma.drugage.domain

import org.openrdf.repository.RepositoryConnection
import org.openrdf.repository.sail.SailRepository
import org.openrdf.sail.memory.MemoryStore
import org.w3.banana.sesame.Sesame
import squants.mass.Kilograms
import squants.time.Hours


import org.denigma.drugage.UnitSpec

class BananaDrugRepositoryTest extends UnitSpec {

  val drug: Drug = Drug(compoundName = "compound name", synonyms = Set("synonym"),
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
}
