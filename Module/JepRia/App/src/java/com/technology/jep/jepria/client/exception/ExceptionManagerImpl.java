package com.technology.jep.jepria.client.exception;

import org.jepria.security.SsoUiConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.InvocationException;
import com.technology.jep.jepria.client.entrance.Entrance;
import com.technology.jep.jepria.client.message.JepMessageBoxImpl;

public class ExceptionManagerImpl implements ExceptionManager {

  public static final ExceptionManager instance = new ExceptionManagerImpl();

  public void handleException(Throwable th) {
    handleException(th, null);
  }

  public void handleException(Throwable th, String message) {
    // Workaround для 12152 (пока "проглатываем")
    Log.error("ExceptionManager(" + th + "," + message + ")");
    if(is12152StatusCodeException(th)) {
      while(th.getCause() != null) {
        Log.debug("ExceptionManager(): th.getCause() = " + th.getCause());
        Log.debug("ExceptionManager(): th.getMessage() = " + th.getMessage());
        th = th.getCause();
      }
      Log.error("ExceptionManager(" + th + "," + message + "): 12152 StatusCodeException cause = " + th);
      return;
    }
    
    if(isJavaSsoTimeout(th) || isSsoTimeout(th)) {
       // logout на серверной стороне уже выполнен силами javasso, "закрепляем" состояние logout со стороны клиента
      Entrance.logout(); 
    } else {
      Log.error(message, th);
      JepMessageBoxImpl.instance.showError(th, message);
    }
  }

  private static boolean is12152StatusCodeException(Throwable th) {
    String strException = th.toString();
    Log.error("ExceptionManager().is12152StatusCodeException(): strException = " + strException);
    return strException.contains("12152");
  }

  /**
   * Метод проверяет, не похож ли текст ошибки на JavaSso-ошибку логина,
   * получаемую при устаревании сессии (только для OC4J)
   */
  private static boolean isJavaSsoTimeout(Throwable caught) {
    String message = caught.getMessage();
    return message != null && message.contains("JavaSSO");  // TODO Сделать строже
  }
  
  /**
   * Метод проверяет, не похож ли текст ошибки на Sso-ошибку логина,
   * получаемую при устаревании сессии (только для Tomcat)
   */
  private static boolean isSsoTimeout(Throwable caught) {
    String message = caught.getMessage();
    return caught instanceof InvocationException 
        && message != null && message.contains("id=\"" + SsoUiConstants.LOGIN_FORM_HTML_ID + "\"");  // TODO Сделать строже
  }
}
