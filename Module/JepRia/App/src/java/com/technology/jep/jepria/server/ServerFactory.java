package com.technology.jep.jepria.server;

import com.technology.jep.jepria.server.dao.transaction.TransactionFactory;

/**
 * Серверная фабрика.
 * @param <D> интерфейс Dao
 */
public class ServerFactory<D> implements DaoProvider<D> {
	
	/**
	 * Исходный объект Dao.
	 */
	protected D dao;
	/**
	 * Прокси для Dao, обеспечивающий транзакционность.
	 */
	private D proxyDao;
	/**
	 * JNDI-имя источника данных.
	 */
	private String dataSourceJndiName;
	
	/**
	 * Создаёт серверную фабрику.
	 * @param dao объект Dao
	 * @param dataSourceJndiName JNDI-имя источника данных
	 */
	public ServerFactory(D dao, String dataSourceJndiName){
		this.dao = dao;
		this.dataSourceJndiName = dataSourceJndiName;
	}
	
	/**
	 * Возвращает прокси для Dao, обеспечивающий транзакционность.
	 */
	@Override
	public D getDao() {
		if (proxyDao == null) {
			proxyDao = TransactionFactory.createProxy(dao, dataSourceJndiName);
		}
		return proxyDao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataSourceJndiName() {
		return dataSourceJndiName;
	}
}
