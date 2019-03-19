package com.technology.jep.jepria.server.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.lob.JepFileReference;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Используется для мэппинга данных из {@code ResultSet} в {@code Dto}.
 *
 * @param <T> тип объекта Dto
 */
public abstract class ResultSetMapper<T> {
  protected static Logger logger = Logger.getLogger(ResultSetMapper.class.getName());  
  
  private static Calendar DEFAULT_CALENDAR = Calendar.getInstance();
  
  /**
   * Данный метод осуществляет мэппинг полей между объектом {@code dto} и {@code ResultSet}.
   * 
   * @param rs     экземпляр класса {@code ResultSet}
   * @param dto    экземпляр {@code dto}
   * @throws SQLException
   */
  public abstract void map(ResultSet rs, T dto) throws SQLException;
  
  /**
   * Получение объекта {@link com.technology.jep.jepria.shared.field.option.JepOption} из {@code ResultSet}. Если значение {@code == null}, то метод возвращает {@code EMPTY_OPTION}.
   * 
   * @deprecated Так как тип значения, возвращаемого rs.getObject(columnOptionValue), может отличаться от ожидаемого, 
   * например, вместо Integer может вернуться BigDecimal, что приведет к потенциальным ClassCastException в прикладном коде.
   * @param rs                  {@code ResultSet}
   * @param columnOptionValue     Имя столбца для значения опции
   * @param columnOptionName    Имя столбца для наименования опции 
   * @return опция из указанных значений имени и значения
   * @throws SQLException
   */
  @Deprecated
  public static JepOption getOption(ResultSet rs, String columnOptionValue, String columnOptionName) throws SQLException {
    Object optionValue = rs.getObject(columnOptionValue);
    if (rs.wasNull()) {
      return JepOption.EMPTY_OPTION;
    }
    else {
      return new JepOption(rs.getString(columnOptionName), optionValue);
    }
  }
  
  /**
   * Получение файловой ссылки {@link com.technology.jep.jepria.shared.record.lob.JepFileReference} из {@code ResultSet}. Если значение {@code == null}, то метод возвращает {@code null},
   * а не объект ссылки.
   * 
   * @param rs                  {@code ResultSet}
   * @param columnFileName    Имя колонки для имени файла (может быть {@code null})
   * @param columnKey           Имя колонки для первичного ключа записи
   * @param columnFileExtension   Имя колонки для расширения файла
   * @param columnMimeType       Имя колонки для mime-type файла
   * @return опция из указанных значений имени и значения
   * @throws SQLException
   */
  public static JepFileReference getFileReference(ResultSet rs, String columnFileName, String columnKey, String columnFileExtension, String columnMimeType) throws SQLException {
    String fileExtension = rs.getString(columnFileExtension); // важно получить значение перед проверкой rs.wasNull()
    JepFileReference fileReference = null;
    if (!rs.wasNull()) {
      fileReference = JepRiaUtil.isEmpty(columnFileName) ? new JepFileReference(rs.getObject(columnKey), fileExtension, rs.getString(columnMimeType)) : 
          new JepFileReference(rs.getString(columnFileName), rs.getObject(columnKey), rs.getString(columnFileExtension), rs.getString(columnMimeType));
    }
    return fileReference;
  }
  
  /**
   * Получение {@code Boolean} из {@code ResultSet}. Если значение {@code == null}, то метод возвращает {@code null},
   * а не {@code false}, как {@code rs.getBoolean(...)}.
   * 
   * @param rs                {@code ResultSet}
   * @param columnName        Имя столбца
   * @return значение столбца
   * @throws SQLException
   */
  public static Boolean getBoolean(ResultSet rs, String columnName) throws SQLException {
    boolean result = rs.getBoolean(columnName);
    
    if (rs.wasNull()) {
         return null;
    } else {
         return result;
    }
  }

    
  /**
   * Получение {@code Integer} из {@code ResultSet}. Если значение {@code == null}, то метод возвращает {@code null},
   * а не 0, как rs.getInt(...).
   * 
   * @param rs                {@code ResultSet}
   * @param columnName        Имя столбца
   * @return значение столбца
   * @throws SQLException
   */
  public static Integer getInteger(ResultSet rs, String columnName) throws SQLException {
    int result = rs.getInt(columnName);
    
    if (rs.wasNull()) {
      return null;
    } else {
      return result;
    }
  }
  
  /**
   * Данный метод решает проблему получения дат 01.04.1981-01.04.1984 из {@code ResultSet}.
   * 
   * @param rs           {@code ResultSet}
   * @param columnName   Имя столбца
   * @return значение столбца
   * @throws SQLException
   */
  public static Timestamp getTimestamp(ResultSet rs, String columnName) throws SQLException {
    return rs.getTimestamp(columnName, DEFAULT_CALENDAR);
  }
  
  /**
   * Данный метод решает проблему получения дат 01.04.1981-01.04.1984 из {@code ResultSet}.
   * 
   * @param rs           {@code ResultSet}
   * @param columnName   Имя столбца
   * @return значение столбца
   * @throws SQLException
   */
  public static java.util.Date getDate(ResultSet rs, String columnName) throws SQLException {
    Date date = rs.getDate(columnName, DEFAULT_CALENDAR);
    date = workaroundForWrongDates(date);
    java.util.Date utilDate = null;
    if(date != null) {
      // Преобразование в java.util.Date, чтобы сериализация не отрезала часы, минуты, секунды
      utilDate = new java.util.Date(date.getTime());
    }
    
    return utilDate;
  }

  private final static long DATE_01_01_0001 = -62135780400000L;
  private static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
  private static boolean isWrongDate = false;
  /**
   * Workaround<br/>
   * <strong>Внимание !!!</strong><br/>
   * Даты, значение time которых меньше -62135780400000L (до 01.01.0001) считаются некорректными.<br/>
   * Для таких дат знак меняется на положительный (посредством форматирования и обратного парсинга).<br/>
   *
   * @param date проверяемая дата
   * @return корректная дата
   */
  private static Date workaroundForWrongDates(Date date) {
    if(date != null) {
      long time = date.getTime();
      if(time < DATE_01_01_0001) {
        String strDate = formatter.format(date);
        try {
          java.util.Date javaUtilDate = formatter.parse(strDate);
          date = new Date(javaUtilDate.getTime());
        } catch (ParseException ex) {
          logger.error("Wrong date format", ex);
        }
        // Логирование
        if(!isWrongDate) {
          isWrongDate = true; 
          logger.error("Wrong date time detected: time = " + time);
        }
      } else {
        // Логирование
        if(isWrongDate) {
          isWrongDate = false; 
          logger.error("Date times become right: time = " + time);
        }
      }
    }
    
    return date;
  }
}
