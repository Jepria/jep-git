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
import com.technology.jep.jepria.server.ejb.JepDataStandard;

public class TransactionFactory {

	private static class TransactionInvocationHandler<D extends JepDataStandard> implements InvocationHandler {

		private final D dao;
		private final String dataSourceJndiName;

		public TransactionInvocationHandler(D dao, String dataSourceJndiName) {
			this.dao = dao;
			this.dataSourceJndiName = dataSourceJndiName;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Class<? extends JepDataStandard> daoClass = dao.getClass();
			Method implementingMethod = daoClass.getMethod(
					method.getName(), method.getParameterTypes());
			
			Before before = implementingMethod.getAnnotation(Before.class);
			Class<? extends StartTransactionHandler> startTransactionHandlerClass =
					before != null ? before.startTransactionHandler() : StartTransactionHandlerImpl.class;
				
			After after = implementingMethod.getAnnotation(After.class);
			Class<? extends EndTransactionHandler> endTransactionHandlerClass =
					after != null ? after.endTransactionHandler() : EndTransactionHandlerImpl.class;
					
			startTransactionHandlerClass.newInstance().handle(dataSourceJndiName);
						
			Throwable caught = null;
			Object result = null;
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
			
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public static <D extends JepDataStandard> D process(D dao, String dataSourceJndiName) {
		Class<?> daoClass = dao.getClass();
		return (D) Proxy.newProxyInstance(
				TransactionFactory.class.getClassLoader(),
				daoClass.getInterfaces(),
				new TransactionInvocationHandler<D>(dao, dataSourceJndiName));
	}
}
