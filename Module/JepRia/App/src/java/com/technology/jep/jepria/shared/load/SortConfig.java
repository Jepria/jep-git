package com.technology.jep.jepria.shared.load;

/**
 * Класс содержащий параметры (конфигурацию) необходимые для выполнения сортировки данных списка.
 */
public class SortConfig extends PagingConfig {
  private static final long serialVersionUID = 1L;
  
  /**
   * Поле сортировки.
   */
  private String sortField;
  
  /**
   * Направление сортировки.
   */
  private SortDir sortDir = SortDir.NONE;

  /**
   * Направление сортировки.
   */
  public enum SortDir {

    /**
     * Сортировка отсутствует.
     */
    NONE,

    /**
     * Сортировка по возрастанию.
     */
    ASC,

    /**
     * Сортировка по убыванию.
     */
    DESC;
  }
  
  /**
   * Создает конфигурацию сортировки с атрибутами по умолчанию.
   */
  public SortConfig() {
  }
  
  /**
   * Создает конфигурацию сортировки с заданным полем и направлением сортировки.
   *
   * @param sortField поле сортировки
   * @param sortDir направление сортировки
   */
  public SortConfig(String sortField, SortDir sortDir) {
    this.sortField = sortField;
    this.sortDir = sortDir;
  }

  /**
   * Возвращает поле сортировки.
   * 
   * @return поле сортировки
   */
  public String getSortField() {
    return sortField;
  }

  /**
   * Устанавливает поле сортировки.
   * 
   * @param sortField поле сортировки
   */
  public void setSortField(String sortField) {
    this.sortField = sortField;
  }

  /**
   * Возвращает направление сортировки.
   * 
   * @return направление сортировки
   */
  public SortDir getSortDir() {
    return sortDir;
  }

  /**
   * Устанавливает направление сортировки.
   * 
   * @param sortDir направление сортировки
   */
  public void setSortDir(SortDir sortDir) {
    this.sortDir = sortDir;
  }

  /**
   * Переводит объект класса конфигурации в строковое представление.
   *
   * @return строковое представление конфигурации
   */
  public String toString() {
    StringBuffer sbResult = new StringBuffer();
    sbResult.append("sortField=");
    sbResult.append(sortField);
    sbResult.append(",\n");
    sbResult.append("sortDir=");
    sbResult.append(sortDir);
    sbResult.append(",\n");
    sbResult.append(super.toString());
    return sbResult.toString();
  }
}
