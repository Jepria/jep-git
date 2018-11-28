package org.jepria.server.data;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Класс для сравнения записей, представленных в виде Map, для сортировки списка
 */
// TODO переименовать в MapRecordComparator?
public class RecordComparator implements Comparator<Map<String, ?>> {

  private final LinkedHashMap<String, Comparator<Object>> fieldComparators;
  
  public RecordComparator(LinkedHashMap<String, Comparator<Object>> fieldComparators) {
    this.fieldComparators = fieldComparators;
  }

  @Override
  public int compare(Map<String, ?> record1, Map<String, ?> record2) {
    
    if (record1 == null || record2 == null) {
      if (record1 == null && record2 == null) {
        return 0;
      } else {
        // nulls are the last
        return record1 == null ? 1 : -1;
      }
    }
    
    if (fieldComparators != null) {
      
      for (String fieldName: fieldComparators.keySet()) {
        
        final Object v1 = record1.get(fieldName);
        final Object v2 = record2.get(fieldName);
        
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

}
