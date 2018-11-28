package org.jepria.server.data;

/**
 * Тип вхождения подстроки
 */
public enum MatchType {
  /**
   * Тип поиска &laquo;По точному совпадению&raquo;.
   */
  MATCHES,

  /**
   * Тип поиска &laquo;По первым символам&raquo;.
   */
  STARTS,

  /**
   * Тип поиска &laquo;По последним символам&raquo;.
   */
  ENDS,

  /**
   * Тип поиска &laquo;По вхождению символов&raquo;.
   */
  CONTAINS;
}
