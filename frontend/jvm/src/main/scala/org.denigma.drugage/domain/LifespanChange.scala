package org.denigma.drugage.domain

import squants.Time

sealed trait LifespanChange

case class ChangesBy(ratio: Double) extends LifespanChange

case class IncreasesAtLeastBy(delta: Time) extends LifespanChange

case class IncreasesUpTo(age: Time) extends LifespanChange
