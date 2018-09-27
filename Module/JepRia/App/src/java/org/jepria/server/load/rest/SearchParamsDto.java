package org.jepria.server.load.rest;

import java.util.Map;

public class SearchParamsDto {
  
  private Map<String, Object> model;
  
  private Integer maxRowCount;

  public Map<String, Object> getModel() {
    return model;
  }

  public void setModel(Map<String, Object> model) {
    this.model = model;
  }

  public Integer getMaxRowCount() {
    return maxRowCount;
  }

  public void setMaxRowCount(Integer maxRowCount) {
    this.maxRowCount = maxRowCount;
  }
  
}
