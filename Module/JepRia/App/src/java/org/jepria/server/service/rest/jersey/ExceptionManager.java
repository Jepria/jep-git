package org.jepria.server.service.rest.jersey;

import org.jepria.server.service.rest.ErrorDto;

public interface ExceptionManager {

  /**
   * Logs an exception and returns errorId generated
   * @param e
   * @return
   */
  String registerException(Throwable e);

  /**
   * Logs an exception and returns an {@link ErrorDto} prepared for the response
   * @param e
   * @return
   */
  ErrorDto registerExceptionAndPrepareErrorDto(Throwable e);

  static ExceptionManager newInstance() {
    return new ExceptionManagerImpl();
  }
}
