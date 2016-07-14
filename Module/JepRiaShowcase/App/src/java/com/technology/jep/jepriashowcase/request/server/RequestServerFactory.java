package com.technology.jep.jepriashowcase.request.server;

import static com.technology.jep.jepriashowcase.request.server.RequestServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepriashowcase.request.server.dao.Request;
import com.technology.jep.jepriashowcase.request.server.dao.RequestDao;

public class RequestServerFactory extends ServerFactory<Request> {

  private RequestServerFactory() {
    super(new RequestDao(), DATA_SOURCE_JNDI_NAME);
  }

  public static final RequestServerFactory instance = new RequestServerFactory();
  
}
