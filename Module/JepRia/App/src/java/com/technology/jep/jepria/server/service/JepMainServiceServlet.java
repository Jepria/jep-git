package com.technology.jep.jepria.server.service;

import static com.technology.jep.jepria.shared.JepRiaConstant.JEP_USER_NAME_FIELD_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.JEP_USER_ROLES_FIELD_NAME;
import static com.technology.jep.jepria.shared.field.JepFieldNames.OPERATOR_ID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.technology.jep.jepria.server.security.module.JepSecurityModule;
import org.apache.log4j.Logger;

import com.technology.jep.jepria.server.security.SecurityFactory;
import com.technology.jep.jepria.shared.dto.JepDto;
import com.technology.jep.jepria.shared.service.JepMainService;

/**
 * Абстрактный предок сервисов Jep.<br/>
 * <br/>
 * TODO: Хорошо бы оповещать пользователя о том, что в кеш вместились все записи.
 */
@SuppressWarnings("serial")
public class JepMainServiceServlet extends JepServiceServlet implements JepMainService {
  protected static Logger logger = Logger.getLogger(JepMainServiceServlet.class.getName());
  
  /**
   * Получение данных пользователя (имени, operatorId, ролей, ...).
   * 
   * @return данные пользователя
   */
  public JepDto getUserData() {
    logger.debug("getUserData()");
    JepDto userData = new JepDto();
    
    JepSecurityModule securityModule = SecurityFactory.getSecurityModule(getThreadLocalRequestWrapper());
    userData.set(JEP_USER_NAME_FIELD_NAME, securityModule.getUsername());
    userData.set(OPERATOR_ID, getOperatorId());
    userData.set(JEP_USER_ROLES_FIELD_NAME, securityModule.getRoles());
    return userData;
  }
  
  public String logout(String currentUrl) throws Exception {
    logger.debug("logout()");
    HttpServletRequest request = getThreadLocalRequestWrapper();
    HttpServletResponse response =  getThreadLocalResponseWrapper();
    return SecurityFactory.getSecurityModule(request).logout(request, response, currentUrl);
  }
}
