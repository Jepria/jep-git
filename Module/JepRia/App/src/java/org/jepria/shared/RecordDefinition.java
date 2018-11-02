package org.jepria.shared;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;

/**
 * Описание записи
 */
public interface RecordDefinition {
  /**
   * Полный набор имён полей, которые может содержать запись
   */
  Set<String> getFieldNames();
  
  /**
   * Определяет тип поля записи
   * @param fieldName
   * @return
   */
  JepTypeEnum getFieldType(String fieldName);
  
  /**
   * Определяет тип строкового вхождения при поиске по полю записи  
   * @param fieldName
   * @return
   */
  JepLikeEnum getFieldMatch(String fieldName);
  
  /**
   * @return первичный ключ записи (простой или составной), non-null
   */
  List<String> getPrimaryKey();
  
  /**
   * Определяет способ сравнения значений определённого поля записи
   * @param fieldName
   * @return
   */
  Comparator<Object> getFieldComparator(String fieldName);
  
  public static class IncompletePrimaryKeyException extends Exception {
    private static final long serialVersionUID = 1L;
  }
  
  /**
   * Извлекает из данной записи первичный ключ
   * @param record запись, non-null
   * @return первичный ключ, non-null
   * @throws IncompletePrimaryKeyException если входная запись содержит не все поля из первичного ключа
   * @throws IllegalArgumentException если входная запись есть null
   */
  default <X> Map<String, X> buildPrimaryKey(Map<String, X> record) throws IncompletePrimaryKeyException {
    if (record == null) {
      throw new IllegalArgumentException();
    }

    final List<String> primaryKey = getPrimaryKey();
    
    // Проверяем, что все поля первичного ключа присутствуют в записи
    if (!record.keySet().containsAll(primaryKey)) {
      throw new IncompletePrimaryKeyException(); 
    }
    
    return record.keySet().stream().filter(k -> primaryKey.contains(k)).collect(Collectors.toMap(k -> k, v -> (X)v));
  }
}
