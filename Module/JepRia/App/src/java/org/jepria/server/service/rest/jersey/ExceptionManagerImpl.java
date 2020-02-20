package org.jepria.server.service.rest.jersey;

import org.jepria.server.data.RuntimeSQLException;
import org.jepria.server.service.rest.ErrorDto;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExceptionManagerImpl implements ExceptionManager {

  @Override
  public String registerException(Throwable e) {
    if (e == null) {
      return null;
    }

    final String errorId = generateErrorId();

    // print everything to the same stream
    final PrintStream errStream  = System.err;

    errStream.println(new Date() + ": Exception handled (errorId=" + errorId + "):"); // even if errorId is null, print "null"
    e.printStackTrace(errStream);

    return errorId;
  }

  @Override
  public ErrorDto registerExceptionAndPrepareErrorDto(Throwable e) {
    final String errorId = registerException(e);

    ErrorDto errorDto = new ErrorDto();

    errorDto.setErrorId(errorId);

    Throwable e2 = e;
    if (e instanceof RuntimeSQLException) {
      RuntimeSQLException runtimeSQLException = (RuntimeSQLException) e;
      e2 = runtimeSQLException.getSQLException();
    }

    String message = null;
    
    if (e2 instanceof SQLException) {

      SQLException sqlException = (SQLException)e2;
      
      message = getErrorMessage(sqlException);
      errorDto.setErrorCode(sqlException.getErrorCode());

    } else {
      message = e2.getMessage();
    }
  
    if (message == null || "".equals(message)) {
      message = e2.getClass().getSimpleName() + " (no details attached)"; // default message
    }
  
    errorDto.setErrorMessage(message);

    return errorDto;
  }

  protected String getErrorMessage(final SQLException e) {
    // try extract top-level message only from the SQLException message (oracle's stacktrace) // TODO refine or refactor
    String message = e.getMessage();

    if (message != null) {
      Matcher m = Pattern.compile("ORA-\\d+:\\s+(.+?)(\\R.*)?", Pattern.DOTALL).matcher(message);
      if (m.matches()) {
        message = m.group(1);
      }
    }

    return message;
  }

  protected String generateErrorId() {
    return String.valueOf(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
  }
}
