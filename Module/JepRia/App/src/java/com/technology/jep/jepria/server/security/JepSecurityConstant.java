package com.technology.jep.jepria.server.security;


public class JepSecurityConstant {
  //Пользователь Гость.
  public static final String GUEST_LOGIN = "Guest";//Логин.
  public static final String GUEST_PASSWORD = "Guest";//Пароль.
  
  /**
   * Имя свойства принципала, содержащего username.
   */
  public static final String PRINCIPAL_PROPERTY_NAME_USERNAME = "username";

  /**
   * Имя атрибута сессии, в котором хранится объект класса JepSecurityModule.
   */
  public static final String JEP_SECURITY_MODULE_ATTRIBUTE_NAME = "jepSecurityModule";

  /**
   * Имя параметра http-запроса: mtSID.
   */
  public static final String HTTP_REQUEST_PARAMETER_MTSID = "mtSID";
}