package org.jepria.server.data;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Описание записи
 */
// TODO replace this class with annotations on Dto?
public interface RecordDefinition {
  /**
   * Полный набор имён полей, которые может содержать запись
   */
  // TODO this can be determined from the Dto annotations
  Set<String> getFieldNames();
  
  /**
   * @return первичный ключ записи (простой или составной), non-null
   */
  // TODO this can be determined from the Dto annotations
  List<String> getPrimaryKey();

  /**
   * Определяет тип поля записи
   * @param fieldName
   * @return
   */
  // TODO Class or Type?
  // TODO this can be determined from the Dto annotations
  Class<?> getFieldType(String fieldName);

  /**
   * Определяет способ сравнения значений определённого поля записи
   * @param fieldName
   * @return custom field comparator or {@code null} to use default comparator
   */
  default Comparator<Object> getFieldComparator(String fieldName) {
    return null;
  }
  
  public class IncompletePrimaryKeyException extends Exception {
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
