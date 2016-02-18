package com.technology.jep.jepria.server.dao;

import java.sql.SQLException;

import com.google.gwt.user.server.rpc.UnexpectedException;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Класс содержит функциональность поддержки EJB-вызовов
 */
public class CallContext {
	private static ThreadLocal<CallContext> context = new ThreadLocal<CallContext>();
	
	private Db db = null;
	private Integer currentUserId = null;
	
	public CallContext(String dataSourceJndiName) {
		db = new Db(dataSourceJndiName, false);
	}
	
	public static void begin(String dataSourceJndiName) {
		CallContext callContext = new CallContext(dataSourceJndiName);
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
		if (db != null) {
			db.closeAll();
		}
		this.db = null;
	}

	public static void attach(CallContext callContext) {
	    context.set(callContext);
	}

	public static CallContext detach() {
		CallContext result = (CallContext) context.get();
		context.set(null);
		return result;
	}

//	/**
//	 * Обёртка для исключения. Используется при желании добавить к исходному исключению дополнительную информацию.
//	 * 
//	 * @param message сообщение
//	 * @param ex исходное исключение
//	 */
//	public static void processException(String message, Exception ex) throws ApplicationException {
//		CallContext.processException(new ApplicationException(message, ex));
//	}
//
//	public static void processException(Exception ex) throws ApplicationException {
//		CallContext c = (CallContext) context.get();
//		c.sessionContext.setRollbackOnly(); // В случае Exception делаем откат транзакции
//		if (ex instanceof ApplicationException) {
//			// TODO обработать исключение
//			throw (ApplicationException) ex;
//		} else if (ex instanceof SystemException) {
//			// TODO обработать исключение
//			throw (SystemException) ex;
//		} else {
//			throw new UnexpectedException("Unknown exception", ex);
//		}
//	}

	public static Integer getCurrentUserId() {
		CallContext c = (CallContext) context.get();
		return c.currentUserId;
	}
	
	public static void commit() throws SQLException {
		getDb().commit();
	}
	
	public static void rollback() throws SQLException {
		getDb().rollback();
	}
}
