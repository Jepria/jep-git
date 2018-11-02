package org.jepria.server.load;

/**
 * Конфигурация сортировки записей в списке по определённому столбцу
 */
public class ColumnSortConfiguration {
  
  /**
   * имя столбца для сортировки
   */
  private final String columnName;
  
  /**
   * порядок сортировки: >=0 натуральный порядок, <0: обратный порядок
   */
  private final int sortOrder;

  /**
   * 
   * @param columnName
   * @param sortOrder
   * @see #columnName
   * @see #sortOrder
   */
  public ColumnSortConfiguration(String columnName, int sortOrder) {
    this.columnName = columnName;
    this.sortOrder = sortOrder;
  }

  /**
   * @see #columnName
   */
  public String getColumnName() {
    return columnName;
  }

  /**
   * @see #sortOrder
   */
  public int getSortOrder() {
    return sortOrder;
  }

}
