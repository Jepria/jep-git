package com.technology.jep.jepria.server.security.cas;

import static com.technology.jep.jepria.server.JepRiaServerConstant.CAS_SERVER_NAME_CONTEXT_PARAMETER;
import static com.technology.jep.jepria.server.JepRiaServerConstant.ENVIRONMENT_CAS_SERVER_ADDRESS;
import static com.technology.jep.jepria.server.security.JepSecurityConstant.JEP_SECURITY_MODULE_ATTRIBUTE_NAME;

import java.net.InetAddress;
import java.security.Principal;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.technology.jep.jepcommon.security.pkg_Operator;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.server.security.JepAbstractSecurityModule;
import com.technology.jep.jepria.server.security.JepSecurityModule;
import com.technology.jep.jepria.server.util.JepServerUtil;
import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.jep.jepria.shared.exceptions.SystemException;

public class JepSecurityModule_CAS extends JepAbstractSecurityModule {

	static {
		logger = Logger.getLogger(JepSecurityModule_CAS.class.getName());
	}

	private JepSecurityModule_CAS() {
		init();
	}

	/**
	 * Возвращает объект типа JepSecurityModule из сессии. Если объект не
	 * найден в сессии или устаревший (например, оставшийся в сессии модуля после logout()),
	 * то создается новый объект и помещается в сессию.
	 * 
	 * @param request запрос, из которого получим сессию
	 * @return объект типа JepSecurityModule из сессии
	 * TODO Попробовать уменьшить размер синхронизируемого кода (synchronized). Точно ли нужна синхронизация ?
	 */
	public static synchronized JepSecurityModule getInstance(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Principal principal = request.getUserPrincipal();
		JepSecurityModule_CAS securityModule;
		if(principal == null) { // Работает гость ?
			securityModule = (JepSecurityModule_CAS) session.getAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME);
			if(securityModule == null) { // Первый вход ?
				securityModule = new JepSecurityModule_CAS();
				session.setAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME, securityModule);
				securityModule.doLogonByGuest();
			}
		} else {	// Входили через SSO
			securityModule = (JepSecurityModule_CAS) session.getAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME);
			if (securityModule == null || isObsolete(securityModule.db, principal, securityModule.operatorId)) {
				securityModule = new JepSecurityModule_CAS();
				session.setAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME, securityModule);
				securityModule.updateSubject(principal);
			}
		}
		return securityModule;
	}

	@Override
	// TODO Перенести в CAS-клиента
	public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		removeJavaSSOCookie(request, response);
		request.getSession().invalidate();
		
		String casServerAddress = JepServerUtil.getEnvironmentValue(ENVIRONMENT_CAS_SERVER_ADDRESS);
		
        ServletContext context = request.getSession().getServletContext();
        String casServerContextName = context.getInitParameter(CAS_SERVER_NAME_CONTEXT_PARAMETER);
        if(casServerContextName != null) {
    		final String casLogoutPath = casServerContextName + "/logout";
    		String casLogoutAdress = casServerAddress + "/" + casLogoutPath;
    		String canonicalHostName = InetAddress.getLocalHost().getCanonicalHostName();
    		int serverPort = request.getServerPort();
    		String contextPath = request.getContextPath();
    		String logoutUrl = casLogoutAdress + "?service=http://" + canonicalHostName + ":" + serverPort + contextPath;
    		return logoutUrl;
        } else {
        	throw new SystemException("casServerName context parameter not found");
        }
	}

	private void removeJavaSSOCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = getOC4JSsoCookie(request);
		deleteCookie(cookie, response);
	}

	private void deleteCookie(Cookie cookie, HttpServletResponse response) {
		if(cookie != null) {
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
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
	
	
	@Override
	public Integer getJepPrincipalOperatorId(Principal principal) {
		Integer result = null;
		String principalName = principal.getName();
		try {
			Integer _operatorId = pkg_Operator.logon(db, principalName);
			if(!_operatorId.equals(operatorId)) {	// Обновить свойства, если operatorId изменялся
				updateSubject(principal);	// TODO выпрямить, оптимизировать
			}
			result = operatorId;
		} catch (SQLException ex) {
			logger.error("pkg_Operator.getJepPrincipalOperatorId() error", ex);
		}

		return result;
	}

	@Override
	protected void updateSubject(Principal principal) {
		logger.trace(this.getClass() + ".updateSubject() BEGIN");
		String principalName = principal.getName();
		logger.trace("principalName = " + principalName);
		this.username = principalName;

		try {
			roles = pkg_Operator.getRoles(db, principalName);
			Integer _operatorId = pkg_Operator.logon(db, principalName);
			if(_operatorId != null) {
				operatorId = _operatorId;
			}
		} catch (SQLException ex) {
			logger.error("pkg_Operator error", ex);
		}
		
		logger.trace(this.getClass() + ".updateSubject() END");
	}

	private static boolean isObsolete(Db db, Principal principal, Integer currentOperatorId) {
		boolean result = true;
		try {
			Integer _operatorId = pkg_Operator.logon(db, principal.getName());
			if(_operatorId != null) {
				if(_operatorId.equals(currentOperatorId)) {	// Если operatorID совпадают, значит объект "свежий"
					result = false;
				}
			}
		} catch (SQLException ex) {
			logger.error("pkg_Operator.logon() error", ex);
		}
		
		return result;
	}
}
