package com.technology.jep.jepria.server.security.cas;

import static com.technology.jep.jepria.server.security.JepSecurityConstant.JEP_SECURITY_MODULE_ATTRIBUTE_NAME;

import java.security.Principal;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jasig.cas.client.authentication.AttributePrincipal;

import com.technology.jep.jepcommon.security.pkg_Operator;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.server.security.JepAbstractSecurityModule;
import com.technology.jep.jepria.server.security.JepSecurityModule;
import com.technology.jep.jepria.shared.exceptions.NotImplementedYetException;

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
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		throw new NotImplementedYetException("JepSecurityModule_CAS.logout()");
	}

	@Override
	public Integer getJepPrincipalOperatorId(Principal principal) {
		Integer result = null;
		AttributePrincipal attributePrincipal = (AttributePrincipal)principal;
		String principalName = attributePrincipal.getName();
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
		AttributePrincipal attributePrincipal = (AttributePrincipal)principal;
		String principalName = attributePrincipal.getName();
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
