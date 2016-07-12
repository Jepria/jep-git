package com.technology.jep.jepria.client.widget.event;

/**
 * Базовый класс для "лёгких" (не относящихся к EventBus) событий Jep.
 */
public class JepEvent {

  private Object source;
  
  private Object parameter;

  public Object getParameter() {
    return parameter;
  }

  /**
   * Конструктор события для случаев, когда важен только сам факт наступления.
   */
  public JepEvent() {
    this(null, null);
  }

  public JepEvent(Object source) {
    this(source, null);
  }
  
  public JepEvent(Object source, Object parameter) {
    this.source = source;
    this.parameter = parameter;
  }

  /**
   * Возвращает объект-источник события.
   * 
   * @return объект-источник события
   */
  public Object getSource() {
    return source;
  }

  /**
   * Установка объекта-источник события.
   * 
   * @param source источник события
   */
  public void setSource(Object source) {
    this.source = source;
  }

}
