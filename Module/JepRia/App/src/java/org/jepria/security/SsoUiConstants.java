package org.jepria.security;

public class SsoUiConstants {
  
  /**
   * Параметр, содержащий имя модуля, на который нужно вернуться после авторизации через Sso.
   */
  public static final String REQUEST_PARAMETER_ENTER_MODULE = "enterModule";
  
  /**
   * Параметр, содержащий queryString запроса, после которого потребовалась авторизация через Sso.
   */
  public static final String REQUEST_PARAMETER_QUERY_STRING = "queryString";
  
  /**
   * ID html-страницы с логин-формой. Нужно как для автоматизации тестирования, так и для проверки логин-формы по тексту HttpResonse.
   */
  public static final String LOGIN_FORM_HTML_ID = "loginForm";
}
