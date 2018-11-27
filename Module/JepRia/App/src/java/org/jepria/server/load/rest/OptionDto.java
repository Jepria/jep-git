package org.jepria.server.load.rest;

/**
 * Класс для представления базовых опций (в общем случае значение опции представляется строкой).
 */
public class OptionDto {
  
  public OptionDto() {}
  
  private String name;
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
