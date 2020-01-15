package com.technology.jep.jepria.server.security;

import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.server.security.module.JepSecurityModule;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;
import org.jepria.oauth.sdk.*;
import org.jepria.oauth.sdk.util.URIUtil;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static com.technology.jep.jepria.server.JepRiaServerConstant.DEFAULT_DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepria.server.security.JepSecurityConstant.JEP_SECURITY_MODULE_ATTRIBUTE_NAME;
import static org.jepria.oauth.sdk.OAuthConstants.*;

public class TokenRequestWrapper extends HttpServletRequestWrapper {

  private static Logger logger = Logger.getLogger(TokenRequestWrapper.class.getName());
  public static final String AUTH_TYPE = "JWT";

  protected HttpServletRequest delegate;
  private String tokenString = null;
  private JepPrincipal principal;
  private Db db;

  public TokenRequestWrapper(HttpServletRequest request) {
    super(request);
    delegate = request;
  }

  public String getTokenFromRequest() {
    if (tokenString != null) {
      return tokenString;
    }
    Cookie[] cookies = delegate.getCookies();
    for (Cookie cookie: cookies) {
      if (cookie.getName().equalsIgnoreCase(RFI_OAUTH_TOKEN)) {
        tokenString = cookie.getValue();
        break;
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

  @Deprecated
  public boolean isUserInRoles(String[] roles) {
    logger.trace("BEGIN isUserInRoles(" + roles + ")");
    JepSecurityModule securityModule = delegate.getSession().getAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME) != null ? (JepSecurityModule) delegate.getSession().getAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME) : null;
    if (securityModule != null && securityModule.isAuthorizedBySso()) {
      return Arrays.asList(roles).stream().anyMatch(role -> securityModule.getRoles().contains(role));
    } else {
      String rolesString = "";
      for (String role : roles) {
        rolesString += "'" + role + "',";
      }
      if (rolesString.length() > 0) {
        rolesString = rolesString.trim().substring(0, rolesString.length() - 1);
      }
      //language=Oracle
      String sqlQuery = "select decode(count(1), 0, 0, 1)" +
        " from op_role opr" +
        " inner join v_op_operator_role vopr" +
        " on vopr.role_id = opr.role_id" +
        " and vopr.operator_id = " + principal.getOperatorId() +
        " and opr.SHORT_NAME in (" + rolesString + ")";

      //Добавили индекс role_id + short_name в op_role т.к. больше никаких данных не используется, на выходе получим FAST INDEX FULL SCAN
      Integer result = null;
      try {
        CallableStatement callableStatement = getDb().prepare(sqlQuery);

        ResultSet resultSet = callableStatement.executeQuery();
        if (resultSet.next()) {
          result = new Integer(resultSet.getInt(1));
        }
        if (callableStatement.wasNull()) result = null;
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        db.closeStatement(sqlQuery);
      }

      logger.trace("END isUserInRole()");
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
    TokenInfoRequest request = new TokenInfoRequest.Builder()
      .resourceURI(URI.create(delegate.getRequestURL().toString().replaceFirst(delegate.getRequestURI(), OAUTH_TOKENINFO_CONTEXT_PATH)))
      .clientId(delegate.getServletContext().getInitParameter(CLIENT_ID_PROPERTY))
      .clientSecret(delegate.getServletContext().getInitParameter(CLIENT_SECRET_PROPERTY))
      .token(tokenString)
      .build();
    TokenInfoResponse response =  request.execute();
    logger.trace("END getTokenInfo()");
    return response;
  }

  private void buildAuthorizationRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    try {
      /**
       * Create state param and save it to Cookie for checking in future to prevent 'Replay attacks'
       */
      State state = new State();
      Cookie stateCookie = new Cookie(RFI_OAUTH_CSRF_TOKEN, state.toString());
      stateCookie.setPath("/");
      stateCookie.setHttpOnly(true);
      httpServletResponse.addCookie(stateCookie);

      String authorizationRequestURI = new AuthorizationRequest.Builder()
        .resourceURI(URI.create(httpServletRequest.getRequestURL().toString().replaceFirst(httpServletRequest.getRequestURI(), OAUTH_AUTHORIZATION_CONTEXT_PATH)))
        .responseType(ResponseType.AUTHORIZATION_CODE)
        .clientId(httpServletRequest.getServletContext().getInitParameter(CLIENT_ID_PROPERTY))
        .redirectionURI(URI.create(URIUtil.removeQueryParameter(httpServletRequest.getRequestURL().toString() + "?" + httpServletRequest.getQueryString(), CODE, STATE)))
        .state(state)
        .build()
        .toString();

      httpServletResponse.sendRedirect(authorizationRequestURI);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public boolean authorize() throws IOException {
    String tokenString = getTokenFromRequest();
    if (tokenString != null) {
      TokenInfoResponse tokenClaims = getTokenInfo();
      if (tokenClaims != null && tokenClaims.getActive()) {
        String[] userCredentials = tokenClaims.getSub().split(":");
        principal = new JepPrincipal(userCredentials[0], Integer.valueOf(userCredentials[1]));
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException {
    logger.trace("BEGIN authenticate()");
    try {
      String tokenString = getTokenFromRequest();
      if (tokenString == null) {
        logger.trace("ERROR authenticate() - token not found, redirecting to OAuth Server");
        buildAuthorizationRequest(delegate, httpServletResponse);
        return false;
      }
      if (!authorize()) {
        logger.trace("ERROR authenticate() - Токен не валиден");
        Cookie[] cookies = delegate.getCookies();
        for (Cookie cookie: cookies) {
          if (cookie.getName().equalsIgnoreCase(RFI_OAUTH_TOKEN)) {
            logger.trace("TRACE authenticate() - delete cookie");
            Cookie deletedCookie = new Cookie(cookie.getName(), cookie.getValue());
            deletedCookie.setMaxAge(0);
            deletedCookie.setPath("/");
            deletedCookie.setHttpOnly(true);
            httpServletResponse.addCookie(deletedCookie);
          }
        }
        buildAuthorizationRequest(delegate, httpServletResponse);
        return false;
      }
    } catch (Throwable e) {
      e.printStackTrace();
      buildAuthorizationRequest(delegate, httpServletResponse);
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
      TokenRevocationRequest request = new TokenRevocationRequest.Builder()
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
