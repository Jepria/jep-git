package com.technology.jep.jepriashowcase.custom.server.dao.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.technology.jep.jepria.server.ejb.JepDataStandard;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler.EndTransactionHandler;
import com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler.EndTransactionHandlerImpl;
import com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler.StartTransactionHandler;
import com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler.StartTransactionHandlerImpl;

public class TransactionFactory {

	private static class TransactionInvocationHandler<S extends JepDataStandard> implements InvocationHandler {

		private final S service;
		private final String dataSourceJndiName;
		private final String resourceBundleName;

		public TransactionInvocationHandler(S service, String dataSourceJndiName, String resourceBundleName) {
			this.service = service;
			this.dataSourceJndiName = dataSourceJndiName;
			this.resourceBundleName = resourceBundleName;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Class<? extends JepDataStandard> serviceClass = service.getClass();
			Method realMethod = serviceClass.getMethod(
					method.getName(), method.getParameterTypes());
			
			MultipleTransactional multipleTransaction = realMethod.getAnnotation(MultipleTransactional.class);
			
			Class<? extends StartTransactionHandler> startTransactionHandler = StartTransactionHandlerImpl.class;
			Class<? extends EndTransactionHandler> endTransactionHandler = EndTransactionHandlerImpl.class;
			
			Set<Method> transactionMethodList = null;
			// придерживаемся подхода, что транзакция определяется телом метода
			// если хотим в рамках одного метода выполнить другие - перед началом основного тела
			// помечаем основной метод анотацией MultipleTransactional, а выполняемые методы - Transaction 
			if (multipleTransaction != null) {
				String globalTransactionName = multipleTransaction.name(); 
				startTransactionHandler = multipleTransaction.startTransactionHandler();
				endTransactionHandler = multipleTransaction.endTransactionHandler();
				
				Method[] transactionMethods = serviceClass.getDeclaredMethods();
				transactionMethodList = new TreeSet<Method>(new Comparator<Method>() {
					@Override
					public int compare(Method o1, Method o2) {
						return o1.getAnnotation(TransactionMethod.class).runOrder() - o2.getAnnotation(TransactionMethod.class).runOrder();
					}
				});
				for (Method transactionMethod : transactionMethods){
					TransactionMethod transaction = transactionMethod.getAnnotation(TransactionMethod.class);
					if (transaction != null && globalTransactionName.equals(transaction.name())) {
						transactionMethodList.add(transactionMethod);
					}
				}
			}
			
			startTransactionHandler.newInstance().handle(dataSourceJndiName, resourceBundleName);
			
			if (!JepRiaUtil.isEmpty(transactionMethodList)){
				// Если список методов не пуст, то multipleTransaction явно не null
				multipleTransaction.handler().newInstance().handle(transactionMethodList, service);
			}
			
			boolean successful = true;
			Object result = null;
			try {
				result = method.invoke(service, args);
			}
			catch(Throwable th){
				successful = false;
			}
			
			endTransactionHandler.newInstance().handle(successful);
			
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public static <S extends JepDataStandard> S process(S service) {
		S result = service;
		Class<?> serviceClass = service.getClass();
		try {
			Field dataSourceJndiNameField = serviceClass.getSuperclass().getDeclaredField("dataSourceJndiName"),
					resourceBundleNameField = serviceClass.getSuperclass().getDeclaredField("resourceBundleName");
			dataSourceJndiNameField.setAccessible(true);
			resourceBundleNameField.setAccessible(true);
			result = (S) Proxy.newProxyInstance(
					TransactionFactory.class.getClassLoader(),
					serviceClass.getInterfaces(),
					new TransactionInvocationHandler<S>(service, 
							(String) dataSourceJndiNameField.get(service),
								(String) resourceBundleNameField.get(service)));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}		
		return result;
	}
}
