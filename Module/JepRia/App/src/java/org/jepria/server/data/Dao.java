package org.jepria.server.data;

import java.util.List;
import java.util.Map;

/**
 * Базовый интерфейс работы с записями в БД.
 */
public interface Dao {
  
  /**
   * Поиск записей
   * 
   * @param model модель, по которой выполняется поиск
   * @param maxRowCount максимальное число возвращаемых записей
   * @param operatorId идентификатор пользователя
   * @return список записей
   */
  List<Map<String, ?>> find(Map<String, ?> model, Integer maxRowCount, Integer operatorId);
  
  /**
   * Создание записи
   * 
   * @param record создаваемая запись
   * @param operatorId идентификатор пользователя
   * @return первичный ключ созданной записи (простой или составной)
   */
  Object create(Map<String, ?> record, Integer operatorId);

  /**
   * Редактирование записи
   * 
   * @param record запись с новыми значениями
   * @param operatorId идентификатор пользователя
   */
  void update(Map<String, ?> record, Integer operatorId);

  /**
   * Удаление записи
   * 
   * @param record удаляемая запись (одного поля recordId может быть недостаточно для случаев с составным первичным ключом)
   * @param operatorId идентификатор пользователя
   */
  void delete(Map<String, ?> primaryKeyMap, Integer operatorId);
}
