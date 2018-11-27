package org.jepria.server.load.rest;

/**
 * Общий класс Dto для опций, не зависящий от типа значений опции
 */
public class OptionDto {
  
  public OptionDto() {
  }
  
  private String name;
  private Object value;
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public <X> X getValue() {
    return (X)value;
  }
  
  public void setValue(Object value) {
    this.value = value;
  }
  
}
