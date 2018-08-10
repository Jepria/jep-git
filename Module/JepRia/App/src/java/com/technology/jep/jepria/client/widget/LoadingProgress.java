package com.technology.jep.jepria.client.widget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Контейнер загрузки.
 */
public final class LoadingProgress {
  
  public static final String LOADING_PROGRESS_ID = "loadingProgress";
  
  public static final String LOADING_HEADER_ID = "loadingHeader";
  
  public static final String LOADING_MESSAGE_ID = "loadingMessage";
  
  private Element loadingProgress;
  
  private Element loadingHeader;
  
  private Element loadingMessage;
  
  public LoadingProgress() {
    loadingProgress = Document.get().getElementById(LOADING_PROGRESS_ID);
    loadingHeader = Document.get().getElementById(LOADING_HEADER_ID);
    loadingMessage = Document.get().getElementById(LOADING_MESSAGE_ID);
  }

  public void setHeader(String header) {
    if (!JepRiaUtil.isEmpty(header)) {
      if (loadingHeader != null) { // loadingHeader может отсутствовать в кастомном модуле
        loadingHeader.setInnerHTML(header);
      }
    }
  }

  public void setMessage(String message) {
    if (!JepRiaUtil.isEmpty(message)) {
      if (loadingMessage != null) { // loadingMessage может отсутствовать в кастомном модуле
        loadingMessage.setInnerHTML(message);
      }
    }
  }

  public void show() {
    if (loadingProgress != null) {
      loadingProgress.getStyle().setDisplay(Display.INLINE_BLOCK);
    }
  }

  public void hide() {
    if (loadingProgress != null) {
      loadingProgress.getStyle().setDisplay(Display.NONE);
    }
  }
}
