package com.technology.jep.jepria.server.service;

import static com.technology.jep.jepria.server.JepRiaServerConstant.JEP_USER_NAME_FIELD_NAME;
import static com.technology.jep.jepria.server.JepRiaServerConstant.JEP_USER_ROLES_FIELD_NAME;
import static com.technology.jep.jepria.shared.field.JepFieldNames.OPERATOR_ID;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.shared.dto.JepDto;

/**
 * Реализация gwt-сервиса главного модуля приложения.
 */
public class JepMainServiceServletTest extends JepMainServiceServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(JepMainServiceServletTest.class.getName());
	
	/**
	 * Получение тестовых данных пользователя (имени, operatorId, ролей, ...).
	 * 
	 * @return данные пользователя
	 */
	@SuppressWarnings("unchecked")
	public JepDto getUserData() {
		List roles = new ArrayList();
		roles.add("JrsEditSupplier");
		roles.add("JrsEditGoods");
		roles.add("JrsEditShopGoods");
		roles.add("JrsEditShopGoods");
		roles.add("JrsEditRequest");
		roles.add("JrsEditRequestProcess");
	
		JepDto userData = new JepDto();
		
		userData.set(JEP_USER_NAME_FIELD_NAME, "talyshevvv");
		userData.set(OPERATOR_ID, 3);
		userData.set(JEP_USER_ROLES_FIELD_NAME, roles);
		
		logger.debug("getUserData() : userData = " + userData);
		
		return userData;
	}
	
}
