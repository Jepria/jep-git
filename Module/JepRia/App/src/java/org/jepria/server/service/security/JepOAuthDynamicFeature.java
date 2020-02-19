package org.jepria.server.service.security;

import com.technology.jep.jepria.server.db.Db;
import oracle.jdbc.OracleTypes;
import org.glassfish.jersey.server.model.AnnotatedMethod;
import org.jepria.oauth.sdk.TokenInfoResponse;
import org.jepria.oauth.sdk.jaxrs.OAuthContainerRequestFilter;
import org.jepria.server.env.EnvironmentPropertySupport;
import org.jepria.server.service.rest.MetaInfoResource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.FeatureContext;

import java.io.IOException;
import java.security.Principal;
import java.sql.CallableStatement;
import java.sql.SQLException;

import static com.technology.jep.jepria.server.JepRiaServerConstant.DEFAULT_DATA_SOURCE_JNDI_NAME;
import static org.jepria.oauth.sdk.OAuthConstants.CLIENT_ID_PROPERTY;
import static org.jepria.oauth.sdk.OAuthConstants.CLIENT_SECRET_PROPERTY;

public class JepOAuthDynamicFeature implements DynamicFeature {
  @Override
  public void configure(ResourceInfo resourceInfo, FeatureContext context) {

    final AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());
    // HttpBasic annotation on the method
    OAuth resourceAnnotation = resourceInfo.getResourceClass().getAnnotation(OAuth.class);
    OAuth methodAnnotation = am.getAnnotation(OAuth.class);
    if (resourceAnnotation != null) {
      context.register(new JepOAuthContainerRequestFilter());
      return;
    } else if (methodAnnotation != null) {
      context.register(new JepOAuthContainerRequestFilter());
      return;
    } else if (MetaInfoResource.class.equals(resourceInfo.getResourceClass())) {
      // регистрируем фильтр для ресурса MetaInfoResource так, как будто на нём есть аннотация @OAuth

      // TODO
      // создать аннотацию вроде @Protected, которая будет работать аналогично аннотации @HttpBasic, с той лишь разницей, что
      // @Protected не зависит от метода аутентификации (HttpBasic, OAuth и т.д.).
      // @Protected просто говорит о том, что ресурс защищён (неважно каким образом).
      // Далее ресурс MetaInfoResource можно пометить такой аннотацией и убрать его регистрацию отсюда

      context.register(new JepOAuthContainerRequestFilter());
    }
  }

  public static class JepOAuthContainerRequestFilter extends OAuthContainerRequestFilter {

    public static final String AUTHENTICATION_SCHEME = "BEARER";

    @Context
    HttpServletRequest request;
    private String moduleName;

    @Override
    protected SecurityContext getSecurityContext(TokenInfoResponse tokenInfo) {
      String[] credentials = tokenInfo.getSub().split(":");
      return new SecurityContext(credentials[0], Integer.valueOf(credentials[1]));
    }

    @Override
    protected String getClientSecret() {
      return EnvironmentPropertySupport.getInstance(request).getProperty(moduleName + "/" + CLIENT_ID_PROPERTY);
    }

    @Override
    protected String getClientId() {
      return EnvironmentPropertySupport.getInstance(request).getProperty(moduleName + "/" + CLIENT_SECRET_PROPERTY);
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
      moduleName = request.getServletContext().getContextPath().replaceFirst("/", "");
      super.filter(containerRequestContext);
    }
  }

  private static final class SecurityContext implements javax.ws.rs.core.SecurityContext {

    private Db getDb() {
      return new Db(DEFAULT_DATA_SOURCE_JNDI_NAME);
    }

    private final String username;
    private final Integer operatorId;

    public SecurityContext(String username, Integer operatorId) {
      this.username = username;
      this.operatorId = operatorId;
    }

    @Override
    public boolean isUserInRole(final String roleName) {
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
        callableStatement.setInt(2, operatorId);
        callableStatement.setString(3, roleName);
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

    @Override
    public Principal getUserPrincipal() {
      return new PrincipalImpl(username, operatorId);
    }

    @Override
    public String getAuthenticationScheme() {
      return JepOAuthContainerRequestFilter.AUTHENTICATION_SCHEME;
    }

    @Override
    public boolean isSecure() {
      return false;
    }
  }
}
