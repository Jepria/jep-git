package com.technology.jep.jepria.client.widget.field.masked;

import static com.technology.jep.jepria.client.widget.field.masked.MaskItemType.LITERAL;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.Event;
import com.technology.jep.jepria.client.widget.field.masked.Mask.MaskItem;
import com.technology.jep.jepria.client.widget.field.multistate.event.InputForbiddenEvent;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

public class MaskedTextBoxMobile extends MaskedTextBox {
  
  public MaskedTextBoxMobile(String mask) {
    super(mask);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid() {
    boolean isValid = true;
    String value = getText();
    isValid = mask.match(value.toCharArray(), false);
    return isValid || isEmpty();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isEmpty() {
    String value = getText();
    return JepRiaUtil.isEmpty(value) || mask.getText(new char[mask.size()], true).equals(value);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getValue() {
   char[] charValue = getText().toCharArray();
    if (mask.match(charValue, false)) {
      return mask.getText(charValue, false);
    }
    else {
      return null;
    }
  }
  
  /**
   * Сохраняем значение поля ввода при нажатии клавиши
   */
  private String beforeValue;
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void onKeyDownEvent(KeyDownEvent event) {
    beforeValue = getText();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void onKeyPressEvent(KeyPressEvent event) {
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void onPasteEvent(Event event) {
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void onKeyUpEvent(KeyUpEvent event) {
    if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
      int position = getCursorPos();
      MaskItem item = mask.items.get(position);
      if (item.itemType != LITERAL && position >= 0) {
        char[] chars = replaceCharByPos(beforeValue, position, '\0');
        beforeValue = mask.getText(chars, true);
      }
      setText(new String(beforeValue));
      setCursorPos(position);
    } else {
      handlerPostInput(getText(), beforeValue, getCursorPos());
    }
  }
  
  /**
   * Разница между двумя строками
   * @param value1 первое сравниваемое значение
   * @param value2 второе сравниваемое значение
   * @return char предполагается, что разница будет в один символ
   */
  protected char diffValues(String value1, String value2) {
    char result = 0;
    // suppose value1 always more then value2
    for (int i = 0; i < value1.toCharArray().length; i++) {
      char ch_ = value1.toCharArray()[i];
      if (i > value2.length()) {
        result = ch_;
        break;
      }
      char _ch = value2.toCharArray()[i];
      if (ch_ != _ch) {
        result = ch_;
        break;
      }
    }
    return result;
  }
  
  /**
   * Заполнение массива маской отсутствующих значений, например: 12.11 -> 12.11.****
   * @param chars массив символов для привидения к маске
   * @param position позиция символа для маскировании 
   * @param correctPos сдвиг позиции 
   * @return массив символов с маскированием отсутствующих символов
   */
  protected char[] fillValueByMask(char[] chars, int position, int correctPos) {
    MaskItem item = mask.items.get(position);
    char[] newChars = new char[mask.size()];
    for (int i = 0; i < position; i++) {
      newChars[i] = chars[i];
    }
    if (item.mandatory) {
      newChars[position] = '\0';
      for (int i = position + 1; i < mask.size(); i++) {
        char currCH = chars[i + correctPos];
        newChars[i] = i + correctPos < chars.length ? currCH : '\0';
      }
    }
    else {
      for (int i = position; i < mask.size() - 1; i++) {
        newChars[i] = i + 1 < chars.length ? chars[i+1] : '\0';
      }
    }
    
    return newChars;
  }
  
  /**
   * Заменя символа в массиве по позиции
   * @param value Строка для замены
   * @param position позиция замены
   * @param replacedChar символ замены
   * @return массив символов с результатом подстановки
   */
  protected char[] replaceCharByPos(String value, int position, char replacedChar) {
    char[] chars = new char[mask.size()];
    for (int i = 0; i < mask.size(); i++) {
      if (i < value.length()) {
        chars[i] = position == i ? replacedChar : value.toCharArray()[i];
      } else {
        chars[i] = '\0';
      }
      
    }
    return chars;
  }
  
  /**
   * Форматирование текстового значения по маске на основе разницы двух текстовых полей и позиции курсора
   * @param postValue значение поля после ввода с клавиатуры
   * @param preValue значение поля перед вводом с клавиатру
   * @param position позиция воода
   */
  protected void handlerPostInput(String postValue, String preValue, int position) {
    int correctedPosition = position > 0 ? position - 1 : 0;
    char currentCharacter = diffValues(postValue, preValue); // получаем введенный символ, путем вычисления разницы текущего и предыдущего состояния поля
    
    if (Mask.inRange(currentCharacter, 48,57)
          && mask.size() >= position 
          && preValue.toCharArray().length <= mask.size()) {
      
      char[] newRawValue = new char[mask.size()];
      int newCurPos = correctedPosition;
      String result = new String(mask.getText(newRawValue, true));
      try {
        // если длина текста в поле меньше, чем длина маски 
        if (preValue.toCharArray().length < mask.size()) {
          char[] temp = new char[mask.size()];
          // просто берем что получилось после ввода и пытаемся проверить на соответствие маски
          temp = postValue.toCharArray();
          boolean isValid = mask.match(temp, true);
          
          if (isValid) {
            // если проверку прошли, то либо, если длина введеного значения соответствует длине маски, передаем как есть, 
            // либо маскируем отсутствующие значения по маске 
            newRawValue = temp.length == mask.size() ? temp : fillValueByMask(temp, position, 0);
            // вычисляем позицию курсора после форматирования введеного значения
            newCurPos = position == mask.size() ? correctedPosition : mask.getCursorPositionOnInsert(newRawValue, correctedPosition, currentCharacter);
            result = mask.getText(newRawValue, true);
          } else {
            // если проверку не прошли первый раз, то пытаемся предсказать что пользователь имел ввиду
            char[] tempValue = fillValueByMask(temp, position, -1); // после введенного значения подставляем символ маски, т.о. предполагаем, что до полной маски не хватает одного символа
            isValid = mask.match(tempValue, true);
            
            if (isValid) {
              newRawValue = tempValue;
              newCurPos = mask.getCursorPositionOnInsert(newRawValue, correctedPosition, currentCharacter);
              result = mask.getText(newRawValue, true);
            } else {// если не удалось подтвердить предположение, что имел ввиду пользователь, то возвращаем предыдущее состояние поля ввода
              
              newRawValue = preValue.toCharArray();
              newCurPos = correctedPosition;
              result = preValue;
            } 
            
          }
        } else {
            // стандартная реализация подстановки введенного символа
            newRawValue = mask.insertChar(mask.removeChar(preValue.toCharArray(), correctedPosition), correctedPosition, currentCharacter);
            newCurPos = mask.getCursorPositionOnInsert(newRawValue, correctedPosition, currentCharacter);
            result = mask.getText(newRawValue, true);
        }
      } catch(Exception e) {
        // возвращаем предыдущее состояние поля
        newRawValue = preValue.toCharArray();
        newCurPos = position;
        result = mask.getText(newRawValue, true);
      } finally {
        // фиксируем изменения
        setText(result);
        setCursorPos(newCurPos > result.length() ? result.length() : newCurPos);
      }

    } else {
      setText(preValue);
      setCursorPos(position > 0 ? position - 1 : 0);
      fireEvent(new InputForbiddenEvent());
    }
  }
 
  /**
   * Подтверждаем, что своим функ-м можем поддержать соответствующую мобильную платформу
   * поддерживаются следующие платформы: 
   * <br> - Android
   * <br> - iPhone
   * @return если Истина, то поддерживаем соответствующую платформу, Ложь - не поддерживаем
   */
  public static native boolean isSupportedMobilePlatform() /*-{
    return navigator.userAgent.match(/Android/i)
       || window.navigator.userAgent.indexOf('iPhone') != -1;
  }-*/;
}