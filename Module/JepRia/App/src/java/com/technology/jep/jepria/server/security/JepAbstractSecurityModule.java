package com.technology.jep.jepria.server.security;

import static com.technology.jep.jepria.server.JepRiaServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepria.server.security.JepSecurityConstant.GUEST_LOGIN;
import static com.technology.jep.jepria.server.security.JepSecurityConstant.GUEST_PASSWORD;
import static com.technology.jep.jepria.server.security.JepSecurityConstant.JEP_SECURITY_MODULE_ATTRIBUTE_NAME;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.log4j.Logger;

import com.technology.jep.jepcommon.security.pkg_Operator;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Модуль поддержки безопасности
 */
public abstract class JepAbstractSecurityModule implements JepSecurityModule {
	protected static Logger logger;
	
	protected Db db = null;
	protected List<String> roles = new ArrayList<String>();
	protected Integer operatorId = null;
	protected String username;

	/*
	 * Кеширование данных гостя.
	 */
	private static List<String> guestRoles = new ArrayList<String>();
	private static Integer guestOperatorId = null;
	private static long guestCacheTime = 0;
	private static final int GUEST_REFRESH_TIME_DEFAULT = 900;		// 15 минут.

	protected void init() {
		db = new Db(DATA_SOURCE_JNDI_NAME);
	}

	abstract protected void updateSubject(Principal principal);
	
	/**
	 * Определяет: принадлежит ли указанная роль role текущему оператору. Если
	 * параметр makeError установлен в true, то при отсутствии роли у текущего
	 * оператора выбрасывается исключение.
	 * 
	 * @param role проверяемая роль
	 * @param makeError признак: выбрасывать исключение (значение true) или нет (значение false)
	 * @return true - если текущему оператору принадлежит роль role, false или исключение
	 * (в зависимости от параметра makeError) в противном случае
	 * @throws Exception
	 */
	@Override
	public boolean isRole(String role, boolean makeError) throws Exception {
		boolean include = roles.contains(role);
		if (include)
			return true;
		else if (makeError)
			throw new ApplicationException("You haven't enough rights to perform this operation (RoleId = " + role + ")", null);
		return false;
	}

	/**
	 * Возвращает идентификатор текущего пользователя.
	 */
	public Integer getOperatorId() {
		return operatorId;
	}
	
	/**
	 * Возвращает имя (username) текущего пользователя.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Возвращает роли текущего пользователя.
	 */
	public List<String> getRoles() {
		return roles;
	}

	public void valueBound(HttpSessionBindingEvent bindingEvent) {
		if(JEP_SECURITY_MODULE_ATTRIBUTE_NAME.equals(bindingEvent.getName())) {
			onStartSession(bindingEvent.getSession().getId());
		}
	}

	/*
	 * Оставлено для подстраховочной очистки ресурсов (закрытия соединений с БД)
	 */
	public void valueUnbound(HttpSessionBindingEvent bindingEvent) {
		if(JEP_SECURITY_MODULE_ATTRIBUTE_NAME.equals(bindingEvent.getName())) {
			onExpiredSession(bindingEvent.getSession().getId());
		}
	}

	/**
	 * Возвращает идентификатор текущего пользователя.
	 */
	public Integer getCurrentUserId() {
		return operatorId;
	}

	protected void doLogonByGuest() {
		logger.trace("doLogonByGuest()");

		long currentTime = System.currentTimeMillis();
		long age = (currentTime - guestCacheTime) / 1000;	// Сколько секунд находится в кэше.

		try {
			if(age > GUEST_REFRESH_TIME_DEFAULT) {
				logger.trace("doLogonByGuest(): age > GUEST_REFRESH_TIME_DEFAULT");
				guestOperatorId = pkg_Operator.logon(db, GUEST_LOGIN, GUEST_PASSWORD);
				guestRoles = pkg_Operator.getRoles(db, guestOperatorId);
				guestCacheTime = System.currentTimeMillis();
			}
			
			operatorId = guestOperatorId;
			roles = guestRoles;
			
		} catch (SQLException e) {
			throw new SystemException("pkg_Operator.logon() ERROR", e);
		} finally {
			db.closeAll();
		}
	}

	private void onStartSession(String sessionId) {
		logger.trace("onStartSession(): sessionId = " + sessionId);
	}

	private void onExpiredSession(String sessionId) {
		logger.trace("onExpiredSession(): sessionId = " + sessionId);
		db.closeAll();
	}
}
