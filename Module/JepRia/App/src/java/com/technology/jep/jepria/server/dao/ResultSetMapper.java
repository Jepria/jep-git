package com.technology.jep.jepria.server.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

/**
 * Используется для мэппинга данных из ResultSet в Dto.
 *
 * @param <T> тип объекта Dto
 */
public abstract class ResultSetMapper<T> {
	protected static Logger logger = Logger.getLogger(ResultSetMapper.class.getName());	
	
	private static Calendar DEFAULT_CALENDAR = Calendar.getInstance();
	
	/**
	 * Данный метод осуществляет мэппинг полей между объектом dto и ResultSet.
	 * 
	 * @param rs     экземпляр класса ResultSet
	 * @param dto    экземпляр dto
	 * @throws SQLException
	 */
	public abstract void map(ResultSet rs, T dto) throws SQLException;
	
	/**
	 * Получение Integer из ResultSet. Если значение == null, то метод возвращает null,
	 * а не 0, как rs.getInt(...).
	 * 
	 * @param rs                ResultSet
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
	 * Данный метод решает проблему получения дат 01.04.1981-01.04.1984 из ResultSet.
	 * 
	 * @param rs           ResultSet
	 * @param columnName   Имя столбца
	 * @return значение столбца
	 * @throws SQLException
	 */
	public static Timestamp getTimestamp(ResultSet rs, String columnName) throws SQLException {
		return rs.getTimestamp(columnName, DEFAULT_CALENDAR);
	}
	
	/**
	 * Данный метод решает проблему получения дат 01.04.1981-01.04.1984 из ResultSet.
	 * 
	 * @param rs           ResultSet
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
