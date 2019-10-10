package com.technology.jep.jepria.shared.record;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.technology.jep.jepria.shared.exceptions.SystemException;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @deprecated for Rest use {@link org.jepria.server.data.RecordDefinition} instead
 */
@Deprecated
public class JepRecordDefinition implements IsSerializable {
  
  private String[] primaryKey;
  private Map<String, JepTypeEnum> typeMap;
  /**
   * Карта полей формы с режимом поиска по текстовым полям.
   */
  private Map<String, JepLikeEnum> likeMap;
  
  protected JepRecordDefinition(Map<String, JepTypeEnum> typeMap, String[] primaryKey) {
    this.typeMap = typeMap;
    this.primaryKey = primaryKey;
  }
  
  protected void setTypeMap(Map<String, JepTypeEnum> typeMap) {
    this.typeMap = typeMap;
  }
  
  public Map<String, JepTypeEnum> getTypeMap() {
    return typeMap;
  }
  
  public JepTypeEnum getType(String fieldName) {
    return typeMap.get(fieldName);
  }
  
  public Map<String, Object> buildPrimaryKeyMap(JepRecord record) {
    Map<String, Object> primaryKeyMap = new HashMap<String, Object>();
    for(int i = 0; i < primaryKey.length; i++) {
      Object pkPart = record.get(primaryKey[i]);
      if(pkPart != null) {
        primaryKeyMap.put(primaryKey[i], pkPart);
      } else {
        throw new SystemException("buildPrimaryKeyMap error: primary key part is null");
      }
    }
    return primaryKeyMap;
  }
  
  public String[] getPrimaryKey() {
    return primaryKey;
  }

  /**
   * Получение карты полей формы с режимом поиска по текстовым полям.
   */
  public Map<String, JepLikeEnum> getLikeMap() {
    return likeMap;
  }
  
  /**
   * Установка карты полей формы с режимом поиска по текстовым полям.
   */
  public void setLikeMap(Map<String, JepLikeEnum> likeMap) {
    this.likeMap = likeMap;
  }
  
  
}
