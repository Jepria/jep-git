package com.technology.jep.jepriashowcase.main.shared;

import com.technology.jep.jepria.shared.JepRiaConstant;


/**
 * Общие константы приложения.
 */
public class JepRiaShowcaseConstant extends JepRiaConstant {

  public static final String URL_EMBEDDED = "JepRiaShowcaseEmbedded.jsp?em=Goods&es=sh#sh:sm=Goods&ws=sh";
  public static final String SUCCESS = "Success!";
  
  /**
   * Адрес сервлета для программной авторизации на Tomcat-окружении
   */
  public static final String TOMCAT_AUTLOGON_URL = "/JepRiaShowcase/autoLogonServlet?username={0}&password={1}&url=/JepRiaShowcase/ProtectedPage.jsp";
  
  /**
   * Адрес SSO-модуля для запроса авторизации на сервере Oracle Application Server
   */
  public static final String OAS_SSO_MODULE_URL = "/jsso/autoLogonServlet?username={0}&password={1}&url=/JepRiaShowcase/ProtectedPage.jsp";
}
