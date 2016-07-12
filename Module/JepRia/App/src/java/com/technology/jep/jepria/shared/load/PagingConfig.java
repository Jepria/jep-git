package com.technology.jep.jepria.shared.load;

import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Класс содержащий параметры (конфигурацию) необходимые для выполнения листания данных списка.
 */
public class PagingConfig extends FindConfig {
  private static final long serialVersionUID = 1L;
  
  /**
   * Размер страницы набора данных по умолчанию.
   */
  public static final int DEFAULT_PAGE_SIZE = 25;

  /**
   * Размер страницы набора данных.
   */
  private Integer pageSize = new Integer(DEFAULT_PAGE_SIZE);

  /**
   * Текущяя активная страница набора данных.
   */
  private Integer activePage = null;

  /**
   * Создает конфигурацию листания с атрибутами по умолчанию.
   */
  public PagingConfig() {
  }
  
  /**
   * Создает новую конфигурацию листания данных на основе размера страницы набора данных и номера активной страницы.
   *
   * @param pageSize размер страницы набора данных
   * @param activePage текущяя активная страница набора данных
   */
  public PagingConfig(Integer pageSize, Integer activePage) {
    this.pageSize = pageSize;
    this.activePage = activePage;
  }

  /**
   * Создает новую конфигурацию листания данных на основе поискового шаблона.
   *
   * @param templateRecord поисковый шаблон
   */
  public PagingConfig(JepRecord templateRecord) {
    setTemplateRecord(templateRecord);
  }

  /**
   * Установка размера страницы набора данных.
   * 
   * @param pageSize размер страницы набора данных
   */
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * Получение размера страницы набора данных.
   * 
   * @return размер страницы набора данных
   */
  public Integer getPageSize() {
    return pageSize;
  }

  /**
   * Установка текущей активной страницы набора данных.
   *
   * @param activePage текущяя активная страница набора данных
   */
  public void setActivePage(Integer activePage) {
    this.activePage = activePage;
  }
  
  /**
   * Получение текущей активной страницы набора данных.
   *
   * @return текущяя активная страница набора данных
   */
  public Integer getActivePage() {
    return activePage;
  }
  
  /**
   * Переводит объект класса конфигурации в строковое представление.
   *
   * @return строковое представление конфигурации
   */
  public String toString() {
    StringBuffer sbResult = new StringBuffer();
    sbResult.append("pageSize=");
    sbResult.append(pageSize);
    sbResult.append(",\n");
    sbResult.append("activePage=");
    sbResult.append(activePage);
    sbResult.append(",\n");
    sbResult.append(super.toString());
    return sbResult.toString();
  }

}
