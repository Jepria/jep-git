package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.util.JepClientUtil.isSpecialKey;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DECIMAL_FORMAT;

import java.math.BigDecimal;
import java.text.ParseException;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Event;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.field.BigDecimalBox;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле для ввода числа денежного формата.
 */
public class JepMoneyField extends JepBaseNumberField<BigDecimalBox> {


  /**
   * Символ тысячного разделителя 
   */
  private static final char DECIMAL_RANK_SEPARATOR = ' ';
  
  /**
   * Количество символов, разрешенных для ввода после разделителя разрядов (по умолчанию, 2)
   */
  private Integer maxNumberCharactersAfterDecimalSeparator = 2;
  
  public JepMoneyField(){
    this(null);
  }
  
  public JepMoneyField(String fieldLabel) {
    this(null, fieldLabel);
  }
  
  public JepMoneyField(String fieldIdAsWebEl, String fieldLabel) {
    super(fieldIdAsWebEl, fieldLabel);
    // Установка формата числа.
    setNumberFormat(NumberFormat.getFormat(DEFAULT_DECIMAL_FORMAT));
  }
  
  /**
   * Для сохранения предыдущего значния при вствке из буфера обмена
   */
  private String previewValue = null;
  /**
   * {@inheritDoc}
   */
  @Override
  protected void addEditableCard() {
    editableCard = new BigDecimalBox() {
      {
        Event.sinkEvents(getElement(), Event.ONPASTE);
      }
      
      private void pasteHandler(Event event) {
        String temp = getRawValueWithSeparatorRanks();
        if (isDecimalPartValid(temp.replaceAll("\\" + DECIMAL_RANK_SEPARATOR, ""))) {
          previewValue = temp;
        }
      }
      
      @Override
      public void onBrowserEvent(Event event) {
        switch (event.getTypeInt()) {
          case Event.ONPASTE: {
            pasteHandler(event);
            break;
          }
          default: 
            super.onBrowserEvent(event);
          }
      }
      
      @Override
      public void setValue(BigDecimal value) {
        String text = BigDecimalRenderer.instance().render(value).replaceAll(groupingSeparator, "");
        if (!JepRiaUtil.isEmpty(text)) {
          formatNumber(text, -1);
        } else {
          super.setText(text);
        }
      }
      
      @Override
      public BigDecimal getValueOrThrow() throws NumberFormatException {
        String text = getText();
        BigDecimal parseResult = null;
        if (!JepRiaUtil.isEmpty(text)) {
          try {
            String prepareText = text.replaceAll("\\" + DECIMAL_RANK_SEPARATOR, "");
            getNumberFormat().parse(prepareText);
            parseResult = new BigDecimal(prepareText);
          } catch(NumberFormatException e) {
            throw e;
          }
        }
        
        return parseResult;
      }
    };
    
    // Переопределяем обработчик поднятия клавиши (сигнатура метода отлична от определенного в родителе - KeyUpEvent)
    editableCard.addKeyUpHandler(new KeyUpHandler() {
        @Override
        public void onKeyUp(KeyUpEvent event) {
            keyUpEventHandler(event);
        }
    });
    
    editableCard.addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
          keyDownEventHandler(event);
      }
    });
      
    editablePanel.add(editableCard);
    
    // Добавляем обработчик события "нажатия клавиши" для проверки ввода символов.
    initKeyPressHandler();
  }
  
    /**
   * Получение разделителя разрядов
   * 
   * @return разделитель разрядов
   */
  public String getGroupingSeparator() {
    return groupingSeparator;
  }
  
  /**
   * Установка разделителя разрядов
   * 
   * @param groupingSeparator разделитель разрядов
   */
  public void setGroupingSeparator(String groupingSeparator) {
    this.groupingSeparator = groupingSeparator;
  }
  
  /**
   * Получение количества разрешенных для ввода символов после точки
   * 
   * @return количество разрешенных для ввода символов после точки
   */
  public Integer getMaxNumberCharactersAfterDecimalSeparator() {
      return maxNumberCharactersAfterDecimalSeparator;
  }
  
  /**
   * Установка количества разрешенных для ввода символов после точки
   * 
   * @param maxNumberCharactersAfterDecimalSeparator  количество разрешенных для ввода символов после точки
   */
  public void setMaxNumberCharactersAfterDecimalSeparator(
      Integer maxNumberCharactersAfterDecimalSeparator) {
      this.maxNumberCharactersAfterDecimalSeparator = maxNumberCharactersAfterDecimalSeparator;
  }  
  
  /**
   * Установка значения для карты Просмотра.<br/>
   * Метод переопределён, чтобы в качестве разделителя групп разрядов использовался пробел.
   * @param value значение для карты Просмотра
   */
  @Override
  protected void setViewValue(Object value) {
    super.setViewValue(value);
    viewCard.setHTML(value != null ? viewCard.getHTML().replaceAll(groupingSeparator, " ") : null);
  }
  
  
  /**
   * Обработка события ввода символов в поле.<br/>
   * Особенность :
   * <ul>
   *  <li>данное событие вешается по умолчанию на карту для редактирования, 
   * при этом проверяется сколько введено символов после десятичного разделителя точки.</li>
   * </ul>
   * 
   * @param event  срабатываемое событие
   * @return true - если ввод символов не был отменен, иначе - прерван
   */
  @Override
  protected boolean keyPressEventHandler(DomEvent<?> event) {
    boolean result = super.keyPressEventHandler(event);
    if (result){
      if (!checkNumberFormat(event.getNativeEvent().getCharCode())) {
          event.preventDefault();
      }
    }
    return result;
  }
  
  /**
   *  Признак, что была надата комбинация кнопок SHIFT+INSERT
   */
  private boolean isPressedSHIFTplusINSERT = false;
  
  /**
   * Обработчик опускания кнопок
   */
  
  @Override
  protected boolean keyDownEventHandler(DomEvent<?> event) {
    boolean result  = super.keyDownEventHandler(event);
    if (result) {
      int keyCode = event.getNativeEvent().getKeyCode();
      if( event.getNativeEvent().getShiftKey() && keyCode == KeyCodes.KEY_INSERT) {
        isPressedSHIFTplusINSERT = true;
      }
      
    }
    return result;
  }
  
  /**
   * Обработчик поднятия кнопок
   */
  protected void keyUpEventHandler(KeyUpEvent event) {
    int keyCode = event.getNativeKeyCode();
    
    if (!isSpecialKey(keyCode) || isModifierKey(keyCode) || isPressedSHIFTplusINSERT) {
      setFormatedText(keyCode);
      isPressedSHIFTplusINSERT = false;
    }
  }
  
  /**
   * Форматирования текста и установка курсора со смещением
   * @param keyCode
   */
  private void setFormatedText(int keyCode) {
    int currentPosCursor = getEditableCard().getCursorPos();
    int shift = formatNumber(getRawValueWithSeparatorRanks(), keyCode);
    currentPosCursor += shift;
    getEditableCard().setCursorPos(currentPosCursor);
  }
  
  /**
   * Форматирование числа
   * @param value
   * @param keyPressed
   * @return смещение курсора после форматирования
   */
  protected int formatNumber(String value, int keyPressed) {
    if (!JepRiaUtil.isEmpty(value)) {
      // Получаем текущюю позицию курсора
      int currentPositionCursor = editableCard.getCursorPos();
      // Вычисляем пол-во разделителей до форматирования
      int countSpacesBefore = value.split("\\" + DECIMAL_RANK_SEPARATOR).length - 1;
      // удаляем тысячные разделители 
      value = value.replaceAll("\\" + DECIMAL_RANK_SEPARATOR, "");
      // разделяем целую часть числа от дровной
      String[] parts = value.split("\\" + decimalSeparator, 2);
      String decimalPart1, decimalPart2 = "";
      boolean has2Parts = false;
      if (parts.length == 2 ) {
        decimalPart1 = parts[0];
        decimalPart2 = parts[1];
        has2Parts = true;
      } else {
        decimalPart1 = parts[0];
      }
      
      //непосредственное форматирование введенных значений
      String reverseText = new StringBuilder(decimalPart1).reverse().toString();
      StringBuilder formatNumber = new StringBuilder();
      char[] charArray = reverseText.toCharArray();
      long sizeText = reverseText.length();
      int countSpacesAfter = 0;
      for (int i = 0; i < sizeText; i++) {
        char ch = charArray[i];
        if (i % 3 == 0 && i > 0) {
          formatNumber.append(DECIMAL_RANK_SEPARATOR);
          ++countSpacesAfter;
        } 
        formatNumber.append(ch);
      }
      
      // Проверяем, что отформатированный текст по длине не превыщает ограничения 
      if (formatNumber.length() + (!decimalPart2.equals("") ? decimalPart2.length() + 1 : 0) >  getMaxLength()) {
        formatNumber = formatNumber.reverse().delete(getMaxLength(), formatNumber.length());
        return formatNumber(formatNumber.toString(), keyPressed);
      }
      
      // меняем направление текста
      String fromatedText = formatNumber.reverse().toString();
      String temp = fromatedText.trim() + (has2Parts ? decimalSeparator + decimalPart2 : "");
      if (!isDecimalPartValid(temp.replaceAll("\\" + DECIMAL_RANK_SEPARATOR, ""))) {
        if (previewValue != null) {
          getEditableCard().setText(previewValue);
        }
      } else {
        getEditableCard().setText(fromatedText.trim() + (has2Parts ? decimalSeparator + decimalPart2 : ""));
      }
      
      // Получаем длину отформатированного значения
      int lengthText = getRawValueWithSeparatorRanks().length();
      // вычисляем сдвиг позиции курсора после форматирования - для предсказуемого поведения курсора после форматирования 
      int shiftPosition = 0;
      if (countSpacesBefore > countSpacesAfter) {
        if (currentPositionCursor > lengthText) {
          shiftPosition = countSpacesBefore - countSpacesAfter;
        } else {
          shiftPosition = countSpacesAfter - countSpacesBefore;
        }
      } else if (countSpacesBefore < countSpacesAfter) {
        if (currentPositionCursor > lengthText) {
          shiftPosition = countSpacesBefore - countSpacesAfter;
        } else {
          if (keyPressed == KeyCodes.KEY_BACKSPACE) {
            shiftPosition = 0;
          } else if (keyPressed == KeyCodes.KEY_DELETE) {
            shiftPosition = countSpacesAfter - countSpacesBefore;
          } else {
            shiftPosition = countSpacesAfter - countSpacesBefore;
          }
          
        }
      }

      // Проверяем, что не вышли за допустимый диапозон
      if (currentPositionCursor + shiftPosition > lengthText) {
        if (currentPositionCursor > lengthText) {
          shiftPosition = lengthText - currentPositionCursor;
        } else {
          shiftPosition = currentPositionCursor - lengthText;
        }
      } else if (currentPositionCursor + shiftPosition < 0) {
        shiftPosition = 0;
      }
      return shiftPosition;
    } else {
      return 0;
    }
  }
  
  
  /**
   * Форматирование введенного числа с разделителем тысячных групп разрядов числа и дробной частью до сотых  
   * @param keyCode
   * @return true если вычисляемое значение число
   */
  protected boolean checkNumberFormat(int keyCode) {
    int currentPosCursor = editableCard.getCursorPos();
    
    StringBuilder value = replaceSelectedText("", true);
    
    StringBuilder temp = new StringBuilder(getRawValueWithSeparatorRanks());
    
    temp.insert(currentPosCursor, (char)keyCode);
    
    // Проверка на корректность введенного числа
    if (!isDecimalPartValid(temp.toString().replaceAll("\\" + DECIMAL_RANK_SEPARATOR, ""))) {
      return false;
    }
    
    if (value != null) {
      getEditableCard().setText(value.toString().trim());
      getEditableCard().setCursorPos(currentPosCursor > getRawValueWithSeparatorRanks().length() ? getRawValueWithSeparatorRanks().length() : currentPosCursor);
    }
    
    return true;
  }
    
  /**
   * Замещение выделенного текста указанным значением
   * @return возвращает позицию курсора после замещения выделенного текста 
   */
  protected StringBuilder replaceSelectedText(String newValue, boolean forcereplace) {
    StringBuilder targetValue = null;
    
    if (editableCard.getSelectedText() != null && editableCard.getSelectedText().length() > 0) {
      int currentPosCursor = editableCard.getCursorPos();
      targetValue = new StringBuilder(getRawValueWithSeparatorRanks());
      String selectedText = editableCard.getSelectedText();
      targetValue = targetValue.replace(currentPosCursor, currentPosCursor + selectedText.length(), newValue);
      if (forcereplace) {
        getEditableCard().setText(targetValue.toString());
      }
    }
    
    return targetValue;
  }
  /**
   * Проверка что код нажатой кнопки принадлежит к группе редактируемых кнопок - DELTE или BACKSPACE
   * @param keyCode
   * @return если надатая клавиша является BACKSPACE или DELETE, то возвращаем истину, иначе - ложь 
   */
  protected boolean isModifierKey(int keyCode) {
    return keyCode == KeyCodes.KEY_BACKSPACE || keyCode == KeyCodes.KEY_DELETE;
  }
  
  @Override
  public String getRawValue() {
    return getInputElement().getPropertyString("value").replaceAll("\\s", "");
  }
  
  /**
   * 
   * @return возвращает введенное значение без изменений - антипод методу getRawValue()
   */
  public String getRawValueWithSeparatorRanks() {
    return getInputElement().getPropertyString("value").trim();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid() {
    String value = editableCard.getText().replaceAll("\\" + DECIMAL_RANK_SEPARATOR, "");
    boolean isValid = super.isValid(value);
    try {
      // Проверка на наличие недопустимых символов необходима
      // для случаев копирования значения из буфера обмена.
      if (!isDecimalPartValid(value)) {
        throw new ParseException(null, -1);
      }
      if (value.contains(decimalSeparator) && value.length() == 1) {
        throw new ParseException(null, -1);
      }
    } catch(ParseException e) {
      markInvalid(JepClientUtil.substitute(JepTexts.numberField_nanText(), getRawValue()));
      return false;
    }
    return isValid;
  }
  
  /**
   * Метод, проверяющий является ли переданное значение десятичным числом.
   * 
   * @param value    проверяемое значение
   * @return true, если значение - десятичное, в противном случае - false
   */
  private boolean isDecimalPartValid(String value){
    if (!JepRiaUtil.isEmpty(value)) {
      //строку разбиваем посредством разделителя разрядов на 2 части
      final String[] vector = value.split(decimalSeparator);
      
      // Если ввели больше одного дробного разделителя
      if (vector != null && vector.length > 2) {
        return false;
      }
      if (value != null) {
        if (value.length() - (value.replace(decimalSeparator, "") != null ? value.replace(decimalSeparator, "").length() : 0) > 1) {
          return false;
        }
      }
      
      if (vector != null && vector.length > 0) {
        String decimal = vector[0];
        for (int i = 0; decimal != null && i < decimal.length(); ++i) {
          char ch = decimal.charAt(i);
          if (!Character.isDigit(ch)) {
             return false;
          }
       }
      }
      if (vector != null && vector.length == 2) {
        String fraction = vector[1];
        //запрет ввода символов, если длина второй части строки превышает заданное количество
        if (fraction != null && fraction.length() > maxNumberCharactersAfterDecimalSeparator) {
          return false;
        }
      }
    }
    return true;
  }
}
