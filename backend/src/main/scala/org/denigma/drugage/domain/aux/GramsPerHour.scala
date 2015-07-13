package org.denigma.drugage.domain.aux

import squants.MetricSystem
import squants.motion.MassFlowUnit
import squants.time.Time

object GramsPerHour extends MassFlowUnit {
  val symbol = "g/hr"
  val conversionFactor = MetricSystem.Milli / Time.SecondsPerHour
}
