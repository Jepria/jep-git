package com.technology.jep.jepriashowcase.simple.client.ui.plain;

import com.google.gwt.event.dom.client.ClickHandler;
import com.technology.jep.jepria.client.ui.plain.PlainModuleView;

public interface SimpleModuleView extends PlainModuleView {

  void addSupplierButtonClickHandler(ClickHandler clickHandler);

  void addErrorButtonClickHandler(ClickHandler clickHandler);
  
  void addCustomModuleClickHandler(ClickHandler clickHandler);
}
