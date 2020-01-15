package org.jepria.server.service.security;

import com.technology.jep.jepcommon.security.pkg_Operator;
import com.technology.jep.jepria.server.db.Db;
import oracle.jdbc.OracleTypes;
import org.glassfish.jersey.server.model.AnnotatedMethod;
import org.jepria.server.service.rest.MetaInfoResource;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.Principal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Base64;

import static com.technology.jep.jepria.server.JepRiaServerConstant.DEFAULT_DATA_SOURCE_JNDI_NAME;
import static org.jepria.server.service.security.HttpBasic.PASSWORD;
import static org.jepria.server.service.security.HttpBasic.PASSWORD_HASH;

/**
 * Dynamic feature MUST be registered in <i>ApplicationConfig</i> for usage of <b>@HttpBasic</b> annotation.<br/>
 * Provides <b>Http Basic Authentication</b> filter for JAX-RS Adapters.<br/>
 * <b>@HttpBasic</b> annotation MUST be configured with PASSWORD/PASSWORD_HASH value for properly usage.
 */
public class HttpBasicDynamicFeature implements DynamicFeature {

  protected Db getDb() {
    return new Db(DEFAULT_DATA_SOURCE_JNDI_NAME);
  }

  @Override
  public void configure(ResourceInfo resourceInfo, FeatureContext context) {
    final AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());
    // HttpBasic annotation on the method
    HttpBasic resourceAnnotation = resourceInfo.getResourceClass().getAnnotation(HttpBasic.class);
    HttpBasic methodAnnotation = am.getAnnotation(HttpBasic.class);
    if (resourceAnnotation != null) {
      context.register(new HttpBasicContainerRequestFilter(resourceAnnotation.passwordType()));
      return;
    } else if (methodAnnotation != null) {
      context.register(new HttpBasicContainerRequestFilter(methodAnnotation.passwordType()));
      return;
    } else if (MetaInfoResource.class.equals(resourceInfo.getResourceClass())) {
      // регистрируем фильтр для ресурса MetaInfoResource так, как будто на нём есть аннотация @HttpBasic

      // TODO
      // создать аннотацию вроде @Protected, которая будет работать аналогично аннотации @HttpBasic, с той лишь разницей, что
      // @Protected не зависит от метода аутентификации (HttpBasic, OAuth и т.д.).
      // @Protected просто говорит о том, что ресурс защищён (неважно каким образом).
      // Далее ресурс MetaInfoResource можно пометить такой аннотацией и убрать его регистрацию отсюда

      context.register(new HttpBasicContainerRequestFilter(PASSWORD));
    }
  }

  @Priority(Priorities.AUTHENTICATION)
  public final class HttpBasicContainerRequestFilter  implements ContainerRequestFilter {

    @Context
    HttpServletRequest request;
    String passwordType;

    public HttpBasicContainerRequestFilter(String passwordType) {
      if (passwordType == null || !(PASSWORD.equals(passwordType) || PASSWORD_HASH.equals(passwordType))) {
        throw new IllegalArgumentException("Password type MUST be in (PASSWORD, PASSWORD_HASH)");
      }
      this.passwordType = passwordType;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
      String authString = requestContext.getHeaderString("authorization");
      if (authString == null) {
        throw new WebApplicationException(
          Response.status(Response.Status.UNAUTHORIZED)
            .header(HttpHeaders.WWW_AUTHENTICATE, "Basic").build());
      }
      authString = authString.replaceFirst("[Bb]asic ", "");
      String[] credentials = new String(Base64.getDecoder().decode(authString)).split(":");
      Db db = getDb();
      try {
        Integer operatorId = null;
        if (PASSWORD.equals(passwordType)) {
          operatorId = pkg_Operator.logon(db, credentials[0], credentials[1], null);
        } else {
          operatorId = pkg_Operator.logon(db, credentials[0], null, credentials[1]);
        }
        requestContext.setSecurityContext(new JerseySecurityContext(credentials[0], operatorId));
      } catch (SQLException e) {
        throw new WebApplicationException(
          Response.status(Response.Status.UNAUTHORIZED)
            .header(HttpHeaders.WWW_AUTHENTICATE, "Basic").build());
      } finally {
        db.closeAll();
      }
    }

    final class JerseySecurityContext implements javax.ws.rs.core.SecurityContext {

      private final String username;
      private final Integer operatorId;

      public JerseySecurityContext(String username, Integer operatorId) {
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
        return "BASIC";
      }

      @Override
      public boolean isSecure() {
        return false;
      }
    }
  }
}
