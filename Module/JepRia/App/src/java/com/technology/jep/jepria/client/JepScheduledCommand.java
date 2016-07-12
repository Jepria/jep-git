package com.technology.jep.jepria.client;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;

/**
 * Абстрактный класс для отложенной команды.<br/>
 * Является наследником GWT-интерфейса ScheduledCommand, позволяющим передавать внутри себя данные.
 * 
 * @param <T> тип хранимых данных
 */
abstract public class JepScheduledCommand<T> implements ScheduledCommand {
  /**
   * Хранимые данные.
   */
  private T data;

  /**
   * Помещение хранимых данных.
   * 
   * @param data данные
   */
  public void setData(T data) {
    this.data = data;
  }

  /**
   * Извлечение хранимых данных.
   * 
   * @return данные
   */
  public T getData() {
    return data;
  }

}
