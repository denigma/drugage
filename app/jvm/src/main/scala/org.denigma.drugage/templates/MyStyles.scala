package org.denigma.drugage.templates

import scala.language.postfixOps

import scalacss.Defaults._

object MyStyles extends StyleSheet.Standalone {
  import dsl._

  "#logo"-(
    maxHeight(40 vh),
    backgroundColor(lightgoldenrodyellow)
    )

}
