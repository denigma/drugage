package org.denigma.drugage.domain

trait DrugRepository {
  def getById(id: DrugId): Option[Drug]

  def delete(id: DrugId)
  def upsert(depositor: Drug)
}
