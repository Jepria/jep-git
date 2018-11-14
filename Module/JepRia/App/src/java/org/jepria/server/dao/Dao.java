package org.jepria.server.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Базовый интерфейс работы с записями в БД на прикладном уровне.
 */
public interface Dao {
  
  default void sort(List<?> dataset, LinkedHashMap<String, Comparator<Object>> fieldComparators) {
    
    if (dataset == null) {
      return;
    }

    final Comparator<Map<String, ?>> recordComparator = new Comparator<Map<String, ?>>() {
      @Override
      public int compare(Map<String, ?> r1, Map<String, ?> r2) {
        
        if (r1 == null || r2 == null) {
          if (r1 == null && r2 == null) {
            return 0;
          } else {
            // nulls are the last
            return r1 == null ? 1 : -1;
          }
        }
        
        if (fieldComparators != null) {
          
          for (String fieldName: fieldComparators.keySet()) {
            
            final Object v1 = r1.get(fieldName);
            final Object v2 = r2.get(fieldName);
            
            Comparator<Object> fieldComparator = fieldComparators.get(fieldName);
            
            int cmpResult = fieldComparator.compare(v1,  v2);
            
            if (cmpResult != 0) {
              // return immediately on the first differing field
              return cmpResult;
            }
          }
        }
        
        // условно считаем записи равными
        return 0;
      }
    };
    
    
    // assert dataset is of type List<Map<String, ?>>
    Collections.sort((List<Map<String, ?>>)dataset, recordComparator);
    
  }
  
  /**
   * Поиск.
   * 
   * @param model модель, по которому выполняется поиск
   * @param maxRowCount максимальное число возвращаемых записей
   * @param operatorId идентификатор пользователя
   * @return список записей
   */
  List<Map<String, ?>> find(Map<String, ?> model, Integer maxRowCount, Integer operatorId);
  
  /**
   * Создание записи
   * 
   * @param record создаваемая запись
   * @param operatorId идентификатор пользователя
   * @return первичный ключ созданной записи (простой или составной)
   */
  Object create(Map<String, ?> record, Integer operatorId);

  /**
   * Редактирование записи
   * 
   * @param record запись с новыми значениями
   * @param operatorId идентификатор пользователя
   */
  void update(Map<String, ?> record, Integer operatorId);

  /**
   * Удаление записи
   * 
   * @param record удаляемая запись (одного поля recordId может быть недостаточно для случаев с составным первичным ключом)
   * @param operatorId идентификатор пользователя
   */
  void delete(Map<String, ?> primaryKeyMap, Integer operatorId);
}
