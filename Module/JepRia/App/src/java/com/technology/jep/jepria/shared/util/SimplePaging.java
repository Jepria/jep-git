package com.technology.jep.jepria.shared.util;

import static com.technology.jep.jepria.shared.load.PagingConfig.DEFAULT_PAGE_SIZE;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс позволяющий выполнять элементарные действия по листанию списка.
 */
public class SimplePaging {

  /**
   * Список для листания.
   */
  private List list = null;
  
  /**
   * Количество элементов на одной странице.
   */
  private int pageSize = DEFAULT_PAGE_SIZE;
  
  /**
   * Номер последней запрошенной страницы.
   */
  private int pageIndex = 1;
  
  /**
   * Клнструктор создающий объект для листания списка.
   *
   * @param list список для листания
   * @param pageSize количество элементов на одной странице
   */
  public SimplePaging(List list, int pageSize) {
    this.list = list;
    this.pageSize = pageSize;
  }

  /**
   * Получает заданну страницу (подмножество элементов) из списка.
   *
   * @param requestedIndex запрошенная страница
   * @return страница (подмножество элементов) из списка
   */
  public List getPage(int requestedIndex) {
    pageIndex = requestedIndex;
    List result = new ArrayList();
    
    int size = list.size();
    if(size == 0) {
      pageIndex = 1;
      return result;
    }

    if(pageSize < 1) {
      pageSize = DEFAULT_PAGE_SIZE;
    }
    
    if(pageIndex < 1) {
      pageIndex = 1;
    }
    
    int offset = pageSize * (pageIndex - 1);
    if(offset > (size - 1)) {
      pageIndex = (int) Math.ceil((double) size / pageSize);
      offset = pageSize * (pageIndex - 1);
    }
    
    int last = Math.min(offset + pageSize - 1, size - 1);
    
    for(int i = offset; i <= last; i++) {
      result.add(list.get(i));
    }
    
    return result;
  }
  
  /**
   * Получение реального номера последней запрошенной страницы.
   *
   * @return реальный номер последней запрошенной страницы
   */
  public int getPageIndex() {
    return pageIndex;
  }

  /**
   * Получение реального размера последней запрошенной страницы.
   *
   * @return реальный размер последней запрошенной страницы
   */
  public int getPageSize() {
    return pageSize;
  }

}
