package com.technology.jep.jepriashowcase.search.server;

import static com.technology.jep.jepriashowcase.search.server.SearchServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.dao.transaction.TransactionFactory;
import com.technology.jep.jepriashowcase.search.server.dao.Search;
import com.technology.jep.jepriashowcase.search.server.dao.SearchDao;

public class SearchServerFactory implements ServerFactory<Search> {

	private SearchServerFactory(){}

	public static final ServerFactory<Search> instance = new SearchServerFactory();
	
	private Search dao;
	
	@Override
	public Search getDao() {
		if (dao == null) {
			dao = TransactionFactory.process(new SearchDao(), DATA_SOURCE_JNDI_NAME);
		}
		return dao;
	}

}
