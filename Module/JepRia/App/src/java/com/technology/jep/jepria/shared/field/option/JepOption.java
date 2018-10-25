package com.technology.jep.jepria.shared.field.option;

import static com.technology.jep.jepria.shared.history.JepHistoryConstant.LIST_VALUE_SEPARATOR;
import static com.technology.jep.jepria.shared.history.JepHistoryConstant.LIST_VALUE_SEPARATOR_REGEXP;
import static com.technology.jep.jepria.shared.history.JepHistoryConstant.OPTION_NAME_VALUE_SEPARATOR;
import static com.technology.jep.jepria.shared.history.JepHistoryConstant.OPTION_NAME_VALUE_SEPARATOR_REGEXP;

import java.util.ArrayList;
import java.util.List;

import com.technology.jep.jepria.shared.dto.JepDto;
import com.technology.jep.jepria.shared.history.JepHistoryConstant;
import com.technology.jep.jepria.shared.history.JepHistoryToken;

public class JepOption extends JepDto {
  private static final long serialVersionUID = 1L;
  
  public static final String OPTION_NAME = "name";
  public static final String OPTION_VALUE = "value";
  
  public static final String UNDEFINED_OPTION_NAME = "undefined_option_name";
  public static final String UNDEFINED_OPTION_VALUE = "undefined_option_value";
  
  /**
   * Пустая опция.
   */
  public static final JepOption EMPTY_OPTION = new JepOption(null, null);
  
  public JepOption() {
  }
  
  public JepOption(String name, Object value) {
    setName(name);
    setValue(value);    
  }
  
  public JepOption(JepOption option) {
    super(option);
  }

  /**
   * Конструктор восстанавливающий (создающий) объект из строкового представления (из History Token'а).
   *
   * @param token строковое представление объекта (History Token)
   *
   * @see com.technology.jep.jepria.shared.history.JepHistoryToken#tokenToValue(String token)
   */
  public JepOption(String token) {
    String[] valueTab = token.split(OPTION_NAME_VALUE_SEPARATOR_REGEXP, 2); // Считаем, что наименование - это все, что после первого разделителя.
    
    String valueToken = valueTab[0];
    if (valueToken != null) {
      valueToken = valueToken.replace(JepHistoryConstant.OPTION_VALUE_TYPE_SEPARATOR, JepHistoryConstant.MAP_NAME_TYPE_VALUE_SEPARATOR);
    }
    final Object value = JepHistoryToken.tokenToValue(valueToken); 
        
    String name = valueTab.length > 1 ? valueTab[1] : null;
    setValue(value);    
    setName(name);
  }
  
  /**
   * Получение значения опции.<br/>
   * Если в качестве параметра передан null или переданный параметр не является наследником JepOption, то возвращаемый результат будет null.<br/>
   * Пример использования в прикладном модуле:
   * <pre>
   *   ...
   *   Integer integerValue = JepOption.&lt;Integer&gt;getValue(&lt;Объект типа JepOption&gt;); // Получение значения типа Integer или null.
   *   ...
   *   String stringValue = JepOption.&lt;String&gt;getValue(&lt;Объект типа JepOption&gt;); // Получение значения типа String или null.
   *   ...
   * </pre>
   *
   * @param option опция, значение которой необходимо получить
   * @return значение опции
   * @throws ClassCastException если передан объект не класса {@link JepOption}
   */
  @SuppressWarnings("unchecked")
  public static <X> X getValue(Object option) {
    if (option instanceof JepOption) {
      return (X)((JepOption)option).getValue();
    }

    return (X) option;
  }

  public Object getValue() {
    return super.get(OPTION_VALUE);
  }

  public String getName() {
    Object name = super.get(OPTION_NAME);
    
    return name == null ? null : name.toString();
  }
  
  public void setValue(Object value) {
    set(OPTION_VALUE, value);
  }
  
  public void setName(String name) {
    set(OPTION_NAME, name);
  }
  
  public String toString() {
    StringBuffer sbResult = new StringBuffer();

    sbResult.append("{");
    sbResult.append(this.<Object>get(OPTION_VALUE));
    sbResult.append(",");
    sbResult.append(this.<String>get(OPTION_NAME));
    sbResult.append("}");

    return sbResult.toString();
  }
  
  /**
   * Преобразует объект в строковое представление (в History Token).
   *
   * @return строковое представление объекта (History Token)
   *
   * @see com.technology.jep.jepria.shared.history.JepHistoryToken#valueToToken(Object value)
   */
  public String toHistoryToken() {
    StringBuffer sbResult = new StringBuffer();

    final Object value = this.<Object>get(OPTION_VALUE);
    
    String valueToken = JepHistoryToken.valueToToken(value);
    if (valueToken != null) {
      valueToken = valueToken.replace(JepHistoryConstant.MAP_NAME_TYPE_VALUE_SEPARATOR, JepHistoryConstant.OPTION_VALUE_TYPE_SEPARATOR);
    }
        
    sbResult.append(valueToken); // Сначала идет значение - чтобы снизить вероятность наличия в первых символах token'а разделителя.
    sbResult.append(OPTION_NAME_VALUE_SEPARATOR);
    sbResult.append(this.<String>get(OPTION_NAME));

    return sbResult.toString();
  }
  
  /**
   * Преобразует список объектов List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt; в строковое представление 
   * (в History Token).
   *
   * @param list исходный список объектов List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt;
   * @return строковое представление списка объектов List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt; (History Token)
   *
   * @see com.technology.jep.jepria.shared.history.JepHistoryToken#valueToToken(Object value)
   */
  public static String getListAsToken(List<JepOption> list) {
    StringBuilder resultToken = new StringBuilder();
    for (JepOption option: list) {
      if (option != null) {
        
        if (resultToken.length() > 0) {
          resultToken.append(LIST_VALUE_SEPARATOR);
        }
        
        resultToken.append(option.toHistoryToken());
      }
    }
    
    return resultToken.toString();
  }
  
  /**
   * Преобразует строковое представление (так называемый History Token) в список объектов 
   * List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt;.
   *
   * @param listToken строковое представление списка (так называемый History Token)
   * @return список объектов List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt;
   *
   * @see com.technology.jep.jepria.shared.history.JepHistoryToken#tokenToValue(String token)
   */
  public static List<JepOption> buildListFromToken(String listToken) {
    List<JepOption> resultList = new ArrayList<JepOption>();
    if (listToken != null) {
      String[] listTokenTab = listToken.split(LIST_VALUE_SEPARATOR_REGEXP);
      int entryNumber = listTokenTab.length;
      for (int i = 0; i < entryNumber; i++) {
        if (listTokenTab[i] != null) {
          resultList.add(new JepOption(listTokenTab[i]));
        }
      }
    }
    return resultList;
  }
  
  /**
   * Преобразование списка опций к строке путём конкатенации их наименований 
   * через указанный разделитель
   * 
   * @param options список опций
   * @param separator разделитель
   * @return список наименований опций, перечисленных через указанный разделитель
   */
  public static String getOptionNamesAsString(List<JepOption> options, String separator){
    StringBuilder sbOptions = new StringBuilder();
    if(options != null) {
      for(JepOption option : options) {        
        if(option != null) {
          if(sbOptions.length() > 0) {
            sbOptions.append(separator);
          }
          sbOptions.append(option.getName());
        }
      }
    } 
    return sbOptions.toString();
  }  
  
  /**
   * Преобразование списка опций к строке путём конкатенации их наименований
   * через запятую и пробел
   * 
   * @param options список опций
   * @return список наименований опций, перечисленных через запятую и пробел
   */
  public static String getOptionNamesAsString(List<JepOption> options){
    return getOptionNamesAsString(options, ", ");
  }
  
  /**
   * Преобразование списка опций к строке путём конкатенации их идентификаторов 
   * через указанный разделитель
   * 
   * @param options список опций
   * @param separator разделитель
   * @return список идентификаторов опций, перечисленных через указанный разделитель
   */
  public static String getOptionValuesAsString(List<JepOption> options, String separator){
    StringBuilder sbOptions = new StringBuilder();
    if(options != null) {
      for(JepOption option : options) {        
        if(option != null) {
          if(sbOptions.length() > 0) {
            sbOptions.append(separator);
          }
          sbOptions.append(option.getValue().toString());
        }
      }
    } 
    return sbOptions.toString();
  }
  
  /**
   * Преобразование списка опций к строке путём конкатенации их идентификаторов
   * через запятую и пробел
   * 
   * @param options список опций
   * @return список идентификаторов опций, перечисленных через запятую и пробел
   */
  public static String getOptionValuesAsString(List<JepOption> options){
    return getOptionValuesAsString(options, ", ");
  }
}
