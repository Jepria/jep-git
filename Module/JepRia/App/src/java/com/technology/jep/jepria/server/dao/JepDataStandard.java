package com.technology.jep.jepria.server.dao;

import java.util.List;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;

/**
 * Базовый интерфейс работы с данными БД.
 * @deprecated for Rest use {@link org.jepria.server.data.Dao} instead
 */
@Deprecated
public interface JepDataStandard {
  /**
   * Поиск.
   * 
   * @param templateRecord запись-образец, по которому выполняется поиск
   * @param autoRefreshFlag контейнер, в который помещается флаг автообновления
   * @param maxRowCount максимальное число возвращаемых записей
   * @param operatorId идентификатор пользователя
   * @return список объектов в виде List&lt; com.technology.jep.jepria.shared.dto.JepRecord &gt;
   * @throws ApplicationException
   */
  List<JepRecord> find(JepRecord templateRecord, Mutable<Boolean> autoRefreshFlag, Integer maxRowCount, Integer operatorId) throws ApplicationException;
  
  /**
   * Создание.
   * 
   * @param record создаваемая запись
   * @param operatorId идентификатор пользователя
   * @return идентификатор записи или null, если запись идентифицируется по сложному
   * первичному ключу
   * @throws ApplicationException
   */
  Object create(JepRecord record, Integer operatorId) throws ApplicationException;

  /**
   * Редактирование.
   * 
   * @param record запись с новыми значениями
   * @param operatorId идентификатор пользователя
   * @throws ApplicationException
   */
  void update(JepRecord record, Integer operatorId) throws ApplicationException;

  /**
   * Удаление.
   * 
   * @param record удаляемая запись
   * @param operatorId идентификатор пользователя
   * @throws ApplicationException
   */
  void delete(JepRecord record, Integer operatorId) throws ApplicationException;
}
