package com.technology.jep.jepriashowcase.search.server;

import static com.technology.jep.jepriashowcase.search.server.SearchServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepriashowcase.search.server.dao.Search;
import com.technology.jep.jepriashowcase.search.server.dao.SearchDao;

public class SearchServerFactory extends ServerFactory<Search> {

  private SearchServerFactory() {
    super(new SearchDao(), DATA_SOURCE_JNDI_NAME);
  }

  public static final SearchServerFactory instance = new SearchServerFactory();
  
}
