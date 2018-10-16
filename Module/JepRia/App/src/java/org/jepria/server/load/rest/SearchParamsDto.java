package org.jepria.server.load.rest;

import java.util.List;
import java.util.Map;

public class SearchParamsDto {
  
  private Map<String, Object> template;
  
  private Integer maxRowCount;

  private List<FieldSortConfigDto> fieldSearchConfigs;
  
  public Map<String, Object> getTemplate() {
    return template;
  }

  public void setTemplate(Map<String, Object> template) {
    this.template = template;
  }
  
  public List<FieldSortConfigDto> getFieldSearchConfigs() {
    return fieldSearchConfigs;
  }

  public void setFieldSearchConfigs(List<FieldSortConfigDto> fieldSearchConfigs) {
    this.fieldSearchConfigs = fieldSearchConfigs;
  }

  public Integer getMaxRowCount() {
    return maxRowCount;
  }

  public void setMaxRowCount(Integer maxRowCount) {
    this.maxRowCount = maxRowCount;
  }
  
}
