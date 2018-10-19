package org.jepria.server.load.rest;

import java.util.List;
import java.util.Map;

public class SearchParamsDto {
  
  private Map<String, Object> template;
  
  private Integer maxResultsetSize;

  private List<ColumnSortConfigurationDto> listSortConfiguration;
  
  public Map<String, Object> getTemplate() {
    return template;
  }

  public void setTemplate(Map<String, Object> template) {
    this.template = template;
  }
  
  public List<ColumnSortConfigurationDto> getListSortConfiguration() {
    return listSortConfiguration;
  }

  public void setListSortConfiguration(List<ColumnSortConfigurationDto> listSortConfiguration) {
    this.listSortConfiguration = listSortConfiguration;
  }

  public Integer getMaxResultsetSize() {
    return maxResultsetSize;
  }

  public void setMaxResultsetSize(Integer maxResultsetSize) {
    this.maxResultsetSize = maxResultsetSize;
  }
  
}
