package com.technology.jep.jepriashowcase.simple.client;

import com.google.gwt.core.client.GWT;
import com.technology.jep.jepriashowcase.simple.shared.SimpleConstant;
import com.technology.jep.jepriashowcase.simple.shared.text.SimpleText;

public class SimpleClientConstant extends SimpleConstant {
  
  /**
   * Тексты клиентской части Simple.
   */
  public static SimpleText simpleText = (SimpleText) GWT.create(SimpleText.class);
}
