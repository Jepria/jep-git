package com.technology.jep.jepria.server;

import com.technology.jep.jepria.server.dao.transaction.TransactionFactory;

/**
 * Серверная фабрика.
 * @param <D> интерфейс Dao
 * @deprecated for Rest use {@link org.jepria.server.ServerFactory} instead
 */
@Deprecated
public abstract class ServerFactory<D> implements DaoProvider<D> {
  
  /**
   * Исходный объект Dao.
   */
  private final D dao;
  /**
   * Прокси для Dao, обеспечивающий транзакционность.
   */
  private D proxyDao;
  /**
   * JNDI-имя источника данных.
   */
  private String dataSourceJndiName;
  /**
   * Имя модуля (module_name), передаваемое в DB.
   */
  private String moduleName;
  
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
      proxyDao = TransactionFactory.createProxy(dao, dataSourceJndiName, moduleName);
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

  /**
   * {@inheritDoc}
   */
  @Override
  public String getModuleName() {
    return moduleName;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }
}
