package com.technology.jep.jepria.server.security;

import com.technology.jep.jepcommon.security.pkg_Operator;
import com.technology.jep.jepria.server.db.Db;
import org.apache.log4j.Logger;
import org.jepria.jwt.token.TokenImpl;
import org.jepria.jwt.token.VerifierRSA;
import org.jepria.jwt.token.interfaces.Token;
import org.jepria.jwt.token.interfaces.Verifier;
import org.jepria.ssoutils.JepPrincipal;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static com.technology.jep.jepria.server.JepRiaServerConstant.DEFAULT_DATA_SOURCE_JNDI_NAME;

public class TokenRequestWrapper extends HttpServletRequestWrapper {

  private static Logger logger = Logger.getLogger(TokenRequestWrapper.class.getName());
  public static final String RFI_OAUTH_TOKEN = "RFI_OAUTH_TOKEN";
  public static final String AUTH_TYPE = "TOKEN";

  protected HttpServletRequest delegate;
  private
  String tokenString = null;
  private JepPrincipal principal;
  private Db db;

  public TokenRequestWrapper(HttpServletRequest request) {
    super(request);
    delegate = request;
  }

  public static String getTokenFromRequest(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie: cookies) {
      if (cookie.getName().equalsIgnoreCase(RFI_OAUTH_TOKEN)) {
        return cookie.getValue();
      }
    }
    return null;
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
  public boolean isUserInRole(String s) {
    logger.trace("BEGIN isUserInRole()");
    //language=Oracle
    String sqlQuery = "select decode(count(1), 0, 0, 1)" +
                      " from op_role opr" +
                        " inner join v_op_operator_role vopr" +
                          " on vopr.role_id = opr.role_id" +
                            " and vopr.operator_id = " + principal.getOperatorId() +
                            " and opr.SHORT_NAME in (" + s + ")";

    //Добавили индекс role_id + short_name в op_role т.к. больше никаких данных не используется, на выходе получим FAST INDEX FULL SCAN
    Integer result = null;
    try {
      CallableStatement callableStatement = getDb().prepare(sqlQuery);

      ResultSet resultSet = callableStatement.executeQuery();
      if (resultSet.next()) {
        result = new Integer(resultSet.getInt(1));
      }
      if(callableStatement.wasNull()) result = null;
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      db.closeStatement(sqlQuery);
    }

    logger.trace("END isUserInRole()");
    return result != null && result.intValue() == 1;
  }

  @Override
  public Principal getUserPrincipal() {
    return principal;
  }

  @Override
  public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException {
    logger.trace("BEGIN authenticate()");
    try {
      ServletContext servletContext = delegate.getServletContext();
      tokenString = getTokenFromRequest(delegate);
      if (tokenString == null) {
        httpServletResponse.sendError(401, "Токен не найден");
        return false;
      }
      Token token = TokenImpl.parseFromString(tokenString);
      Verifier verifier = new VerifierRSA(null,//TODO сделать нормальную реализацию
        Collections.singletonList("JRFeature"),
        "http://msk-dit-20507-1/auth/jwt",
        new Date(),
        servletContext.getInitParameter("org.jepria.auth.jwt.PublicKey"));
      if (verifier.verify(token)) {
        String[] userCredentials = token.getSubject().split(":");
        principal = new JepPrincipal(userCredentials[0], 132846);//Integer.valueOf(userCredentials[1]); TODO
      } else {
        logger.trace("ERROR authenticate() - Токен не валиден");
        httpServletResponse.sendError(401, "Токен не валиден");
        return false;
      }
    } catch (ParseException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
      httpServletResponse.sendError(401, "Токен не валиден\n" + e.getLocalizedMessage() + "\n" + e.getStackTrace());
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
    throw new UnsupportedOperationException();
  }
}
