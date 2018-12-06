package org.jepria.server.data;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;

import org.jepria.compat.CoreCompat;

/**
 * Класс для сравнения записей, представленных в виде Map, для сортировки списка
 */
// TODO переименовать в MapRecordComparator?
public class RecordComparator implements Comparator<Map<String, ?>> {

  public static interface FieldComparatorSupplier {
    Comparator<Object> getFieldComparator(String fieldName);
  }
  
  public static interface SortOrderSupplier {
    int getSortOrder(String fieldName);
  }
  
  protected final LinkedHashSet<String> fieldNames;
  
  protected final FieldComparatorSupplier fieldComparatorSupplier;
  
  protected final SortOrderSupplier sortOrderSupplier;
  
  protected final boolean strictCompare;
  
  /**
   * 
   * @param fieldNames упорядоченная последовательность полей записи для сравнения значений
   * @param fieldComparatorSupplier поставщик компараторов для сравнения значений полей. Если null, используется компаратор по умолчанию {@link #getDefaultFieldComparator()}
   * @param sortOrderSupplier поставщик направления сортировки полей. Если null, применяется натуральный порядок сортировки.
   * @param strictCompare флаг строгого сравнения записей
   */
  public RecordComparator(LinkedHashSet<String> fieldNames, FieldComparatorSupplier fieldComparatorSupplier, 
      SortOrderSupplier sortOrderSupplier, boolean strictCompare) {
    this.fieldNames = fieldNames;
    this.fieldComparatorSupplier = fieldComparatorSupplier;
    this.sortOrderSupplier = sortOrderSupplier;
    this.strictCompare = strictCompare;
  }
  
  public RecordComparator(LinkedHashSet<String> fieldNames, FieldComparatorSupplier fieldComparatorSupplier, 
      SortOrderSupplier sortOrderSupplier) {
    this(fieldNames, fieldComparatorSupplier, sortOrderSupplier, false);
  }
  
  public RecordComparator(LinkedHashSet<String> fieldNames) {
    this(fieldNames, null, null);
  }

  @Override
  public int compare(Map<String, ?> record1, Map<String, ?> record2) {
    // сравнение null-записей
    if (record1 == null || record2 == null) {
      if (record1 == null && record2 == null) {
        return onBothNull(record1, record2);
      } else {
        return record1 == null ? onFirstNull(record1, record2) : onSecondNull(record1, record2);
      }
    }
    
    // базовое сравнение записей
    int cmpResult = compareByFields(record1, record2, fieldNames, fieldComparatorSupplier, sortOrderSupplier);
    
    if (cmpResult != 0) {
      return cmpResult;
    }
    
    
    // Записи "условно" равны (равны значения только тех полей, которые явно участвуют в сравнении)

    
    if (!strictCompare) {
      // Достаточно "условного" равенства записей
      return 0;
    } else {
      // строгое сравнение значений остальных полей записей (которые явно не участвуют в сравнении)
      return strictCompare(record1, record2);
    }
  }
  
  /**
   * Сравнение двух записей по заданному упорядоченному набору полей. Возвращает результат сравнения по первому различию значений
   * @param record1 non null
   * @param record2 non null
   * @param fieldNames
   * @param fieldComparatorSupplier
   * @param sortOrderSupplier
   * @return 
   */
  protected final int compareByFields(Map<String, ?> record1, Map<String, ?> record2, 
      LinkedHashSet<String> fieldNames, FieldComparatorSupplier fieldComparatorSupplier, 
      SortOrderSupplier sortOrderSupplier) {
    
    if (fieldNames != null) {
      
      for (String fieldName: fieldNames) {
        
        final Object v1 = record1.get(fieldName);
        final Object v2 = record2.get(fieldName);
        
        
        
        // компаратор
        Comparator<Object> fieldComparator = null;
        if (fieldComparatorSupplier != null) {
          fieldComparator = fieldComparatorSupplier.getFieldComparator(fieldName);
        }
        if (fieldComparator == null) {
          fieldComparator = getDefaultFieldComparator();
        }
        
        
        
        // порядок сортировки
        int sortOrder = 1;// default order is natural
        
        // apply sort order, if any
        if (sortOrderSupplier != null) {
          Integer sortOrder0 = sortOrderSupplier.getSortOrder(fieldName);
          if (sortOrder0 != null) {
            sortOrder = sortOrder0;
          }
        }
        
        
        
        int cmpResult;
        
        if (sortOrder < 0) {
          cmpResult = fieldComparator.compare(v2,  v1);
        } else {
          cmpResult = fieldComparator.compare(v1,  v2);
        }
        
        
        if (cmpResult != 0) {
          // return immediately on the first differing field
          return cmpResult;
        }
      }
    }
    
    return 0;
  }
  
  public static Comparator<Object> getDefaultFieldComparator() {
    return CoreCompat.getDefaultComparator();
  }
  
  /**
   * @param record1 null
   * @param record2 null
   * @return результат сравнения двух null-записей
   */
  protected int onBothNull(Map<String, ?> record1, Map<String, ?> record2) {
    return 0;
  }
  
  /**
   * @param record1 null
   * @param record2 non null
   * @return результат сравнения null-записи и не-null-записи
   */
  protected int onFirstNull(Map<String, ?> record1, Map<String, ?> record2) {
    // nulls go to the tail of the list
    return 1;
  }
  
  /**
   * @param record1 non null
   * @param record2 null
   * @return результат сравнения не-null-записи и null-записи
   */
  protected int onSecondNull(Map<String, ?> record1, Map<String, ?> record2) {
    // nulls go to the tail of the list
    return -1;
  }
  
  /**
   * @param record1 non null
   * @param record2 non null
   * @return результат строгого (уточняющего) сравнения записей
   */
  //  При сравнении записи будут считаться "условно" равными, если будут равны значения только тех полей, которые участвуют в сравнении.
  //  Однако в этом случае результат сортировки двух списков, состоящих из одних и тех же элементов (но различающиеся порядком элементов)
  //  по одним и тем же полям может различаться в зависимости от изначального порядка элементов в этих списках.
  //  Чтобы этого избежать, при "условном" равенстве записей (в последнюю очередь) нужно проводить уточняющую сортировку по остальным полям записи
  //  до тех пор, пока не будут найдены различные значения.
  protected int strictCompare(Map<String, ?> record1, Map<String, ?> record2) {
    
    // объединение полей обеих записей, по которым не проводилось основное сравнение
    LinkedHashSet<String> remainFieldNames = new LinkedHashSet<>();
    remainFieldNames.addAll(record1.keySet());
    remainFieldNames.addAll(record2.keySet());
    if (fieldNames != null) {
      remainFieldNames.removeAll(fieldNames);
    }
    
    // строгое сравнение проводится дефолтным компаратором по возрастанию
    return compareByFields(record1, record2, remainFieldNames, fieldName -> getDefaultFieldComparator(), fieldName -> 1);
  }

}
