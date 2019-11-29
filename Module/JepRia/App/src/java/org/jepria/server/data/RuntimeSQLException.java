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

  public SQLException getSQLException() {
    return (SQLException) getCause();
  }
}
