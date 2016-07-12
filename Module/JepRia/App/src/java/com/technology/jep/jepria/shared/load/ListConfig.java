package com.technology.jep.jepria.shared.load;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Класс содержащий параметры (конфигурацию) необходимые для выполнения различных действий над списками.
 */
public class ListConfig implements IsSerializable {
  private static final long serialVersionUID = 1466864803043323075L;
  
  /**
   * Уникальный идентификатор списка.
   */
  private Integer listUID = null;

  /**
   * Создает новую конфигурацию для работы со списком.
   */
  public ListConfig() {
  }
  
  /**
   * Переводит объект класса конфигурации в строковое представление.
   *
   * @return строковое представление конфигурации
   */
  public String toString() {
    StringBuilder sbResult = new StringBuilder();
    sbResult.append("listUID=");
    sbResult.append(listUID);
    return sbResult.toString();
  }

  /**
   * Установка уникального идентификатора списка.
   *
   * @param listUID уникальный идентификатор списка
   */
  public void setListUID(Integer listUID) {
    this.listUID = listUID;
  }

  /**
   * Получение уникального идентификатора списка.
   *
   * @return уникальный идентификатор списка
   */
  public Integer getListUID() {
    return listUID;
  }
  
}
