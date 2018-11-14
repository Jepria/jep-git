package org.jepria.server.load;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.jepria.compat.CoreCompat;

/**
 * Сортировщик списка записей по определённой конфигурации
 */
public class ListSorter implements Comparator<Map<String, ?>> {

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
  public int compare(Map<String, ?> m1, Map<String, ?> m2) {
    if (columnSortConfigurations != null) {
      
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
