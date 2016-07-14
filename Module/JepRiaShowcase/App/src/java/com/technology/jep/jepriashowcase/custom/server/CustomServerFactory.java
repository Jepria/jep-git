package com.technology.jep.jepriashowcase.custom.server;

import static com.technology.jep.jepriashowcase.custom.server.CustomServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepriashowcase.custom.server.dao.Custom;
import com.technology.jep.jepriashowcase.custom.server.dao.CustomDao;

public class CustomServerFactory extends ServerFactory<Custom> {

  private CustomServerFactory() {
    super(new CustomDao(), DATA_SOURCE_JNDI_NAME);
  }

  public static final CustomServerFactory instance = new CustomServerFactory();
  
}
