package com.technology.jep.jepria.server;

import com.technology.jep.jepria.server.dao.transaction.TransactionFactory;

public class ServerFactory<D> implements DaoProvider<D> {
	
	protected D dao;
	private D proxyDao;
	private String dataSourceJndiName;
	
	public ServerFactory(D dao, String dataSourceJndiName){
		this.dao = dao;
		this.dataSourceJndiName = dataSourceJndiName;
	}
	
	@Override
	public D getDao(){ // в случае необходимости, можно вернуть исходное dao, переопределив данный метод
		if (proxyDao == null) {
			proxyDao = TransactionFactory.createProxy(dao, dataSourceJndiName);
		}
		return proxyDao;
	}
	
	@Override
	public String getDataSourceJndiName() { // может быть пустым
		return dataSourceJndiName;
	}
}
