package org.jepria.server.service.security;

import java.util.List;

/**
 * Адаптивный интерфейс для идентификации пользователя в приложении
 */
public interface Credential {
  int getOperatorId();
  String getUsername();
  // Note: метод getRoles неэффективен (не должен быть нужен), вместо него следует использовать isUserInRole
  boolean isUserInRole(String roleShortName);
}
