package com.technology.jep.jepriashowcase.arsenic.server;

import static com.technology.jep.jepriashowcase.arsenic.server.ArsenicServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepriashowcase.arsenic.server.dao.Arsenic;
import com.technology.jep.jepriashowcase.arsenic.server.dao.ArsenicDao;

public class ArsenicServerFactory extends ServerFactory<Arsenic> {

  private ArsenicServerFactory() {
    super(new ArsenicDao(), DATA_SOURCE_JNDI_NAME);
  }

  public static final ArsenicServerFactory instance = new ArsenicServerFactory();
  
}
