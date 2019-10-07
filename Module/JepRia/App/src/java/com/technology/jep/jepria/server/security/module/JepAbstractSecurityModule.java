package com.technology.jep.jepria.server.security.module;

import com.technology.jep.jepcommon.security.pkg_Operator;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionBindingEvent;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.technology.jep.jepria.server.JepRiaServerConstant.DEFAULT_DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepria.server.security.JepSecurityConstant.*;

/**
 * Модуль поддержки безопасности
 */
public abstract class JepAbstractSecurityModule implements JepSecurityModule {
  protected static Logger logger;
  
  protected Db db = null;
  
  /**
   * Список ролей текущего пользователя 
   */
  protected List<String> roles = new ArrayList<String>();
  
  /**
   * Текущий идентификатор пользователя 
   */
  protected Integer operatorId = null;
  
  /**
   * Логин пользователя 
   */
  protected String username;

  /**
   * Список ролей пользователя Гость
   */
  private static List<String> guestRoles = new ArrayList<String>();
  
  /**
   * Идентификатор пользователя Гость
   */
  private static Integer guestOperatorId = null;
  private static long guestCacheTime = 0;
  private static final int GUEST_REFRESH_TIME_DEFAULT = 900;    // 15 минут.
  
  /**
   * Признак того, что был вход через SSO
   */
  protected boolean isAuthorizedBySso = false;

  protected void init() {
    db = new Db(DEFAULT_DATA_SOURCE_JNDI_NAME);
  }

  protected JepAbstractSecurityModule(){
    init();
  }
  
  /**
   * Обновление полномочий пользователя в соответствии с указанным принципалом 
   * 
   * @param principal    принципал, содержащий информацию об Id оператора и его ролях
   */
  abstract protected void updateSubject(Principal principal);
  
  /**
   * Проверка на устаревания принципала 
   * 
   * @param principal     проверяемый принципал
   */
  abstract protected boolean isObsolete(Principal principal);
  
  /**
   * Проверяет, принадлежит ли указанная роль role текущему оператору. Если
   * параметр makeError установлен в true, то при отсутствии роли у текущего
   * оператора выбрасывается исключение.
   * 
   * @param role проверяемая роль
   * @param makeError признак: выбрасывать исключение (значение true) или нет (значение false)
   * @return true - если текущему оператору принадлежит роль role, false или исключение
   * (в зависимости от параметра makeError) в противном случае
   * @throws ApplicationException
   */
  @Override
  public boolean isRole(String role, boolean makeError) throws ApplicationException {
    boolean include = roles.contains(role);
    if (include)
      return true;
    else if (makeError)
      throw new ApplicationException("You haven't enough rights to perform this operation (RoleId = " + role + ")", null);
    return false;
  }

  /**
   * Возвращает идентификатор текущего пользователя.
   */
  @Override
  public Integer getOperatorId() {
    return operatorId;
  }
  
  /**
   * Возвращает имя (username) текущего пользователя.
   */
  @Override
  public String getUsername() {
    return username;
  }

  /**
   * Возвращает роли текущего пользователя.
   */
  @Override
  public List<String> getRoles() {
    return roles;
  }

  @Override
  public void valueBound(HttpSessionBindingEvent bindingEvent) {
    if(JEP_SECURITY_MODULE_ATTRIBUTE_NAME.equals(bindingEvent.getName())) {
      onStartSession(bindingEvent.getSession().getId());
    }
  }

  /*
   * Оставлено для подстраховочной очистки ресурсов (закрытия соединений с БД)
   */
  @Override
  public void valueUnbound(HttpSessionBindingEvent bindingEvent) {
    if(JEP_SECURITY_MODULE_ATTRIBUTE_NAME.equals(bindingEvent.getName())) {
      onExpiredSession(bindingEvent.getSession().getId());
    }
  }
  
  /**
   * Вход в систему с правами пользователя Гость
   */
  protected void doLogonByGuest() {
    logger.trace("doLogonByGuest()");

    long currentTime = System.currentTimeMillis();
    long age = (currentTime - guestCacheTime) / 1000;  // Сколько секунд находится в кэше.

    try {
      if(age > GUEST_REFRESH_TIME_DEFAULT) {
        logger.trace("doLogonByGuest(): age > GUEST_REFRESH_TIME_DEFAULT");
        guestOperatorId = pkg_Operator.logon(db, GUEST_LOGIN, GUEST_PASSWORD, null);
        guestRoles = pkg_Operator.getRoles(db, guestOperatorId);
        guestCacheTime = System.currentTimeMillis();
      }
      
      operatorId = guestOperatorId;
      roles = guestRoles;
      
    } catch (SQLException e) {
      throw new SystemException("pkg_Operator.logon() ERROR", e);
    } finally {
      db.closeAll();
    }
  }

  private void onStartSession(String sessionId) {
    logger.trace("onStartSession(): sessionId = " + sessionId);
  }

  private void onExpiredSession(String sessionId) {
    logger.trace("onExpiredSession(): sessionId = " + sessionId);
    db.closeAll();
  }
    
  /**
   * Функция определяет необходимость смены пароля пользователем.
   * 
   * @param operatorId идентификатор пользователя
   * @return true - если пользователю необходимо сменить пароль, false - в противном случае.
   */
  @Override
  public boolean isChangePassword(Integer operatorId) {
    try {
      return pkg_Operator.isChangePassword(this.db, operatorId);
    } catch (SQLException ex) {
      throw new SystemException("Password change check error", ex);
    } finally {
      db.closeAll();
    }
    
  }
  
  /**
   * Изменение пароля пользователя.
   * 
   * @param operatorId идентификатор пользователя
   * @param password пароль пользователя
   * @param newPassword новый пароль пользователя
   * @param newPasswordConfirm подтверждение нового пароля пользователя
   */
  @Override
  public void changePassword(Integer operatorId, String password, String newPassword, String newPasswordConfirm) {
    try {
      pkg_Operator.changePassword(this.db, operatorId, password, newPassword, newPasswordConfirm);
    } catch (SQLException ex) {
      throw new SystemException("Wrong authentication", ex);
    } finally {
      db.closeAll();
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getGuestRoles(){
    return guestRoles;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAuthorizedBySso() {
    return isAuthorizedBySso;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return logout(request, response, null);
  }
}
