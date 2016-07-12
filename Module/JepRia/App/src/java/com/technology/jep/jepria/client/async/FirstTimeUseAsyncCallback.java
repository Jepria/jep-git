package com.technology.jep.jepria.client.async;

import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;

/**
 * Обработчик результата после отработки события {@link com.technology.jep.jepria.client.widget.event.JepEventType#FIRST_TIME_USE_EVENT }.
 */
public abstract class FirstTimeUseAsyncCallback<T> extends JepAsyncCallback<T> {

  /**
   * Событие.
   */
  private JepEvent event;
  
  public FirstTimeUseAsyncCallback(JepEvent event) {
    this.event = event;
  }
  
  /**
   * Пользовательский обработчик успешной отработки события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#FIRST_TIME_USE_EVENT }, 
   * перегружается в наследниках (на прикладном уровне).
   *
   * @param result результат успешной отработки события {@link com.technology.jep.jepria.client.widget.event.JepEventType#FIRST_TIME_USE_EVENT }
   */
  abstract public void onSuccessLoad(T result);

  /**
   * Обработчик успешной отработки события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#FIRST_TIME_USE_EVENT }.<br/>
   * Вызывает пользовательский обработчик <code>onSuccessLoad(T result)</code>.
   *
   * @param result результат успешной отработки события {@link com.technology.jep.jepria.client.widget.event.JepEventType#FIRST_TIME_USE_EVENT }
   */
  public void onSuccess(T result) {
    onSuccessLoad(result);
    Object source = event.getSource();
    if (source instanceof JepComboBoxField){
      ((JepComboBoxField) source).afterFirstTimeUseSuccess();
    }
  }
  
  /**
   * Обработчик НЕуспешной отработки события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#FIRST_TIME_USE_EVENT }.
   *
   * @param caught возникшее исключение при отработке события {@link com.technology.jep.jepria.client.widget.event.JepEventType#FIRST_TIME_USE_EVENT }
   */
  public void onFailure(Throwable caught) {
    Object source = event.getSource();
    if (source instanceof JepComboBoxField){
      ((JepComboBoxField) source).afterFirstTimeUseFailure();
    }
    super.onFailure(caught);
  }
}
