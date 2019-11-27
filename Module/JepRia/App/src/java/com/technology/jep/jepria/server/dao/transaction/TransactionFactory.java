package com.technology.jep.jepria.server.dao.transaction;

import com.technology.jep.jepria.server.dao.transaction.annotation.After;
import com.technology.jep.jepria.server.dao.transaction.annotation.Before;
import com.technology.jep.jepria.server.dao.transaction.handler.EndTransactionHandler;
import com.technology.jep.jepria.server.dao.transaction.handler.EndTransactionHandlerImpl;
import com.technology.jep.jepria.server.dao.transaction.handler.StartTransactionHandler;
import com.technology.jep.jepria.server.dao.transaction.handler.StartTransactionHandlerImpl;
import com.technology.jep.jepria.server.db.Db;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Фабрика, создающая прокси для выполнения методов Dao в рамках одной транзакции.
 */
public class TransactionFactory {

  protected static Logger logger = Logger.getLogger(TransactionFactory.class.getName());

  /**
   * Обработчик вызова метода Dao, обеспечивающий его выполнение в рамках одной транзакции.<br/>
   * Механизм работы следующий:
   * <ul>
   *   <li>Вызывается обработчик старта транзакции.</li>
   *   <li>Вызывается метод Dao, который выполняется в рамках транзакции. При возникновении исключения
   *   оно перехватывается.</li>
   *   <li>Вызывается обработчик завершения транзакции.</li>
   * </ul>
   * @param <D> интерфейс Dao
   */
  private static class TransactionInvocationHandler<D> implements InvocationHandler {

    /**
     * Объект Dao.
     */
    private final D dao;
    /**
     * JNDI-имя источника данных.
     */
    private final String dataSourceJndiName;
    /**
     * Имя модуля для передачи в DB.
     */
    private final String moduleName;

    /**
     * Создаёт экземпляр транзакционного обработчика.
     * @param dao объект Dao
     * @param dataSourceJndiName JNDI-имя источника данных
     * @param moduleName имя модуля для передачи в DB
     */
    public TransactionInvocationHandler(D dao, String dataSourceJndiName, String moduleName) {
      this.dao = dao;
      this.dataSourceJndiName = dataSourceJndiName;
      this.moduleName = moduleName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      Class<?> daoClass = dao.getClass();
      Method implementingMethod = daoClass.getMethod(
          method.getName(), method.getParameterTypes());
      
      Before before = implementingMethod.getAnnotation(Before.class);
      Class<? extends StartTransactionHandler> startTransactionHandlerClass =
          before != null ? before.startTransactionHandler() : StartTransactionHandlerImpl.class;
        
      After after = implementingMethod.getAnnotation(After.class);
      Class<? extends EndTransactionHandler> endTransactionHandlerClass =
          after != null ? after.endTransactionHandler() : EndTransactionHandlerImpl.class;
          
      Db db = startTransactionHandlerClass.newInstance().handle(dataSourceJndiName, moduleName);
      Throwable caught = null;
      Object result = null;
      synchronized (db) {
        try {
          final long startTime = System.currentTimeMillis();
          result = method.invoke(dao, args);
          logger.trace(dao.getClass() +"." + method.getName() + " execution time: " + (System.currentTimeMillis() - startTime)/1000.00 + " (seconds)");
        } catch(Exception exc) {
          /*
           * Необходимо вызвать getCause(), поскольку выброшенное из Dao исключение
           * будет обёрнуто в InvocationTargetException.
           */
          caught = exc.getCause();
        }

        endTransactionHandlerClass.newInstance().handle(caught);

        if (caught != null) {
          throw caught;
        }
      }
      return result;
    }
  }

  /**
   * Создаёт прокси для переданного Dao.
   * @param dao объект Dao
   * @param dataSourceJndiName JNDI-имя источника данных
   * @param moduleName имя модуля
   * @return созданный прокси
   */
  @SuppressWarnings("unchecked")
  public static <D> D createProxy(D dao, String dataSourceJndiName, String moduleName) {
    Class<?> daoClass = dao.getClass();
    return (D) Proxy.newProxyInstance(
        TransactionFactory.class.getClassLoader(),
        daoClass.getInterfaces(),
        new TransactionInvocationHandler<D>(dao, dataSourceJndiName, moduleName));
  }
}
