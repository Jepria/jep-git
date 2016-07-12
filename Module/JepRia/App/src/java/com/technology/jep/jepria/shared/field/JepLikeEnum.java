package com.technology.jep.jepria.shared.field;

/**
 * Поддерживаемые типы поиска по текстовым полям.
 */
public enum JepLikeEnum {
  /**
   * Тип поиска &laquo;По точному совпадению&raquo;.
   */
  EXACT,

  /**
   * Тип поиска &laquo;По первым символам&raquo;.
   */
  FIRST,

  /**
   * Тип поиска &laquo;По последним символам&raquo;.
   */
  LAST,

  /**
   * Тип поиска &laquo;По вхождению символов&raquo;.
   */
  CONTAINS;
}
