package org.jepria.server.data;

/**
 * Класс для представления опций в общем виде
 */
public class OptionDto<T> {
  
  public OptionDto() {}
  
  private String name;
  
  private T value;
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public T getValue() {
    return value;
  }
  
  public void setValue(T value) {
    this.value = value;
  }
  
}
