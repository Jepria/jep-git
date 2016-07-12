package com.technology.jep.jepria.client.widget.field.wysiwyg;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.field.wysiwyg.toolbar.CustomRichTextArea;
import com.technology.jep.jepria.client.widget.field.wysiwyg.toolbar.RichTextToolbar;

/**
 * This widget will create a WYSIWYG-editor.
 */
public class RichTextEditorField extends Grid implements HasValue<String> {

  /**
   * Непечатаемый символ.
   */
  public static final String NONPRINTABLE_ELEMENT = "\u200B"; 
  
  /**
   * Текстовая область, в которой размещается форматируемый пользователем текст.
   */
  private CustomRichTextArea textArea;
  
  /**
   * Панель форматирования текста.
   */
  private RichTextToolbar toolbar;
  
  /**
   * Высота текстовая области, по умолчанию.
   */
  private final static int DEFAULT_RICHTEXTEDITOR_FIELD_HEIGHT = 150;
  
  /**
   * Наименование селектора (класса стилей) компонента.
   */
  private static final String RICH_TEXT_EDITOR_FIELD_STYLE = "jepRia-RichTextEditorField-Input";
  
  public RichTextEditorField(){
    super(2, 1); // Grid with 1 column and 2 rows
    
    // Create the text area and toolbar
    textArea = new CustomRichTextArea();
    textArea.setSize("100%", DEFAULT_RICHTEXTEDITOR_FIELD_HEIGHT + Unit.PX.getType());
    
      toolbar = new RichTextToolbar(textArea);
      toolbar.setWidth("100%");

      // Add the components to a panel
      setWidget(0, 0, toolbar);
      setWidget(1, 0, textArea);
      
      // customize style
      setStyleName(RICH_TEXT_EDITOR_FIELD_STYLE);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getValue(){
    // erase unprinted symbols
    return textArea.getHTML().replaceAll(NONPRINTABLE_ELEMENT, "").trim();
  }

  /**
   * {@inheritDoc}
   * 
   * @param handler  обработчик события изменения значения WYSIWYG-редактора
   * @return  ссылка на соответстующую запись обработчика
   */
  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(String value) {
    setValue(value, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(String value, boolean fireEvents) {
    final String oldValue = getValue();
    
    textArea.setHTML(value != null ? value : "");
    
    if (fireEvents) {
      ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setHeight(String height) {
    double newWidth = JepClientUtil.extractLengthValue(height);
    textArea.setHeight((newWidth - toolbar.getOffsetHeight()) + Unit.PX.getType());
    super.setHeight((newWidth + 2) + Unit.PX.getType());
  }
  
  /**
   * Сброс состояния поля
   */
  public void reset(){
    toolbar.reset();
  }
}
