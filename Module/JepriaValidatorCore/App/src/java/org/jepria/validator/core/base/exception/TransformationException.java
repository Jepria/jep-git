package org.jepria.validator.core.base.exception;

/**
 * Исключение, которое следует выбрасывать при невозможности произвести прикладную трансформацию
 */
public class TransformationException extends RuntimeException {

  private static final long serialVersionUID = -2824257240764631685L;

  public TransformationException() {
    super();
  }

  public TransformationException(String message) {
    super(message);
  }

  public TransformationException(String message, Throwable cause) {
    super(message, cause);
  }

  public TransformationException(Throwable cause) {
    super(cause);
  }
  
  
  

}
