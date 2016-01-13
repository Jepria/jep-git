package com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class TransactionHandlerImpl implements TransactionHandler {

	@Override
	public void handle(Set<Method> methods, Object service) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		for (Method method : methods){
			method.invoke(service);
		}
	}
}
