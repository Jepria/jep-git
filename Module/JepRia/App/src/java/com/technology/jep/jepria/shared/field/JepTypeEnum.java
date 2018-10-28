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
  STRING(HistoryToken.S),

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.Integer</code>.
   */
  INTEGER(HistoryToken.I),

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.Float</code>.
   */
  FLOAT(HistoryToken.F),

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.Double</code>.
   */
  DOUBLE(HistoryToken.D),

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.math.BigDecimal</code>.
   */
  BIGDECIMAL(HistoryToken.BD),

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.lang.Boolean</code>.
   */
  BOOLEAN(HistoryToken.B),

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * <code>java.util.Date</code>.
   */
  DATE(HistoryToken.DE),

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * {@link com.technology.jep.jepria.shared.time.JepTime}.
   */
  TIME(HistoryToken.TE),

  DATE_TIME(HistoryToken.DTE),

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса 
   * {@link com.technology.jep.jepria.shared.field.option.JepOption}.
   */
  OPTION(HistoryToken.O),

  /**
   * Тип поля, для представления денежных единиц с тысячным разделителем - пробелом
   */

  MONEY(HistoryToken.MNY),

  RECORD(HistoryToken.R),
  
  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса реализующего интерфейс
   * List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt;.
   */
  LIST_OF_OPTION(HistoryToken.LO),

  /**
   * Тип поля, для представления списков примитивных данных
   */
  LIST_OF_PRIMITIVE(HistoryToken.LP),

  /**
   * Тип поля, для представления списков записей
   */
  LIST_OF_RECORD(HistoryToken.LR),

  BINARY_FILE(HistoryToken.BFE),
  TEXT_FILE(HistoryToken.TFE),
  CLOB(HistoryToken.C);

  private final HistoryToken historyToken;

  private JepTypeEnum(HistoryToken historyToken) {
    this.historyToken = historyToken;
  }

  public static enum HistoryToken {
    /**
     * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
     * <code>java.lang.String</code>.
     */
    S,

    /**
     * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
     * <code>java.lang.Integer</code>.
     */
    I,

    /**
     * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
     * <code>java.lang.Float</code>.
     */
    F,

    /**
     * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
     * <code>java.lang.Double</code>.
     */
    D,

    /**
     * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
     * <code>java.math.BigDecimal</code>.
     */
    BD,

    /**
     * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
     * <code>java.lang.Boolean</code>.
     */
    B,

    /**
     * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
     * <code>java.util.Date</code>.
     */
    DE,

    /**
     * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
     * {@link com.technology.jep.jepria.shared.time.JepTime}.
     */
    TE,

    /**
     * DATE_TIME_TOKEN
     */
    DTE,

    /**
     * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса 
     * {@link com.technology.jep.jepria.shared.field.option.JepOption}.
     */
    O,

    /**
     * Строковое представление (History Token) представляющее тип поля, содержащий в качестве своего значения экземпляр класса реализующего интерфейс
     * List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt;.
     */
    LO,

    /**
     * LIST_OF_PRIMITIVE_TOKEN
     */
    LP,

    /**
     * LIST_OF_RECORD_TOKEN
     */
    LR,

    /**
     * BINARY_FILE_TOKEN
     */
    BFE,
    /**
     * TEXT_FILE_TOKEN
     */
    TFE,

    /**
     * CLOB_TOKEN
     */
    C,

    /**
     * MONEY_TOKEN
     */
    MNY,
    
    /**
     * JEP_RECORD
     */
    R
    
  }

  /**
   * Преобразует тип в строковое представление (в History Token).
   *
   * @return строковое представление типа
   */
  public String toHistoryToken() {
    return historyToken.name();
  }

  /**
   * Преобразует строковое представление (History Token) в тип.
   *
   * @return тип, созданный на основе строкового представления или <code>null</code>, если определить тип невозможно
   */
  public static JepTypeEnum buildTypeFromToken(String token) {
    final HistoryToken historyToken;
    try {
      historyToken = HistoryToken.valueOf(token);
    } catch (IllegalArgumentException e) {
      return null;
    }

    switch (historyToken) {
    case S: {
      return STRING;
    }
    case I: {
      return INTEGER;      
    }
    case F: {
      return FLOAT;
    }
    case D: {
      return DOUBLE;
    }
    case BD: {
      return BIGDECIMAL;
    }
    case B: {
      return BOOLEAN;
    }
    case DE: {
      return DATE;
    }
    case TE: {
      return TIME;
    }
    case DTE: {
      return DATE_TIME;
    }
    case O: {
      return OPTION;
    }
    case LO: {
      return LIST_OF_OPTION;
    }
    case LP: {
      return LIST_OF_PRIMITIVE;
    }
    case LR: {
      return LIST_OF_RECORD;
    }
    case BFE: {
      return BINARY_FILE;
    }
    case TFE: {
      return TEXT_FILE;
    }
    case C: {
      return CLOB;
    }
    case MNY: {
      return MONEY;
    }
    case R: {
      return RECORD;
    }
    }
    return null;
  }

}
