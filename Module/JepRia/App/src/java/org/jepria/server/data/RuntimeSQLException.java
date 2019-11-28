package org.jepria.server.data;

import java.sql.SQLException;

/**
 * Unchecked wrapper for an {@link SQLException}
 */
public class RuntimeSQLException extends RuntimeException {

  public RuntimeSQLException(String message, SQLException cause) {
    super(message, cause);
  }

  public RuntimeSQLException(SQLException cause) {
    super(cause);
  }

  public RuntimeSQLException(String message, SQLException cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  /**
   * Get the original SQLException cause which this RuntimeSQLException is a wrapper for
   * @return SQLException cause
   */
  public SQLException getSQLException() {
    return getCause();
  }

  @Override
  public synchronized SQLException getCause() {
    // the cause may only be an SQLException
    return (SQLException) super.getCause();
  }
}
