package com.technology.jep.jepria.client.widget.toolbar;

import com.technology.jep.jepria.client.widget.event.JepObservable;

/**
 * Интерфейс инструментальной панели управления листанием набора данных.
 */
public interface PagingToolBar extends JepObservable {
  String PAGINGBAR_BUTTON_ID  =  "PAGINGBAR_BUTTON";

  /**
   * Установка размера страницы набора данных.
   * 
   * @param pageSize размер страницы набора данных
   */
  void setPageSize(int pageSize);

  /**
   * Получение размера страницы набора данных.
   * 
   * @return размер страницы набора данных
   */
  int getPageSize();

  /**
   * Установка значения количества строк в наборе данных.
   *
   * @param totalLength количество строк в наборе данных
   */
  void setTotalLength(int totalLength);

  /**
   * Установка текущей активной страницы набора данных.
   *
   * @param activePage текущяя активная страница набора данных
   */
  void setActivePage(int activePage);
  
  /**
   * Настройка панели в соответствии с новыми параметрами.
   */
  void adjust();
  
  /**
   * Накладывает маску на панель и приостанавливает взаимодействие панели с пользователем.
   * 
   * @return элемент-маска
   */
  Object mask();

  /**
   * Снимает маску с панели и возобновляет взаимодействие с пользователем.
    */
  void unmask();

}