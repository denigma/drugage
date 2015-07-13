package org.denigma.drugage.domain

case class Organism(specie: String, strain: Option[String], gender: Option[Gender])
