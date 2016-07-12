package com.technology.jep.jepria.client.widget.event;

import java.util.List;

/**
 * Интерфейс шаблона проектирования "Наблюдатель"
 */
public interface JepObservable {

  /**
   * Добавление слушателя элемента для указанного типа события.
   * 
   * @param eventType      тип события
   * @param listener      слушатель события
   */
  void addListener(JepEventType eventType, JepListener listener);

  /**
   * Получение списка слушателей элемента для указанного типа события.
   * 
   * @param eventType      тип события
   * @return список слушатель для указанного типа события
   */
  List<JepListener> getListeners(JepEventType eventType);

  /**
   * Удаление слушателя для указанного типа события.
   * 
   * @param eventType      тип события
   * @param listener      удаляемый слушатель события
   */
  void removeListener(JepEventType eventType, JepListener listener);
  
  /**
   * Оповещение слушателей элемента указанного типа события
   * 
   * @param eventType      тип события
   * @param event        оповещаемое событие
   */
  void notifyListeners(JepEventType eventType, JepEvent event);
}
