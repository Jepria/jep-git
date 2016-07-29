package com.technology.jep.jepria.shared.exceptions;

import java.io.IOException;

import javax.naming.NamingException;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Общий предок исключений, "порождаемых" платформой (J2EE-контейнером, базой данных и т.п.).<br/>
 * <br/>
 * Данный тип <strong>непроверяемых исключений</strong> описывает класс исключительных ситуаций, возникаемых на системном уровне.
 * В основном, его использование предназначено для целей системного уровня и возможности обертывания исключений, 
 * возникаемых во время работы приложения ({@link NamingException}, {@link IOException} и т.д.).<br/> 
 * При использовании в прикладном коде, для генерации ислючительных ситуаций клиентского уровня рекомендуется
 * проверить наличие искомого среди уже имеющихся потомков данного класса. В случае отсутствия, следует  
 * создать наследника от данного класса или использовать производные классы общего предка {@link RuntimeException}.
 */
public class SystemException extends RuntimeException implements IsSerializable {
  
  private static final long serialVersionUID = 1L;
  
  public SystemException() {
  }
  
  public SystemException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public SystemException(String message) {
    this(message, null);
  }

}
