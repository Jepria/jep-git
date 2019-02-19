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
  protected final CallableStatement delegate;

  protected CallableStatementWrapper(CallableStatement cs) {
    delegate = cs;
  }
  
  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return delegate.unwrap(iface);
  }

  public ResultSet executeQuery(String sql) throws SQLException {
    return delegate.executeQuery(sql);
  }
  
  @Override
  public ResultSet executeQuery() throws SQLException {
    return delegate.executeQuery();
  }
  
  @Override
  public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
    delegate.registerOutParameter(parameterIndex, sqlType);
  }
  
  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return delegate.isWrapperFor(iface);
  }
  
  @Override
  public int executeUpdate(String sql) throws SQLException {
    return delegate.executeUpdate(sql);
  }
  
  @Override
  public int executeUpdate() throws SQLException {
    return delegate.executeUpdate();
  }
  
  @Override
  public void setNull(int parameterIndex, int sqlType) throws SQLException {
    delegate.setNull(parameterIndex, sqlType);
  }
  
  @Override
  public void close() throws SQLException {
    delegate.close();
  }
  
  @Override
  public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
    delegate.registerOutParameter(parameterIndex, sqlType, scale);
  }
  
  @Override
  public int getMaxFieldSize() throws SQLException {
    return delegate.getMaxFieldSize();
  }
  
  @Override
  public void setBoolean(int parameterIndex, boolean x) throws SQLException {
    delegate.setBoolean(parameterIndex, x);
  }
  
  @Override
  public void setByte(int parameterIndex, byte x) throws SQLException {
    delegate.setByte(parameterIndex, x);
  }
  
  @Override
  public void setMaxFieldSize(int max) throws SQLException {
    delegate.setMaxFieldSize(max);
  }
  
  @Override
  public boolean wasNull() throws SQLException {
    return delegate.wasNull();
  }
  
  @Override
  public void setShort(int parameterIndex, short x) throws SQLException {
    delegate.setShort(parameterIndex, x);
  }
  
  @Override
  public String getString(int parameterIndex) throws SQLException {
    return delegate.getString(parameterIndex);
  }
  
  @Override
  public int getMaxRows() throws SQLException {
    return delegate.getMaxRows();
  }
  
  @Override
  public void setInt(int parameterIndex, int x) throws SQLException {
    delegate.setInt(parameterIndex, x);
  }
  
  @Override
  public void setMaxRows(int max) throws SQLException {
    delegate.setMaxRows(max);
  }
  
  @Override
  public void setLong(int parameterIndex, long x) throws SQLException {
    delegate.setLong(parameterIndex, x);
  }
  
  @Override
  public boolean getBoolean(int parameterIndex) throws SQLException {
    return delegate.getBoolean(parameterIndex);
  }
  
  @Override
  public void setFloat(int parameterIndex, float x) throws SQLException {
    delegate.setFloat(parameterIndex, x);
  }
  
  @Override
  public void setEscapeProcessing(boolean enable) throws SQLException {
    delegate.setEscapeProcessing(enable);
  }
  
  @Override
  public byte getByte(int parameterIndex) throws SQLException {
    return delegate.getByte(parameterIndex);
  }
  
  @Override
  public void setDouble(int parameterIndex, double x) throws SQLException {
    delegate.setDouble(parameterIndex, x);
  }
  
  @Override
  public short getShort(int parameterIndex) throws SQLException {
    return delegate.getShort(parameterIndex);
  }
  
  @Override
  public int getQueryTimeout() throws SQLException {
    return delegate.getQueryTimeout();
  }
  
  @Override
  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
    delegate.setBigDecimal(parameterIndex, x);
  }
  
  @Override
  public int getInt(int parameterIndex) throws SQLException {
    return delegate.getInt(parameterIndex);
  }
  
  @Override
  public void setQueryTimeout(int seconds) throws SQLException {
    delegate.setQueryTimeout(seconds);
  }
  
  @Override
  public void setString(int parameterIndex, String x) throws SQLException {
    delegate.setString(parameterIndex, x);
  }
  
  @Override
  public long getLong(int parameterIndex) throws SQLException {
    return delegate.getLong(parameterIndex);
  }
  
  @Override
  public void setBytes(int parameterIndex, byte[] x) throws SQLException {
    delegate.setBytes(parameterIndex, x);
  }

  public float getFloat(int parameterIndex) throws SQLException {
    return delegate.getFloat(parameterIndex);
  }
  
  @Override
  public void cancel() throws SQLException {
    delegate.cancel();
  }
  
  @Override
  public double getDouble(int parameterIndex) throws SQLException {
    return delegate.getDouble(parameterIndex);
  }
  
  @Override
  public void setDate(int parameterIndex, Date x) throws SQLException {
    delegate.setDate(parameterIndex, x);
  }
  
  @Override
  public SQLWarning getWarnings() throws SQLException {
    return delegate.getWarnings();
  }
  
  @Deprecated
  @Override
  public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
    return delegate.getBigDecimal(parameterIndex, scale);
  }
  
  @Override
  public void setTime(int parameterIndex, Time x) throws SQLException {
    delegate.setTime(parameterIndex, x);
  }
  
  @Override
  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
    delegate.setTimestamp(parameterIndex, x);
  }
  
  @Override
  public void clearWarnings() throws SQLException {
    delegate.clearWarnings();
  }
  
  @Override
  public byte[] getBytes(int parameterIndex) throws SQLException {
    return delegate.getBytes(parameterIndex);
  }
  
  @Override
  public void setCursorName(String name) throws SQLException {
    delegate.setCursorName(name);
  }
  
  @Override
  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
    delegate.setAsciiStream(parameterIndex, x, length);
  }
  
  @Override
  public Date getDate(int parameterIndex) throws SQLException {
    return delegate.getDate(parameterIndex);
  }
  
  @Override
  public Time getTime(int parameterIndex) throws SQLException {
    return delegate.getTime(parameterIndex);
  }
  
  @Deprecated
  @Override
  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
    delegate.setUnicodeStream(parameterIndex, x, length);
  }
  
  @Override
  public boolean execute(String sql) throws SQLException {
    return delegate.execute(sql);
  }
  
  @Override
  public Timestamp getTimestamp(int parameterIndex) throws SQLException {
    return delegate.getTimestamp(parameterIndex);
  }
  
  @Override
  public Object getObject(int parameterIndex) throws SQLException {
    return delegate.getObject(parameterIndex);
  }
  
  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
    delegate.setBinaryStream(parameterIndex, x, length);
  }
  
  @Override
  public ResultSet getResultSet() throws SQLException {
    return delegate.getResultSet();
  }
  
  @Override
  public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
    return delegate.getBigDecimal(parameterIndex);
  }
  
  @Override
  public int getUpdateCount() throws SQLException {
    return delegate.getUpdateCount();
  }
  
  @Override
  public void clearParameters() throws SQLException {
    delegate.clearParameters();
  }
  
  @Override
  public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
    return delegate.getObject(parameterIndex, map);
  }
  
  @Override
  public boolean getMoreResults() throws SQLException {
    return delegate.getMoreResults();
  }
  
  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
    delegate.setObject(parameterIndex, x, targetSqlType);
  }
  
  @Override
  public void setFetchDirection(int direction) throws SQLException {
    delegate.setFetchDirection(direction);
  }
  
  @Override
  public Ref getRef(int parameterIndex) throws SQLException {
    return delegate.getRef(parameterIndex);
  }
  
  @Override
  public void setObject(int parameterIndex, Object x) throws SQLException {
    delegate.setObject(parameterIndex, x);
  }
  
  @Override
  public int getFetchDirection() throws SQLException {
    return delegate.getFetchDirection();
  }
  
  @Override
  public Blob getBlob(int parameterIndex) throws SQLException {
    return delegate.getBlob(parameterIndex);
  }
  
  @Override
  public void setFetchSize(int rows) throws SQLException {
    delegate.setFetchSize(rows);
  }
  
  @Override
  public Clob getClob(int parameterIndex) throws SQLException {
    return delegate.getClob(parameterIndex);
  }
  
  @Override
  public int getFetchSize() throws SQLException {
    return delegate.getFetchSize();
  }
  
  @Override
  public boolean execute() throws SQLException {
    return delegate.execute();
  }
  
  @Override
  public Array getArray(int parameterIndex) throws SQLException {
    return delegate.getArray(parameterIndex);
  }
  
  @Override
  public int getResultSetConcurrency() throws SQLException {
    return delegate.getResultSetConcurrency();
  }
  
  @Override
  public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
    return delegate.getDate(parameterIndex, cal);
  }
  
  @Override
  public int getResultSetType() throws SQLException {
    return delegate.getResultSetType();
  }
  
  @Override
  public void addBatch(String sql) throws SQLException {
    delegate.addBatch(sql);
  }
  
  @Override
  public void addBatch() throws SQLException {
    delegate.addBatch();
  }
  
  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
    delegate.setCharacterStream(parameterIndex, reader, length);
  }
  
  @Override
  public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
    return delegate.getTime(parameterIndex, cal);
  }

  public void clearBatch() throws SQLException {
    delegate.clearBatch();
  }

  public int[] executeBatch() throws SQLException {
    return delegate.executeBatch();
  }

  public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
    return delegate.getTimestamp(parameterIndex, cal);
  }

  public void setRef(int parameterIndex, Ref x) throws SQLException {
    delegate.setRef(parameterIndex, x);
  }

  public void setBlob(int parameterIndex, Blob x) throws SQLException {
    delegate.setBlob(parameterIndex, x);
  }

  public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
    delegate.registerOutParameter(parameterIndex, sqlType, typeName);
  }

  public void setClob(int parameterIndex, Clob x) throws SQLException {
    delegate.setClob(parameterIndex, x);
  }

  public void setArray(int parameterIndex, Array x) throws SQLException {
    delegate.setArray(parameterIndex, x);
  }

  public Connection getConnection() throws SQLException {
    return delegate.getConnection();
  }

  public ResultSetMetaData getMetaData() throws SQLException {
    return delegate.getMetaData();
  }

  public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
    delegate.registerOutParameter(parameterName, sqlType);
  }

  public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
    delegate.setDate(parameterIndex, x, cal);
  }

  public boolean getMoreResults(int current) throws SQLException {
    return delegate.getMoreResults(current);
  }

  public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
    delegate.registerOutParameter(parameterName, sqlType, scale);
  }

  public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
    delegate.setTime(parameterIndex, x, cal);
  }

  public ResultSet getGeneratedKeys() throws SQLException {
    return delegate.getGeneratedKeys();
  }

  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
    delegate.setTimestamp(parameterIndex, x, cal);
  }

  public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
    delegate.registerOutParameter(parameterName, sqlType, typeName);
  }

  public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
    return delegate.executeUpdate(sql, autoGeneratedKeys);
  }

  public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
    delegate.setNull(parameterIndex, sqlType, typeName);
  }

  public URL getURL(int parameterIndex) throws SQLException {
    return delegate.getURL(parameterIndex);
  }

  public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
    return delegate.executeUpdate(sql, columnIndexes);
  }

  public void setURL(int parameterIndex, URL x) throws SQLException {
    delegate.setURL(parameterIndex, x);
  }

  public void setURL(String parameterName, URL val) throws SQLException {
    delegate.setURL(parameterName, val);
  }

  public ParameterMetaData getParameterMetaData() throws SQLException {
    return delegate.getParameterMetaData();
  }

  public void setNull(String parameterName, int sqlType) throws SQLException {
    delegate.setNull(parameterName, sqlType);
  }

  public void setRowId(int parameterIndex, RowId x) throws SQLException {
    delegate.setRowId(parameterIndex, x);
  }

  public int executeUpdate(String sql, String[] columnNames) throws SQLException {
    return delegate.executeUpdate(sql, columnNames);
  }

  public void setBoolean(String parameterName, boolean x) throws SQLException {
    delegate.setBoolean(parameterName, x);
  }

  public void setNString(int parameterIndex, String value) throws SQLException {
    delegate.setNString(parameterIndex, value);
  }

  public void setByte(String parameterName, byte x) throws SQLException {
    delegate.setByte(parameterName, x);
  }

  public void setShort(String parameterName, short x) throws SQLException {
    delegate.setShort(parameterName, x);
  }

  public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
    delegate.setNCharacterStream(parameterIndex, value, length);
  }

  public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
    return delegate.execute(sql, autoGeneratedKeys);
  }

  public void setInt(String parameterName, int x) throws SQLException {
    delegate.setInt(parameterName, x);
  }

  public void setNClob(int parameterIndex, NClob value) throws SQLException {
    delegate.setNClob(parameterIndex, value);
  }

  public void setLong(String parameterName, long x) throws SQLException {
    delegate.setLong(parameterName, x);
  }

  public void setFloat(String parameterName, float x) throws SQLException {
    delegate.setFloat(parameterName, x);
  }

  public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    delegate.setClob(parameterIndex, reader, length);
  }

  public void setDouble(String parameterName, double x) throws SQLException {
    delegate.setDouble(parameterName, x);
  }

  public boolean execute(String sql, int[] columnIndexes) throws SQLException {
    return delegate.execute(sql, columnIndexes);
  }

  public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
    delegate.setBigDecimal(parameterName, x);
  }

  public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
    delegate.setBlob(parameterIndex, inputStream, length);
  }

  public void setString(String parameterName, String x) throws SQLException {
    delegate.setString(parameterName, x);
  }

  public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    delegate.setNClob(parameterIndex, reader, length);
  }

  public void setBytes(String parameterName, byte[] x) throws SQLException {
    delegate.setBytes(parameterName, x);
  }

  public void setDate(String parameterName, Date x) throws SQLException {
    delegate.setDate(parameterName, x);
  }

  public boolean execute(String sql, String[] columnNames) throws SQLException {
    return delegate.execute(sql, columnNames);
  }

  public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    delegate.setSQLXML(parameterIndex, xmlObject);
  }

  public void setTime(String parameterName, Time x) throws SQLException {
    delegate.setTime(parameterName, x);
  }

  public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
    delegate.setTimestamp(parameterName, x);
  }

  public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
    delegate.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
  }

  public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
    delegate.setAsciiStream(parameterName, x, length);
  }

  public int getResultSetHoldability() throws SQLException {
    return delegate.getResultSetHoldability();
  }

  public boolean isClosed() throws SQLException {
    return delegate.isClosed();
  }

  public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
    delegate.setBinaryStream(parameterName, x, length);
  }

  public void setPoolable(boolean poolable) throws SQLException {
    delegate.setPoolable(poolable);
  }

  public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    delegate.setAsciiStream(parameterIndex, x, length);
  }

  public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
    delegate.setObject(parameterName, x, targetSqlType, scale);
  }

  public boolean isPoolable() throws SQLException {
    return delegate.isPoolable();
  }

  public void closeOnCompletion() throws SQLException {
    delegate.closeOnCompletion();
  }

  public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    delegate.setBinaryStream(parameterIndex, x, length);
  }

  public boolean isCloseOnCompletion() throws SQLException {
    return delegate.isCloseOnCompletion();
  }

  public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
    delegate.setObject(parameterName, x, targetSqlType);
  }

  public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
    delegate.setCharacterStream(parameterIndex, reader, length);
  }

  public void setObject(String parameterName, Object x) throws SQLException {
    delegate.setObject(parameterName, x);
  }

  public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    delegate.setAsciiStream(parameterIndex, x);
  }

  public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    delegate.setBinaryStream(parameterIndex, x);
  }

  public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
    delegate.setCharacterStream(parameterName, reader, length);
  }

  public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    delegate.setCharacterStream(parameterIndex, reader);
  }

  public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
    delegate.setDate(parameterName, x, cal);
  }

  public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    delegate.setNCharacterStream(parameterIndex, value);
  }

  public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
    delegate.setTime(parameterName, x, cal);
  }

  public void setClob(int parameterIndex, Reader reader) throws SQLException {
    delegate.setClob(parameterIndex, reader);
  }

  public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
    delegate.setTimestamp(parameterName, x, cal);
  }

  public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
    delegate.setNull(parameterName, sqlType, typeName);
  }

  public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    delegate.setBlob(parameterIndex, inputStream);
  }

  public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    delegate.setNClob(parameterIndex, reader);
  }

  public String getString(String parameterName) throws SQLException {
    return delegate.getString(parameterName);
  }

  public boolean getBoolean(String parameterName) throws SQLException {
    return delegate.getBoolean(parameterName);
  }

  public byte getByte(String parameterName) throws SQLException {
    return delegate.getByte(parameterName);
  }

  public short getShort(String parameterName) throws SQLException {
    return delegate.getShort(parameterName);
  }

  public int getInt(String parameterName) throws SQLException {
    return delegate.getInt(parameterName);
  }

  public long getLong(String parameterName) throws SQLException {
    return delegate.getLong(parameterName);
  }

  public float getFloat(String parameterName) throws SQLException {
    return delegate.getFloat(parameterName);
  }

  public double getDouble(String parameterName) throws SQLException {
    return delegate.getDouble(parameterName);
  }

  public byte[] getBytes(String parameterName) throws SQLException {
    return delegate.getBytes(parameterName);
  }

  public Date getDate(String parameterName) throws SQLException {
    return delegate.getDate(parameterName);
  }

  public Time getTime(String parameterName) throws SQLException {
    return delegate.getTime(parameterName);
  }

  public Timestamp getTimestamp(String parameterName) throws SQLException {
    return delegate.getTimestamp(parameterName);
  }

  public Object getObject(String parameterName) throws SQLException {
    return delegate.getObject(parameterName);
  }

  public BigDecimal getBigDecimal(String parameterName) throws SQLException {
    return delegate.getBigDecimal(parameterName);
  }

  public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
    return delegate.getObject(parameterName, map);
  }

  public Ref getRef(String parameterName) throws SQLException {
    return delegate.getRef(parameterName);
  }

  public Blob getBlob(String parameterName) throws SQLException {
    return delegate.getBlob(parameterName);
  }

  public Clob getClob(String parameterName) throws SQLException {
    return delegate.getClob(parameterName);
  }

  public Array getArray(String parameterName) throws SQLException {
    return delegate.getArray(parameterName);
  }

  public Date getDate(String parameterName, Calendar cal) throws SQLException {
    return delegate.getDate(parameterName, cal);
  }

  public Time getTime(String parameterName, Calendar cal) throws SQLException {
    return delegate.getTime(parameterName, cal);
  }

  public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
    return delegate.getTimestamp(parameterName, cal);
  }

  public URL getURL(String parameterName) throws SQLException {
    return delegate.getURL(parameterName);
  }

  public RowId getRowId(int parameterIndex) throws SQLException {
    return delegate.getRowId(parameterIndex);
  }

  public RowId getRowId(String parameterName) throws SQLException {
    return delegate.getRowId(parameterName);
  }

  public void setRowId(String parameterName, RowId x) throws SQLException {
    delegate.setRowId(parameterName, x);
  }

  public void setNString(String parameterName, String value) throws SQLException {
    delegate.setNString(parameterName, value);
  }

  public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
    delegate.setNCharacterStream(parameterName, value, length);
  }

  public void setNClob(String parameterName, NClob value) throws SQLException {
    delegate.setNClob(parameterName, value);
  }

  public void setClob(String parameterName, Reader reader, long length) throws SQLException {
    delegate.setClob(parameterName, reader, length);
  }

  public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
    delegate.setBlob(parameterName, inputStream, length);
  }

  public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
    delegate.setNClob(parameterName, reader, length);
  }

  public NClob getNClob(int parameterIndex) throws SQLException {
    return delegate.getNClob(parameterIndex);
  }

  public NClob getNClob(String parameterName) throws SQLException {
    return delegate.getNClob(parameterName);
  }

  public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
    delegate.setSQLXML(parameterName, xmlObject);
  }

  public SQLXML getSQLXML(int parameterIndex) throws SQLException {
    return delegate.getSQLXML(parameterIndex);
  }

  public SQLXML getSQLXML(String parameterName) throws SQLException {
    return delegate.getSQLXML(parameterName);
  }

  public String getNString(int parameterIndex) throws SQLException {
    return delegate.getNString(parameterIndex);
  }

  public String getNString(String parameterName) throws SQLException {
    return delegate.getNString(parameterName);
  }

  public Reader getNCharacterStream(int parameterIndex) throws SQLException {
    return delegate.getNCharacterStream(parameterIndex);
  }

  public Reader getNCharacterStream(String parameterName) throws SQLException {
    return delegate.getNCharacterStream(parameterName);
  }

  public Reader getCharacterStream(int parameterIndex) throws SQLException {
    return delegate.getCharacterStream(parameterIndex);
  }

  public Reader getCharacterStream(String parameterName) throws SQLException {
    return delegate.getCharacterStream(parameterName);
  }

  public void setBlob(String parameterName, Blob x) throws SQLException {
    delegate.setBlob(parameterName, x);
  }

  public void setClob(String parameterName, Clob x) throws SQLException {
    delegate.setClob(parameterName, x);
  }

  public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
    delegate.setAsciiStream(parameterName, x, length);
  }

  public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
    delegate.setBinaryStream(parameterName, x, length);
  }

  public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
    delegate.setCharacterStream(parameterName, reader, length);
  }

  public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
    delegate.setAsciiStream(parameterName, x);
  }

  public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
    delegate.setBinaryStream(parameterName, x);
  }

  public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
    delegate.setCharacterStream(parameterName, reader);
  }

  public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
    delegate.setNCharacterStream(parameterName, value);
  }

  public void setClob(String parameterName, Reader reader) throws SQLException {
    delegate.setClob(parameterName, reader);
  }

  public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
    delegate.setBlob(parameterName, inputStream);
  }

  public void setNClob(String parameterName, Reader reader) throws SQLException {
    delegate.setNClob(parameterName, reader);
  }

  public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
    return delegate.getObject(parameterIndex, type);
  }

  public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
    return delegate.getObject(parameterName, type);
  }  
  
}
