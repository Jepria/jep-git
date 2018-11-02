package org.jepria.server.load.rest;

import java.util.List;
import java.util.Map;

/**
 * Dto для представления пользовательских параметров поиска
 */
public class SearchParamsDto {
  
  private Map<String, Object> template;
  
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

}
