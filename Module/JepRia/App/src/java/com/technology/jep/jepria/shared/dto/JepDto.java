package com.technology.jep.jepria.shared.dto;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Унифицированный Dto, реализованный в виде Map.<br/>
 * <br/>
 * JepDto предназначен для использования в Gwt-модулях в качестве базового носителя данных (Dto).<br/>
 * Традиционные Dto в клиентском коде использовать невозможно из-за отсутствия поддержки reflection в Gwt.<br/>
 * <br/>
 * Особенности:<br/>
 * Dto принимает участие в Rpc-вызовах, поэтому требует сериализации, для проведения которой требуется не только пометить
 * данный класс интерфейсом {@link com.google.gwt.user.client.rpc.IsSerializable}, но и указать в качестве полей все возможные ссылки на типы передаваемых значений 
 * (для этого требуется использование fake-полей, на основе которых Gwt-компилятор создаст rpcPolicyMap, в который поместит информацию об указанных типах и позволит избежать {@link com.google.gwt.user.client.rpc.SerializationException}).
 */
public class JepDto extends HashMap<String, Object> implements IsSerializable {
  private static final long serialVersionUID = 1L;
  
  /**
   * Фейк-поле для типа {@link java.util.Date}.<br/>
   * Обязательно должно присутствовать данное поле (во избежание проблем с сериализацией при установке его как значение бина ({@link com.technology.jep.jepria.shared.dto.JepDto}))<br/>
   * http://www.sencha.com/forum/archive/index.php/t-114607.html
   */
  private Date dateFake;

  /**
   * Фейк-поле для типа {@link java.lang.Boolean}.<br/>
   * Обязательно должно присутствовать данное поле (во избежание проблем с сериализацией при установке его как значение бина ({@link com.technology.jep.jepria.shared.dto.JepDto}))<br/>
   * http://www.sencha.com/forum/archive/index.php/t-114607.html
   */
  private Boolean booleanFake;
  
  /**
   * Фейк-поле для типа {@link java.lang.String}.<br/>
   * Обязательно должно присутствовать данное поле (во избежание проблем с сериализацией при установке его как значение бина ({@link com.technology.jep.jepria.shared.dto.JepDto}))<br/>
   * http://www.sencha.com/forum/archive/index.php/t-114607.html
   */
  private String stringFake;
  
  /**
   * Фейк-поле для типа {@link java.lang.Number} и его наследников : {@link java.math.BigDecimal}, {@link java.math.BigInteger}, {@link java.lang.Byte}, {@link java.lang.Double}, {@link java.lang.Float}, {@link java.lang.Integer}, {@link java.lang.Long}, а также {@link java.lang.Short}<br/>
   * Обязательно должно присутствовать данное поле (во избежание проблем с сериализацией при установке его как значение бина ({@link com.technology.jep.jepria.shared.dto.JepDto}))<br/>
   * http://www.sencha.com/forum/archive/index.php/t-114607.html
   */
  private Number numberFake;
  
  /**
   * Фейк-поле для типа {@link com.google.gwt.safehtml.shared.SafeHtml} и его наследников : {@link com.google.gwt.safehtml.shared.SafeHtmlString}.<br/>
   * Обязательно должно присутствовать данное поле (во избежание проблем с сериализацией при установке его как значение бина ({@link com.technology.jep.jepria.shared.dto.JepDto}))<br/>
   * http://www.sencha.com/forum/archive/index.php/t-114607.html
   */
  private SafeHtml safeHtmlFake;

  /**
   * Создает объект JepDto.
   */
  public JepDto() {
  }
  
  /**
   * Клонирующий конструктор.
   *
   * @param dto Dto, на основании свойств которого будет создан новый объект
   */
  public JepDto(JepDto dto) {
    setProperties(dto.getProperties());
  }

  public String toString() {
    StringBuilder sbResult = new StringBuilder();
    Collection<String> propertyNames = keySet();
    for(String name: propertyNames) {
      if(sbResult.length() > 0) {
        sbResult.append(",\n");
      }
      sbResult.append(name);
      sbResult.append(" = '");
      appendPropertyValue(sbResult, get(name));
      sbResult.append("'");
    }
    return sbResult.toString();
  }

  private void appendPropertyValue(StringBuilder sbResult, Object property) {
    sbResult.append(property);
    if(property instanceof Date) {
      sbResult.append(" (" + ((Date)property).getTime()+ ")");
    }
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    Collection<String> propertyNames = keySet();
    for(String name: propertyNames) {
      Object property = get(name);
      result = prime * result + ((property == null) ? 0 : property.hashCode());
    }
    return result;
  }

  /**
   * Сравнивает объекты JepDto.
   *
   * @param obj   объект, с которым сравнивается текущий экземпляр класса
   * @return true - объекты идентичны друг другу, false - в противном случае
   */
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof JepDto))
      return false;
    JepDto other = (JepDto) obj;
    Collection<String> propertyNames = this.keySet();
    if(propertyNames.size() != other.keySet().size())
      return false;
    for(String name: propertyNames) {
      Object property = this.get(name);
      if (property == null) {
        if (other.get(name) != null)
          return false;
      } else if (!property.equals(other.get(name)))
        return false;
    }
    return true;
  }

  /**
   * Копирование (а не присваивание) используется для обновления содержимого при сохрании самого объекта.
   * 
   * @param newValues запись с новыми значениями
   */
  public void update(JepDto newValues) {
    setProperties(newValues.getProperties());
  }
  
  /**
   * Установка свойств (полей) Dto (парами ключ/значение).
   * 
   * @param properties пары ключ/значение
   */
  public void setProperties(Map<String, Object> properties) {
    for (String property: properties.keySet()) {
      put(property, properties.get(property));
    }
  }

  /**
   * Получение свойств (полей) Dto (парами ключ/значение).
   * 
   * @return пары ключ/значение
   */
  public Map<String, Object> getProperties() {
    Map<String, Object> newMap = new HashMap<String, Object>();

    newMap.putAll(this);

    return newMap;
  }
  
  /**
   * Устанавливает значение заданного поля.<br/>
   * Метод создан для обратной совместимости с версией JepRia 7.X.X и ниже.<br/>
   * В реализации вызывает HashMap.put(key, value) и возращает результат выполнения.
   * 
   * @param key идентификатор поля
   * @param value значение поля
   * @return предыдущее значение поля
   */
  public Object set(String key, Object value) {
    return put(key, value);
  }
  
  /**
   * Возвращает значение заданного поля.<br/>
   * Метод создан для обратной совместимости с JepRia 7.X.X и ниже
   * и для избежания необходимости выполнять cast на прикладном уровне.<br/>
   * В реализации вызывает HashMap.get(key) и осуществляет cast в указанный тип.
   * @param <X> возвращаемый тип
   * @param key идентификатор поля
   * @return значение поля
   */
  public <X> X get(String key) {
    return (X) super.get(key);
  }

}
