package com.technology.jep.jepria.client.async;

import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.field.multistate.JepBaseTextField;

/**
 * Обработчик результата после отработки события {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }.
 */
public abstract class TypingTimeoutAsyncCallback<T> extends JepAsyncCallback<T> {

  /**
   * Событие.
   */
  private JepEvent event;
  
  public TypingTimeoutAsyncCallback(JepEvent event) {
    this.event = event;
  }
  
  /**
   * Пользовательский обработчик успешной отработки события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }, 
   * перегружается в наследниках (на прикладном уровне).
   *
   * @param result результат успешной отработки события {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }
   */
  abstract public void onSuccessLoad(T result);

  /**
   * Обработчик успешной отработки события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }.<br/>
   * Вызывает пользовательский обработчик <code>onSuccessLoad(T result)</code>.
   *
   * @param result результат успешной отработки события {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }
   */
  public void onSuccess(T result) {
    onSuccessLoad(result);
    Object source = event.getSource();
    if (source instanceof JepBaseTextField){
      ((JepBaseTextField) event.getSource()).afterTypingTimeoutSuccess();
    }    
  }
  
  /**
   * Обработчик НЕуспешной отработки события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }.
   *
   * @param caught возникшее исключение при отработке события {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }
   */
  public void onFailure(Throwable caught) {
    Object source = event.getSource();
    if (source instanceof JepBaseTextField){
      ((JepBaseTextField) event.getSource()).afterTypingTimeoutFailure();
    }
    super.onFailure(caught);
  }
}
