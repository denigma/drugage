package org.denigma.drugage.views

import org.denigma.binding.extensions._
import org.denigma.binding.views.BindableView
import org.denigma.drugage.views.stubs.WithDomain
import org.querki.jquery._
import org.scalajs.dom.raw.HTMLElement
import rx._
/**
 * View for the sidebar
 */
class SidebarView (val elem: HTMLElement, val params: Map[String, Any] = Map.empty[String, Any]) extends BindableView with WithDomain{

  override def activateMacro(): Unit = { extractors.foreach(_.extractEverything(this))}

  val logo = Var("/resources/drugs_lifespan.gif")

  override def bindElement(el: HTMLElement): Unit = {
    super.bindElement(el)
    $(".ui.accordion").dyn.accordion()
  }

  override protected def attachBinders(): Unit =  binders = BindableView.defaultBinders(this)
}
