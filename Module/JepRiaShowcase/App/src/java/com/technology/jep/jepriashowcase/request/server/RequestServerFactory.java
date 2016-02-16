package com.technology.jep.jepriashowcase.request.server;

import static com.technology.jep.jepriashowcase.request.server.RequestServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.dao.transaction.TransactionFactory;
import com.technology.jep.jepriashowcase.request.server.dao.Request;
import com.technology.jep.jepriashowcase.request.server.dao.RequestDao;

public class RequestServerFactory implements ServerFactory<Request> {

	private RequestServerFactory(){}

	public static final ServerFactory<Request> instance = new RequestServerFactory();
	
	private Request dao;
	
	@Override
	public Request getDao() {
		if (dao == null) {
			dao = TransactionFactory.process(new RequestDao(), DATA_SOURCE_JNDI_NAME);
		}
		return dao;
	}

}
