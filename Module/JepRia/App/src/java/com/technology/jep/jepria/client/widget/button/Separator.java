package com.technology.jep.jepria.client.widget.button;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

/**
 * Разделитель тулбара.
 */
public class Separator extends Widget {
  
  private static final String SEPARATOR_STYLE = "jepRia-Separator";
  
  /**
   * Создаёт разделитель тулбара.
   */
  public Separator() {
    setElement(DOM.createSpan());
    addStyleName(SEPARATOR_STYLE);
  }
  
}
