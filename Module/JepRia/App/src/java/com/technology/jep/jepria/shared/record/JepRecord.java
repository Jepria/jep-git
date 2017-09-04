package com.technology.jep.jepria.shared.record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.technology.jep.jepria.shared.dto.JepDto;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.lob.JepFileReference;
import com.technology.jep.jepria.shared.time.JepTime;

public class JepRecord extends JepDto {
  private static final long serialVersionUID = 1L;

  private List<String> primaryKey = null;
  
  /**
   * Фейк-поле для типа {@link com.technology.jep.jepria.shared.record.lob.JepFileReference}, необходимое для передачи и получения значений из полей формы JepLargeField.<br/>
   * Обязательно должно присутствовать данное поле (во избежание проблем с сериализацией при установке его как значение бина ({@link com.technology.jep.jepria.shared.record.JepRecord}))<br/>
   * http://www.sencha.com/forum/archive/index.php/t-114607.html
   */
  private JepFileReference<IsSerializable> fileReference;
  
  /**
   * Фейк-поле для типа {@link com.technology.jep.jepria.shared.field.option.JepOption}, необходимое для передачи и получения значений из полей формы {@link com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField}.<br/>
   * Обязательно должно присутствовать данное поле (во избежание проблем с сериализацией при установке его как значение бина ({@link com.technology.jep.jepria.shared.record.JepRecord}))<br/>
   * http://www.sencha.com/forum/archive/index.php/t-114607.html
   */
  private JepOption optionFake;
  
  /**
   * Фейк-поле для типа {@link com.technology.jep.jepria.shared.time.JepTime}, необходимое для передачи и получения значений из полей формы JepTimeField.<br/>
   * Обязательно должно присутствовать данное поле (во избежание проблем с сериализацией при установке его как значение бина ({@link com.technology.jep.jepria.shared.record.JepRecord}))<br/>
   * http://www.sencha.com/forum/archive/index.php/t-114607.html
   */
  private JepTime jepTimeFake;
  
  /**
   * Создает объект JepRecord.
   */
  public JepRecord() {
  }

  /**
   * Клонирующий конструктор.
   *
   * @param record JepRecord, на основании свойств которого будет создан новый объект
   */
  public JepRecord(JepRecord record) {
    super(record);
  }
  
  /**
   * Метод перегружен для обеспечения хранения currentRecord в списке Grid-а (для эффективного извлечения оттуда).<br/>
   * <br/>
   * hashCode и equals добавлены для идентификации записи по первичному ключу.
   */
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    Collection<String> propertyNames = primaryKey != null ? primaryKey : keySet();
    for(String name: propertyNames) {
      Object property = get(name);
      result = prime * result + ((property == null) ? 0 : property.hashCode());
    }
    return result;
  }

  /**
   * Определяет: равен ли текущий объект заданному.<br/>
   * Объекты считаются равными, если равны их первичные ключи. Если первичный ключ текущего объекта равен null, то наименования полей первичного
   * ключа берутся из первичного ключа сравниваемой записи. Если первичные ключи обеих записей равны null, то сравнение идет по всем полям текущего
   * объекта.<br/>
   * <br/>
   * TODO Убедиться, что соблюдаются совместные правила реализации hashCode и equals.
   * 
   * @param obj объект, с которым сравнивается текущий объект
   * @return true - если объекты равны, false - в противном случае
   */
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof JepRecord))
      return false;
    JepRecord other = (JepRecord) obj;
    
    Collection<String> propertyNames;
    if(this.primaryKey != null) {
      propertyNames = this.primaryKey;
    } else if(other.primaryKey != null) {
      propertyNames = other.primaryKey;
    } else {
      propertyNames = this.keySet();
    }
    
    for(String name: propertyNames) {
      Object property = this.get(name);
      if (property == null) {
        if (other.get(name) != null) {
          return false;
        }
      } else if (!property.equals(other.get(name))) {
        return false;
      }
    }
    return true;
  }

  public void setPrimaryKey(String[] primaryKey) {
    if(primaryKey != null) {
      this.primaryKey = new ArrayList<String>();
      for(int i = 0; i < primaryKey.length; i++) {
        this.primaryKey.add(primaryKey[i]);
      }
    }
  }

  public List<String> getPrimaryKey() {
    return primaryKey;
  }

}
