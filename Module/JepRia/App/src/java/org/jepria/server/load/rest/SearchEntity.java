package org.jepria.server.load.rest;

import java.util.Map;

/**
 * Сущность, описывающая поисковый запрос и его результат на серверной стороне
 */
public class SearchEntity {
  
  /**
   * Количество записей в результирующем списке поиска
   */
  private int resultSize;

  /**
   * Исходный (пользовательский) поисковый шаблон
   */
  private Map<String, Object> template;
  
  /**
   * Поисковый шаблон, полученный из поисковой модели
   */
  private Map<String, Object> model;
  
  public int getResultSize() {
    return resultSize;
  }

  public void setResultSize(int resultSize) {
    this.resultSize = resultSize;
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

  @Override
  public String toString() {
    return "SearchEntity [resultSize=" + resultSize + ", template=" + template + ", model=" + model + "]";
  }
  
}
