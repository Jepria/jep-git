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
   * Исходная поисковая модель
   */
  private Map<String, Object> model;
  
  /**
   * Поисковый шаблон, полученный из поисковой модели
   */
  private Map<String, Object> template;
  
  public int getResultSize() {
    return resultSize;
  }

  public void setResultSize(int resultSize) {
    this.resultSize = resultSize;
  }

  public Map<String, Object> getModel() {
    return model;
  }

  public void setModel(Map<String, Object> model) {
    this.model = model;
  }

  public Map<String, Object> getTemplate() {
    return template;
  }

  public void setTemplate(Map<String, Object> template) {
    this.template = template;
  }

  @Override
  public String toString() {
    return "SearchEntity [resultSize=" + resultSize + ", model=" + model + ", template=" + template + "]";
  }
  
}
