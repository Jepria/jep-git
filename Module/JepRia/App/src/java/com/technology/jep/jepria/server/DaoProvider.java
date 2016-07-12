package com.technology.jep.jepria.server;

/**
 * Интерфейс фабрики, возвращающей прокси для Dao.
 * @param <D> интерфейс Dao
 */
public interface DaoProvider<D> {
	/**
	 * Возвращает Dao (или прокси для него).
	 * @return объект Dao или прокси
	 */
	D getDao();
	/**
	 * Возвращает JNDI-имя источника данных.
	 * @return JNDI-имя источника данных.
	 */
	String getDataSourceJndiName();
	
	/**
	 * Возвращает имя модуля для передачи в DB.
	 * @return имя модуля
	 */
	String getModuleName();
	
	/**
	 * Устанавливает имя модуля для передачи в DB.
	 * @param moduleName имя модуля.
	 */
	void setModuleName(String moduleName);
}
