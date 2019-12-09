package org.jepria.server.service.rest.jersey;

import org.jepria.server.service.rest.ErrorDto;

/**
 * Application-level usage example:<br/>
 * <pre/>
 * try {
 *   // invoke code that throws a business-logic exception
 *   service.doSomething();
 * } catch (BusinessLogicException e) {
 *   ErrorDto errorDto = ExceptionManager.newInstance().registerExceptionAndPrepareErrorDto(e);
 *   errorDto.setErrorCode(1234);
 *   errorDto.setErrorMessage("A business-logic exception occurred!");
 *   return Response.serverError().entity(errorDto).build();
 * }</pre>
 */
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
