package org.denigma.drugage.domain

trait DrugRepository {
  def getById(id: DrugId): Option[Drug]
  def getBySpecification(spec: DrugSpecification): Seq[Drug]

  def delete(id: DrugId)
  def upsert(depositor: Drug)
}
