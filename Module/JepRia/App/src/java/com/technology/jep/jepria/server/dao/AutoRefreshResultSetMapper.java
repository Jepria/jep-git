package com.technology.jep.jepria.server.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.technology.jep.jepria.shared.util.Mutable;

/**
 * Используется для мэппинга данных из ResultSet в Dto.<br/>
 * Дополнен функционалом проверки необходимости автообновления.
 *
 * @param <T> тип объекта Dto
 */
public abstract class AutoRefreshResultSetMapper<T> extends ResultSetMapper<T> {

  /**
   * Контейнер с флагом автообновления.
   */
  Mutable<Boolean> autoRefreshFlag;
  
  /**
   * Вложенный маппер.
   */
  ResultSetMapper<T> mapper;
  
  /**
   * Создание маппера с автообновлением на основе стандартного ResultSetMapper.
   * 
   * @param autoRefreshFlag контейнер, с флагом автообновления
   * @param mapper маппер
   */
  public AutoRefreshResultSetMapper(Mutable<Boolean> autoRefreshFlag, ResultSetMapper<T> mapper) {
    super();
    this.autoRefreshFlag = autoRefreshFlag;
    this.mapper = mapper;
  }

  /**
   * Переопределённый метод, проверяющий необходимость автообновления, а потом вызывающий map() у внутреннего маппера.
   */
  @Override
  public void map(ResultSet rs, T dto) throws SQLException {
    if (isRefreshNeeded(rs)) {
      autoRefreshFlag.set(true);
    }
    mapper.map(rs, dto);
  }
  
  /**
   * Абстрактный метод, предназначенный для проверки необходимости автообновления.
   * 
   * @param rs результирующий набор
   * @return истина, если содержимое набора сигнализирует о необходимости обновления, и ложь в противном случае 
   * @throws SQLException
   */
  public abstract boolean isRefreshNeeded(ResultSet rs) throws SQLException;
  
}
