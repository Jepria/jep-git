package org.jepria.server.service.rest;

import java.util.LinkedHashMap;
import java.util.Map;

public interface SearchRequest {
  /**
   * @return клиентский поисковый шаблон
   */
  Map<String, Object> getTemplate();
  /**
   * @return упорядоченная последовательность имён столбцов списка с указанием порядка сортировки данных в каждом столбце (>=0 или <0)
   */
  LinkedHashMap<String, Integer> getListSortConfig();
}
