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
   * @param template поисковый шаблон (объект прикладного класса SearchDto)
   * @param operatorId идентификатор пользователя
   * @return список записей (список объектов прикладного класса Dto)
   */
  // maxRowCount это атрибут шаблона, а не отдельное поле!
  List<?> find(Object template, Integer operatorId);

  /**
   * Возвращает список записей, найденных по заданному первичному ключу.
   *
   * Если существует запись с заданным первичным ключом, то список должен состоять из одной записи.
   * Если не существует записи с заданным первичным ключом, то список должен быть пустым либо {@code null}.
   * Список, состоящий из более чем одной записи считается некорректным состоянием программы.
   *
   * Возвращаемое значение является именно списком из одной записи, а не собственно записью для того, чтобы
   * сигнатуры и реализации методов {@link #findByPrimaryKey(Map, Integer)} и {@link #find(Object, Integer)} были идентичными,
   * а также во избежание лишних проверок на прикладном уровне.
   *
   * @param primaryKeyMap первичный ключ (простой или составной) с именами полей в регистре lower snake case (например, {@code entity_id})
   * @param operatorId
   * @return
   */
  // primaryKey не передаётся в виде целого Dto потому что здесь нужен именно первичный ключ. primaryKey является мапом потому что первичный ключ может быть составным
  List<?> findByPrimaryKey(Map<String, ?> primaryKeyMap, Integer operatorId);

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
   * @param primaryKey первичный ключ редактируемой записи (простой или составной) с именами полей в регистре lower snake case (например, {@code entity_id})
   * @param record запись с новыми значениями
   * @param operatorId идентификатор пользователя
   */
  // primaryKey не передаётся в виде целого Dto потому что здесь нужен именно первичный ключ. primaryKey является мапом потому что первичный ключ может быть составным
  void update(Map<String, ?> primaryKey, Object record, Integer operatorId);

  /**
   * Удаление записи
   * 
   * @param primaryKey первичный ключ удаляемой записи (простой или составной) с именами полей в регистре lower snake case (например, {@code entity_id})
   * @param operatorId идентификатор пользователя
   */
  // primaryKey не передаётся в виде целого Dto потому что здесь нужен именно первичный ключ. primaryKey является мапом потому что первичный ключ может быть составным
  void delete(Map<String, ?> primaryKey, Integer operatorId);
}
