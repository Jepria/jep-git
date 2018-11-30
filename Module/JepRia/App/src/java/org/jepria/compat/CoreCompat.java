package org.jepria.compat;

import java.util.Comparator;

import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.jep.jepria.shared.field.JepFieldNames;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.util.DefaultComparator;

/**
 * Временный класс для поддержания обратной совместимости "новой" и "старой" архитектур Jerpia.
 * Предназначен для использования только в системном коде
 */
@Deprecated
public class CoreCompat {

  public static Comparator<Object> getDefaultComparator() {
    return DefaultComparator.instance;
  }
  
  public static final int DEFAULT_MAX_ROW_COUNT = JepRiaConstant.DEFAULT_MAX_ROW_COUNT;
  public static final String MAX_ROW_COUNT__FIELD_NAME = JepFieldNames.MAX_ROW_COUNT;
  
  public static final String OPTION_NAME_KEY = JepOption.OPTION_NAME;
  public static final String OPTION_VALUE_KEY = JepOption.OPTION_VALUE;
  
  public static final int DEFAULT_PAGE_SIZE = PagingConfig.DEFAULT_PAGE_SIZE;
}
