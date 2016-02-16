package com.technology.jep.jepriashowcase.requestprocess.server;

import static com.technology.jep.jepriashowcase.requestprocess.server.RequestProcessServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.dao.transaction.TransactionFactory;
import com.technology.jep.jepriashowcase.requestprocess.server.dao.RequestProcess;
import com.technology.jep.jepriashowcase.requestprocess.server.dao.RequestProcessDao;

public class RequestProcessServerFactory implements ServerFactory<RequestProcess> {

	private RequestProcessServerFactory(){}

	public static final ServerFactory<RequestProcess> instance = new RequestProcessServerFactory();
	
	private RequestProcess dao;
	
	@Override
	public RequestProcess getDao() {
		if (dao == null) {
			dao = TransactionFactory.process(new RequestProcessDao(), DATA_SOURCE_JNDI_NAME);
		}
		return dao;
	}

}
