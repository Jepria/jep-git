package com.technology.jep.jepria.client.widget.field;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepMultiStateField;
import com.technology.jep.jepria.client.widget.field.multistate.large.JepLargeField;
import com.technology.jep.jepria.client.widget.field.validation.Validator;
import com.technology.jep.jepria.shared.exceptions.IdNotFoundException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Класс управления полями.
 */
@SuppressWarnings("rawtypes")
public class FieldManager extends HashMap<String, JepMultiStateField> implements Validator {

  private static final long serialVersionUID = 1L;
  
  /**
   * Префикс автогенерации web-ID.
   */
  private String autoGenerateWebIdPrefix;

  /**
   * Устанавливает значение префикса для автогенерации web-ID.
   * @param autoGenerateWebIdPrefix Префикс автогенерации web-ID.
   */
  public void setAutoGenerateWebIdPrefix(String autoGenerateWebIdPrefix) {
    this.autoGenerateWebIdPrefix = autoGenerateWebIdPrefix;
  }

  /**
   * Текущее состояние.
   */
  protected WorkstateEnum _workstate = null;
  
  /**
   * Установка нового состояния полей
   * 
   * @param workstate новое состояние
   */
  public void changeWorkstate(WorkstateEnum workstate) {
    // Только в случае, если действительно изменяется состояние.
    if(workstate != null && !workstate.equals(_workstate)) {
      onChangeWorkstate(workstate);
      _workstate = workstate;
    }
  }
  
  /**
   * Обработчик нового состояния
   * 
   * @param newWorkstate новое состояние
   */
  protected void onChangeWorkstate(WorkstateEnum newWorkstate) {
    Collection<JepMultiStateField> fields = this.values();
    for(JepMultiStateField field: fields) {
      field.changeWorkstate(newWorkstate);
    }
  }
  
  /**
   * Проверяет: содержат ли допустимые значения <strong>видимые</strong> поля.
   *
   * @return true - если все поля содержат допустимые значения, false - в противном случае
   *
   * @see #isValid(boolean all)
   */
  @Override
  public boolean isValid() {
    return isValid(false);
  }

  /**
   * Проверяет: содержат ли допустимые значения поля.
   *
   * @param all true - учитывать все поля, false - учитывать только видимые поля
   * @return true - если все поля содержат допустимые значения, false - в противном случае
   */
  public boolean isValid(boolean all) {
    boolean valid = true;
    
    Collection<JepMultiStateField> fields = this.values();
    for(JepMultiStateField field: fields) {
      if(field.isVisible() || all) {
        if(!field.isValid()) {
          valid = false;
        }
      }
    }

    return valid;
  }

  /**
   * Заполнение полей значениями из записи JepRecord.
   * 
   * @param record запись для заполнения полей
   */
  public void setValues(JepRecord record) {
    if(record != null) {
      Set<Map.Entry<String, JepMultiStateField>> entries = this.entrySet();
      for (Map.Entry<String, JepMultiStateField> entry: entries) {
        String fieldId = entry.getKey();
        Object value = record.get(fieldId);
        if(value == null || (value instanceof List && ((List<?>)value).size() == 0)) {
          entry.getValue().clear();
        } else {
          entry.getValue().setValue(value);
        }
      }
    }
  }
  
  /**
   * Получение значений <strong>видимых</strong> полей в виде записи JepRecord.
   *
   * @return значение полей в виде записи JepRecord
   *
   * @see #getValues(boolean all)
   */
  public JepRecord getValues() {
    return getValues(false);
  }

  /**
   * Получение значений полей в виде записи JepRecord.
   *
   * @param all true - учитывать все поля, false - учитывать только видимые поля
   * @return значение полей в виде записи JepRecord
   */
  public JepRecord getValues(boolean all) {
    JepRecord record = new JepRecord();

    Set<Map.Entry<String, JepMultiStateField>> entries = this.entrySet();
    for(Map.Entry<String, JepMultiStateField> entry: entries) {
      JepMultiStateField field = entry.getValue();
      if(field.isVisible() || all) {
        String fieldId = entry.getKey();
        Object value = field.getValue();
        record.set(fieldId, value);
      }
    }
    
    return record;
  }

  /**
   * Очищает значение поля.
   *
   * @param fieldId поле
   */
  public void clearField(String fieldId) {
    get(fieldId).clear();
  }

  /**
   * Очистка значений полей.
   */
  public void clear() {
    Collection<JepMultiStateField> fields = this.values();
    for(JepMultiStateField field: fields) {
      field.clear();
    }
  }
  
  /**
   * Установка значения поля.
   *
   * @param fieldId поле
   * @param value значение поля
   */
  public void setFieldValue(String fieldId, Object value) {
    get(fieldId).setValue(value);
  }
  
  /**
   * Получение значения поля.
   *
   * @param fieldId поле
   * @return значение поля
   */
  @SuppressWarnings("unchecked")
  public <X> X getFieldValue(String fieldId) {
    return (X) get(fieldId).getValue();
  }
  
  /**
   * Получение значения опции {@link com.technology.jep.jepria.shared.field.option.JepOption} из поля возвращающего опцию.<br/>
   * В реализации метод вызывает {@link com.technology.jep.jepria.shared.field.option.JepOption#getValue(Object option)}.<br/>
   * Пример использования в презентере прикладного модуля:
   * <pre>
   *   ...
   *   fields.&lt;Integer&gt;getFieldValueFromOption(CITY_ID); // Получение значения типа Integer или null.
   *   ...
   *   fields.&lt;String&gt;getFieldValueFromOption(COMPANY_CODE); // Получение значения типа String или null.
   *   ...
   * </pre>
   * 
   * @param fieldId поле
   * @return значение опции поля
   */
  protected <X> X getFieldValueFromOption(String fieldId) {
    return JepOption.<X>getValue(get(fieldId).getValue());
  }

  /**
   * Установка списка опций List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt; для полей типа:
   * <ul>
   *   <li>{@link com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField}</li>
   * </ul>
   * 
   * @param fieldId поле
   * @param options список опций List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt;
   */
  public void setFieldOptions(String fieldId, List<JepOption> options) {
    JepMultiStateField field = get(fieldId);
    if(field instanceof JepOptionField) {
      ((JepOptionField)field).setOptions(options);
    }
  }
  
  /**
   * Установка списка опций (с учетом возможности вставки пустой опции) List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt; для полей типа:
   * <ul>
   *   <li>{@link com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField}</li>
   * </ul>
   * 
   * @param fieldId поле
   * @param options список опций List&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt;
   * @param hasEmptyOption флаг для вставки пустой опции
   */
  public void setFieldOptions(String fieldId, List<JepOption> options, boolean hasEmptyOption) {
    JepMultiStateField field = get(fieldId);
    if(field instanceof JepComboBoxField) {
      ((JepComboBoxField)field).setOptions(options, hasEmptyOption);
    }
  }
  
  /**
   * Установка видимости поля.
   * 
   * @param fieldId поле
   * @param visible true - поле отображается, false - поле скрыто.
   */
  public void setFieldVisible(String fieldId, boolean visible) {
    get(fieldId).setVisible(visible);
  }
  
  /**
   * Установка доступности или недоступности (карты Редактирования) поля для редактирования.
   * 
   * @param fieldId поле
   * @param enabled true - поле доступно для редактирования, false - поле не доступно для редактирования
   */
  public void setFieldEnabled(String fieldId, boolean enabled) {
    get(fieldId).setEnabled(enabled);
  }
  
  /**
   * Установка возможности отображения карты Редактирования поля.
   * 
   * @param fieldId поле
   * @param editable true - карта Редактирования поля отображается (обычный режим), false - всегда отображается только карта Просмотра поля
   */
  public void setFieldEditable(String fieldId, boolean editable) {
    get(fieldId).setEditable(editable);
  }
  
  /**
   * Определяет, является ли пустое значение допустимым значением поля.
   * 
   * @param fieldId поле
   * @param allowBlank true - допускает пустое значение поля, false - поле обязательное для заполнения.
   */
  public void setFieldAllowBlank(String fieldId, boolean allowBlank) {
    get(fieldId).setAllowBlank(allowBlank);
  }
  
  /**
   * Показ или скрытие индикатора загрузки у поля.
   * @param fieldId поле
   * @param imageVisible true - показать, false - скрыть
   */
  public void setFieldLoadingImage(String fieldId, boolean imageVisible) {
    get(fieldId).setLoadingImage(imageVisible);
  }
  
  /**
   * Добавление слушателя заданного типа для поля.
   *
   * @param fieldId поле
   * @param eventType тип события
   * @param listener слушатель
   */
  public void addFieldListener(String fieldId, JepEventType eventType, JepListener listener) {
    get(fieldId).addListener(eventType, listener);
  }
  
  /**
   * Установка ширины наименования полей.
   * 
   * @param labelWidth ширина наименования полей
   */
  public void setLabelWidth(int labelWidth) {
    Collection<JepMultiStateField> fields = this.values();
    for(JepMultiStateField field: fields) {
      field.setLabelWidth(labelWidth);
    }
  }
  
  /**
   * Регистрация поля, как предназначенного для управления объектом класса.
   * 
   * @param fieldId идентификатор поля
   * @param field поле
   * @return зарегистрированное (добавленное) поле   
   */
  @Override
  public JepMultiStateField put(String fieldId, JepMultiStateField field) {
    if (field instanceof JepLargeField) {
      JepLargeField largeField = (JepLargeField) field;
      if (JepRiaUtil.isEmpty(largeField.getFieldName())) {
        largeField.setFieldName(fieldId);
      }
    }
    
    // Автогенерация web-ID, если задан префикс.
    if (!JepRiaUtil.isEmpty(autoGenerateWebIdPrefix)
        && JepRiaUtil.isEmpty(field.getWebId())) {
      
      StringBuilder sb = new StringBuilder();
      sb.append(autoGenerateWebIdPrefix);
      sb.append(fieldId);
      sb.append(JEP_FIELD_POSTFIX);
      
      field.setWebId(sb.toString().toUpperCase());
    }
    
    return super.put(fieldId, field);
  }
  
  /**
   * {@inheritDoc}
   * 
   * Особенности:<br/>
   * Добавлена проверка на существование запрашиваемого поля. В случае его отстутствия, 
   * fieldManager генерирует {@link com.technology.jep.jepria.shared.exceptions.IdNotFoundException}
   */
  @Override
  public JepMultiStateField get(Object fieldId) {
    JepMultiStateField field = super.get(fieldId);
    // Проверка на существование запрашиваемого идентификатора поля.
    if (field == null) {
      throw new IdNotFoundException(JepClientUtil.substitute(JepTexts.fieldManager_idNotFoundError(), fieldId));
    }
    return field;
  }
}
