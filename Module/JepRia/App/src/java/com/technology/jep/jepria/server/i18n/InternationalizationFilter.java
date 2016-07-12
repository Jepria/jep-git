package com.technology.jep.jepria.server.i18n;

import static com.technology.jep.jepria.server.JepRiaServerConstant.HTTP_REQUEST_PARAMETER_LANG;
import static com.technology.jep.jepria.server.JepRiaServerConstant.HTTP_REQUEST_PARAMETER_LOCALE;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Фильтр используется для поддержки совместимости GWT-модулей с модулями,
 * работающими с языком по параметру 'lang'.<br/>
 * Поскольку переключение локали в GWT выполняется по параметру 'locale', фильтр
 * переименовывает параметр запроса 'lang' в 'locale'.
 */
public class InternationalizationFilter implements Filter {
  public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = ((HttpServletRequest) servletRequest);
    String lang = (String) request.getParameter(HTTP_REQUEST_PARAMETER_LANG);
    if (lang != null) {
      // Замена 'lang' на 'locale'
      String requestUrl = request.getRequestURL().toString();
      Enumeration<?> paramEnum = request.getParameterNames();
      Map<?, ?> paramMap = request.getParameterMap();
      StringBuffer newQueryString = new StringBuffer();
      while(paramEnum.hasMoreElements()) {
        String name = (String)paramEnum.nextElement();
        String[] value = null; 
        if(newQueryString.length() > 0) {
          newQueryString.append("&");
        }
        newQueryString.append(HTTP_REQUEST_PARAMETER_LANG.equals(name) ? HTTP_REQUEST_PARAMETER_LOCALE : name);
        newQueryString.append("=");
        value = (String[])paramMap.get(name); 
        newQueryString.append((String)value[0]);
      }
      
      String newRequest = requestUrl + "?" + newQueryString;
      ((HttpServletResponse) response).sendRedirect(newRequest);
    }

    chain.doFilter(request, response);
  }

  public void init(FilterConfig arg0) throws ServletException {
    // TODO Auto-generated method stub

  }

  public void destroy() {
    // TODO Auto-generated method stub

  }
}