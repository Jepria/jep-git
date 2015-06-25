package com.technology.jep.jepria.server.service;

import static com.technology.jep.jepria.shared.JepRiaConstant.JEP_USER_NAME_FIELD_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.JEP_USER_ROLES_FIELD_NAME;
import static com.technology.jep.jepria.shared.field.JepFieldNames.OPERATOR_ID;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.security.JepSecurityModule;
import com.technology.jep.jepria.server.util.JepServerUtil;
import com.technology.jep.jepria.shared.JepRiaConstant;
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
		
		JepSecurityModule securityModule = ServerFactory.getSecurityModule(getThreadLocalRequest());
		userData.set(JEP_USER_NAME_FIELD_NAME, securityModule.getUsername());
		userData.set(OPERATOR_ID, getOperatorId());
		userData.set(JEP_USER_ROLES_FIELD_NAME, securityModule.getRoles());
		return userData;
	}
	
	public void logout() {
		logger.debug("logout()");
		HttpServletRequest request = getThreadLocalRequest();
		HttpServletResponse response =  getThreadLocalResponse();
		removeSsoIntegrationCookies(request, response);
		ServerFactory.getSecurityModule(request).logout(request, response);
	}

	private void removeSsoIntegrationCookies(HttpServletRequest request, HttpServletResponse response) {
		List<Cookie> ssoIntegrationCookies = getSsoIntegrationCookies(request);
		for(Cookie cookie: ssoIntegrationCookies) {
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		
		// Если Инициатива WebLogic, удаляем cookie JavaSSO (при инициативе OC4J он сам их удалит через JavaSSO Logout)
		if(!JepServerUtil.isJavaSSO(request)) {
			Cookie oc4jSsoCookie = getOC4JSsoCookie(request);
			if(oc4jSsoCookie != null) {
				oc4jSsoCookie.setMaxAge(0);
				oc4jSsoCookie.setPath("/");
				response.addCookie(oc4jSsoCookie);
			}
		}
	}
	

	private static List<Cookie> getSsoIntegrationCookies(HttpServletRequest request) {
		List<Cookie> result = new ArrayList<Cookie>();
		
		Cookie[] cookies = request.getCookies();
		String integrationCookieDomain = null;
		String cookieName;
		int i;
		for (i = 0; i < cookies.length; i++) {
		  cookieName = cookies[i].getName();
		  logger.trace("getSsoIntegrationCookies(): cookieName = " + cookieName);
		  if(cookieName.startsWith(JepRiaConstant.ORA_WL_INTEGRATION_SSO_COOKIE_NAME_PREFIX)) {
			  if(integrationCookieDomain == null) {
				  integrationCookieDomain = cookies[i].getDomain(); 
			  }
			  result.add(cookies[i]);
		  }
		}
		
		return result;
	}

	private static Cookie getOC4JSsoCookie(HttpServletRequest request) {
		Cookie result = null;
		Cookie[] cookies = request.getCookies();
		for(int i = 0; i < cookies.length; i++) {
		  String cookieName = cookies[i].getName();
		  if(JepRiaConstant.ORA_OC4J_SSO_COOKIE_NAME.equals(cookieName)) {
			  result = cookies[i];
			  break;
		  }
		}
		
		return result;
	}
}
