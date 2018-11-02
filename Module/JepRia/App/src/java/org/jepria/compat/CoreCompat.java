package org.jepria.compat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jepria.shared.RecordDefinition;

import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.JepFieldNames;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
import com.technology.jep.jepria.shared.util.DefaultComparator;
import com.technology.jep.jepria.shared.util.Mutable;

/**
 * Временный класс для поддержания обратной совместимости "новой" и "старой" архитектур Jerpia.
 * Предназначен для использования только в системном коде
 */
@Deprecated
public class CoreCompat {
  
  public static RecordDefinition convertRecordDefinition(JepRecordDefinition jepRecordDefinition) {
    if (jepRecordDefinition == null) {
      return null;
    }
    
    return new RecordDefinition() {
      
      @Override
      public List<String> getPrimaryKey() {
        return Arrays.asList(jepRecordDefinition.getPrimaryKey());
      }
      
      @Override
      public JepTypeEnum getFieldType(String fieldName) {
        return jepRecordDefinition.getType(fieldName);
      }
      
      @Override
      public Set<String> getFieldNames() {
        Set<String> fields = new HashSet<>();
        fields.addAll(jepRecordDefinition.getTypeMap().keySet());
        fields.addAll(jepRecordDefinition.getLikeMap().keySet());
        fields.addAll(getPrimaryKey());
        return fields;
      }
      
      @Override
      public JepLikeEnum getFieldMatch(String fieldName) {
        return jepRecordDefinition.getLikeMap().get(fieldName);
      }
      
      @Override
      public Comparator<Object> getFieldComparator(String fieldName) {
        return null;
      }
    };
  }
  
  /**
   * Convert List(Map(String, Object)) to List(JepRecord)
   */
  public static List<Map<String, Object>> convertList(List<JepRecord> list) {
    if (list == null) {
      return null;
    }
    
    return list.stream().map(rec -> (Map<String, Object>)rec).collect(Collectors.toList());
  }
  
  public static Comparator<Object> getDefaultComparator() {
    return DefaultComparator.instance;
  }
  
  public static final int DEFAULT_MAX_ROW_COUNT = JepRiaConstant.DEFAULT_MAX_ROW_COUNT;
  public static final String MAX_ROW_COUNT__FIELD_NAME = JepFieldNames.MAX_ROW_COUNT;
  
  /**
   * Descendand of this class is an org.jepria.server.dao.JepDataStandard with com.technology.jep.jepria.server.dao.JepDataStandard's implementation
   */
  public static org.jepria.server.dao.JepDataStandard convertDao(com.technology.jep.jepria.server.dao.JepDataStandard dao) {

    if (dao == null) {
      return null;
    }
    
    return new org.jepria.server.dao.JepDataStandard() {
      @Override
      public List<Map<String, Object>> find(Map<String, Object> model, Integer maxRowCount, Integer operatorId) {
        JepRecord templateRecord = new JepRecord();
        templateRecord.putAll(model);
        
        final List<JepRecord> result;
        try {
          result = dao.find(templateRecord, new Mutable<Boolean>(false), maxRowCount, operatorId);
        } catch (ApplicationException e) {
          throw new RuntimeException(e);
        }
        
        return convertList(result);
      }
  
      @Override
      public Object create(Map<String, Object> record, Integer operatorId) {
        JepRecord templateRecord = new JepRecord();
        templateRecord.putAll(record);
        
        final Object result;
        try {
          result = dao.create(templateRecord, operatorId);
        } catch (ApplicationException e) {
          throw new RuntimeException(e);
        }
        
        return result;
      }
  
      @Override
      public void update(Map<String, Object> record, Integer operatorId) {
        JepRecord templateRecord = new JepRecord();
        templateRecord.putAll(record);
        
        try {
          dao.update(templateRecord, operatorId);
        } catch (ApplicationException e) {
          throw new RuntimeException(e);
        }
      }
  
      @Override
      public void delete(Map<String, Object> record, Integer operatorId) {
        JepRecord templateRecord = new JepRecord();
        templateRecord.putAll(record);
        
        try {
          dao.delete(templateRecord, operatorId);
        } catch (ApplicationException e) {
          throw new RuntimeException(e);
        }
      }
    };
    
  }
}
