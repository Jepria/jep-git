package com.technology.jep.jepria.server.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс-обёртка для {@link CallableStatement}, реализующий особенности взаимодействия с Oracle.
 * <ul>
 *   <li>Методы, которые должны вернуть {@link ResultSet}, возвращают {@link ResultSetWrapper}.</li>
 *   <li>Если при вызове {@link CallableStatement#execute} было получено исключение
 *       ORA-04068: Existing state of packages has been discarded, то метод вызывается повторно.</li>
 * </ul>
 * @author EydlinA
 *
 */
public class OracleCallableStatementWrapper extends CallableStatementWrapper {

  private static final String PACKAGE_STATE_DISCARDED_ERROR_CODE = "ORA-04068";
  
  private OracleCallableStatementWrapper(CallableStatement cs) {
    super(cs);
  }
  
  /**
   * Wraps an instance of {@link CallableStatement} to inject Oracle-specific functionality.
   * @param cs instance to wrap
   * @return @return new instance of {@link OracleCallableStatementWrapper} wrapping the CallableStatement, or {@code null} if the argument is {@code null}.
   */
  public static OracleCallableStatementWrapper wrap(CallableStatement cs) {
    if (cs == null) {
      return null;
    } else {
      return new OracleCallableStatementWrapper(cs);
    }
  }

  @Override
  public ResultSet executeQuery(String sql) throws SQLException {
    return ResultSetWrapper.wrap(super.executeQuery(sql));
  }

  @Override
  public ResultSet executeQuery() throws SQLException {
    return ResultSetWrapper.wrap(super.executeQuery());
  }

  @Override
  public ResultSet getResultSet() throws SQLException {
    return ResultSetWrapper.wrap(super.getResultSet());
  }

  @Override
  public ResultSet getGeneratedKeys() throws SQLException {
    return ResultSetWrapper.wrap(super.getGeneratedKeys());
  }

  @Override
  public int executeUpdate(String sql) throws SQLException {
    try {
      return super.executeUpdate(sql);
    } catch (SQLException e) {
      if (e.getMessage().startsWith(PACKAGE_STATE_DISCARDED_ERROR_CODE)) {
        return this.executeUpdate(sql);
      } else {
        throw e;
      }
    }
  }

  @Override
  public int executeUpdate() throws SQLException {
    try {
      return super.executeUpdate();
    } catch (SQLException e) {
      if (e.getMessage().startsWith(PACKAGE_STATE_DISCARDED_ERROR_CODE)) {
        return this.executeUpdate();
      } else {
        throw e;
      }
    }
  }

  @Override
  public boolean execute(String sql) throws SQLException {
    try {
      return super.execute(sql);
    } catch (SQLException e) {
      if (e.getMessage().startsWith(PACKAGE_STATE_DISCARDED_ERROR_CODE)) {
        return this.execute(sql);
      } else {
        throw e;
      }
    }
  }

  @Override
  public boolean execute() throws SQLException {
    try {
      return super.execute();
    } catch (SQLException e) {
      if (e.getMessage().startsWith(PACKAGE_STATE_DISCARDED_ERROR_CODE)) {
        return this.execute();
      } else {
        throw e;
      }
    }
  }

  @Override
  public int[] executeBatch() throws SQLException {
    try {
      return super.executeBatch();
    } catch (SQLException e) {
      if (e.getMessage().startsWith(PACKAGE_STATE_DISCARDED_ERROR_CODE)) {
        return this.executeBatch();
      } else {
        throw e;
      }
    }
  }

  @Override
  public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
    try {
      return super.executeUpdate(sql, autoGeneratedKeys);
    } catch (SQLException e) {
      if (e.getMessage().startsWith(PACKAGE_STATE_DISCARDED_ERROR_CODE)) {
        return this.executeUpdate(sql, autoGeneratedKeys);
      } else {
        throw e;
      }
    }
  }

  @Override
  public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
    try {
      return super.executeUpdate(sql, columnIndexes);
    } catch (SQLException e) {
      if (e.getMessage().startsWith(PACKAGE_STATE_DISCARDED_ERROR_CODE)) {
        return this.executeUpdate(sql, columnIndexes);
      } else {
        throw e;
      }
    }
  }

  @Override
  public int executeUpdate(String sql, String[] columnNames) throws SQLException {
    try {
      return super.executeUpdate(sql, columnNames);
    } catch (SQLException e) {
      if (e.getMessage().startsWith(PACKAGE_STATE_DISCARDED_ERROR_CODE)) {
        return this.executeUpdate(sql, columnNames);
      } else {
        throw e;
      }
    }
  }

  @Override
  public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
    try {
      return super.execute(sql, autoGeneratedKeys);
    } catch (SQLException e) {
      if (e.getMessage().startsWith(PACKAGE_STATE_DISCARDED_ERROR_CODE)) {
        return this.execute(sql, autoGeneratedKeys);
      } else {
        throw e;
      }
    }
  }

  @Override
  public boolean execute(String sql, int[] columnIndexes) throws SQLException {
    try {
      return super.execute(sql, columnIndexes);
    } catch (SQLException e) {
      if (e.getMessage().startsWith(PACKAGE_STATE_DISCARDED_ERROR_CODE)) {
        return this.execute(sql, columnIndexes);
      } else {
        throw e;
      }
    }
  }

  @Override
  public boolean execute(String sql, String[] columnNames) throws SQLException {
    try {
      return super.execute(sql, columnNames);
    } catch (SQLException e) {
      if (e.getMessage().startsWith(PACKAGE_STATE_DISCARDED_ERROR_CODE)) {
        return this.execute(sql, columnNames);
      } else {
        throw e;
      }
    }
  }  
}
