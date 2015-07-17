package org.denigma.drugage.domain

trait Gender

case object MALE extends Gender {
  override val toString: String = "male"
}

case object FEMALE extends Gender {
  override val toString: String = "female"
}
