package com.technology.jep.jepria.client.widget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Информацонное сообщение о загрузке.
 */
public final class LoadingProgress {
  
  /**
   * Идентификатор родительство тега. 
   */
  public static final String LOADING_PROGRESS_ID = "loadingProgress";
  
  /**
   * Идентификатор тега с заголовком.
   */
  public static final String LOADING_HEADER_ID = "loadingHeader";
  
  /**
   * Идентификатор тега с текстом.
   */
  public static final String LOADING_MESSAGE_ID = "loadingMessage";
  
  /**
   * Родительский элемент.
   */
  private Element loadingProgress;
  
  /**
   * Элемент заголовка.
   */
  private Element loadingHeader;
  
  /**
   * Элемент текста.
   */
  private Element loadingMessage;
  
  /**
   * Конструктор. Маппинг элементов по идентификаторам.
   */
  public LoadingProgress() {
    loadingProgress = Document.get().getElementById(LOADING_PROGRESS_ID);
    loadingHeader = Document.get().getElementById(LOADING_HEADER_ID);
    loadingMessage = Document.get().getElementById(LOADING_MESSAGE_ID);
  }

  /**
   * Устанавливает заголовок.
   * @param header Текст заголовка.
   */
  public void setHeader(String header) {
    if (!JepRiaUtil.isEmpty(header)) {
      if (loadingHeader != null) { // loadingHeader может отсутствовать в кастомном модуле
        loadingHeader.setInnerHTML(header);
      }
    }
  }

  /**
   * Устанавливает сообщение.
   * @param message Текст сообщения.
   */
  public void setMessage(String message) {
    if (!JepRiaUtil.isEmpty(message)) {
      if (loadingMessage != null) { // loadingMessage может отсутствовать в кастомном модуле
        loadingMessage.setInnerHTML(message);
      }
    }
  }

  /**
   * Отображает контейнер.
   */
  public void show() {
    if (loadingProgress != null) {
      loadingProgress.getStyle().clearDisplay();
    }
  }

  /**
   * Скрывает контейнер.
   */
  public void hide() {
    if (loadingProgress != null) {
      loadingProgress.getStyle().setDisplay(Display.NONE);
    }
  }
}
