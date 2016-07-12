package com.technology.jep.jepria.shared.load;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Класс содержит результирующий найденный набор данных и информацию необходимую для листания данного набора.
 */
public class PagingResult<T extends JepRecord> implements IsSerializable {
  private static final long serialVersionUID = 1466864868543323075L;
  
  /**
   * Размер страницы набора данных.
   */
  private Integer pageSize = null;

  /**
   * Количество строк в наборе данных.
   */
  private Integer size = null;

  /**
   * Текущяя активная страница набора данных.
   */
  private Integer activePage = null;
  
  /**
   * Набор данных текущей активной страницы.
   */
  private List<T> data = null;

  /**
   * Создает новую конфигурацию для работы со списком.
   */
  public PagingResult() {
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
   * Установка значения количества строк в наборе данных.
   *
   * @param size количество строк в наборе данных
   */
  public void setSize(Integer size) {
    this.size = size;
  }

  /**
   * Получение значения количества строк в наборе данных.
   *
   * @return количество строк в наборе данных
   */
  public Integer getSize() {
    return size;
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
   * Установка набора данных текущей активной страницы.
   *
   * @param data набор данных текущей активной страницы
   */
  public void setData(List<T> data) {
    this.data = data;
  }
  
  /**
   * Получение набора данных текущей активной страницы.
   *
   * @return набор данных текущей активной страницы
   */
  public List<T> getData() {
    return data;
  }
  
  /**
   * Переводит объект класса конфигурации в строковое представление.
   *
   * @return строковое представление конфигурации
   */
  public String toString() {
    StringBuilder sbResult = new StringBuilder();
    sbResult.append("pageSize=");
    sbResult.append(pageSize);
    sbResult.append(",\n");
    sbResult.append("size=");
    sbResult.append(size);
    sbResult.append(",\n");
    sbResult.append("activePage=");
    sbResult.append(activePage);
    return sbResult.toString();
  }

}
