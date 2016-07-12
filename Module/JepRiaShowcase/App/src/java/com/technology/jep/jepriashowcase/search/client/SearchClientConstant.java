package com.technology.jep.jepriashowcase.search.client;

import com.google.gwt.core.client.GWT;
import com.technology.jep.jepriashowcase.search.shared.SearchConstant;
import com.technology.jep.jepriashowcase.search.shared.text.SearchText;

public class SearchClientConstant extends SearchConstant {
  
  /**
   * Тексты клиентской части Search.
   */
  public static SearchText searchText = (SearchText) GWT.create(SearchText.class);
}
