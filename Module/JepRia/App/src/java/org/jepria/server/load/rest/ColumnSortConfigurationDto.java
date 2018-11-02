package org.jepria.server.load.rest;

/**
 * Dto для представления {@link org.jepria.server.load.ColumnSortConfiguration}
 */
public class ColumnSortConfigurationDto {
  
  private String columnName;
  
  private String sortOrder;

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public String getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
  }
}

