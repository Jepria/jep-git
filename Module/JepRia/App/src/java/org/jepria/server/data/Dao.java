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
   * @param template поисковый шаблон
   * @param operatorId идентификатор пользователя
   * @return список записей
   */
  // maxRowCount это атрибут шаблона, а не отдельное поле!
  List<?> find(Object template, Integer operatorId);

  Object findByPrimaryKey(Map<String, ?> primaryKeyMap, Integer operatorId);

  /**
   * Создание записи
   * 
   * @param record создаваемая запись
   * @param operatorId идентификатор пользователя
   * @return первичный ключ созданной записи (простой или составной)
   */
  Object create(Object record, Integer operatorId);

  /**
   * Редактирование записи
   *
   * @param primaryKey первичный ключ редактируемой записи (простой или составной)
   * @param record запись с новыми значениями
   * @param operatorId идентификатор пользователя
   */
  void update(Map<String, ?> primaryKey, Object record, Integer operatorId);

  /**
   * Удаление записи
   * 
   * @param primaryKey первичный ключ удаляемой записи (простой или составной)
   * @param operatorId идентификатор пользователя
   */
  void delete(Map<String, ?> primaryKey, Integer operatorId);
}
