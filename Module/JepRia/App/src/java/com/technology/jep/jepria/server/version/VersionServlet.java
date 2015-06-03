package com.technology.jep.jepria.server.version;

import static com.technology.jep.jepria.server.JepRiaServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepria.server.JepRiaServerConstant.VERSION_BEAN_JNDI_NAME;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.technology.jep.jepcommon.security.pkg_Operator;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.server.util.JepServerUtil;
import com.technology.jep.jepria.server.version.ejb.JepVersion;
import com.technology.jep.jepria.shared.exceptions.SystemException;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

public class VersionServlet extends HttpServlet {
	protected static Logger logger = Logger.getLogger(VersionServlet.class.getName());
		
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		createAppInstallResult(request, response);
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		doPost(request, response);
	}
	
	private void createAppInstallResult(HttpServletRequest request, HttpServletResponse response) {
		Db db = null;
		Integer operatorId = null, appInstallResultId = null;
		
		String svnRoot = request.getParameter("svnRoot");
		String initPath = request.getParameter("initPath");
		String modVersion = request.getParameter("modVersion");
		String instVersion = request.getParameter("instVersion");
		String deployPath = request.getParameter("deployPath");		
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		try {
			login = decode(login);
			password = decode(password);
		}
		catch (UnsupportedEncodingException ignore){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			String message = "Wrong charset for login/password";
			logger.error(message, ignore);
			ignore.printStackTrace();
		}
		
		logger.trace("BEGIN createAppInstallResult(" + svnRoot + ", " + initPath + ", " + modVersion + ", " + instVersion + ", " + deployPath + ", " + login + ", " + password + ")");
		try {		
			if (!JepRiaUtil.isEmpty(svnRoot) &&
					!JepRiaUtil.isEmpty(initPath) &&
						!JepRiaUtil.isEmpty(modVersion) &&
							!JepRiaUtil.isEmpty(instVersion) &&
								!JepRiaUtil.isEmpty(deployPath) &&
									!JepRiaUtil.isEmpty(login) &&
										!JepRiaUtil.isEmpty(password)){
				db = new Db(DATA_SOURCE_JNDI_NAME);
				// Попытка получить идентификатор пользователя.
				try{
					operatorId = pkg_Operator.logon(db, login, password);
				}
				catch(Exception ignore){
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					String message = "Wrong login/password";
					logger.error(message, ignore);
					response.getWriter().println("<html><body><p>" + message + "</p></body></html>");
					ignore.printStackTrace();
					return;
				}				
								
				JepVersion ejb = (JepVersion) JepServerUtil.ejbLookup(VERSION_BEAN_JNDI_NAME);
				// Cоздаем запись об устанавливаемом модуле.
				appInstallResultId = ejb.createAppInstallResult(
						svnRoot
						, initPath
						, modVersion
						, deployPath
						, instVersion
						, null // В базе передастся sysdate.
						, operatorId);
			}
		} catch (Throwable th) {
			String message = "Create Application Version error";
			logger.error(message, th);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			throw new SystemException(message, th);
		}
		finally{
			if (!JepRiaUtil.isEmpty(db))
				db.closeAll();
		}
		
		logger.trace("END createAppInstallResult was succeeded : ID = " + appInstallResultId + "(" + svnRoot + ", " + initPath + ", " + modVersion + ", " + instVersion + ", " + deployPath+ ", " + login + ", " + password + ")");
	}
	
	/**
	 * Декодирование строки.
	 * 
	 * @param decodeString	декодируемая строка
	 * @return раскодированная строка
	 * @throws UnsupportedEncodingException
	 */
	private String decode(String decodeString) 
		throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();
		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < decodeString.length() - 1; i += 2) {
			// grab the hex in pairs
			String output = decodeString.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);
			temp.append(decimal);
		}
		return URLDecoder.decode(sb.toString(), "UTF-8");
	}
}