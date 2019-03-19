package com.technology.jep.jepria.client.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Клиентский аналог серверного {@link com.technology.jep.jepria.server.security.JepSecurityModule}.
 */
public class ClientSecurity {
  
  public static final ClientSecurity instance = new ClientSecurity();
  
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

  public void setRoles(String... roles) {
    this.roles = roles == null ? new ArrayList<>() : Arrays.asList(roles);
  }
  
  public boolean isUserHaveRole(String role) {
    return !JepRiaUtil.isEmpty(roles) && roles.contains(role);
  }

  /**
   * Проверка наличия у пользователя хотя бы одной из заданных ролей.
   * 
   * @param checkedRoles роли, наличие <b>любой из которых</b> проверяется
   * @return true, если есть хотя бы одна роль, иначе - false
   */
  public boolean isUserHaveRoles(String... roles) {
    if (roles != null) {
      for (String role: roles) {
        if (this.roles.contains(role)) {
          return true;
        }
      }
      return false;
    } else {
      return true;
    }
  }
  
  /**
   * Проверка наличия у пользователя всех заданных ролей.
   * 
   * @param checkedRoles роли, наличие <b>всех из которых</b> проверяется
   * @return true, если есть все заданные роли, иначе - false
   */
  public boolean isUserHaveAllRoles(String...roles) {
    if (roles != null) {
      return this.roles.containsAll(Arrays.asList(roles));
    } else {
      return true;
    }
  }

}
