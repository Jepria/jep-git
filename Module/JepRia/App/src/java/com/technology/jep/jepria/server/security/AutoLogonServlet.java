package com.technology.jep.jepria.server.security;

import static com.technology.jep.jepria.server.JepRiaServerConstant.LOGIN_SUFFIX_FOR_HASH_AUTHORIZATION;
import static com.technology.jep.jepria.server.util.JepServerUtil.getServerUrl;
import static java.util.Calendar.SECOND;
import static javax.servlet.http.HttpServletRequest.FORM_AUTH;
import static javax.servlet.http.HttpServletResponse.SC_MOVED_TEMPORARILY;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.security.jazn.sso.SSOCookieToken;
import oracle.security.jazn.sso.SSOTokenFormatException;
import oracle.security.jazn.sso.app.SSOServletConfig;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.shared.util.JepRiaUtil;

public class AutoLogonServlet extends HttpServlet {

	private static final long serialVersionUID = 2098412793923852409L;
	private static final String MAX_LOGIN_ATTEMPTS = "max-login-attempts";
	private static final Logger logger = Logger.getLogger(AutoLogonServlet.class.getName());
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_config = new SSOServletConfig(getServletContext());
		getServletContext().setAttribute("sso.app.config", _config);
		_cfgSessionTimeout = (Integer) _config.getProperty("custom.sso.session.timeout");
		_cfgTokenName = (String) _config.getProperty("custom.sso.cookie.name");
		_cfgTokenDomain = (String) _config.getProperty("custom.sso.cookie.domain");
		_cfgSecureToken = (Boolean) _config.getProperty("custom.sso.cookie.secure");
		_cfgTokenPath = (String) _config.getProperty("custom.sso.cookie.path");
		_cfgErrorUrl = (String) _config.getProperty("custom.sso.url.error");
		_issuerID = getIssuerID();
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		doPost(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse resp)
		throws ServletException, IOException {
		// fetch request parameters for an authorization on server
		String initURL = request.getParameter("url"),
				login = request.getParameter("username"),
					password = request.getParameter("password");
		// all parameters are mandatory
		if (JepRiaUtil.isEmpty(initURL) || JepRiaUtil.isEmpty(login) || JepRiaUtil.isEmpty(password)){
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}	
		// security constraint for requested resource (only within this server)
		String serverURL = getServerUrl(request); 
		if (initURL.startsWith("http://")){
			initURL = new URL(initURL).getFile();
			initURL = serverURL + initURL;
		}
		else {
			initURL = serverURL + (initURL.startsWith("/") ? "" : "/") + initURL;
		}
		// construct url and open connection to specified resource
		URL uri = new URL(initURL);
		
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.connect();
				
		// get the cookie if need, for login
		String cookies = conn.getHeaderField("Set-Cookie");
		if (!JepRiaUtil.isEmpty(cookies)){
			cookies = cookies.split(";", 2)[0];
			String[] keyAndValue = cookies.split("=");
			Cookie cook = new Cookie(keyAndValue[0], keyAndValue[1]);
			cook.setPath("/");
			resp.addCookie(cook);
		}
		
		resp.setHeader("Cookie", cookies);
		
		int port = uri.getPort();
		String httpAddress = uri.getProtocol() + "://" + uri.getHost() + (port == -1 ? "" : (":" + uri.getPort()));
		httpAddress += "/jsso/j_security_check?j_username=" + login + "&j_password=" + password;
		// connect to JavaSSO with credentials: login and password
		uri = new URL(httpAddress);
		conn = (HttpURLConnection) uri.openConnection();
		conn.setInstanceFollowRedirects(false);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		 // Add the cookie information from the first request
		conn.addRequestProperty("Cookie", cookies);
		conn.connect();
		
		HttpSession session = request.getSession(); 
		switch(conn.getResponseCode()){
			case SC_OK: { 
				Integer attempts = (Integer) session.getAttribute(MAX_LOGIN_ATTEMPTS);
				attempts = (attempts == null) ? 1 : (attempts + 1);
				session.setAttribute(MAX_LOGIN_ATTEMPTS, attempts);
				resp.addCookie(deleteCookieToken());
				resp.sendRedirect(resp.encodeRedirectURL(initURL));
				break;
			}
			case SC_MOVED_TEMPORARILY: {
				session.setAttribute(MAX_LOGIN_ATTEMPTS, 1);
				String redirectURL = conn.getHeaderField("location");
				try{ 
					SSOCookieToken ssoCookie = new SSOCookieToken();
					ssoCookie.setAuthType(FORM_AUTH);
					ssoCookie.setIdentity(login.split(LOGIN_SUFFIX_FOR_HASH_AUTHORIZATION)[0]);
					Calendar c = Calendar.getInstance();
					java.util.Date issueTime = c.getTime();
					ssoCookie.setIssueTime(issueTime);
					Integer sessionTimeout = _cfgSessionTimeout;
					c.add(SECOND, sessionTimeout.intValue());
					java.util.Date expireTime = c.getTime();
					ssoCookie.setExpiryTime(expireTime);
					ssoCookie.setIssuerID(_issuerID);
					String encodedSSOCookie = ssoCookie.encode();
					Cookie ssoHttpCookie = createCookieToken(encodedSSOCookie);
					resp.addCookie(ssoHttpCookie);
					logger.trace("redirect url = " + redirectURL);
					resp.sendRedirect(resp.encodeRedirectURL(redirectURL));
				}
				catch(SSOTokenFormatException stfe){
					logger.error(stfe.getStackTrace().toString());
					session.setAttribute(MAX_LOGIN_ATTEMPTS, 1);
					request.setAttribute("javax.servlet.jsp.JspException", stfe);
					String errorPage = _cfgErrorUrl;
					RequestDispatcher rd = request.getRequestDispatcher(errorPage);
					rd.forward(request, resp);
				}
				break;
			}
			case SC_NOT_FOUND : {
				resp.sendRedirect(resp.encodeRedirectURL(initURL)); 
				break;
			}
		}
	}
	
	private Cookie createCookieToken(String cookieValue) {
		logger.trace("createCookieToken(): cookie " + _cfgTokenName + " = " + cookieValue);
		Cookie ssoCookie = new Cookie(_cfgTokenName, cookieValue);
		String domain = _cfgTokenDomain;
		if (null != domain && 0 != domain.length())
			ssoCookie.setDomain(domain);
		ssoCookie.setPath(_cfgTokenPath);
		Boolean secure = _cfgSecureToken;
		ssoCookie.setSecure(secure.booleanValue());
		return ssoCookie;
	}
	
	private Cookie deleteCookieToken() {
		logger.trace("deleteCookieToken()");
		Cookie ssoCookie = new Cookie(_cfgTokenName, null);
		ssoCookie.setMaxAge(0);
		ssoCookie.setPath(_cfgTokenPath);
		return ssoCookie;
	}
	
	private String getIssuerID() {
		String addr = "127.0.0.1";
		try {
			addr = InetAddress.getLocalHost().getCanonicalHostName();
			if (null == addr || 0 == addr.length())
				addr = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "OC4J_" + addr;
	}
	
	private SSOServletConfig _config;
	private Integer _cfgSessionTimeout;
	private String _cfgTokenName;
	private String _cfgErrorUrl;
	private String _cfgTokenDomain;
	private Boolean _cfgSecureToken;
	private String _cfgTokenPath;
	private String _issuerID;
}
