package com.technology.jep.jepriashowcase.custom.server.dao;

import java.sql.SQLException;

import com.google.gwt.user.server.rpc.UnexpectedException;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Класс содержит функциональность поддержки EJB-вызовов
 */
public class XCallContext {
	private static ThreadLocal<XCallContext> context = new ThreadLocal<XCallContext>();

	private final boolean transaction;
	private XDb db = null;
	private Integer currentUserId = null;
	
	public XCallContext(String dataSourceJndiName, String resourceBundleName, boolean transaction) throws ApplicationException {
		this.transaction = transaction;
		try {
			db = new XDb(dataSourceJndiName, !transaction);
		} catch (Throwable th) {
			th.printStackTrace();
			throw new SystemException("Create connection error", th);
		}
	}
	
	public static void begin(String dataSourceJndiName, String resourceBundleName) throws ApplicationException {
		begin(dataSourceJndiName, resourceBundleName, false);
	}
	
	public static void begin(String dataSourceJndiName, String resourceBundleName, boolean transaction) throws ApplicationException {
		XCallContext callContext = new XCallContext(dataSourceJndiName, resourceBundleName, transaction);
		context.set(callContext);
	}
	
	public static XDb getDb() {
		XCallContext c = (XCallContext) context.get();
		return c.db;
	}

	public static void end() {
		XCallContext c = detach();
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

	public static void attach(XCallContext callContext) {
	    context.set(callContext);
	}

	public static XCallContext detach() {
		XCallContext result = (XCallContext) context.get();
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
//		XCallContext.processException(new ApplicationException(message, ex));
//	}
//
//	public static void processException(Exception ex) throws ApplicationException {
//		XCallContext c = (XCallContext) context.get();
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
		XCallContext c = (XCallContext) context.get();
		return c.currentUserId;
	}
	
	public static boolean isTransaction() {
		XCallContext c = (XCallContext) context.get();
		if (c != null) {
			return c.transaction;
		}
		return false;
	}
	
	public static void commit() throws SQLException {
		getDb().commit();
	}
	
	public static void rollback() {
		getDb().rollback();
	}
}
