package com.technology.jep.jepria.server.ejb;

import java.security.Principal;

import javax.ejb.SessionContext;

import com.google.gwt.user.server.rpc.UnexpectedException;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Класс содержит функциональность поддержки EJB-вызовов
 */
public class CallContext {
	private static ThreadLocal<CallContext> context = new ThreadLocal<CallContext>();
	
	private SessionContext sessionContext = null;
	private Db db = null;
	private Integer currentUserId = null;
	
	public CallContext(SessionContext sessionContext, String dataSourceJndiName, String resourceBundleName) throws ApplicationException {
		try {
			Principal principal = sessionContext.getCallerPrincipal();
			this.sessionContext = sessionContext;

			db = new Db(dataSourceJndiName);
		} catch (Throwable th) {
			th.printStackTrace();
			throw new SystemException("Create connection error", th);
		}
	}
	
	public static void begin(SessionContext sessionContext, String dataSourceJndiName, String resourceBundleName) throws ApplicationException {
		CallContext callContext = new CallContext(sessionContext, dataSourceJndiName, resourceBundleName);
		context.set(callContext);
	}
	
	public static Db getDb() {
		CallContext c = (CallContext) context.get();
		return c.db;
	}

	public static void end() {
		CallContext c = detach();
		if (c != null) {
			c.close();
		}
	}

	public void close() {
		try {
			if (db != null) {
				db.closeAll();
			}
			this.db = null;
		} catch (Throwable e) {
			e.printStackTrace(System.err);
		}
	}

	public static void attach(CallContext callContext) {
	    context.set(callContext);
	}

	public static CallContext detach() {
		CallContext result = (CallContext) context.get();
		context.set(null);
		return result;
	}

	/**
	 * Обёртка для исключения. Используется при желании добавить к исходному исключению дополнительную информацию.
	 * 
	 * @param message сообщение
	 * @param ex исходное исключение
	 */
	public static void processException(String message, Exception ex) throws ApplicationException {
		CallContext.processException(new ApplicationException(message, ex));
	}

	public static void processException(Exception ex) throws ApplicationException {
		CallContext c = (CallContext) context.get();
		c.sessionContext.setRollbackOnly(); // В случае Exception делаем откат транзакции
		if (ex instanceof ApplicationException) {
			// TODO обработать исключение
			throw (ApplicationException) ex;
		} else if (ex instanceof SystemException) {
			// TODO обработать исключение
			throw (SystemException) ex;
		} else {
			throw new UnexpectedException("Unknown exception", ex);
		}
	}

	public static Integer getCurrentUserId() {
		CallContext c = (CallContext) context.get();
		return c.currentUserId;
	}
}
