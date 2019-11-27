package com.technology.jep.jepria.server.security.servlet;

import static com.technology.jep.jepria.server.JepRiaServerConstant.LOGIN_SUFFIX_FOR_HASH_AUTHORIZATION;
import static com.technology.jep.jepria.shared.util.JepRiaUtil.isEmpty;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.technology.jep.jepria.server.util.JepServerUtil;

/**
 * GET-параметры запроса:<br>
 * <br>
 * username<br>
 * password<br>
 * url - либо полный URL в виде <code>http://host[:port]/resource?query</code>, либо только часть относительно сервера <code>/resource?query</code><br>  
 */
public class AutoLogonServlet extends HttpServlet {

  private static final long serialVersionUID = 209841279392385240L;
  
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
    if (isEmpty(login) || isEmpty(password)){
      resp.setStatus(SC_BAD_REQUEST);
      return;
    }  
    
    // security constraint for requested resource (only within this server)
    String serverURL = getServerUrl(request);
    if (initURL != null) {
      try {
        URL url = new URL(initURL);
        initURL = serverURL + url.getFile();
      } catch (MalformedURLException e) {
        initURL = serverURL + (initURL.startsWith("/") ? "" : "/") + initURL;
      }
    }
    
    // if the user was authorized, execute logout before login procedure
    String pureLogin = login != null && login.contains(LOGIN_SUFFIX_FOR_HASH_AUTHORIZATION) ? login.split(LOGIN_SUFFIX_FOR_HASH_AUTHORIZATION)[0] : login;
    if (request.getUserPrincipal() == null
        || (request.getUserPrincipal() != null && !request.getUserPrincipal().getName().equalsIgnoreCase(pureLogin))) {
      try {
        request.getSession().invalidate();
        request.logout();
        request.login(login, password);
      } catch (ServletException e) {
        e.printStackTrace();
        resp.sendError(SC_UNAUTHORIZED, e.getMessage());
        return;
      }
    }

    if (initURL != null) {
      resp.sendRedirect(resp.encodeRedirectURL(initURL));
    } else {
      resp.setStatus(200);
    }
  }
  
  /**
   * Копия метода {@link JepServerUtil#getServerUrl} для обеспечения автономности
   * данного класса от JepServerUtil
   */
  public static String getServerUrl(HttpServletRequest request) {
    return request.getScheme().toLowerCase()
        + "://"
        + request.getServerName()
        + ":"
        + request.getServerPort();
  }
}

