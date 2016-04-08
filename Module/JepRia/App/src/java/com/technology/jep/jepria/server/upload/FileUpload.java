package com.technology.jep.jepria.server.upload;

import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Интерфейс выгрузки файла
 */
public interface FileUpload {

	/**
	 * Начало длинной транзакции записи файла в LOB.
	 * 
	 * @param tableName 		имя таблицы, в которую выполняется запись
	 * @param fileFieldName 	имя поля, в которое выполняется запись
	 * @param keyFieldName 	имя поля, идентифицирующего строку таблицы
	 * @param rowId 				идентификатор строки таблицы
	 * @param dataSourceJndiName	JNDI-имя источника данных модуля
	 * @return рекомендуемый размер буфера
	 * @throws ApplicationException 
	 */
	int beginWrite(
		String tableName
		, String fileFieldName
		, String keyFieldName
		, Object rowId
		, String dataSourceJndiName) 
		throws ApplicationException;

	/**
	 * Функция-обертка для {@link #beginWrite(String tableName, String fileFieldName, String keyFieldName, Object rowId, String dataSourceJndiName)}.
	 * В классе реализации в конкретном модуле данный метод перегружаем вызывая в нем 
	 * {@link #beginWrite(String tableName, String fileFieldName, String keyFieldName, Object rowId, String dataSourceJndiName)}
	 * с подставленными из констант класса реализации параметрами:<br/>
	 * <code>
	 * tableName,<br/>
	 * fileFieldName,<br/>
	 * keyFieldName,<br/>
	 * dataSourceJndiName<br/>
	 * </code>.
	 * 
	 * @param rowId 				идентификатор строки таблицы
	 * @return рекомендуемый размер буфера
	 * @throws ApplicationException 
	 */
	int beginWrite(
		Object rowId) 
		throws ApplicationException;

	/**
	 * Окончание выгрузки.
	 * После выполнения этого метода stateful bean должен быть удалён. 
	 * Для удаления bean необходимо в классе реализации перед методом указать декларацию Remove.
	 *
	 * @throws SpaceException
	 */
	void endWrite() throws SpaceException;

	/**
	 * Откат длинной транзакции.
	 */
	void cancel();
}
