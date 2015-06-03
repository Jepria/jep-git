package com.technology.jep.jepria.client.widget.event;

/**
 * Интерфейс объектов, уведомляемых событиями JepEvent.
 * 
 * <pre>
    JepButton btn = new JepButton();
    btn.addListener(Events.Select, new JepListener&lt;ButtonEvent>() {
      public void handleEvent(ButtonEvent be) {
        Button button = be.button;
      }
    });
 * </pre>
 */
public interface JepListener {

  /**
   * Вызывается при появлении событий, на которые выполнялась "подписка".
   * 
   * @param event событие
   */
  public void handleEvent(JepEvent event);

}
