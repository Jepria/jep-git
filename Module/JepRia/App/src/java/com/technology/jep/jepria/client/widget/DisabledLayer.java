package com.technology.jep.jepria.client.widget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.technology.jep.jepria.client.util.JepClientUtil;

/**
 * Контейнер слоя маски, закрывающей экран.
 */
public final class DisabledLayer {
  
  /**
   * Класc слоя маски.
   */
  public static final String DISABLED_LAYER_STYLE = "jepRia-disabledLayer";
  
  /**
   * Идентификатор слоя маски.
   */
  public static final String DISABLED_LAYER_ID = "disabledLayerId";

  /**
   * Элемент слоя маски.
   */
  private Element disabledLayer;
  
  /**
   * Конструктор. Маппинг элементов по идентификаторам.
   */
  public DisabledLayer() {
    disabledLayer = Document.get().getElementById(DISABLED_LAYER_ID);
  }

  /**
   * Отображает маску.
   */
  public void show() {
    if (disabledLayer == null) { 
      create(); // Для обратной совместимости, если контейнера нет в разметке (в jsp).
    }
    
    disabledLayer.getStyle().setDisplay(Display.BLOCK);
  }

  /**
   * Скрывает маску.
   */
  public void hide() {
    if (disabledLayer != null) {
      disabledLayer.getStyle().setDisplay(Display.NONE);
    }
  }
  
  /**
   * Создает контейнер, если вызвать в конструкторе, 
   * то {@link com.technology.jep.jepria.client.util.JepClientUtil#BODY} не успеет инициализироваться.
   */
  private void create() {
    disabledLayer = Document.get().createDivElement();
    disabledLayer.setId(DISABLED_LAYER_ID);
    disabledLayer.addClassName(DISABLED_LAYER_STYLE);
    
    JepClientUtil.BODY.appendChild(disabledLayer);
  }
}
