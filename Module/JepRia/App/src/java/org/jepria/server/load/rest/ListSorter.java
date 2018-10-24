package org.jepria.server.load.rest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.technology.jep.jepria.shared.util.DefaultComparator;

/**
 * Сортировщик списка записей по определённой конфигурации
 */
public class ListSorter implements Comparator<Map<String, Object>> {

  /**
   * non-null and non-empty
   */
  private final List<ColumnSortConfigurationDto> columnSortConfigurations;
  
  /**
   * non-null
   */
  private final Map<String, Comparator<Object>> fieldComparators;
  
  /**
   * @param columnSortConfigurations
   * @param fieldComparators
   */
  public ListSorter(List<ColumnSortConfigurationDto> columnSortConfigurations, Map<String, Comparator<Object>> fieldComparators) {
    this.columnSortConfigurations = columnSortConfigurations;
    this.fieldComparators = fieldComparators == null ? Collections.emptyMap() : fieldComparators;
  }
  
  public ListSorter(List<ColumnSortConfigurationDto> fieldSortConfigs) {
    this(fieldSortConfigs, null);
  }
  
  @Override
  public int compare(Map<String, Object> m1, Map<String, Object> m2) {
    
    for (ColumnSortConfigurationDto columnSortConfiguration: columnSortConfigurations) {
      
      final String fieldName = columnSortConfiguration.getColumnName();
      
      final Object v1 = m1.get(fieldName);
      final Object v2 = m2.get(fieldName);
      
      final Comparator<Object> fieldComparator = fieldComparators.getOrDefault(fieldName, getDefaultComparator());
      
      int cmpResult = fieldComparator.compare(v1,  v2);
      
      // apply field sort order to a particular comparison rseult
      int fieldSortOrder = "desc".equals(columnSortConfiguration.getSortOrder()) ? -1 : 1; 
      cmpResult *= fieldSortOrder;
      
      if (cmpResult != 0) {
        // return immediately on the first difference
        return cmpResult;
      }
    }
    
    // all field values from fieldSortConfigs are equal
    return 0;
  }
  
  protected Comparator<Object> getDefaultComparator() {
    return DefaultComparator.instance;
  }
  
  /**
   * Invokes {@link Collections#sort(List)} if the internal state is valid
   * @param list
   */
  public void sort(List<Map<String, Object>> list) {
    if (columnSortConfigurations != null && columnSortConfigurations.size() > 0) {
      Collections.sort(list, this);
    }
  }
}
