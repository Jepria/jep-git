package com.technology.jep.jepria.shared.history;

/**
 * Константы, которые используются для работы с History.<br/>
 * Многие константы используются как разделители в строке Url (в History Token'е). Поэтому очень важно (для корректного разбора History Token'а
 * и правильного восстановления объектов на его основе), чтобы значения констант-разделителей были уникальными.<br/>
 * <br/>
 * @see com.technology.jep.jepria.client.history
 */
public class JepHistoryConstant {

  /**
   * Разделитель между представлениями {@link com.technology.jep.jepria.client.history.scope.JepScope} в строке History, представляющей 
   * состояние приложения {@link com.technology.jep.jepria.client.history.scope.JepScopeStack} .<br/>
   * <br/>
   * @see com.technology.jep.jepria.client.history
   */
  public static final String SCOPE_SEPARATOR = "$/$";
  
  /**
   * Регулярное выражение позволяющее выделить разделитель между представлениями {@link com.technology.jep.jepria.client.history.scope.JepScope} 
   * из строки History, представляющей состояние приложения {@link com.technology.jep.jepria.client.history.scope.JepScopeStack} .<br/>
   * <br/>
   * @see com.technology.jep.jepria.client.history
   */
  public static final String SCOPE_SEPARATOR_REGEXP = "\\$\\/\\$";

  /**
   * Разделитель между параметрами {@link com.technology.jep.jepria.client.history.scope.JepScope} в строке History.<br/>
   * <br/>
   * @see com.technology.jep.jepria.client.history
   */
  public static final String SCOPE_PARAMETER_SEPARATOR = "&";

  /**
   * Разделитель между именем и значением параметра {@link com.technology.jep.jepria.client.history.scope.JepScope} в строке History.<br/>
   * <br/>
   * @see com.technology.jep.jepria.client.history
   */
  public static final String SCOPE_PARAMETER_NAME_VALUE_SEPARATOR = "=";

  /**
   * Разделитель между элементами <code>java.util.Map</code> в строке History.<br/>
   * <br/>
   * @see com.technology.jep.jepria.client.history
   */
  public static final String MAP_PROPERTY_SEPARATOR = "$m$";

  /**
   * Регулярное выражение позволяющее выделить разделитель между элементами <code>java.util.Map</code> в строке History.<br/>
   * <br/>
   * @see com.technology.jep.jepria.client.history
   */
  public static final String MAP_PROPERTY_SEPARATOR_REGEXP = "\\$m\\$";
  
  /**
   * Разделитель между именем и значением элемента <code>java.util.Map</code> в строке History.<br/>
   * <br/>
   * @see com.technology.jep.jepria.client.history
   */
  public static final String MAP_NAME_TYPE_VALUE_SEPARATOR = ":";

  /**
   * Разделитель между именем и значением элемента {@link com.technology.jep.jepria.shared.field.option.JepOption} в строке History.<br/>
   * <br/>
   * @see com.technology.jep.jepria.shared.history.JepHistoryToken#valueToToken(Object value)
   * @see com.technology.jep.jepria.client.history
   */
  public static final String OPTION_NAME_VALUE_SEPARATOR = "$c$";
  
  /**
   * Разделитель между именем типа и значением <code>JepOption</code> в строке History.<br/>
   */
  public static final String OPTION_VALUE_TYPE_SEPARATOR = "$t$";
  
  /**
   * Регулярное выражение позволяющее выделить разделитель между именем и значением элемента 
   * {@link com.technology.jep.jepria.shared.field.option.JepOption} в строке History.<br/>
   * <br/>
   * @see com.technology.jep.jepria.shared.history.JepHistoryToken#tokenToValue(String token)
   * @see com.technology.jep.jepria.client.history
   */
  public static final String OPTION_NAME_VALUE_SEPARATOR_REGEXP = "\\$c\\$";
  
  /**
   * Разделитель между элементами списка в строке History.<br/>
   * <br/>
   * @see com.technology.jep.jepria.shared.history.JepHistoryToken#valueToToken(Object value)
   * @see com.technology.jep.jepria.client.history
   */
  public static final String LIST_VALUE_SEPARATOR = "$l$";
  
  /**
   * Регулярное выражение позволяющее выделить разделитель между элементами списка в строке History.<br/>
   * <br/>
   * @see com.technology.jep.jepria.shared.history.JepHistoryToken#tokenToValue(String token)
   * @see com.technology.jep.jepria.client.history
   */
  public static final String LIST_VALUE_SEPARATOR_REGEXP = "\\$l\\$";
  
}
