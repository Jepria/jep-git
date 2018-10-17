package org.jepria.server.load.rest;

import java.util.List;
import java.util.Map;

/**
 * Сущность, описывающая поисковый запрос и его результат на серверной стороне
 */
public class SearchEntity {
  
  /**
   * Количество записей в результирующем списке поиска
   */
  private int resultsetSize;

  /**
   * Исходный (пользовательский) поисковый шаблон
   */
  private Map<String, Object> template;
  
  /**
   * Поисковый шаблон, полученный из поисковой модели
   */
  private Map<String, Object> model;
  
  private List<?> resultset;
  
  private Boolean autoRefresh;
  
  
  
  
  public int getResultsetSize() {
    return resultsetSize;
  }

  public void setResultsetSize(int resultsetSize) {
    this.resultsetSize = resultsetSize;
  }

  public Map<String, Object> getTemplate() {
    return template;
  }

  public void setTemplate(Map<String, Object> template) {
    this.template = template;
  }

  public Map<String, Object> getModel() {
    return model;
  }

  public void setModel(Map<String, Object> model) {
    this.model = model;
  }
  
  public List<?> getResultset() {
    return resultset;
  }

  public void setResultset(List<?> resultset) {
    this.resultset = resultset;
  }
  
  

  public Boolean getAutoRefresh() {
    return autoRefresh;
  }

  public void setAutoRefresh(Boolean autoRefresh) {
    this.autoRefresh = autoRefresh;
  }

  @Override
  public String toString() {
    return "SearchEntity [resultSize=" + resultsetSize + ", template=" + template + ", model=" + model + "]";
  }
  
}
