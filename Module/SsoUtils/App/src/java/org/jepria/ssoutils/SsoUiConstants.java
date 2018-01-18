package org.jepria.ssoutils;

public class SsoUiConstants {
  
  /**
   * Параметр, содержащий имя модуля, на который нужно вернуться после авторизации через Sso.
   */
  public static final String REQUEST_PARAMETER_ENTER_MODULE = "fwdem";
  
  /**
   * Параметр, содержащий название модуля, которое нужно отобразить в заголовке SsoUi.
   */
  public static final String REQUEST_PARAMETER_MODULE_TITLE = "fwdmt";
  
  /**
   * Параметр, содержащий queryString запроса, после которого потребовалась авторизация через Sso (часть URL после символа ?).
   */
  public static final String REQUEST_PARAMETER_QUERY_STRING = "fwdqs";
  
  /**
   * Параметр, содержащий fragment запроса, после которого потребовалась авторизация через Sso (часть URL после символа #).
   */
  public static final String REQUEST_PARAMETER_FRAGMENT = "fwdfg";
  
  /**
   * ID html-страницы с логин-формой. Нужно как для автоматизации тестирования, так и для проверки логин-формы по тексту HttpResonse.
   */
  public static final String LOGIN_FORM_HTML_ID = "loginForm";
}
