package com.technology.jep.jepria.server.dao.transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.technology.jep.jepria.server.dao.transaction.annotation.After;
import com.technology.jep.jepria.server.dao.transaction.annotation.Before;
import com.technology.jep.jepria.server.dao.transaction.handler.EndTransactionHandler;
import com.technology.jep.jepria.server.dao.transaction.handler.EndTransactionHandlerImpl;
import com.technology.jep.jepria.server.dao.transaction.handler.StartTransactionHandler;
import com.technology.jep.jepria.server.dao.transaction.handler.StartTransactionHandlerImpl;
import com.technology.jep.jepria.server.db.Db;

/**
 * Фабрика, создающая прокси для выполнения методов Dao в рамках одной транзакции.
 */
public class TransactionFactory {

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
          result = method.invoke(dao, args);
        }
        catch(Exception exc) {
          /*
           * Необходимо вызвать getCause(), поскольку выброшенное из Dao исключение
           * будет обёрнуто в InvocationTargetException.
           */
          caught = exc.getCause();
        }
        endTransactionHandlerClass.newInstance().handle(caught);
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
