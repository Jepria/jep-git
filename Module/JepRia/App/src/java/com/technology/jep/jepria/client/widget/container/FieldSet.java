package com.technology.jep.jepria.client.widget.container;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.CaptionPanel;

/**
 * Контейнер, обрамляющий содержимое рамкой с подписью в верхем левом углу.<br>
 * Является обёрткой стандартного GWT-класса {@link CaptionPanel}.
 */
public class FieldSet extends CaptionPanel {

  private static final String FIELDSET_DEFAULT_STYLE = "jepRia-FieldSet";
  
  public FieldSet() {
    this("", false);
  }

  public FieldSet(SafeHtml caption) {
    this(caption.asString(), true);
  }

  public FieldSet(String captionText) {
    this(captionText, false);
  }

  public FieldSet(String caption, boolean asHTML) {
    super(caption, asHTML);
    getElement().addClassName(FIELDSET_DEFAULT_STYLE);
  }

}
