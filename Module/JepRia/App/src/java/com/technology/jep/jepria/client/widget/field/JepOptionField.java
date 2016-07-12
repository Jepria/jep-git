package com.technology.jep.jepria.client.widget.field;

import java.util.List;

import com.technology.jep.jepria.shared.field.option.JepOption;

/**
 * Интерфейс элементов пользовательского интерфейса, для которых предусмотрено заполнение 
 * возможных значений опциями.
 */
public interface JepOptionField {

  /**
   * Установка списка опций
   * 
   * @param options  устанавливаемые опции
   */
  void setOptions(List<JepOption> options);
}
