package com.technology.jep.jepria.server.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.UnexpectedException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Обёртка для доступа к БД.
 * Реализуются сервисы для получения и аккуратного закрытия JDBC ресурсов.
 */
public class Db {
	private static Logger logger = Logger.getLogger(Db.class.getName());
	
	private static Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
	private boolean autoCommit;
	private Connection connection;
	private String dataSourceJndiName;
	// Здесь по ключу - текст sql лежит открытый курсор.
	private ConcurrentHashMap<String, CallableStatement> statementsMap = new ConcurrentHashMap<String, CallableStatement>();

	public Db(String dataSourceJndiName) {
		this(dataSourceJndiName, true);
	}
	
	public Db(String dataSourceJndiName, boolean autoCommit) {
		this.dataSourceJndiName = dataSourceJndiName;
	}

	/**
	 * Подготавливает Statement
	 */
	public CallableStatement prepare(String sql) throws SQLException {
		CallableStatement cs = (CallableStatement) statementsMap.get(sql);
		if (cs == null) {
			try {
				cs = getConnection().prepareCall(sql);
			} catch(Throwable th) {
				throw new SystemException("PrepareCall Error for query '" + sql + "'", th);
			}

			statementsMap.put(sql, cs);
		}
		return cs;
	}

	/**
	 * Метод закрывает все ресурсы связанные с данным Db: connection и все курсоры из statementsMap.
	 * Метод должен вызываться в конце сессии пользователя
	 */
	public void closeAll() {
		logger.trace("closeAll()");
		
		Iterator<String> it = statementsMap.keySet().iterator();
		while (it.hasNext()) {
			CallableStatement cs = (CallableStatement) statementsMap.get(it.next());
			try {
				cs.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		statementsMap.clear();
//		statementsMap = null;
		try {
			if(connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		connection = null;
	}

	public boolean closeStatement(String sql) {
		logger.trace("closeStatement()");
		
		Statement st = (Statement) statementsMap.get(sql);
		boolean existsFlag = st != null;
		try {
			if(existsFlag) {
				st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		statementsMap.remove(sql);
		return existsFlag;
	}

	public void rollback() {
		logger.trace("rollback()");
		try {
			connection.rollback();
		} catch (SQLException ex) {
		}
	}
	
	private Connection getConnection() {
		if(this.isClosed()) {
			connection = createConnection(dataSourceJndiName, autoCommit);
		}
		return connection;
	}

	private synchronized static Connection createConnection(String dataSourceJndiName, boolean autoCommit) {
		logger.trace("BEGIN createConnection(" + dataSourceJndiName + ", " + autoCommit + ")");
		
		try {
			DataSource dataSource = dataSourceMap.get(dataSourceJndiName);
			if (dataSource == null) {
				InitialContext ic = new InitialContext();
				dataSource = (DataSource) ic.lookup(dataSourceJndiName);
				dataSourceMap.put(dataSourceJndiName, dataSource);
			}
			Connection con = dataSource.getConnection();
			con.setAutoCommit(autoCommit);
			return con;
		} catch (NamingException ex) {
			logger.error(ex);
			throw new SystemException("DataSource '" + dataSourceJndiName + "' not found", ex);
		} catch (SQLException ex) {
			logger.error(ex);
			throw new SystemException("Connection creation error for '" + dataSourceJndiName + "' dataSource", ex);
		} finally {
			logger.trace("END createConnection(" + dataSourceJndiName + ")");
		}
	}

	private boolean isClosed() {
		try {
			return connection == null || connection.isClosed();
		} catch (SQLException ex) {
			throw new SystemException("Check Db connection error", ex);
		}
	}

	/*
	 * Ниже - методы, не используемые ни в JepRia, ни в JepCommon.
	 * TODO Удалять ? 
	 */
	public void commit() throws SQLException {
		logger.trace("commit()");
		getConnection().commit();
	}

	public static void closeConnection(Connection dbConnection) {
		if (dbConnection != null) {
			try {
				dbConnection.close();
			} catch (Exception e) {
				// ignore it
			}
		}
	}

	public synchronized static void closeConnection(String dataSourceJndiName) {
		DataSource dataSource = dataSourceMap.get(dataSourceJndiName);
		try {
			if (dataSource != null) {
				Connection connection = dataSource.getConnection();
				if(connection != null) {
					connection.close();
				}
				dataSourceMap.remove(dataSourceJndiName);
			} else {
				throw new UnexpectedException("dataSource '" + dataSourceJndiName + "' not found", null);
			}
		} catch (SQLException ex) {
			throw new SystemException("Connection close error for '" + dataSourceJndiName + "' dataSource", ex);
		}
	}
};