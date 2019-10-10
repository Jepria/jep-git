package org.jepria.server.data;

import java.util.*;

/**
 * Описание записи
 */
// TODO replace this class with annotations on Dto?
public interface RecordDefinition {
  /**
   * Полный набор имён полей, которые может содержать запись
   */
  // TODO this can be determined from the Dto annotations
  // TODO объяснить в комментарии, какие поля нужно возвращать (все возможные или часть?)
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
  
  class IncompletePrimaryKeyException extends Exception {
    private static final long serialVersionUID = 1L;
  }
  
  /**
   * Извлекает из данной записи первичный ключ, корректируя регистр полей в соответствии с объявлением в {@link #getPrimaryKey()}
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
    
    final Map<String, X> ret = new HashMap<>();

    for (Map.Entry<String, X> e: record.entrySet()) {
      // Note: проверка полей регистронезависимым методом
      for (String primaryKeyElement: primaryKey) {
        if (primaryKeyElement.equalsIgnoreCase(e.getKey())) {
          ret.put(primaryKeyElement, e.getValue()); // associate original key with a value from the record
          break;
        }
      }
    }

    // Проверяем, что все поля первичного ключа присутствуют в записи (здесь осталось достаточным проверить размер)
    if (ret.size() != primaryKey.size()) {
      throw new IncompletePrimaryKeyException();
    }

    return ret;
  }
}
