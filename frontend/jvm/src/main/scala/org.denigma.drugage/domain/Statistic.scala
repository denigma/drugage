package org.denigma.drugage.domain

sealed trait Statistic

case object MEAN extends Statistic {
  override val toString: String = "mean"
}

case object MAX extends Statistic {
  override val toString: String = "max"
}

case object MEDIAN extends Statistic {
  override val toString: String = "median"
}
