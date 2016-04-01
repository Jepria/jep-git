package com.technology.jep.jepria.server.dao;

import java.sql.SQLException;

import com.technology.jep.jepria.server.db.Db;

/**
 * Класс содержит функциональность поддержки EJB-вызовов
 */
public class CallContext {
	private static ThreadLocal<CallContext> context = new ThreadLocal<CallContext>();
	
	private Db db = null;
	
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
	
	public static void commit() throws SQLException {
		getDb().commit();
	}
	
	public static void rollback() throws SQLException {
		getDb().rollback();
	}
}
