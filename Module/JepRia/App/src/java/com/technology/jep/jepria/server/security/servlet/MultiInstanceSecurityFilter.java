package com.technology.jep.jepria.server.security.servlet;

import javax.servlet.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * Фильтр проверяет наличие зарегистистрированных экземпляров.
 * Если в любом другом экземпляре url-mapping является subPath для текущего фильтра, то он добавляется в список игнорируемых.
 * Таким образом, только один экземпляр отвечает за каждый url-mapping.
 * </pre>
 */
public abstract class MultiInstanceSecurityFilter implements Filter {

  public static final String SECURITY_CONSTRAINT = "security-constraint";
  protected Set<String> securityRoles;
  protected Set<String> subUrlPatterns = new HashSet<>();

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    try {
      String currentFilterName = filterConfig.getFilterName();
      Map<String, ? extends FilterRegistration> filterRegistrations = filterConfig.getServletContext().getFilterRegistrations();
      FilterRegistration currentFilterRegistration = filterRegistrations.get(currentFilterName);
      Set<String> currentFilterMappings = new HashSet<>(currentFilterRegistration.getUrlPatternMappings());
      filterRegistrations
        .entrySet()
        .forEach(filterRegistration -> {
          if (filterRegistration.getValue().getName().equals(currentFilterName)) {
            return;
          }
          Class<? extends Filter> clazz;
          try {
            clazz = (Class<? extends Filter>) Class.forName(filterRegistration.getValue().getClassName());
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
          }
          if (MultiInstanceSecurityFilter.class.isAssignableFrom(clazz)) {
            /**
             * Найдем subPath в других инстансах фильтра
             */
            currentFilterMappings.forEach(currentFilterMapping -> {
              filterRegistration.getValue().getUrlPatternMappings().forEach(filterMapping -> {
                Pattern pattern = Pattern.compile(currentFilterMapping);
                Matcher matcher = pattern.matcher(filterMapping);
                if (matcher.matches()) {
                  throw new IllegalStateException("Two filters matches at the same uri-pattern: " + filterMapping);
                }
                if (matcher.lookingAt()) {
                  subUrlPatterns.add(filterMapping);
                }
              });
            });
            /**
             * Удалим кольцевые зависимости
             */
            currentFilterMappings.forEach(currentFilterMapping -> {
              Iterator<String> i = subUrlPatterns.iterator();
              while (i.hasNext()) {
                String value = i.next();
                Pattern pattern = Pattern.compile(value);
                Matcher matcher = pattern.matcher(currentFilterMapping);
                if (matcher.lookingAt()) {
                  i.remove();
                }
              }
            });
          }
        });
      String securityRolesString = filterConfig.getInitParameter(SECURITY_CONSTRAINT);
      securityRoles = new HashSet<>();
      if (securityRolesString != null && securityRolesString.length() > 0) {
        Collections.addAll(securityRoles, securityRolesString.split("\\s+|\\s*,\\s*|\\s*;\\s*"));
      }
    } catch (Throwable th) {
      th.printStackTrace();
      throw new ServletException(th);
    }
  }

  /**
   * Проверить не содержится ли путь в списке игнорируемых
   * @param path
   * @return
   */
  public boolean isSubPath(String path) {
    for (String mapping : subUrlPatterns) {
      Pattern pattern = Pattern.compile(mapping);
      Matcher matcher = pattern.matcher(path);
      if (matcher.lookingAt() || matcher.matches()) {
        return true;
      }
    }
    return false;
  }

}
