package com.technology.jep.jepria.shared.load;

import static com.technology.jep.jepria.shared.field.JepFieldNames.MAX_ROW_COUNT;

import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Класс содержащий параметры (конфигурацию) необходимые для выполнения поиска данных для заполнения списка.
 */
public class FindConfig extends ListConfig {
  private static final long serialVersionUID = 1466864803043333075L;
  
  /**
   * Параметры (шаблон) поиска.
   */
  private JepRecord templateRecord = null;

  /**
   * Максимальное количество записей возвращаемых при поиске.
   */
  private Integer maxRowCount;
  
  /**
   * Получение максимального количества записей возвращаемых при поиске.
   *
   * @return максимальное количество записей возвращаемых при поиске
   */
  public Integer getMaxRowCount() {
    return maxRowCount;
  }

  /**
   * Создает новую конфигурацию для поиска данных.
   */
  public FindConfig() {
  }
  
  /**
   * Создает новую конфигурацию для поиска данных на основе поискового шаблона.
   *
   * @param templateRecord поисковый шаблон
   */
  public FindConfig(JepRecord templateRecord) {
    setTemplateRecord(templateRecord);
  }

  /**
   * Установка поискового шаблона.
   *
   * @param templateRecord поисковый шаблон
   */
  public void setTemplateRecord(JepRecord templateRecord) {
    this.maxRowCount = templateRecord.get(MAX_ROW_COUNT);
    this.templateRecord = templateRecord;
  }

  /**
   * Получение поискового шаблона.
   *
   * @return templateRecord поисковый шаблон
   */
  public JepRecord getTemplateRecord() {
    return templateRecord;
  }

  /**
   * Метод вычисления хэш-значения объекта.<br/>
   * Данный метод переопределяют совместно с методом {@link FindConfig#equals(Object)}. 
   * При реализации метода следует придерживаться следующей концепции сравнения объектов :
   * <ul>
   *   <li>сперва вычисляются хэш-коды сравниваемых объектов</li>
   *  <li>если их значения отличаются, то это явный признак, что объекты различны</li>
   *  <li>в противном случае - будет отрабатываться логика метода {@link FindConfig#equals(Object)} </li>
   * </ul>
   * Для более точного сравнения объектов следует обеспечивать уникальность хэш-значений объектов.<br/>
   * Для этого в алгоритме {@link FindConfig#hashCode()} в качестве основания для вычисления обычно используются простые числа (17, 31 и т.д.), 
   * а в качестве зависимых значений выступают хэш-коды полей класса.<br/>
   * Более того, метод используется при извлечении значений из хэш-коллекций (например, {@link java.util.HashMap}), 
   * в которых в качестве <strong>КЛЮЧА</strong> используются объекты данного класса. 
   * Уникальность хэш-кодов объекта обеспечивает эффективную работу хэш-коллекции. В противном случае, при поиске ключа
   * будет осуществлен полный перебор коллекции, что значительно повышает временные затраты.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((maxRowCount == null) ? 0 : maxRowCount.hashCode());
    result = prime * result
        + ((templateRecord == null) ? 0 : templateRecord.hashCode());
    return result;
  }

  /**
   * Сравнение двух объектов конфигураций для поиска.<br>
   * Данный метод переопределяют совместно с методом {@link FindConfig#hashCode()}. 
   *
   * @param obj объект конфигурации для поиска
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FindConfig other = (FindConfig) obj;
    if (templateRecord == null) {
      if (other.templateRecord != null)
        return false;
    } else if (!templateRecord.equals(other.templateRecord))
      return false;
    return true;
  }
  
  /**
   * Переводит объект класса конфигурации в строковое представление.
   *
   * @return строковое представление конфигурации
   */
  public String toString() {
    StringBuilder sbResult = new StringBuilder();
    sbResult.append("templateRecord=");
    sbResult.append("{");
    sbResult.append((templateRecord == null ? "null" : templateRecord.toString()));
    sbResult.append("}");
    sbResult.append(",\n");
    sbResult.append("maxRowCount=");
    sbResult.append(maxRowCount);
    sbResult.append(",\n");
    sbResult.append(super.toString());
    return sbResult.toString();
  }

}
