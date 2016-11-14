package com.technology.jep.jepria.client.widget;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.GRID_GLASS_MASK_ID;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class MaskPanel {

  private static final String MASK_PANEL_STYLE = "jepRia-MaskPanel";
  private static final String MASK_PANEL_GLASS_STYLE = "jepRia-MaskPanel-glass";
  private static final String MASK_PANEL_CONTENT_STYLE = "jepRia-MaskPanel-content";

  private Element maskedElement = null;
  private boolean messageShows = false;

  private Element glass;
  private Element content;
  private Element container;

  public MaskPanel() {
    glass = Document.get().createDivElement();
    glass.setClassName(MASK_PANEL_GLASS_STYLE);
    glass.setId(GRID_GLASS_MASK_ID);

    container = Document.get().createDivElement();
    container.setClassName(MASK_PANEL_STYLE);
    
    content = Document.get().createDivElement();
    content.setClassName(MASK_PANEL_CONTENT_STYLE);
    container.appendChild(content);
  }

  public void mask(Element maskingElement, String message) {
    if (maskingElement != null) {
      unmask();
      
      maskedElement = maskingElement;
      maskedElement.appendChild(glass);

      if (message != null) {
        content.setInnerSafeHtml(SafeHtmlUtils.fromString(message));
        maskedElement.appendChild(container);
        messageShows = true;
      }
    }
  }

  public void unmask() {
    if (maskedElement != null) {
      maskedElement.removeChild(glass);
      
      if (messageShows) {
        maskedElement.removeChild(container);
        messageShows = false;
      }
      
      maskedElement = null;
    }
  }

  public boolean isMasked(Element element) {
    return maskedElement != null && maskedElement.equals(element);
  }
}
