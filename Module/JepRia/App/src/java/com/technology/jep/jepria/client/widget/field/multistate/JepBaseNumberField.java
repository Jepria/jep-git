package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.util.JepClientUtil.getChar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.ValueBox;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле для ввода произвольных чисел.
 * Вводимыми символами данного поля могут быть только цифры (запрет ввода точек для целочисленных типов и прочих спецсимволов)
 * Если необходимо запретить ввод отрицательных значений, то это делается методом setAllowNegative(false)
 * 
 * Особенности:<br/>
 * Необходимость данной абстракции обусловлена тем, чтобы в прикладном коде избавиться от потребности генерализации поля {@link JepNumberField}
 */
public abstract class JepBaseNumberField<E extends ValueBox<? extends Number>> extends JepBaseTextField<E> {
  
  /**
   * Набор допустимых символов для ввода.
   */
  private List<Character> allowed = new ArrayList<Character>();
  
  /**
   * Набор цифр.
   */
  private String digitChars = "0123456789";
  
  /**
   * Разделитель целой и десятичной частей.
   */
  protected String decimalSeparator = LocaleInfo.getCurrentLocale().getNumberConstants().decimalSeparator();
  
  /**
   * Разделитель тысячных разрядов.
   */
  protected String groupingSeparator = LocaleInfo.getCurrentLocale().getNumberConstants().groupingSeparator();
  
  /**
   * Признак возможности ввода десятичного разделителя.
   */
  protected boolean allowDecimals = true;
  
  /**
   * Признак возможности ввода отрицательных значений.
   */
  private boolean allowNegative = true;
  
  /**
   * Формат вывода чисел.<br/>
   * По умолчанию не задан (null).
   */
  private NumberFormat numberFormat = null;
  
  /**
   * Знак минус.
   */
  private static final char MINUS = '-';
  
  /**
   * Наименование селектора (класса стилей) текстового поля.
   */
  private static final String TEXT_FIELD_STYLE = "gwt-TextBox";
  
  @Deprecated
  public JepBaseNumberField() {
    this(null);
  }
  
  @Deprecated
  public JepBaseNumberField(String fieldLabel) {
    this(null, fieldLabel);
  }
  
  public JepBaseNumberField(String fieldIdAsWebEl, String fieldLabel) {
    super(fieldIdAsWebEl, fieldLabel);

    // Проинициализируем список допустимых символов.
    initAllowedInputCharacters();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(Object value) {
    Object newValue = value;
    if (!JepRiaUtil.isEmpty(newValue)){
      // Проверка важна для наследников, чтобы не изменить текущее значение.
      if (editableCard instanceof DoubleBox){
        newValue = Double.valueOf(newValue.toString());
      }
    }
    super.setValue(newValue);
  }
  
  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public Number getValue() {
    return editableCard.getValue();
  }

  /**
   * Установка значения для карты Просмотра.<br/>
   * <br/>
   * Если для поля задано значение формата вывода чисел, то значение устанавливается в соответствии 
   * с этим форматом. В противном случае выводится строковое значение, возвращаемое методом toString().<br/>
   * <br/>
   * При перегрузке данного метода в наследниках необходимо обеспечить, чтобы данный метод был быстрым/НЕ ресурсо-затратным.<br/>
   * <br/>
   * Основная идея Jep-полей: они должны быть легкими. Поэтому, карта Просмотра должна быть именно текстовым (или простым Html) представлением
   * значения карты Редактирования.<br/>
   * В тех случаях, когда чисто текстовое представление нецелесообразно (списки, деревья и т.п.) - в поле используется ТОЛЬКО одна карта - 
   * карта Редактирования (т.е. карта Просмотра - вообще НЕ используется).
   *
   * @param value значение для карты Просмотра
   */
  @Override
  protected void setViewValue(Object value) {
    if (numberFormat != null) {
      viewCard.setHTML(value != null ? numberFormat.format((Number) value) : null);
    } else {
      viewCard.setHTML(value != null ? value.toString() : null);
    }
  }
  
  /**
   * Обработка события ввода символов в поле.
   * 
   * @param event  срабатываемое событие
   * @return true - если ввод символов не был отменен, иначе - прерван
   */
  @Override
  protected boolean keyPressEventHandler(DomEvent<?> event){
    boolean result = super.keyPressEventHandler(event); 
    if (result){
      /*
       * Firefox имеет особенность: событие KeyPress генерируется не только при нажатии
       * на алфавитно-цифровые клавиши, поэтому необходимо проверять значение charCode 
       * на равенство нулю.
       */
      NativeEvent nativeEvent = event.getNativeEvent();
      if (nativeEvent.getCharCode() == 0){
        return true;
      }
      /*
       * Не реагируем, если нажата одна из клавиш Alt, Ctrl или Meta, 
       * иначе не будут работать сочетания клавиш наподобие Ctrl-C, Ctrl-V.
       */
      if (nativeEvent.getAltKey() || nativeEvent.getCtrlKey() || nativeEvent.getMetaKey()) {
        return true;
      }
      
      char currentCharacter = getChar(nativeEvent);
      if(!allowed.contains(currentCharacter)) {
        event.preventDefault();
        return false;
      }
      else {
        String symbol = String.valueOf(currentCharacter);
        // Если введен нецифровой символ. 
        if (!digitChars.contains(symbol)){
          // Если знак минус или разделитель разрядов уже 
          // присутствуют - запрещаем ввод
          if (getRawValue().contains(symbol) && 
              !editableCard.getSelectedText().contains(symbol)) {
            event.preventDefault();
            return false;
          }
          // Если вводимый символ - минус, то он может стоять 
          // на первой позиции
          if (currentCharacter == MINUS &&
              editableCard.getCursorPos() != 0){
            event.preventDefault();
            return false;
          }
        }
      }
    }
    return result;
  }
  
    
  /**
   * Получение формата вывода чисел.
   *
   * @return формат вывода чисел
   */
  public NumberFormat getNumberFormat() {
    return numberFormat;
  }

  /**
   * Установка формата вывода чисел.
   *
   * @param numberFormat формат вывода чисел
   */
  public void setNumberFormat(NumberFormat numberFormat) {
    this.numberFormat = numberFormat;
  }
  
  /**
   * Returns true if negative values are allowed.
   * 
   * @return the allow negative value state
   */
  public boolean getAllowNegative() {
    return allowNegative;
  }

  /**
   * Sets whether negative value are allowed.
   * 
   * @param allowNegative true to allow negative values
   */
  public void setAllowNegative(boolean allowNegative) {
    this.allowNegative = allowNegative;
    
    // Если ввод отрицательных чисел разрешен, то добавим в список допустимых символов знак минуса, если это необходимо.
    if (allowNegative){
      if (!allowed.contains(MINUS)){
        allowed.add(MINUS);
      }
    } // иначе - удалим, если такой встречается. 
    else {
      // проверка на contains необязательна, поскольку, если элемент отсутствует в списке, то ничего не произойдет.
      allowed.remove(Character.valueOf(MINUS));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid() {
    boolean isValid = super.isValid();
    try {
        
      Object value = editableCard.getValueOrThrow();
      // Проверка на наличие недопустимых символов необходима
      // для случаев копирования значения из буфера обмена.
      if (!JepRiaUtil.isEmpty(value)){
        String strValue = editableCard.getText();
        for (int index = 0; index < strValue.length(); index++){
          if (!allowed.contains(strValue.charAt(index))){
            throw new ParseException(null, -1);
          }
        }
      }
    }
    catch(ParseException e){
      markInvalid(JepClientUtil.substitute(JepTexts.numberField_nanText(), getRawValue()));
      return false;
    }
    return isValid;
  }

  /**
   * Проверяет, содержит ли поле допустимое значение. <br>
   * Предварительно очищает сообщение об ошибке. Если поле является
   * обязательным, а введённое значение пусто, устанавливает сообщение об
   * ошибке и возвращает false.
   *
   * @param value проверяемое значение
   * @return true - если поле содержит допустимое значение, false - в  противном случае
   */
  public boolean isValid(String value) {
    boolean isValid = super.isValid();
    try {
      // Проверка на наличие недопустимых символов необходима
      // для случаев копирования значения из буфера обмена.
      if (!JepRiaUtil.isEmpty(value)){
        String strValue = value;
        for (int index = 0; index < strValue.length(); index++){
          if (!allowed.contains(strValue.charAt(index))){
            throw new ParseException(null, -1);
          }
        }
      }
    }
    catch(ParseException e){
      markInvalid(JepClientUtil.substitute(JepTexts.numberField_nanText(), getRawValue()));
      return false;
    }
    return isValid;
  }
  
  /**
   * Инициализация списка допустимых символов.
   */
  protected void initAllowedInputCharacters() {
    // Разрешаем ввод всех числовых символов.
    for (int i = 0; i < digitChars.length(); i++) {
      allowed.add(digitChars.charAt(i));
    }
    // Если разрешен ввод отрицательных символов.
    if (allowNegative) {
      allowed.add(MINUS);
    }
    // Если разрешен ввод десятичных чисел.
    if (allowDecimals) {
      for (int i = 0; i < decimalSeparator.length(); i++) {
        allowed.add(decimalSeparator.charAt(i));
      }
    }
  }
  

  /**
   * {@inheritDoc}
   * 
   * Особенности:<br/>
   * Карте редактирования присваиваем стиль TextBox для единообразия.
   */
  @Override
  protected void applyStyle(){
    // Переопределяем стиль поля как TextBox.
    editableCard.addStyleName(TEXT_FIELD_STYLE);
    
    super.applyStyle();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void addChangeValueListener() {
    editableCard.addValueChangeHandler(valueChangeEvent -> {
      notifyListeners(JepEventType.CHANGE_VALUE_EVENT, new JepEvent(JepBaseNumberField.this, valueChangeEvent.getValue()));
    });
  }
}
