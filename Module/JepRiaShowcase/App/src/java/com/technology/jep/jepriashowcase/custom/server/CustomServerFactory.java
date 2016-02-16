package com.technology.jep.jepriashowcase.custom.server;

import static com.technology.jep.jepriashowcase.custom.server.CustomServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.dao.transaction.TransactionFactory;
import com.technology.jep.jepriashowcase.custom.server.dao.Custom;
import com.technology.jep.jepriashowcase.custom.server.dao.CustomDao;

public class CustomServerFactory implements ServerFactory<Custom> {

	private CustomServerFactory(){}

	public static final ServerFactory<Custom> instance = new CustomServerFactory();
	
	private Custom dao;
	
	@Override
	public Custom getDao() {
		if (dao == null) {
			dao = TransactionFactory.process(new CustomDao(), DATA_SOURCE_JNDI_NAME);
		}
		return dao;
	}

}
