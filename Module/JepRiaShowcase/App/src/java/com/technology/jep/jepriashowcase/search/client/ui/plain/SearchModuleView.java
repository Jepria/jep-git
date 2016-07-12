package com.technology.jep.jepriashowcase.search.client.ui.plain;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.technology.jep.jepria.client.ui.plain.PlainModuleView;
import com.technology.jep.jepria.client.widget.list.ListManager;
import com.technology.jep.jepriashowcase.search.client.shop.ShoppingCart;

public interface SearchModuleView extends PlainModuleView {

  void addSearchButtonClickHandler(ClickHandler clickHandler);
  
  String getSearchText();

  ListManager getListManager();
  void setShoppingCart(ShoppingCart cart);
  Label getShoppingCartLabel();
}
