package org.jepria.server.load.rest;

/**
 * Конфигурация сортировки значений определенного поля списка
 */
public class FieldSortConfigDto {
  
  /**
   * Имя поля
   */
  private final String fieldName;
  
  /**
   * Порядок сортировки: "asc" в натуральном порядке, "desc" в обратном порядке
   */
  private final String sortOrder;
  
  /**
   * 
   * @param fieldName
   * @param sortOrder
   */
  public FieldSortConfigDto(String fieldName, String sortOrder) {
    this.fieldName = fieldName;
    this.sortOrder = sortOrder;
  }
  
  public FieldSortConfigDto(String fieldName) {
    this(fieldName, "asc");
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getSortOrder() {
    return sortOrder;
  }
  
  /**
   * 
   * @return >= 0 в натуральном порядке, < 0 в обратном порядке
   */
  public int getSortOrderAsInt() {
    return "asc".equals(getSortOrder()) ? 1 : -1;
  }
}
