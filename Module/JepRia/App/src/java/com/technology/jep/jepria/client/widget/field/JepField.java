package com.technology.jep.jepria.client.widget.field;

/**
 * Интерфейс для полей библиотеки JepRia.
 */
public interface JepField<E, V> {

  /**
   * Установка наименования поля.
   * 
   * @param fieldLabel наименование поля.
   */
  void setFieldLabel(String fieldLabel);

  /**
   * Получение наименования поля.
   *
   * @return наименование поля
   */
  String getFieldLabel();

  /**
   * Установка ширины наименования поля.
   * 
   * @param labelWidth ширина наименования поля
   */
  void setLabelWidth(int labelWidth);
  
  /**
   * Установка ширины компонента редактирования поля.
   * 
   * @param fieldWidth ширина компонента редактирования поля
   */
  void setFieldWidth(int fieldWidth);
  
  /**
   * Установка значения поля.
   *
   * @param value значение поля
   */
  void setValue(Object value);
  
  /**
   * Получение значения поля.
   *
   * @return значение поля
   */
  <X> X getValue();
  
  /**
   * Очищает значение поля.
   */
  void clear();
  
  /**
   * Проверяет: содержит ли допустимое значение поле.
   *
   * @return true - если поле содержит допустимое значение, false - в противном случае
   */
  boolean isValid();
  
  /**
   * Получение карты Просмотра.
   */
  V getViewCard();
  
  /**
   * Получение карты Редактирования.
   */
  E getEditableCard();
  
  /**
   * Установка доступности или недоступности (карты Редактирования) поля для редактирования.
   * 
   * @param enabled true - поле доступно для редактирования, false - поле не доступно для редактирования
   */
  void setEnabled(boolean enabled);
  
  /**
   * Установка возможности отображения карты Редактирования поля.
   * 
   * @param editable true - карта Редактирования поля отображается (обычный режим), false - всегда отображается только карта Просмотра поля
   */
  void setEditable(boolean editable);

  /**
   * Установка возможности ввода пустого значения в поле.
   * 
   * @param allowBlank true - допускает пустое значение поля, false - поле обязательное для заполнения
   */
  void setAllowBlank(boolean allowBlank);
}
