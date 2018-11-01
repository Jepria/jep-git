package org.jepria.server.dao;

import java.util.List;
import java.util.Map;

/**
 * Базовый интерфейс работы с записями в БД на прикладном уровне.
 */
// TODO переименовать, избавиться от слова Jep
public interface JepDataStandard {
  /**
   * Поиск.
   * 
   * @param model модель, по которому выполняется поиск
   * @param maxRowCount максимальное число возвращаемых записей
   * @param operatorId идентификатор пользователя
   * @return список записей
   */
  List<Map<String, Object>> find(Map<String, Object> model, Integer maxRowCount, Integer operatorId);
  
  /**
   * Создание записи
   * 
   * @param record создаваемая запись
   * @param operatorId идентификатор пользователя
   * @return идентификатор созданной записи или null, если запись идентифицируется по сложному
   * первичному ключу
   */
  Object create(Map<String, Object> record, Integer operatorId);

  /**
   * Редактирование записи
   * 
   * @param record запись с новыми значениями
   * @param operatorId идентификатор пользователя
   */
  void update(Map<String, Object> record, Integer operatorId);

  /**
   * Удаление записи
   * 
   * @param record удаляемая запись (одного поля recordId может быть недостаточно для случаев с составным первичным ключом)
   * @param operatorId идентификатор пользователя
   */
  void delete(Map<String, Object> record, Integer operatorId);
}
