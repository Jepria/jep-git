package com.technology.jep.jepcommon.security.authorization;

import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

public class AuthorizationHelper {

	public static AuthorizationProvider getInstance(Db db, String login, String password, String hash) {
		if (JepRiaUtil.isEmpty(login)) {
			throw new IllegalArgumentException("Login should be defined!");
		}
		if (!JepRiaUtil.isEmpty(password) && !JepRiaUtil.isEmpty(hash)) {
			throw new IllegalArgumentException("Password and hash shouldn't be defined at the same time!");
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
