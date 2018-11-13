package org.jepria.server.load;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.jepria.compat.CoreCompat;

/**
 * Сортировщик списка записей по определённой конфигурации
 */
public class ListSorter<T> implements Comparator<T> {

  /**
   * non-null
   */
  private final List<ColumnSortConfiguration> columnSortConfigurations;
  
  /**
   * non-null
   */
  private final Map<String, Comparator<Object>> fieldComparators;
  
  /**
   * @param columnSortConfigurations
   * @param fieldComparators
   */
  public ListSorter(List<ColumnSortConfiguration> columnSortConfigurations, Map<String, Comparator<Object>> fieldComparators) {
    this.columnSortConfigurations = columnSortConfigurations;
    this.fieldComparators = fieldComparators == null ? Collections.emptyMap() : fieldComparators;
  }
  
  public ListSorter(List<ColumnSortConfiguration> fieldSortConfigs) {
    this(fieldSortConfigs, null);
  }
  
  @Override
  public int compare(T r1, T r2) {
    if (columnSortConfigurations != null) {
      

      // convert Dtos to Maps
      final Type type = new HashMap<String, Object>(){}.getClass().getGenericSuperclass();
      Jsonb jsonb = JsonbBuilder.create();
      final Map<String, ?> m1 = jsonb.fromJson(jsonb.toJson(r1), type);
      final Map<String, ?> m2 = jsonb.fromJson(jsonb.toJson(r2), type);
      
      
      for (ColumnSortConfiguration columnSortConfiguration: columnSortConfigurations) {
        
        final String fieldName = columnSortConfiguration.getColumnName();
        
        final Object v1 = m1.get(fieldName);
        final Object v2 = m2.get(fieldName);
        
        Comparator<Object> fieldComparator = fieldComparators.get(fieldName);
        
        if (fieldComparator == null) {
          fieldComparator = getDefaultComparator();
        }
        
        int cmpResult = fieldComparator.compare(v1,  v2);
        
        // apply field sort order to a particular comparison rseult
        cmpResult *= columnSortConfiguration.getSortOrder();
        
        if (cmpResult != 0) {
          // return immediately on the first difference
          return cmpResult;
        }
      }
    }
    
    // условно считаем записи равными
    return 0;
  }
  
  protected Comparator<Object> getDefaultComparator() {
    return CoreCompat.getDefaultComparator();
  }
}
