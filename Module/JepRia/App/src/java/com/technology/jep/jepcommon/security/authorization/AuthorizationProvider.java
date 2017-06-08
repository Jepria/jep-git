package com.technology.jep.jepcommon.security.authorization;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import com.technology.jep.jepcommon.security.pkg_Operator;

public abstract class AuthorizationProvider {
	
	protected static Logger logger = Logger.getLogger(AuthorizationProvider.class.getName());  
	
	abstract public Integer logon() throws SQLException;
}
