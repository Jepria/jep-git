package com.technology.jep.jepriashowcase.custom.server.service;

import static com.technology.jep.jepriashowcase.main.shared.JepRiaShowcaseConstant.*;

import java.text.MessageFormat;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.client.JepRiaClientConstant;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.server.util.JepServerUtil;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;
import com.technology.jep.jepriashowcase.custom.server.CustomServerFactory;
import com.technology.jep.jepriashowcase.custom.server.dao.Custom;
import com.technology.jep.jepriashowcase.custom.shared.service.CustomService;

/**
 * Реализация gwt-сервиса для Модуля с произвольным располажением элементов.
 */
@RemoteServiceRelativePath("CustomService")
public class CustomServiceImpl extends JepDataServiceServlet<Custom> implements CustomService {
  private static final long serialVersionUID = 1L;
  
  public CustomServiceImpl() {
    super(
      null // TODO 8.0: CustomRecordDefinition.instance
      , CustomServerFactory.instance);
  }

  public String getOperatorName(
    Integer operatorId) {
    String result = null;
    try {
      logger.trace("BEGIN TRANSACTION getOperatorName(" + operatorId + ")");
      result = dao.getOperatorName(operatorId);
      
    } catch (Throwable th) {
      throw new SystemException(th.getLocalizedMessage(), th);
    }
    logger.trace("END TRANSACTION getOperatorName(" + operatorId + ")");
    return result;
  }
  
  @Override
  public void transaction() throws ApplicationException {
    try {
      logger.trace("BEGIN TRANSACTION transaction()");
      dao.transaction();
    } catch (Throwable th) {
      logger.error(th.getLocalizedMessage(), th);
      throw new ApplicationException(th.getLocalizedMessage(), th);
    }
    logger.trace("END TRANSACTION transaction()");
  }
  
  @Override
  public String getSsoPath(String login, String password) {
    return MessageFormat.format(JepServerUtil.isTomcat(getThreadLocalRequest()) ? TOMCAT_AUTLOGON_URL : OAS_SSO_MODULE_URL, login, password);
  }
  
}
