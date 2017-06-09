package com.technology.jep.jepcommon.security.authorization;

import com.technology.jep.jepcommon.security.authentication.AuthenticationHelper;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Фабрика получения провайдера авторизации на основе учетных данных пользователя.
 */
public class AuthorizationHelper {

	/**
	 * Получение нужного провайдера авторизации 
	 * 
	 * @param db				объект подключения к БД
	 * @param login				логин учетной записи
	 * @param password			пароль учетной записи
	 * @param hash				хэш пароля учетной записи
	 * @return провайдер авторизации
	 */
	public static AuthorizationProvider getInstance(Db db, String login, String password, String hash) {
		if (!AuthenticationHelper.checkLogin(login)) {
			throw new IllegalArgumentException("Login should be defined!");
		}
		if (!AuthenticationHelper.checkPasswordAndHash(password, hash)) {
			throw new IllegalArgumentException("Password and hash shouldn't be nullable or defined at the same time!");
		}

		if (JepRiaUtil.isEmpty(password) && JepRiaUtil.isEmpty(hash)) {
			return new LoginAuthorization(db, login);
		} else if (!JepRiaUtil.isEmpty(password)) {
			return new AuthorizationByPassword(db, login, password);
		} else {
			return new AuthorizationByPasswordHash(db, login, hash);
		}
	}

}
