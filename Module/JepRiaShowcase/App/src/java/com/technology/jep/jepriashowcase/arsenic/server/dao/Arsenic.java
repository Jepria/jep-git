package com.technology.jep.jepriashowcase.arsenic.server.dao;
 
import java.util.List;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
 
public interface Arsenic extends JepDataStandard {
  /**
   * Метод, имитирующий длительную загрузку данных на сервере. Фактически, просто спит указанное количество времени.
   * @param msec требуемая задержка в миллисекундах.
   * @throws ApplicationException
   */
  void durableFetch(long msec) throws ApplicationException;
  
  List<JepOption> getTreeCatalog(Integer parentCatalogId) throws ApplicationException;
}
