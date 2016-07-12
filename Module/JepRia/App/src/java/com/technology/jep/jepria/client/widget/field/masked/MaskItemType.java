package com.technology.jep.jepria.client.widget.field.masked;

/**
 * Типы символов, используемых в маске.
 */
public enum MaskItemType {
  /**
   * Тип символа - буква или цифра.
   */
  LETTER_OR_DIGIT,
  /**
   * Тип символа - любой.
   */
  CHAR,
  /**
   * Тип символа - буква.
   */
  LETTER,
  /**
   * Тип символа - цифра.
   */
  DIGIT,
  /**
   * Тип символа - знак (+ или -) или цифра.
   */
  SIGN_OR_DIGIT,
  /**
   * Тип символа - литерал.
   */
  LITERAL  
}
