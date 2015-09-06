package org.denigma.drugage.views

import org.denigma.binding.binders.{NavigationBinder, GeneralBinder}
import org.denigma.binding.views.{ItemsSeqView, BindableView}
import org.scalajs.dom.raw.HTMLElement
import rx.Rx
import rx.core.Var
import rx.ops._

import scala.collection.immutable.Map

object TestData{

  val menuItems: List[String] = List("Find Plasmids", "Deposit Plasmids", "How to Order", "Plasmid Reference")

  val prefix = "test/"
}

/**
 * Menu view, this view is devoted to displaying menus
 * @param elem html element
 * @param params view params (if any)
 */
class MenuView(val elem: HTMLElement, val params: Map[String, Any] = Map.empty) extends ItemsSeqView
{

  override type Item = String

  override type ItemView = MenuItem

  override def newItem(item: Item): ItemView = this.constructItemView(item){ case (el, mp)=>
    new MenuItem(el, item, mp).withBinders(i=>List(new GeneralBinder(i), new NavigationBinder(i)))
  }

  override val items: Rx[List[Item]] = Var(TestData.menuItems)

}

class MenuItem(val elem: HTMLElement, value: String, val params: Map[String, Any] = Map.empty) extends BindableView{

  val label: Var[String] = Var(value)
  val uri: Rx[String] = label.map(l=>TestData.prefix + l.replace(" ", "_"))

}
