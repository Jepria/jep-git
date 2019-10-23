package com.technology.jep.jepria.shared.exceptions;

import com.technology.jep.jepria.client.async.JepAsyncCallback;

import java.io.Serializable;

/**
 * Общий предок исключений, "выбрасываемых" сервисными методами системного и прикладного кода.<br/>
 * <br/>
 * Данный тип <strong>проверяемых исключительных ситуаций</strong> (ИС) описывает класс ошибок, возникаемых в результате обращения 
 * сервисных методов к БД, файловой системе, а также при работе с другими внешними ресурсами. Любая нештатная ситуация,
 * происходящая при их работе, оборачивается в данный тип исключений и пробрасывается дальше. В конечном счете 
 * обработка исключений осуществляется на клиентской стороне в методе обработки {@link JepAsyncCallback#onFailure(Throwable)}.<br/>
 * Более того, указание данного проверяемого исключения в сигнатуре сервисного метода, акцентирует внимание прикладного разработчика 
 * на возможное появление ошибок во время его работы.
 */
public class ApplicationException extends Exception implements Serializable {
  
  private static final long serialVersionUID = 1L;

  public ApplicationException() {
  }
  
  public ApplicationException(String message, Throwable cause) {
    super(message, cause);
  }
}
