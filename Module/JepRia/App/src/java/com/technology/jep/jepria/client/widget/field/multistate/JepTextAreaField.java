package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.TEXT_AREA_STYLE;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Поле для ввода многострочного текста.
 */
public class JepTextAreaField extends JepBaseTextField<TextArea> {
  
  private final static int DEFAULT_TEXTAREA_FIELD_HEIGHT = 90;
  
  public JepTextAreaField() {
    this(null);
  }
  
  public JepTextAreaField(String fieldLabel) {
    this(null, fieldLabel);
  }
  
  public JepTextAreaField(String fieldIdAsWebEl, String fieldLabel) {
    super(fieldIdAsWebEl, fieldLabel);
    // установка высоты по умолчанию
    setFieldHeight(DEFAULT_TEXTAREA_FIELD_HEIGHT);
  }
  
  /**
   * Метод добавляющий на панель Редактирование соответствующее поле (компонент) Gxt.<br/>
   * Перегружается в наследниках для добавления соответсвующих наследникам полей Gxt.
   */
  @Override
  protected void addEditableCard() {
    editableCard = new TextArea();
    editablePanel.add(editableCard);

    // Добавляем обработчик события "опускания клавиши" для осуществления перевода строки по нажатию Enter.
    initKeyDownHandler();
  }
  
  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public String getValue(){
    return editableCard.getValue();
  }
  
  /**
   * Установка значения для карты Просмотра.<br/>
   * Установка производится выражением: value.toString().replace("\n", "&lt;br/&gt;") . 
   * Замена символов происходит для корректного перевода строки в Html-отображении.
   *
   * @param value значение для карты Просмотра
   */  
  @Override
  protected void setViewValue(Object value) {
    super.setViewValue(value);
    viewCard.setHTML(value != null ? viewCard.getHTML().replace("\n", "<br/>") : null);
  }

  /**
   * {@inheritDoc}
   * 
   * Особенности:<br/>
   * Для данного компонента разрешено нажатия кнопки Enter, но при этом нативное событие перекрывается.
   */
  protected boolean keyDownEventHandler(DomEvent<?> event){
    boolean result = super.keyDownEventHandler(event); 
    if (result){
      if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
        event.stopPropagation();
      }
    }
    return result;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void applyStyle(){
    super.applyStyle();
    
    editableCard.addStyleName(TEXT_AREA_STYLE);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setFieldHeight(int fieldHeight) {
    editableCard.setHeight(fieldHeight + Unit.PX.getType());
  }
}
