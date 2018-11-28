package org.jepria.server.data;

/**
 * Класс для представления опций в общем виде
 */
public class OptionDto {
  
  public OptionDto() {}
  
  private String name;
  
  // В общем виде значение опции представляется строкой
  private String value;
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getValue() {
    return value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
  
}
