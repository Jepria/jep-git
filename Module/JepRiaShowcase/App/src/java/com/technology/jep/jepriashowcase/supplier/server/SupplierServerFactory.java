package com.technology.jep.jepriashowcase.supplier.server;

import static com.technology.jep.jepriashowcase.supplier.server.SupplierServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.dao.transaction.TransactionFactory;
import com.technology.jep.jepriashowcase.supplier.server.dao.Supplier;
import com.technology.jep.jepriashowcase.supplier.server.dao.SupplierDao;

public class SupplierServerFactory implements ServerFactory<Supplier> {

	private SupplierServerFactory(){}

	public static final ServerFactory<Supplier> instance = new SupplierServerFactory();
	
	private Supplier dao;
	
	@Override
	public Supplier getDao() {
		if (dao == null) {
			dao = TransactionFactory.process(new SupplierDao(), DATA_SOURCE_JNDI_NAME);
		}
		return dao;
	}

}
