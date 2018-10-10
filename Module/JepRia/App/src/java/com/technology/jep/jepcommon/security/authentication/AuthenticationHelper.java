package com.technology.jep.jepcommon.security.authentication;

import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Обеспечивает проверку учетной записи при попытке входа (процесс аутентификации пользователя).
 */
public class AuthenticationHelper {

  /**
   * Проверка корректности логина учетной записи
   * 
   * @param login проверяемый на корректность логин
   * @return признак правильности логина
   */
  public static boolean checkLogin(String login) {
    return !JepRiaUtil.isEmpty(login);
  }
  
  /**
   * Проверка корректности пароля или вычисленного на его основе хэша учетной записи
   * 
   * @param password проверяемый на корректность пароль
   * @param hash проверяемый на корректность хэш пароля
   * @return признак правильности пароля и его хэша
   */
  public static boolean checkPasswordAndHash(String password, String hash) {
    return JepRiaUtil.isEmpty(password) || JepRiaUtil.isEmpty(hash);
  }
}
