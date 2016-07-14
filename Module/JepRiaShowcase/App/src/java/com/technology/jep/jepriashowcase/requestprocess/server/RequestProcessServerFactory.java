package com.technology.jep.jepriashowcase.requestprocess.server;

import static com.technology.jep.jepriashowcase.requestprocess.server.RequestProcessServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepriashowcase.requestprocess.server.dao.RequestProcess;
import com.technology.jep.jepriashowcase.requestprocess.server.dao.RequestProcessDao;

public class RequestProcessServerFactory extends ServerFactory<RequestProcess> {

  private RequestProcessServerFactory() {
    super(new RequestProcessDao(), DATA_SOURCE_JNDI_NAME);
  }

  public static final RequestProcessServerFactory instance = new RequestProcessServerFactory();
  
}
