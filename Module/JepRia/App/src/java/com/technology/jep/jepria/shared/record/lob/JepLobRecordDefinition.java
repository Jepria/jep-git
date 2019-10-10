package com.technology.jep.jepria.shared.record.lob;
 
import java.util.Map;
import java.util.Set;

import com.technology.jep.jepria.shared.exceptions.UnsupportedException;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Определение записи, содержащей одно или несколько Lob-полей.
 * @deprecated for Rest use {@link org.jepria.server.data.RecordDefinition} instead
 */
@Deprecated
public class JepLobRecordDefinition extends JepRecordDefinition {

  /**
   * Имя таблицы, содержащей записи с Lob-полями.
   */
  private String tableName;
  
  /**
   * Имя поля, хранящего простой первичный ключ.
   */
  private String keyFieldName;

  /**
   * Карта соответствия полей формы полям записи БД.
   */
  private Map<String, String> fieldMap;
  
  /**
   * Конструктор используется для случаев, когда запись имеет несколько Lob-полей и сложный первичный ключ.
   * 
   * @param typeMap карта типов полей
   * @param primaryKey первичный ключ
   * @param tableName таблица БД
   * @param fieldMap карта соответствия полей формы полям записи таблицы БД
   */
  public JepLobRecordDefinition(
      Map<String, JepTypeEnum> typeMap,
      String[] primaryKey,
      String tableName,
      Map<String, String> fieldMap){
    
    super(typeMap, primaryKey);
    if(primaryKey.length == 1) {
      keyFieldName = fieldMap.get(primaryKey[0]);
    } else {
      throw new UnsupportedException("JepFileRecordDefinition: complex primary key not supported yet.");
    }
    this.tableName = tableName;
    this.fieldMap = fieldMap;
  }

  public String getTableName() {
    return tableName;
  }

  public String getKeyFieldName() {
    return keyFieldName;
  }

  public Map<String, String> getFieldMap() {
    return fieldMap;
  }

  public void setFieldMap(Map<String, String> fieldMap) {
    this.fieldMap = fieldMap;
  }  

  public boolean hasNonEmptyFileFields(JepRecord record) {
    Map<String, String> fieldMap = getFieldMap();
    Map<String, JepTypeEnum> typeMap = getTypeMap();
    if (fieldMap != null && typeMap != null) {
      Set<Map.Entry<String, String>> entries = fieldMap.entrySet();
      for (Map.Entry<String, String> fieldMapEntry : entries) {
        String fieldName = fieldMapEntry.getKey();
        JepTypeEnum fieldType = typeMap.get(fieldName);
        if (fieldType == JepTypeEnum.BINARY_FILE || fieldType == JepTypeEnum.TEXT_FILE) {
          Object fileNameObject = record.get(fieldName);
          String fileName = fileNameObject instanceof String ? (String)fileNameObject : null;
          if (!JepRiaUtil.isEmpty(fileName)) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
