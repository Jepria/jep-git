package com.technology.jep.jepria.shared.field;

/**
 * Поддерживаемые типы полей.<br/>
 * <br/>
 * <strong>Внимание!!!</strong><br/>
 * Для поддержания корректного функционирования механизма History ({@link com.technology.jep.jepria.client.history}) при добавлении нового типа 
 * данных в перечисление - необходимо модифицировать методы перевода объектов в строковое представление (в так называемый History Token)
 * {@link com.technology.jep.jepria.shared.history.JepHistoryToken#valueToToken(Object value)}, {@link #toHistoryToken()} и методы восстановления 
 * объектов из строкового представления (из так называемого History Token'а) 
 * {@link com.technology.jep.jepria.shared.history.JepHistoryToken#tokenToValue(String token)}, 
 * {@link #buildTypeFromToken(String token)}.
 */
public enum JepTypeEnum {
  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.String</code>.
   */
  STRING,

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.Integer</code>.
   */
  INTEGER,

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.Float</code>.
   */
  FLOAT,

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.Double</code>.
   */
  DOUBLE,

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.math.BigDecimal</code>.
   */
  BIGDECIMAL,

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.Boolean</code>.
   */
  BOOLEAN,

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.util.Date</code>.
   */
  DATE,

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * {@link com.technology.jep.jepria.shared.time.JepTime}.
   */
  TIME,
  
  DATE_TIME,
  
  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * {@link com.technology.jep.jepria.shared.field.option.JepOption}.
   */
  OPTION,

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса реализующего интерфейс
   * List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt;.
   */
  LIST_OF_OPTION,
  
  BINARY_FILE,
  TEXT_FILE,
  CLOB;

  /**
   * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.String</code>.
   */
  protected static final String STRING_TOKEN = "S";

  /**
   * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.Integer</code>.
   */
  protected static final String INTEGER_TOKEN = "I";

  /**
   * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.Float</code>.
   */
  protected static final String FLOAT_TOKEN = "F";

  /**
   * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.Double</code>.
   */
  protected static final String DOUBLE_TOKEN = "D";

  /**
   * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.math.BigDecimal</code>.
   */
  protected static final String BIGDECIMAL_TOKEN = "BD";

  /**
   * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.Boolean</code>.
   */
  protected static final String BOOLEAN_TOKEN = "B";

  /**
   * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.util.Date</code>.
   */
  protected static final String DATE_TOKEN = "DE";

  /**
   * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
   * {@link com.technology.jep.jepria.shared.time.JepTime}.
   */
  protected static final String TIME_TOKEN = "TE";
  
  protected static final String DATE_TIME_TOKEN = "DTE";
  
  /**
   * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
   * {@link com.technology.jep.jepria.shared.field.option.JepOption}.
   */
  protected static final String OPTION_TOKEN = "O";

  /**
   * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса реализующего интерфейс
   * List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt;.
   */
  protected static final String LIST_OF_OPTION_TOKEN = "LO";
  
  protected static final String BINARY_FILE_TOKEN = "BFE";
  protected static final String TEXT_FILE_TOKEN = "TFE";
  protected static final String CLOB_TOKEN = "C";
  
  /**
   * Преобразует тип в строковое представление (в History Token).
   *
   * @return строковое представление типа
   */
  public String toHistoryToken() {
    String result = "";
    
    switch(this) {
      case STRING:
        result = STRING_TOKEN;
        break;
      case INTEGER:
        result = INTEGER_TOKEN;
        break;
      case FLOAT:
        result = FLOAT_TOKEN;
        break;
      case DOUBLE:
        result = DOUBLE_TOKEN;
        break;
      case BIGDECIMAL:
        result = BIGDECIMAL_TOKEN;
        break;
      case BOOLEAN:
        result = BOOLEAN_TOKEN;
        break;
      case DATE:
        result = DATE_TOKEN;
        break;
      case TIME:
        result = TIME_TOKEN;
        break;
      case DATE_TIME:
        result = DATE_TIME_TOKEN;
        break;
      case OPTION:
        result = OPTION_TOKEN;
        break;
      case LIST_OF_OPTION:
        result = LIST_OF_OPTION_TOKEN;
        break;
      case BINARY_FILE:
        result = BINARY_FILE_TOKEN;
        break;
      case TEXT_FILE:
        result = TEXT_FILE_TOKEN;
        break;
      case CLOB:
        result = CLOB_TOKEN;
        break;
    }
    
    return result;
  }
  
  /**
   * Преобразует строковое представление (History Token) в тип.
   *
   * @return тип, созданный на основе строкового представления или <code>null</code>, если определить тип невозможно
   */
  public static Object buildTypeFromToken(String token) {
    Object result = null;
    
    if(STRING_TOKEN.equals(token)) {
      result = STRING;
    } else if(INTEGER_TOKEN.equals(token)) {
      result = INTEGER;
    } else if(FLOAT_TOKEN.equals(token)) {
      result = FLOAT;
    } else if(DOUBLE_TOKEN.equals(token)) {
      result = DOUBLE;
    } else if(BIGDECIMAL_TOKEN.equals(token)) {
      result = BIGDECIMAL;
    } else if(BOOLEAN_TOKEN.equals(token)) {
      result = BOOLEAN;
    } else if(DATE_TOKEN.equals(token)) {
      result = DATE;
    } else if(TIME_TOKEN.equals(token)) {
      result = TIME;
    } else if(DATE_TIME_TOKEN.equals(token)) {
      result = DATE_TIME;
    } else if(OPTION_TOKEN.equals(token)) {
      result = OPTION;
    } else if(LIST_OF_OPTION_TOKEN.equals(token)) {
      result = LIST_OF_OPTION;
    } else if(BINARY_FILE_TOKEN.equals(token)) {
      result = BINARY_FILE;
    } else if(TEXT_FILE_TOKEN.equals(token)) {
      result = TEXT_FILE;
    } else if(CLOB_TOKEN.equals(token)) {
      result = CLOB;
    }
    
    return result;
  }
  
}
