package org.denigma.drugage.domain

trait DrugRepository {
  def getById(id: DrugId): Option[Drug]
  def get(spec: DrugSpecification): Seq[Drug]

  def delete(id: DrugId): Unit
  def upsert(depositor: Drug): Unit
}
