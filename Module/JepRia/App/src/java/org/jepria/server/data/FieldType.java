package org.jepria.server.data;

/**
 * Аналог JepTypeEnum
 */
public enum FieldType {
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
   * Тип поля, для представления денежных единиц с тысячным разделителем - пробелом
   */
  
  MONEY,

  /**
   * Тип поля, содержащий в качестве своего значения экземпляр класса реализующего интерфейс
   * List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt;.
   */
  LIST_OF_OPTION,

  /**
   * Тип поля, для представления списков примитивных данных
   */
  LIST_OF_PRIMITIVE,

  BINARY_FILE,
  TEXT_FILE,
  CLOB;
}
