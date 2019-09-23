package org.jepria.server.data;

import java.util.List;

/**
 * Dto для представления пользовательского поискового запроса
 */
public class SearchRequestDto<T> {
  
  public SearchRequestDto() {}
  
  private T template;

  private List<ColumnSortConfigurationDto> listSortConfiguration;

  public T getTemplate() {
    return template;
  }

  public void setTemplate(T template) {
    this.template = template;
  }
  
  public List<ColumnSortConfigurationDto> getListSortConfiguration() {
    return listSortConfiguration;
  }

  public void setListSortConfiguration(List<ColumnSortConfigurationDto> listSortConfiguration) {
    this.listSortConfiguration = listSortConfiguration;
  }

}
