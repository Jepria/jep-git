package com.technology.jep.jepria.server.download;

import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Интерфейс загрузки файла.
 */
public interface FileDownload {
	/**
	 * Начало длинной транзакции чтения файла из LOB.
	 * 
	 * @param tableName 		имя таблицы, из которой выполняется чтение
	 * @param fileFieldName 	имя поля, из которого выполняется чтение
	 * @param keyFieldName 	имя поля, идентифицирующего строку таблицы
	 * @param rowId 				идентификатор строки таблицы
	 * @param dataSourceJndiName	JNDI-имя источника данных модуля
	 * @return рекомендуемый размер буфера
	 * @throws ApplicationException 
	 */
	int beginRead(
			String tableName
			, String fileFieldName
			, String keyFieldName
			, Object rowId
			, String dataSourceJndiName) 
			throws ApplicationException;
	
	/**
	 * Метод начинает чтение данных из LOB. 
	 * 
	 * @param rowId идентификатор строки таблицы
	 * @return рекомендуемая величина буфера
	 * @throws ApplicationException
	 */
	int beginRead(
			Object rowId) 
			throws ApplicationException;
	
	/**
	 * Окончание загрузки.
	 * После выполнения этого метода stateful bean должен быть удалён. 
	 * Для удаления bean необходимо в классе реализации перед методом указать декларацию Remove.
	 *
	 * @throws SpaceException
	 */
	void endRead() throws SpaceException;
	
	/**
	 * Откат длинной транзакции.
	 * После выполнения этого метода stateful bean должен быть удалён. 
	 * Для удаления bean необходимо в классе реализации перед методом указать декларацию Remove.
	 */
	void cancel();
}
