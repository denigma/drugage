package org.denigma.drugage.views

import org.denigma.drugage.views.stubs.WithDomain
import org.denigma.binding.views.BindableView
import org.querki.jquery._
import org.scalajs.dom.raw.HTMLElement
import org.semantic.ui._
import rx._
/**
 * View for the sidebar
 */
class SidebarView (val elem: HTMLElement, val params: Map[String, Any] = Map.empty[String, Any]) extends BindableView with WithDomain{

  val logo = Var("/resources/drugs_lifespan.gif")

  override def bindElement(el: HTMLElement): Unit = {
    super.bindElement(el)
    $(".ui.accordion").accordion()
  }

}
