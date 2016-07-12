package com.technology.jep.jepria.server.security.cas;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Workaround для Login WebLogic.
 * Решает проблему перехода на умолчательный URL вместо целевого после прохода через аутентфикации на Login-странице
 * Фильтр, перехватывающий первый запрос к jsp и выполняющий перенаправление по URL, содержащемуся в cookie с именем ContextRoot + "_" + TARGET_URL'.
 * Cookie создаётся в Login.jsp WebLogic-приложения.
 * 
 * TODO Избавиться от Workaround-а.
 */
public class LoginRedirectFilter implements Filter {

  public static final String TARGET_URL_COOKIE_NAME = "targetUrl";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
    String targetUrl = getTargetUrl(request);
      clearTargetUrl(request, response); // TODO Разобраться с времянкой
    if(targetUrl != null) {
//        clearTargetUrl(request, response);
        response.sendRedirect(targetUrl);
    } else {
        chain.doFilter(request, response);
    }
  }

  private void clearTargetUrl(HttpServletRequest request, HttpServletResponse response) {
    deleteCookie(getTargetUrlCookieName(request), response);
  }

  private String getTargetUrl(HttpServletRequest request)  throws UnsupportedEncodingException {
    String targetUrl = null;
    Cookie targetUrlCookie = getCookie(request, getTargetUrlCookieName(request));
    if(targetUrlCookie != null) {
      String encodedTargetUrl = targetUrlCookie.getValue();
      if("null".equals(encodedTargetUrl)) {
        targetUrl = null;
      } else {
        targetUrl = URLDecoder.decode(encodedTargetUrl, "UTF-8");
      }
    }
    
    return targetUrl;
  }

  @Override
  public void destroy() {
  }

  private static Cookie getCookie(HttpServletRequest request, String cookieName) {
    Cookie result = null;
    if(cookieName != null) {
      Cookie[] cookies = request.getCookies();
      if(cookies != null) {
        for(int i = 0; i < cookies.length; i++) {
          String ckName = cookies[i].getName();
          String path = cookies[i].getPath();
          if((path == null || "/".equals(path)) && cookieName.equals(ckName)) {
            result = cookies[i];
            break;
          }
        }
      }
    }
    
    return result;
  }

  private void deleteCookie(String cookieName, HttpServletResponse response) {
    Cookie cookie = new Cookie(cookieName, "toDelete");
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);
  }

  private String getTargetUrlCookieName(HttpServletRequest request) {
    String contextRoot = request.getContextPath().substring(1);
    return contextRoot + "_" + TARGET_URL_COOKIE_NAME;
  }
}
