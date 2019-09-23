package org.jepria.server.data;

import com.technology.jep.jepria.shared.AppCompat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Класс для сравнения записей, представленных в виде Map, для сортировки списка
 */
public class RecordComparator implements Comparator<Object> {

  protected final List<String> fieldNames;
  
  protected final Function<String, Comparator<Object>> fieldComparatorSupplier;
  
  protected final Function<String, Integer> sortOrderSupplier;
  
  /**
   * 
   * @param fieldNames упорядоченная последовательность полей сравниваемых записей для последовательного сравнения значений. // TODO специфицировать порядок сравнения если null или пустой
   * @param fieldComparatorSupplier поставщик компараторов для сравнения значений полей. Если null, используется компаратор по умолчанию {@link #getDefaultFieldComparator()}
   * @param sortOrderSupplier поставщик направления сортировки полей. Если null, применяется натуральный порядок сортировки.
   */
  public RecordComparator(List<String> fieldNames, Function<String, Comparator<Object>> fieldComparatorSupplier,
                          Function<String, Integer> sortOrderSupplier) {
    this.fieldNames = fieldNames;
    this.fieldComparatorSupplier = fieldComparatorSupplier;
    this.sortOrderSupplier = sortOrderSupplier;
  }

  public RecordComparator(List<String> fieldNames) {
    this(fieldNames, null, null);
  }

  @Override
  public int compare(Object record1, Object record2) {
    // сравнение null-записей
    if (record1 == null || record2 == null) {
      if (record1 == null && record2 == null) {
        return onBothNull();
      } else {
        return record1 == null ? onFirstNull(record2) : onSecondNull(record1);
      }
    }


    // TODO кешировать преобразованные в мар поля (прямо по одному), чтобы не дёргать рефлексию почём зря!
    Map<String, ?> map1 = DtoUtil.dtoToMap(record1);
    Map<String, ?> map2 = DtoUtil.dtoToMap(record2);


    int cmpResult = compareByFields(map1, map2, fieldNames, fieldComparatorSupplier, sortOrderSupplier);

    if (cmpResult != 0) {
      return cmpResult;

    } else {

      // Note:
      // При сравнении записи будут считаться "условно" равными, если будут равны значения только тех полей, которые перечислены в списке явно сравниваемых.
      // Однако в этом случае результат сортировки двух списков, состоящих из одних и тех же элементов (но различающиеся порядком элементов)
      // по одним и тем же полям может различаться в зависимости от изначального порядка элементов в этих списках.
      // Чтобы этого избежать, при "условном" равенстве записей (в последнюю очередь) нужно проводить уточняющую сортировку по остальным полям записи
      // до тех пор, пока не будут найдены различные значения.

      // объединение полей обеих записей, по которым не проводилось основное сравнение
      List<String> remainFieldNames = new ArrayList<>();
      remainFieldNames.addAll(map1.keySet());
      remainFieldNames.addAll(map2.keySet());
      if (fieldNames != null) {
        remainFieldNames.removeAll(fieldNames);
      }

      // строгое сравнение проводится дефолтным компаратором по возрастанию
      return compareByFields(map1, map2, remainFieldNames, fieldName -> getDefaultFieldComparator(), fieldName -> 1);
    }
  }
  
  /**
   * Сравнение двух записей по заданному упорядоченному набору полей. Возвращает результат сравнения по первому различию значений
   * @param record1 non null
   * @param record2 non null
   * @param cmpFieldNames non null
   * @param fieldComparatorProvider
   * @param sortOrderProvider
   * @return 
   */
  protected final int compareByFields(Map<String, ?> record1, Map<String, ?> record2, 
      List<String> cmpFieldNames, Function<String, Comparator<Object>> fieldComparatorProvider,
                                      Function<String, Integer> sortOrderProvider) {
    
    if (cmpFieldNames != null) {
      
      for (String fieldName: cmpFieldNames) {
        
        final Object v1 = record1.get(fieldName);
        final Object v2 = record2.get(fieldName);
        
        
        
        // компаратор
        Comparator<Object> fieldComparator = null;
        if (fieldComparatorProvider != null) {
          fieldComparator = fieldComparatorProvider.apply(fieldName);
        }
        if (fieldComparator == null) {
          fieldComparator = getDefaultFieldComparator();
        }
        
        
        
        // порядок сортировки
        int sortOrder = 1;// default order is natural
        
        // apply sort order, if any
        if (sortOrderProvider != null) {
          Integer sortOrder0 = sortOrderProvider.apply(fieldName);
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
    return AppCompat.getDefaultComparator();
  }

  /**
   * @return результат сравнения двух null-записей
   */
  protected int onBothNull() {
    return 0;
  }

  /**
   * @param second second, non-null record
   * @return результат сравнения null-записи и не-null-записи
   */
  protected int onFirstNull(Object second) {
    // nulls go to the tail of the list
    return 1;
  }

  /**
   * @param first first, non-null record
   * @return результат сравнения не-null-записи и null-записи
   */
  protected int onSecondNull(Object first) {
    // nulls go to the tail of the list
    return -1;
  }
}
