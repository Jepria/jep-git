package com.technology.jep.jepria.client.widget.event;

/**
 * Типы событий widget-ов.<br/>
 * Используются для разделения списков слушателей по типам событий.
 */
public enum JepEventType {

  /**
   * Событие возникает при изменении значения полей наследников 
   * {@link com.technology.jep.jepria.client.widget.field.multistate.JepMultiStateField}
   * методом setValue(Object value) .
   */
  CHANGE_VALUE_EVENT,

  /**
   * Событие возникает при потере фокуса полями наследниками 
   * {@link com.technology.jep.jepria.client.widget.field.multistate.JepTextField} .
   */
  LOST_FOCUS_EVENT,

  /**
   * Событие возникает при приостановке набора символов в наследниках 
   * {@link com.technology.jep.jepria.client.widget.field.multistate.JepTextField} .
   */
  TYPING_TIMEOUT_EVENT,

  /**
   * Событие возникает при первом использовании поля 
   * {@link com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField}.
   */
  FIRST_TIME_USE_EVENT,

  /**
   * Событие возникает при изменении значения поля 
   * {@link com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField}
   * выбором из выпадающего списка.
   */
  CHANGE_SELECTION_EVENT,

  /**
   * Событие возникает при клике на флажке 
   * {@link com.technology.jep.jepria.client.widget.field.multistate.JepCheckBoxField} .
   */
  CHANGE_CHECK_EVENT,

  /**
   * Событие возникает при клике на компоненте-списке, которым управляют классы наследники 
   * {@link com.technology.jep.jepria.client.widget.list.ListManager}.
   */
  ROW_CLICK_EVENT,

  /**
   * Событие возникает при двойном клике на компоненте-списке, которым управляют классы наследники 
   * {@link com.technology.jep.jepria.client.widget.list.ListManager}.
   */
  ROW_DOUBLE_CLICK_EVENT,

  /**
   * Событие возникает при сортировке в компоненте-списке, которым управляют классы наследники 
   * {@link com.technology.jep.jepria.client.widget.list.ListManager}.
   */
  CHANGE_SORT_EVENT,

  /**
   * Событие возникает при запросе обновления набора данных посредством инструментальной панели управления листанием 
   * {@link com.technology.jep.jepria.client.widget.toolbar.PagingToolBar}.
   */
  PAGING_REFRESH_EVENT,

  /**
   * Событие возникает при запросе установки количества записей набора данных на странице посредством инструментальной панели управления листанием 
   * {@link com.technology.jep.jepria.client.widget.toolbar.PagingToolBar}.
   */
  PAGING_SIZE_EVENT,

  /**
   * Событие возникает при запросе перехода на заданную страницу набора данных посредством инструментальной панели управления листанием 
   * {@link com.technology.jep.jepria.client.widget.toolbar.PagingToolBar}.
   */
  PAGING_GOTO_EVENT,

  /**
   * Событие возникает при попытке захвата строки списочной формы во время ее перетаскивания.
   */
  DRAG_START_EVENT,
  
  /**
   * Событие возникает при положении строки списочной формы над другой во время ее перетаскивания.
   */
  DRAG_OVER_EVENT,
  
  /**
   * Событие возникает при покидании положения строки списочной формы над другой во время ее перетаскивания.
   */
  DRAG_LEAVE_EVENT,
  
  /**
   * Событие возникает при отпускании строки списочной формы во время ее перетаскивания.
   */
  DROP_EVENT,
  
  /**
   * Событие возникает при изменении отметки необходимости удаления файла в поле 
   * {@link com.technology.jep.jepria.client.widget.field.multistate.large.JepLargeField}.
   */
  CHANGE_IS_DELETED_FILE_EVENT;
}
