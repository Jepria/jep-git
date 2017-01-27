package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name=MODULE_ROLES_ATTRIBUTE)
@XmlAccessorType(XmlAccessType.FIELD)
public class Roles {
  
  @XmlElement(name=MODULE_ROLE_ATTRIBUTE)
  private List<String> roles;
  
  @SuppressWarnings("unused")
  private Roles(){}
  
  public Roles(String[] roleNames){
    setRoles(roleNames);
  }
  public Roles(List<String> roleNames){
    this.roles = roleNames;
  }
  
  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(String[] names) {
    this.roles = Arrays.asList(names);
  }
}
