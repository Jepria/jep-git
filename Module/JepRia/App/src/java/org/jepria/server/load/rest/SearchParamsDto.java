package org.jepria.server.load.rest;

import java.util.Map;

public class SearchParamsDto {
  
  private Map<String, Object> template;
  
  private Integer maxRowCount;

  public Map<String, Object> getTemplate() {
    return template;
  }

  public void setTemplate(Map<String, Object> template) {
    this.template = template;
  }

  public Integer getMaxRowCount() {
    return maxRowCount;
  }

  public void setMaxRowCount(Integer maxRowCount) {
    this.maxRowCount = maxRowCount;
  }
  
}
