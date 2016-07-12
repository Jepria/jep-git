package com.technology.jep.jepria.shared.util;

/**
 * Простейший контейнер для иммутабельных объектов.
 * 
 * @param <T> тип хранимого объекта
 */
public class Mutable<T> {
  
  /**
   * Хранимое значение.
   */
  private T value;
  
  /**
   * Конструктор, создающий контейнер с заданным объектом.
   * @param value объект, помещаемый в контейнер
   */
  public Mutable(T value) {
    this.value = value;
  }

  /**
   * Помещает объект в контейнер.
   * @param value помещаемый объект
   */
  public void set(T value) {
    this.value = value;
  }

  /**
   * Возвращает объект, содержащийся в контейнере.
   * @return объект, содержащийся в контейнере
   */
  public T get() {
    return value;
  }
  
}
