package com.technology.jep.jepria.server.security.oc4j;

import static com.technology.jep.jepria.server.security.JepSecurityConstant.JEP_SECURITY_MODULE_ATTRIBUTE_NAME;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.security.jazn.oc4j.JAZNUserAdaptor;
import oracle.security.jazn.sso.util.JSSOUtil;

import org.apache.log4j.Logger;

import com.technology.jep.jepcommon.security.JepPrincipal;
import com.technology.jep.jepria.server.security.JepAbstractSecurityModule;
import com.technology.jep.jepria.server.security.JepSecurityModule;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Модуль поддержки безопасности
 */
public class JepSecurityModule_OC4J extends JepAbstractSecurityModule {

	static {
		logger = Logger.getLogger(JepSecurityModule_OC4J.class.getName());	
	}
	
	private JepSecurityModule_OC4J() {
		init();
	}
	
	/**
	 * Возвращает объект типа JepSecurityModule из сессии. Если объект не
	 * найден в сессии или устаревший (например, оставшийся в сессии модуля после logout()),
	 * то создается новый объект и помещается в сессию.
	 * 
	 * @param request запрос, из которого получим сессию
	 * @return объект типа JepSecurityModule из сессии
	 * TODO Попробовать уменьшить размер синхронизируемого кода (synchronized)
	 */
	public static synchronized JepSecurityModule getInstance(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Principal principal = request.getUserPrincipal();
		JepSecurityModule_OC4J securityModule;
		
		logger.trace("getInstance(): principal = " + principal);
		
		if(principal == null) { // Работает гость ?
			securityModule = (JepSecurityModule_OC4J) session.getAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME);
			if(securityModule == null) { // Первый вход ?
				logger.trace("getInstance(): securityModule == null");
				securityModule = new JepSecurityModule_OC4J();
				session.setAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME, securityModule);
				securityModule.doLogonByGuest();
			}
		} else {	// Входили через SSO
			// TODO Разобраться с множественностью сессий
			securityModule = (JepSecurityModule_OC4J) session.getAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME);
			if (securityModule == null || isObsolete(securityModule, principal)) {
				securityModule = new JepSecurityModule_OC4J();
				session.setAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME, securityModule);
				securityModule.updateSubject(principal);
			}
		}
		return securityModule;
	}

	/**
	 * Возвращает идентификатор оператора залогинившегося через JavaSSO. Если
	 * пользователь не логинился через javaSSO, то возвращает null.
	 * 
	 * @param principal пользователь, залогинившийся через javaSSO
	 * @return идентификатор оператора, залогинившегося через JavaSSO
	 */
	public Integer getJepPrincipalOperatorId(Principal principal) {
		Integer result = null;
		JAZNUserAdaptor jaznuser = (JAZNUserAdaptor) principal;
		
		if (jaznuser != null) {
			//Получим параметры и роли пользователя javaSSO.
			Subject subject = jaznuser.getSubject();
			Set<Principal> principals = subject.getPrincipals();
			//Получим идентификатор оператора пользователя залогинившегося через javaSSO.
			for (Principal principal1 : principals) {
				JepPrincipal jepPrincipal = (JepPrincipal) principal1;
				Integer newOperatorId = (Integer)jepPrincipal.getOperatorId();
				if(newOperatorId != null) {
					if(!newOperatorId.equals(this.operatorId)) {	// Обновить свойства, если operatorId изменялся
						updateSubject(jaznuser);	// TODO выпрямить, оптимизировать						
					}
					result = this.operatorId;
				}
			}
		}

		return result;
	}

	public String logout(HttpServletRequest request, HttpServletResponse response) {
		logout_oc4j(request, response);
		return null;
	}

	/**
	 * Обновление субъекта (атрибутов безопасности пользователя: operatorId и ролей) по заданному принципалу (JAZNUserAdaptor),
	 * взятому по request.getUserPrincipal().
	 * Если задан null, то субъект обновляется по атрибутам безопасности гостя. 
	 * 
	 * @param principal принципал
	 */
	protected void updateSubject(Principal principal) {
		logger.trace("BEGIN updateSubject()");
		
		roles = new ArrayList<String>();
		if(principal != null) { // principal есть, значит зашли по Java SSO
			// Извлечение ролей и operatorId из принципала
			JAZNUserAdaptor jaznuser = (JAZNUserAdaptor)principal; 
			Subject subject = jaznuser.getSubject();
			Set<Principal> principals = subject.getPrincipals();
			for (Principal subjectPrincipal : principals) {
				JepPrincipal jepPrincipal = (JepPrincipal) subjectPrincipal;
				Integer _operatorId = jepPrincipal.getOperatorId();
				if(_operatorId != null) {
					this.operatorId = _operatorId;
					this.username = jepPrincipal.getName();
				} else {
					roles.add(jepPrincipal.getName());
				}
			}
		}
		logger.trace("END updateSubject()");
	}

	/**
	 * Проверка "свежести" объекта securityModule, закешированного в Http-сессии
	 * Выполняется на основе сравнения значений operatorId principal-а и объекта jepSecurityModule. 
	 * 
	 * @param securityModule объект типа JepSecurityModule из сессии
	 * @param principal принципал
	 * @return true, если объект jepSecurityModule устарел, иначе - false
	 */
	protected static boolean isObsolete(JepSecurityModule securityModule, Principal principal) {
		boolean result = true;
		JAZNUserAdaptor jaznuser = (JAZNUserAdaptor)principal;
		Subject subject = jaznuser.getSubject();
		if(subject != null) {
			Set<Principal> principals = subject.getPrincipals();
			if(principals.size() - 1 == securityModule.getRoles().size()) {	// Если число ролей не совпадает, значит объект точно "несвежий" 
				for (Principal subjectPrincipal : principals) {
					JepPrincipal jepPrincipal = (JepPrincipal) subjectPrincipal;
					Integer _operatorId = jepPrincipal.getOperatorId();
					if(_operatorId != null) {
						if(_operatorId.equals(securityModule.getOperatorId())) {	// Если operatorID совпадают, значит объект "свежий"
							result = false;
						}
						break;
					}
				}
			}
		}
		
		logger.trace("isObsolete() = " + result);
		return result;
	}

	private void logout_oc4j(HttpServletRequest request, HttpServletResponse response) {
		JAZNUserAdaptor jaznuser = (JAZNUserAdaptor) request.getUserPrincipal();
		if (jaznuser != null) {
			try {
				JSSOUtil.logout(response, null);		// Появляется домашняя страница сервера: "Overview ..."
			} catch (Throwable th) {
				throw new SystemException("Logout error", th);
			}
		}
		
		this.updateSubject(null);
		request.getSession().setAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME, null);
	}
}
