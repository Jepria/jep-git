package com.technology.jep.jepria.client.security;

import java.util.ArrayList;
import java.util.List;

import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Клиентский аналог серверного {@link com.technology.jep.jepria.server.security.JepSecurityModule}.
 */
public class ClientSecurity {
  
  public static ClientSecurity instance = new ClientSecurity();
  
  private ClientSecurity() {};

  private String username = null;

  private Integer operatorId = null;

  private List<String> roles = null;
  
  public String getUsername() {
    return username;
  }
    
  public Integer getOperatorId() {
    return operatorId;
  }
  
  public List<String> getRoles() {
    return roles;
  }

  public void setUsername(String username) {
    this.username = username;
  }
  
  public void setOperatorId(Integer operatorId) {
    if(this.operatorId == null) {  // Делается только один раз.
      this.operatorId = operatorId;
    }
  }

  public void setRoles(List<String> roles) {
    if(this.roles == null) {  // Делается только один раз.
      this.roles = roles;
    }
  }
  
  public boolean isUserHaveRole(String role) {
    return !JepRiaUtil.isEmpty(roles) && roles.contains(role);
  }

  /**
   * Проверка наличия у пользователя хотя бы одной из заданных ролей.
   * 
   * @param checkedRoles роли, наличие которых проверяется
   * @return true, если есть хотя бы одна роль, иначе - false
   */
  public boolean isUserHaveRoles(List<String> checkedRoles) {
    if (JepRiaUtil.isEmpty(checkedRoles)) return true;
    if (JepRiaUtil.isEmpty(roles)) return false;
    
    for (String role: checkedRoles) {
      if (roles.contains(role)) {
        return true;
      }
    }
    
    return false;
  }
  
  @Deprecated
  public boolean isUserHaveRoles(String strRoles) {
    return isUserHaveRoles(getRoles(strRoles));
  }

  /**
   * Проверка наличия у пользователя всех заданных ролей.
   * 
   * @param checkedRoles роли, наличие которых проверяется
   * @return true, если есть все заданные роли, иначе - false
   */
  public boolean isUserHaveAllRoles(List<String> checkedRoles) {
    if (JepRiaUtil.isEmpty(checkedRoles)) return true;
    if (JepRiaUtil.isEmpty(roles)) return false;
    
    return roles.containsAll(checkedRoles);
  }

  @Deprecated
  public static List<String> getRoles(String strRoles) {
    String[] rolesArray = strRoles.split(",");
    List<String> roles = new ArrayList<String>();
    for(int i = 0; i < rolesArray.length; i++) {
      roles.add(rolesArray[i].trim());
    }
    return roles;
  }

}
