package com.technology.jep.jepria.server.security.module;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionBindingListener;
import java.security.Principal;
import java.util.List;

public interface JepSecurityModule extends HttpSessionBindingListener {
  /**
   * Получение Id пользователя
   * @return Id пользователя
   */
  Integer getOperatorId();

  /**
   * Получение ролей
   * 
   * @return роли
   */
  List<String> getRoles();

  /**
   * Получение username
   * 
   * @return username
   */
  String getUsername();

  /**
   * Выход из приложения
   * 
   * @param request HTTP-запрос
   * @param response HTTP-ответ
   * 
   * @return Url, на который нужно перейти клиенту после выполнения Logout на сервере
   * @throws Exception
   */
  String logout(HttpServletRequest request, HttpServletResponse response) throws Exception;
  
  /**
   * Выход из приложения
   * 
   * @param request HTTP-запрос
   * @param response HTTP-ответ
   * @param currentUrl Url, с которого выполняется Logout (используется для возврата после Login)
   * 
   * @return Url, на который нужно перейти клиенту после выполнения Logout на сервере
   * @throws Exception
   */
  String logout(HttpServletRequest request, HttpServletResponse response, String currentUrl) throws Exception;

  /**
   * Проверка роли
   * 
   * @param role проверяемая роль
   * @param makeError  признак: выбрасывать исключение (значение true) или нет (значение false)
   * @return true - если текущему оператору принадлежит роль role, false или исключение
   * (в зависимости от параметра makeError) в противном случае
   * @throws ApplicationException
   */
  boolean isRole(String role, boolean makeError) throws ApplicationException;
  
  /**
   * Возвращает идентификатор оператора залогинившегося через JavaSSO. Если
   * пользователь не логинился через javaSSO, то возвращает null.
   * 
   * @param principal пользователь, залогинившийся через javaSSO
   * @return идентификатор оператора, залогинившегося через JavaSSO
   */
  Integer getJepPrincipalOperatorId(Principal principal);

  /**
   * Изменение пароля
   * 
   * @param operatorId operatorId пользователя
   * @param password старый пароль
   * @param newPassword новый пароль
   * @param newPasswordConfirm подтверждение пароля
   */
  void changePassword(Integer operatorId, String password, String newPassword, String newPasswordConfirm);

  /**
   * Проверка необходимости изменения пароля пользователя operatorId
   * 
   * @return true, если пароль пора менять
   */
  boolean isChangePassword(Integer operatorId);

  /**
   * Получение ролей гостя
   * 
   * @return роли гостя
   */
  List<String> getGuestRoles();

  /**
   * Проверка авторизации в SSO
   * @return true, если авторизация уже была
   */
  boolean isAuthorizedBySso();
}
