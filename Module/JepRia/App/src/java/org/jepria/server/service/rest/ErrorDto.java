package org.jepria.server.service.rest;

/**
 * Класс для представления деталей серверной ошибки типа Internal Server Error (HTTP-статус 500) клиенту
 */
public class ErrorDto {
  private int errorCode;
  private String errorMessage;
  private String errorId;

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorId() {
    return errorId;
  }

  public void setErrorId(String errorId) {
    this.errorId = errorId;
  }
}
