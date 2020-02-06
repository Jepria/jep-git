package com.technology.jep.jepria.server.security.servlet;

import com.google.gson.Gson;

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
  protected TreeSet<String> selfFilterMappings;
  protected TreeSet<String> otherFilterMappings = new TreeSet<>();

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    try {
      String currentFilterName = filterConfig.getFilterName();
      Map<String, ? extends FilterRegistration> filterRegistrations = filterConfig.getServletContext().getFilterRegistrations();
      FilterRegistration currentFilterRegistration = filterRegistrations.get(currentFilterName);
      selfFilterMappings = new TreeSet<>(currentFilterRegistration.getUrlPatternMappings());
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
            selfFilterMappings.forEach(selfFilterMapping -> {
              filterRegistration.getValue().getUrlPatternMappings().forEach(filterMapping -> {
                Pattern pattern = Pattern.compile(selfFilterMapping);
                Matcher matcher = pattern.matcher(filterMapping);
                if (matcher.matches()) {
                  throw new IllegalStateException("Two filters matches at the same uri-pattern: " + filterMapping);
                }
                if (matcher.lookingAt()) {
                  otherFilterMappings.add(filterMapping);
                }
              });
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
   * Проверить нужно ли передать проверку другому фильтру
   * @param path
   * @return
   */
  public boolean isSubPath(String path) {
    Iterator<String> otherIterator = otherFilterMappings.descendingIterator();

    while (otherIterator.hasNext()) {
      String otherMapping = otherIterator.next();
      Pattern otherPattern = Pattern.compile(otherMapping);
      Matcher otherMatcher = otherPattern.matcher(path);
      if (otherMatcher.lookingAt() || otherMatcher.matches()) {
        /*
         * если path присутствует в url-mapping другого зарегистрированного фильтра
         */
        Iterator<String> selfIterator = selfFilterMappings.descendingIterator();
        boolean allSelf = true;
        while (selfIterator.hasNext()) {
          String selfMapping = selfIterator.next();
          Pattern selfPattern = Pattern.compile(selfMapping);
          Matcher selfMatcher = selfPattern.matcher(path);
          if (selfMatcher.lookingAt() && !selfPattern.matcher(otherMapping).lookingAt()) {
            /*
             * если совпавший url-mapping другого фильтра < совпавшего url-mapping текущего фильтра то не пропускать обработку
             */
            allSelf = false;
            break;
          }
        }
        if (allSelf) {
          /*
           * если все совпавшие url-mapping текущего фильтра < совпавшего url-mapping другого зарегистрированного фильтра, то пропустить обработку
           */
          return true;
        }
      }
    }

    return false;
  }

}
