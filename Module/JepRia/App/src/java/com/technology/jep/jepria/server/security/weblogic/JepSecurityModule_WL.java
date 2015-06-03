package com.technology.jep.jepria.server.security.weblogic;

import static com.technology.jep.jepria.server.security.JepSecurityConstant.JEP_SECURITY_MODULE_ATTRIBUTE_NAME;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import weblogic.security.Security;
import weblogic.security.principal.WLSAbstractPrincipal;
import weblogic.security.principal.WLSGroupImpl;
import weblogic.security.principal.WLSUserImpl;

import com.technology.jep.jepria.server.security.JepAbstractSecurityModule;
import com.technology.jep.jepria.server.security.JepSecurityModule;
import com.technology.jep.jepria.shared.exceptions.NotImplementedYetException;

/**
 * Модуль поддержки безопасности
 */
public class JepSecurityModule_WL extends JepAbstractSecurityModule {
	private static final String JSESSIONID = "JSESSIONID";
	
	/**
	 * Префикс имени служебного принципала, содержащего operatorId
	 */
	private static final String OPERATOR_ID_PREFIX = "OPERATORID_";

	static {
		logger = Logger.getLogger(JepSecurityModule_WL.class.getName());	
	}

	private JepSecurityModule_WL() {
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
		JepSecurityModule_WL securityModule;
		
		logger.trace("getInstance(): principal = " + principal);
		
		if(principal == null) { // Работает гость ?
			securityModule = (JepSecurityModule_WL) session.getAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME);
			if(securityModule == null) { // Первый вход ?
				logger.trace("getInstance(): securityModule == null");
				securityModule = new JepSecurityModule_WL();
				session.setAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME, securityModule);
				securityModule.doLogonByGuest();
			}
		} else {	// Входили через SSO
			// TODO Разобраться с множественностью сессий
			securityModule = (JepSecurityModule_WL) session.getAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME);
			if (securityModule == null || isObsolete(securityModule, principal)) {
				securityModule = new JepSecurityModule_WL();
				session.setAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME, securityModule);
				securityModule.updateSubject(principal);
			}
		}
		return securityModule;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Logout start");
		
		weblogic.servlet.security.ServletAuthentication.killCookie(request);
		
		deleteSessionCookies(request, response); // Без этого сессионные куки по факту не удаляются

		logger.info("Logout end");
	}
	
	private void deleteSessionCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
		    for (Cookie cookie : cookies) {
		        if (cookie.getName().equals(JSESSIONID)) { // Исходим из того (по факту так и есть), что мы видим только свои куки (из своего домена)
		            	cookie.setPath("/");	// Без этого нужные куки не удаляются
			            cookie.setMaxAge(0);
			            response.addCookie(cookie);
		        }
		    }
		}
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
			Subject subject = weblogic.security.Security.getCurrentSubject();
			Set<Principal> principals = subject.getPrincipals();
			for (Principal subjectPrincipal : principals) {
				WLSAbstractPrincipal wlsAbstractPrincipal = (WLSAbstractPrincipal) subjectPrincipal;
				if(wlsAbstractPrincipal instanceof WLSUserImpl) {
					WLSUserImpl wlsPrincipal = (WLSUserImpl) subjectPrincipal;
					String prName = wlsPrincipal.getName();
					this.username = prName;
				} else if(wlsAbstractPrincipal instanceof WLSGroupImpl) {
					WLSGroupImpl wlsGroup = (WLSGroupImpl) subjectPrincipal;
					String prName = wlsGroup.getName();
					Integer _operatorId = extractOperatorId(prName);
					if(_operatorId != null) {
						operatorId = _operatorId;
					} else {
						roles.add(prName);
					}
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
	private static boolean isObsolete(JepSecurityModule_WL securityModule, Principal principal) {
		boolean result = true;
		
		Subject subject = Security.getCurrentSubject();
		Set<Principal> principals = subject.getPrincipals();
		
		if(principals.size() - 2 == securityModule.roles.size()) {	// Если число ролей не совпадает (с поправкой на один WLSUser и operatorId), значит объект точно "несвежий" 
			for(Principal subjectPrincipal : principals) {
				if(subjectPrincipal instanceof WLSGroupImpl) {
					WLSGroupImpl wlsUser = (WLSGroupImpl) subjectPrincipal;
					String prName = wlsUser.getName();
					Integer _operatorId = extractOperatorId(prName);
					if(_operatorId != null) {
						if(_operatorId.equals(securityModule.operatorId)) {	// Если operatorID совпадают, значит объект "свежий"
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

	private static Integer extractOperatorId(String principalName) {
		Integer result = null;
		if(principalName.startsWith(OPERATOR_ID_PREFIX)) {
			String strResult = principalName.substring(OPERATOR_ID_PREFIX.length());
			result = Integer.parseInt(strResult);
		}
		
		return result;
	}

	@Override
	public Integer getJepPrincipalOperatorId(Principal principal) {
		throw new NotImplementedYetException();
	}
}
