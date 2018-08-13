package com.technology.jep.jepria.client.widget;

/**
 * Панель загрузки
 */
public final class LoadingPanel {
  
  private final LoadingProgress loadingProgress = new LoadingProgress();
  
  private final DisabledLayer disabledLayer = new DisabledLayer();
  
  private int count = 0;
  
  public LoadingPanel() {}

  public void show() {
    count++;
    loadingProgress.show();
    disabledLayer.show();
  }
  
  public void hide() {
    if (count > 0) {
      count--;
    }
    
    if (count == 0) {
      loadingProgress.hide();
      disabledLayer.hide();
    }
  }

  public void setHeader(String header) {
    loadingProgress.setHeader(header);
  }

  public void setMessage(String message) {
    loadingProgress.setMessage(message);
  }
}
