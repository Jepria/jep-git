package com.technology.jep.jepria.server.dao;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * Класс-обёртка для {@link CallableStatement}, реализующий особенности взаимодействия с конкретной СУБД.
 * @author EydlinA
 *
 */
class CallableStatementWrapper implements CallableStatement {
  
  /**
   * Объект {@link CallableStatement}, которому делегируются все вызовы.
   */
  private final CallableStatement delegate;

  /**
   * Создание объектов класса предполагается только путём наследования.
   * @param cs an instance of {@link CallableStatement} to wrap
   */
  protected CallableStatementWrapper(CallableStatement cs) {
    delegate = cs;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return delegate.unwrap(iface);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public ResultSet executeQuery(String sql) throws SQLException {
    return delegate.executeQuery(sql);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public ResultSet executeQuery() throws SQLException {
    return delegate.executeQuery();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
    delegate.registerOutParameter(parameterIndex, sqlType);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return delegate.isWrapperFor(iface);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int executeUpdate(String sql) throws SQLException {
    return delegate.executeUpdate(sql);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int executeUpdate() throws SQLException {
    return delegate.executeUpdate();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNull(int parameterIndex, int sqlType) throws SQLException {
    delegate.setNull(parameterIndex, sqlType);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws SQLException {
    delegate.close();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
    delegate.registerOutParameter(parameterIndex, sqlType, scale);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxFieldSize() throws SQLException {
    return delegate.getMaxFieldSize();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBoolean(int parameterIndex, boolean x) throws SQLException {
    delegate.setBoolean(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setByte(int parameterIndex, byte x) throws SQLException {
    delegate.setByte(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setMaxFieldSize(int max) throws SQLException {
    delegate.setMaxFieldSize(max);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean wasNull() throws SQLException {
    return delegate.wasNull();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setShort(int parameterIndex, short x) throws SQLException {
    delegate.setShort(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getString(int parameterIndex) throws SQLException {
    return delegate.getString(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxRows() throws SQLException {
    return delegate.getMaxRows();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setInt(int parameterIndex, int x) throws SQLException {
    delegate.setInt(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setMaxRows(int max) throws SQLException {
    delegate.setMaxRows(max);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setLong(int parameterIndex, long x) throws SQLException {
    delegate.setLong(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getBoolean(int parameterIndex) throws SQLException {
    return delegate.getBoolean(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setFloat(int parameterIndex, float x) throws SQLException {
    delegate.setFloat(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setEscapeProcessing(boolean enable) throws SQLException {
    delegate.setEscapeProcessing(enable);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public byte getByte(int parameterIndex) throws SQLException {
    return delegate.getByte(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setDouble(int parameterIndex, double x) throws SQLException {
    delegate.setDouble(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public short getShort(int parameterIndex) throws SQLException {
    return delegate.getShort(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getQueryTimeout() throws SQLException {
    return delegate.getQueryTimeout();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
    delegate.setBigDecimal(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getInt(int parameterIndex) throws SQLException {
    return delegate.getInt(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setQueryTimeout(int seconds) throws SQLException {
    delegate.setQueryTimeout(seconds);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setString(int parameterIndex, String x) throws SQLException {
    delegate.setString(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public long getLong(int parameterIndex) throws SQLException {
    return delegate.getLong(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBytes(int parameterIndex, byte[] x) throws SQLException {
    delegate.setBytes(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  public float getFloat(int parameterIndex) throws SQLException {
    return delegate.getFloat(parameterIndex);
  }
  
  @Override
  public void cancel() throws SQLException {
    delegate.cancel();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public double getDouble(int parameterIndex) throws SQLException {
    return delegate.getDouble(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setDate(int parameterIndex, Date x) throws SQLException {
    delegate.setDate(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public SQLWarning getWarnings() throws SQLException {
    return delegate.getWarnings();
  }
  
  /**
   * {@inheritDoc}
   */
  @Deprecated
  @Override
  public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
    return delegate.getBigDecimal(parameterIndex, scale);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setTime(int parameterIndex, Time x) throws SQLException {
    delegate.setTime(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
    delegate.setTimestamp(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void clearWarnings() throws SQLException {
    delegate.clearWarnings();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getBytes(int parameterIndex) throws SQLException {
    return delegate.getBytes(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setCursorName(String name) throws SQLException {
    delegate.setCursorName(name);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
    delegate.setAsciiStream(parameterIndex, x, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Date getDate(int parameterIndex) throws SQLException {
    return delegate.getDate(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Time getTime(int parameterIndex) throws SQLException {
    return delegate.getTime(parameterIndex);
  }
  
  @Deprecated
  @Override
  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
    delegate.setUnicodeStream(parameterIndex, x, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(String sql) throws SQLException {
    return delegate.execute(sql);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Timestamp getTimestamp(int parameterIndex) throws SQLException {
    return delegate.getTimestamp(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Object getObject(int parameterIndex) throws SQLException {
    return delegate.getObject(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
    delegate.setBinaryStream(parameterIndex, x, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public ResultSet getResultSet() throws SQLException {
    return delegate.getResultSet();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
    return delegate.getBigDecimal(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getUpdateCount() throws SQLException {
    return delegate.getUpdateCount();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void clearParameters() throws SQLException {
    delegate.clearParameters();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
    return delegate.getObject(parameterIndex, map);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getMoreResults() throws SQLException {
    return delegate.getMoreResults();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
    delegate.setObject(parameterIndex, x, targetSqlType);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setFetchDirection(int direction) throws SQLException {
    delegate.setFetchDirection(direction);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Ref getRef(int parameterIndex) throws SQLException {
    return delegate.getRef(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setObject(int parameterIndex, Object x) throws SQLException {
    delegate.setObject(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getFetchDirection() throws SQLException {
    return delegate.getFetchDirection();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Blob getBlob(int parameterIndex) throws SQLException {
    return delegate.getBlob(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setFetchSize(int rows) throws SQLException {
    delegate.setFetchSize(rows);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Clob getClob(int parameterIndex) throws SQLException {
    return delegate.getClob(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getFetchSize() throws SQLException {
    return delegate.getFetchSize();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute() throws SQLException {
    return delegate.execute();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Array getArray(int parameterIndex) throws SQLException {
    return delegate.getArray(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getResultSetConcurrency() throws SQLException {
    return delegate.getResultSetConcurrency();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
    return delegate.getDate(parameterIndex, cal);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getResultSetType() throws SQLException {
    return delegate.getResultSetType();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void addBatch(String sql) throws SQLException {
    delegate.addBatch(sql);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void addBatch() throws SQLException {
    delegate.addBatch();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
    delegate.setCharacterStream(parameterIndex, reader, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
    return delegate.getTime(parameterIndex, cal);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void clearBatch() throws SQLException {
    delegate.clearBatch();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int[] executeBatch() throws SQLException {
    return delegate.executeBatch();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
    return delegate.getTimestamp(parameterIndex, cal);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setRef(int parameterIndex, Ref x) throws SQLException {
    delegate.setRef(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBlob(int parameterIndex, Blob x) throws SQLException {
    delegate.setBlob(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
    delegate.registerOutParameter(parameterIndex, sqlType, typeName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setClob(int parameterIndex, Clob x) throws SQLException {
    delegate.setClob(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setArray(int parameterIndex, Array x) throws SQLException {
    delegate.setArray(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Connection getConnection() throws SQLException {
    return delegate.getConnection();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    return delegate.getMetaData();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
    delegate.registerOutParameter(parameterName, sqlType);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
    delegate.setDate(parameterIndex, x, cal);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getMoreResults(int current) throws SQLException {
    return delegate.getMoreResults(current);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
    delegate.registerOutParameter(parameterName, sqlType, scale);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
    delegate.setTime(parameterIndex, x, cal);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public ResultSet getGeneratedKeys() throws SQLException {
    return delegate.getGeneratedKeys();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
    delegate.setTimestamp(parameterIndex, x, cal);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
    delegate.registerOutParameter(parameterName, sqlType, typeName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
    return delegate.executeUpdate(sql, autoGeneratedKeys);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
    delegate.setNull(parameterIndex, sqlType, typeName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public URL getURL(int parameterIndex) throws SQLException {
    return delegate.getURL(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
    return delegate.executeUpdate(sql, columnIndexes);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setURL(int parameterIndex, URL x) throws SQLException {
    delegate.setURL(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setURL(String parameterName, URL val) throws SQLException {
    delegate.setURL(parameterName, val);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public ParameterMetaData getParameterMetaData() throws SQLException {
    return delegate.getParameterMetaData();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNull(String parameterName, int sqlType) throws SQLException {
    delegate.setNull(parameterName, sqlType);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setRowId(int parameterIndex, RowId x) throws SQLException {
    delegate.setRowId(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int executeUpdate(String sql, String[] columnNames) throws SQLException {
    return delegate.executeUpdate(sql, columnNames);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBoolean(String parameterName, boolean x) throws SQLException {
    delegate.setBoolean(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNString(int parameterIndex, String value) throws SQLException {
    delegate.setNString(parameterIndex, value);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setByte(String parameterName, byte x) throws SQLException {
    delegate.setByte(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setShort(String parameterName, short x) throws SQLException {
    delegate.setShort(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
    delegate.setNCharacterStream(parameterIndex, value, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
    return delegate.execute(sql, autoGeneratedKeys);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setInt(String parameterName, int x) throws SQLException {
    delegate.setInt(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNClob(int parameterIndex, NClob value) throws SQLException {
    delegate.setNClob(parameterIndex, value);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setLong(String parameterName, long x) throws SQLException {
    delegate.setLong(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setFloat(String parameterName, float x) throws SQLException {
    delegate.setFloat(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    delegate.setClob(parameterIndex, reader, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setDouble(String parameterName, double x) throws SQLException {
    delegate.setDouble(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(String sql, int[] columnIndexes) throws SQLException {
    return delegate.execute(sql, columnIndexes);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
    delegate.setBigDecimal(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
    delegate.setBlob(parameterIndex, inputStream, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setString(String parameterName, String x) throws SQLException {
    delegate.setString(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    delegate.setNClob(parameterIndex, reader, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBytes(String parameterName, byte[] x) throws SQLException {
    delegate.setBytes(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setDate(String parameterName, Date x) throws SQLException {
    delegate.setDate(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(String sql, String[] columnNames) throws SQLException {
    return delegate.execute(sql, columnNames);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    delegate.setSQLXML(parameterIndex, xmlObject);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setTime(String parameterName, Time x) throws SQLException {
    delegate.setTime(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
    delegate.setTimestamp(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
    delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
    delegate.setAsciiStream(parameterName, x, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getResultSetHoldability() throws SQLException {
    return delegate.getResultSetHoldability();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isClosed() throws SQLException {
    return delegate.isClosed();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
    delegate.setBinaryStream(parameterName, x, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setPoolable(boolean poolable) throws SQLException {
    delegate.setPoolable(poolable);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    delegate.setAsciiStream(parameterIndex, x, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
    delegate.setObject(parameterName, x, targetSqlType, scale);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPoolable() throws SQLException {
    return delegate.isPoolable();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void closeOnCompletion() throws SQLException {
    delegate.closeOnCompletion();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    delegate.setBinaryStream(parameterIndex, x, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCloseOnCompletion() throws SQLException {
    return delegate.isCloseOnCompletion();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
    delegate.setObject(parameterName, x, targetSqlType);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
    delegate.setCharacterStream(parameterIndex, reader, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setObject(String parameterName, Object x) throws SQLException {
    delegate.setObject(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    delegate.setAsciiStream(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    delegate.setBinaryStream(parameterIndex, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
    delegate.setCharacterStream(parameterName, reader, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    delegate.setCharacterStream(parameterIndex, reader);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
    delegate.setDate(parameterName, x, cal);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    delegate.setNCharacterStream(parameterIndex, value);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
    delegate.setTime(parameterName, x, cal);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setClob(int parameterIndex, Reader reader) throws SQLException {
    delegate.setClob(parameterIndex, reader);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
    delegate.setTimestamp(parameterName, x, cal);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
    delegate.setNull(parameterName, sqlType, typeName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    delegate.setBlob(parameterIndex, inputStream);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    delegate.setNClob(parameterIndex, reader);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getString(String parameterName) throws SQLException {
    return delegate.getString(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getBoolean(String parameterName) throws SQLException {
    return delegate.getBoolean(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public byte getByte(String parameterName) throws SQLException {
    return delegate.getByte(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public short getShort(String parameterName) throws SQLException {
    return delegate.getShort(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getInt(String parameterName) throws SQLException {
    return delegate.getInt(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public long getLong(String parameterName) throws SQLException {
    return delegate.getLong(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public float getFloat(String parameterName) throws SQLException {
    return delegate.getFloat(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public double getDouble(String parameterName) throws SQLException {
    return delegate.getDouble(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getBytes(String parameterName) throws SQLException {
    return delegate.getBytes(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Date getDate(String parameterName) throws SQLException {
    return delegate.getDate(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Time getTime(String parameterName) throws SQLException {
    return delegate.getTime(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Timestamp getTimestamp(String parameterName) throws SQLException {
    return delegate.getTimestamp(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Object getObject(String parameterName) throws SQLException {
    return delegate.getObject(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public BigDecimal getBigDecimal(String parameterName) throws SQLException {
    return delegate.getBigDecimal(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
    return delegate.getObject(parameterName, map);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Ref getRef(String parameterName) throws SQLException {
    return delegate.getRef(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Blob getBlob(String parameterName) throws SQLException {
    return delegate.getBlob(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Clob getClob(String parameterName) throws SQLException {
    return delegate.getClob(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Array getArray(String parameterName) throws SQLException {
    return delegate.getArray(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Date getDate(String parameterName, Calendar cal) throws SQLException {
    return delegate.getDate(parameterName, cal);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Time getTime(String parameterName, Calendar cal) throws SQLException {
    return delegate.getTime(parameterName, cal);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
    return delegate.getTimestamp(parameterName, cal);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public URL getURL(String parameterName) throws SQLException {
    return delegate.getURL(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public RowId getRowId(int parameterIndex) throws SQLException {
    return delegate.getRowId(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public RowId getRowId(String parameterName) throws SQLException {
    return delegate.getRowId(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setRowId(String parameterName, RowId x) throws SQLException {
    delegate.setRowId(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNString(String parameterName, String value) throws SQLException {
    delegate.setNString(parameterName, value);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
    delegate.setNCharacterStream(parameterName, value, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNClob(String parameterName, NClob value) throws SQLException {
    delegate.setNClob(parameterName, value);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setClob(String parameterName, Reader reader, long length) throws SQLException {
    delegate.setClob(parameterName, reader, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
    delegate.setBlob(parameterName, inputStream, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
    delegate.setNClob(parameterName, reader, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public NClob getNClob(int parameterIndex) throws SQLException {
    return delegate.getNClob(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public NClob getNClob(String parameterName) throws SQLException {
    return delegate.getNClob(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
    delegate.setSQLXML(parameterName, xmlObject);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public SQLXML getSQLXML(int parameterIndex) throws SQLException {
    return delegate.getSQLXML(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public SQLXML getSQLXML(String parameterName) throws SQLException {
    return delegate.getSQLXML(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getNString(int parameterIndex) throws SQLException {
    return delegate.getNString(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getNString(String parameterName) throws SQLException {
    return delegate.getNString(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Reader getNCharacterStream(int parameterIndex) throws SQLException {
    return delegate.getNCharacterStream(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Reader getNCharacterStream(String parameterName) throws SQLException {
    return delegate.getNCharacterStream(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Reader getCharacterStream(int parameterIndex) throws SQLException {
    return delegate.getCharacterStream(parameterIndex);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Reader getCharacterStream(String parameterName) throws SQLException {
    return delegate.getCharacterStream(parameterName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBlob(String parameterName, Blob x) throws SQLException {
    delegate.setBlob(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setClob(String parameterName, Clob x) throws SQLException {
    delegate.setClob(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
    delegate.setAsciiStream(parameterName, x, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
    delegate.setBinaryStream(parameterName, x, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
    delegate.setCharacterStream(parameterName, reader, length);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
    delegate.setAsciiStream(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
    delegate.setBinaryStream(parameterName, x);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
    delegate.setCharacterStream(parameterName, reader);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
    delegate.setNCharacterStream(parameterName, value);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setClob(String parameterName, Reader reader) throws SQLException {
    delegate.setClob(parameterName, reader);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
    delegate.setBlob(parameterName, inputStream);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setNClob(String parameterName, Reader reader) throws SQLException {
    delegate.setNClob(parameterName, reader);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
    return delegate.getObject(parameterIndex, type);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
    return delegate.getObject(parameterName, type);
  }  
  
}
