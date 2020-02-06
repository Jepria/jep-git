package com.technology.jep.jepria.server.security;

import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.server.security.module.JepSecurityModule;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;
import org.jepria.oauth.sdk.*;
import org.jepria.ssoutils.JepPrincipal;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.CallableStatement;
import java.sql.SQLException;

import static com.technology.jep.jepria.server.JepRiaServerConstant.*;
import static com.technology.jep.jepria.server.security.JepSecurityConstant.JEP_SECURITY_MODULE_ATTRIBUTE_NAME;
import static org.jepria.oauth.sdk.OAuthConstants.*;

public class OAuthRequestWrapper extends HttpServletRequestWrapper {

  private static Logger logger = Logger.getLogger(OAuthRequestWrapper.class.getName());
  public static final String AUTH_TYPE = "JWT";

  protected HttpServletRequest delegate;
  private String tokenString = null;
  private JepPrincipal principal;
  private Db db;

  public OAuthRequestWrapper(HttpServletRequest request) {
    super(request);
    delegate = request;
  }

  public String getTokenFromRequest() {
    if (tokenString != null) {
      return tokenString;
    }
    Cookie[] cookies = delegate.getCookies();
    for (Cookie cookie: cookies) {
      if (cookie.getName().equalsIgnoreCase(OAUTH_TOKEN)) {
        tokenString = cookie.getValue();
        break;
      }
    }
    if (tokenString == null) {
      String headerText = delegate.getHeader("Authorization");
      if (headerText != null && headerText.startsWith("Bearer")) {
        tokenString = headerText.replaceFirst("Bearer ", "");
      } else {
        tokenString = null;
      }
    }
    return tokenString;
  }

  @Override
  public String getAuthType(){
    return AUTH_TYPE;
  }

  private Db getDb() {
    if (this.db == null) {
      this.db = new Db(DEFAULT_DATA_SOURCE_JNDI_NAME);
    }
    return this.db;
  }
  @Override
  public boolean isUserInRole(String role) {
    logger.trace("BEGIN isUserInRole()");
    JepSecurityModule securityModule = delegate.getSession().getAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME) != null ? (JepSecurityModule) delegate.getSession().getAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME) : null;
    if (securityModule != null && securityModule.isAuthorizedBySso()) {
      return securityModule.getRoles().contains(role);
    } else {
      //language=Oracle
      String sqlQuery =
        "begin ? := pkg_operator.isrole(" +
          "operatorid => ?, " +
          "roleshortname => ?" +
          "); " +
          "end;";
      Db db = getDb();
      Integer result = null;
      try {
        CallableStatement callableStatement = db.prepare(sqlQuery);
        callableStatement.registerOutParameter(1, OracleTypes.INTEGER);
        callableStatement.setInt(2, principal.getOperatorId());
        callableStatement.setString(3, role);
        callableStatement.execute();
        result = new Integer(callableStatement.getInt(1));
        if(callableStatement.wasNull()) result = null;
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        db.closeAll();
      }

      return result != null && result.intValue() == 1;
    }
  }

  @Override
  public Principal getUserPrincipal() {
    return principal;
  }

  /**
   * Request token information form OAuth server
   */
  private TokenInfoResponse getTokenInfo() throws IOException {
    logger.trace("BEGIN getTokenInfo()");
    TokenInfoRequest request = TokenInfoRequest.Builder()
      .resourceURI(URI.create(delegate.getRequestURL().toString().replaceFirst(delegate.getRequestURI(), OAUTH_TOKENINFO_CONTEXT_PATH)))
      .clientId(delegate.getServletContext().getInitParameter(CLIENT_ID_PROPERTY))
      .clientSecret(delegate.getServletContext().getInitParameter(CLIENT_SECRET_PROPERTY))
      .token(tokenString)
      .build();
    TokenInfoResponse response =  request.execute();
    logger.trace("END getTokenInfo()");
    return response;
  }

  @Override
  public boolean authenticate(HttpServletResponse httpServletResponse) {
    logger.trace("BEGIN authenticate()");
    try {
      String tokenString = getTokenFromRequest();
      if (tokenString == null) {
        logger.trace("ERROR authenticate() - token not found");
        return false;
      }
      TokenInfoResponse tokenClaims = getTokenInfo();
      if (tokenClaims != null && tokenClaims.getActive()) {
        String[] userCredentials = tokenClaims.getSub().split(":");
        principal = new JepPrincipal(userCredentials[0], Integer.valueOf(userCredentials[1]));
      } else {
        logger.trace("ERROR authenticate() - token is invalid");
        Cookie[] cookies = delegate.getCookies();
        for (Cookie cookie : cookies) {
          if (cookie.getName().equalsIgnoreCase(OAUTH_TOKEN)) {
            logger.trace("TRACE authenticate() - deleting token cookie");
            Cookie deletedCookie = new Cookie(cookie.getName(), cookie.getValue());
            deletedCookie.setMaxAge(0);
            deletedCookie.setPath("/");
            deletedCookie.setHttpOnly(true);
            httpServletResponse.addCookie(deletedCookie);
          }
        }
        return false;
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return false;
    }
    logger.trace("END authenticate()");
    return principal != null;
  }

  @Override
  public void login(String s, String s1) throws ServletException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void logout() throws ServletException {
    String tokenString = getTokenFromRequest();
    if (tokenString != null) {
      TokenRevocationRequest request = TokenRevocationRequest.Builder()
        .resourceURI(URI.create(delegate.getRequestURL().toString().replaceFirst(delegate.getRequestURI(), OAUTH_TOKENREVOKE_CONTEXT_PATH)))
        .token(tokenString)
        .clientId(delegate.getServletContext().getInitParameter(CLIENT_ID_PROPERTY))
        .clientSecret(delegate.getServletContext().getInitParameter(CLIENT_SECRET_PROPERTY))
        .build();
      try {
        request.execute();
      } catch (IOException e) {
        e.printStackTrace();
        throw new ServletException(e);
      }
    } else {
      delegate.logout();
      return;
    }
  }

}
