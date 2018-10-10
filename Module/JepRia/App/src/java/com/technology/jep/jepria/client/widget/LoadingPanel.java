package com.technology.jep.jepria.client.widget;

/**
 * Панель загрузки. Состоит из информацонного блока о загрузке и слоя маски.
 */
public final class LoadingPanel {
  
  /**
   * Экземпляр информационного блока о загрузке.
   */
  private final LoadingProgress loadingProgress = new LoadingProgress();
  
  /**
   * Экземпляр слоя маски.
   */
  private final DisabledLayer disabledLayer = new DisabledLayer();
  
  /**
   * Счетчик. Показывает сколько раз была открыта панель загрузки. <br/> 
   * Не должен принимать отрицательные значения.
   */
  private int count = 0;

  /**
   * Отображает панели, увеличивает счетчик.
   */
  public void show() {
    count++;
    loadingProgress.show();
    disabledLayer.show();
  }
  
  /**
   * Скрывает панели, уменьшает счетчик.
   */
  public void hide() {
    if (count > 0) {
      count--;
    }
    
    if (count == 0) {
      loadingProgress.hide();
      disabledLayer.hide();
    }
  }

  /**
   * Устанавливает текст заголовка.
   * @param header Текст заголовка.
   */
  public void setHeader(String header) {
    loadingProgress.setHeader(header);
  }

  /**
   * Устанавливает текст сообщения.
   * @param message Текст сообщения.
   */
  public void setMessage(String message) {
    loadingProgress.setMessage(message);
  }
}
