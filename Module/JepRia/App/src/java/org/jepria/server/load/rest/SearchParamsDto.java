package org.jepria.server.load.rest;

import java.util.List;
import java.util.Map;

public class SearchParamsDto {
  
  private Map<String, Object> template;
  
  private Integer maxRowCount;

  private List<ColumnSortConfigurationDto> columnSortConfigurations;
  
  public Map<String, Object> getTemplate() {
    return template;
  }

  public void setTemplate(Map<String, Object> template) {
    this.template = template;
  }
  
  public List<ColumnSortConfigurationDto> getColumnSortConfigurations() {
    return columnSortConfigurations;
  }

  public void setSortConfig(List<ColumnSortConfigurationDto> columnSortConfigurations) {
    this.columnSortConfigurations = columnSortConfigurations;
  }

  public Integer getMaxRowCount() {
    return maxRowCount;
  }

  public void setMaxRowCount(Integer maxRowCount) {
    this.maxRowCount = maxRowCount;
  }
  
}
