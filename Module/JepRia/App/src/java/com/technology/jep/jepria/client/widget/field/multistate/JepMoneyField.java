package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.util.JepClientUtil.isSpecialKey;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DECIMAL_FORMAT;

import java.math.BigDecimal;
import java.text.ParseException;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.field.BigDecimalBox;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле для ввода числа денежного формата.
 */
public class JepMoneyField extends JepBaseNumberField<BigDecimalBox> {

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
   * {@inheritDoc}
   */
  @Override
  protected void addEditableCard() {
    editableCard = new BigDecimalBox(){
      @Override
      public void setValue(BigDecimal value) {
        super.setText(BigDecimalRenderer.instance().render(value).replaceAll(groupingSeparator, ""));
      }
      
      @Override
      public BigDecimal getValueOrThrow() throws ParseException {
        String text = getText();
        Double parseResult = null;
        if (!JepRiaUtil.isEmpty(text)) {
            parseResult = getNumberFormat().parse(text.replaceAll("\\" + DECIMAL_RANK_SEPARATOR, ""));
        }
        
        if(parseResult == null)
            return null;
        else
            return new BigDecimal(parseResult);
      }
    };
    
    // Переопределяем обработчик поднятия клавиши (сигнатура метода отлична от определенного в родителе - KeyUpEvent)
    editableCard.addKeyUpHandler(new KeyUpHandler() {
        @Override
        public void onKeyUp(KeyUpEvent event) {
            keyUpEventHandler(event);
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
  
  
  final char MARKER_SYMBOL = '?';
  final char DECIMAL_RANK_SEPARATOR = ' ';
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
  protected boolean keyPressEventHandler(DomEvent<?> event){
    boolean result = super.keyPressEventHandler(event);
    
    if (result){
      if (!numberFormating(event.getNativeEvent().getCharCode())) {
          event.preventDefault();
      }
    }
    return result;
  }
  
  protected void keyUpEventHandler(KeyUpEvent event) {
    int keyCode = event.getNativeKeyCode();
    if(delayedTask != null) {
      if (isModifierKey(keyCode) || !isSpecialKey(event)) {
        startTypingTimeout();
      }
    } else {
      if (isModifierKey(keyCode)) {
        if (!numberFormating(keyCode)) {
            event.preventDefault();
        }
      }
    }
  }
  
  /**
   * Форматирование введенного числа с разделителем тысячных групп разрядов числа и дробной частью до сотых  
   * @param keyCode
   * @return
   */
  protected boolean numberFormating(int keyCode) {
      final StringBuilder sb = new StringBuilder();
      sb.append(String.valueOf(getRawValue()));
      
      boolean isModifierKey = isModifierKey(keyCode);
      
      if (!isModifierKey) {
          sb.insert(editableCard.getCursorPos(), String.valueOf((char) keyCode));
      }
      
      String aux = sb.toString();
      int currentPosCursor = editableCard.getCursorPos();
      
      StringBuilder valueEdit = new StringBuilder();
      
      valueEdit.append(String.valueOf(getRawValueWithSeparatorRanks()));
      
      // Удаление выделенного текста
      if (editableCard.getSelectedText() != null && editableCard.getSelectedText().length() > 0) {
          String selectedText = editableCard.getSelectedText();
          valueEdit.replace(currentPosCursor, currentPosCursor + selectedText.length(), "");
      }
      
      if (!isModifierKey) {
          valueEdit.insert(currentPosCursor, MARKER_SYMBOL);
      }
      
      String value = valueEdit.toString();
      boolean posCursorAfterDecimalSeparator = currentPosCursor > value.indexOf(decimalSeparator);
      boolean alreadyHasDecimalSeparator = value.indexOf(decimalSeparator) > 0;
      // дополнительно проверяем, что мы находимся в области редактирования дробной части
      if (posCursorAfterDecimalSeparator && alreadyHasDecimalSeparator)
          if (!isDecimalPartValid(aux)) {
            return false;
          }
      
      // проверяем,, что позиция курсора находится в целой части числа, необходимо для работы только форматирования целой части числа
      posCursorAfterDecimalSeparator = currentPosCursor > value.indexOf(decimalSeparator);
      alreadyHasDecimalSeparator = value.indexOf(decimalSeparator) > 0;
      if (!JepRiaUtil.isEmpty(getRawValueWithSeparatorRanks())) {
        if (!String.valueOf((char) keyCode).contains(decimalSeparator) ) {
          if (posCursorAfterDecimalSeparator && alreadyHasDecimalSeparator) {
              return true;
          }

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
          
          decimalPart1 = decimalPart1.replaceAll("\\s", "");
          
          //непосредственное форматирование введенных значений
          String reverseText = new StringBuilder(decimalPart1).reverse().toString();
          StringBuilder formatNumber = new StringBuilder();
          char[] charArray = reverseText.toCharArray();
          long sizeText = reverseText.length();
          for (int i = 0; i < sizeText; i++) {
              char ch = charArray[i];
              if (i % 3 == 0 && i > 0) {
                  formatNumber.append(DECIMAL_RANK_SEPARATOR);
              } 
              formatNumber.append(ch);
          }
          
          // меняем направление текста
          String fromatedText = formatNumber.reverse().toString();
          
          // вычисляем позицию маркера 
          int evaluatingPositionCursor = currentPosCursor;
          if (!isModifierKey) {
              evaluatingPositionCursor = fromatedText.indexOf(MARKER_SYMBOL);
          } else {
              evaluatingPositionCursor = evaluatingPositionCursor > fromatedText.length() && evaluatingPositionCursor > 0 ? evaluatingPositionCursor - 1 : evaluatingPositionCursor;
          }
          
          // удаляем маркер
          if (!isModifierKey) {
              fromatedText = fromatedText.replace(String.valueOf(MARKER_SYMBOL), "");
          }
          
          getEditableCard().setText(fromatedText + (has2Parts ? decimalSeparator + decimalPart2 : ""));
          getEditableCard().setCursorPos(evaluatingPositionCursor);
      } 
    }
    return true;
  }
  
  /**
   * Проверка что код нажатой кнопки принадлежит к группе редактируемых кнопок - DELTE или BACKSPACE
   * @param keyCode
   * @return
   */
  protected boolean isModifierKey(int keyCode) {
    return keyCode == KeyCodes.KEY_BACKSPACE || keyCode == KeyCodes.KEY_DELETE;
  }
  
  @Override
  public String getRawValue() {
    String value = getInputElement().getPropertyString("value").replaceAll(" ", "");
    return value;
  }
  
  public String getRawValueWithSeparatorRanks() {
    String value = getInputElement().getPropertyString("value");
    return value;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid(){
    String value = editableCard.getText().replaceAll("\\" + DECIMAL_RANK_SEPARATOR, "");
    boolean isValid = super.isValid(value);
    try {
      // Проверка на наличие недопустимых символов необходима
      // для случаев копирования значения из буфера обмена.
      if (!isDecimalPartValid(value)) {
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
      final String[] vector = value.split("\\" + decimalSeparator, 2);
      if (vector.length > 1) {
        final String decimal = vector[1];
        //запрет ввода символов, если длина второй части строки превышает заданное количество
        if (decimal.length() > maxNumberCharactersAfterDecimalSeparator) {
          return false;
        }
      }
    }
    return true;
  }
}
