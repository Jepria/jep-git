package com.technology.jep.jepria.server.upload;

import com.technology.jep.jepria.server.db.LargeObject;
import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Абстрактный базовый класс для FileUpload Stateful Session EJB 3
 * 
 */
public abstract class AbstractFileUpload implements FileUpload {

	protected CallContext storedContext;
	protected LargeObject largeObject = null;

	/**
	 * Функция-обертка для {@link #beginWrite(String tableName, String fileFieldName, String keyFieldName, Object rowId, String dataSourceJndiName, String resourceBundleName)}.
	 * В классе реализации в конкретном модуле данный метод перегружаем вызывая в нем 
	 * {@link #beginWrite(String tableName, String fileFieldName, String keyFieldName, Object rowId, String dataSourceJndiName, String resourceBundleName)}
	 * с подставленными из констант класса реализации параметрами:<br/>
	 * <code>
	 * tableName,<br/>
	 * fileFieldName,<br/>
	 * keyFieldName,<br/>
	 * dataSourceJndiName,<br/>
	 * resourceBundleName<br/>
	 * </code>.
	 * В данном базовом классе содержит пустую реализацию, возвращающую 0.
	 * 
	 * @param rowId 				идентификатор строки таблицы
	 * @return рекомендуемый размер буфера
	 * @throws ApplicationException 
	 */
	public int beginWrite(Object rowId) 
		throws ApplicationException {

		return 0;
	}	

	/**
	 * Окончание выгрузки.
	 * После выполнения этого метода stateful bean должен быть удалён. 
	 * Для удаления bean необходимо в классе реализации перед методом указать декларацию Remove.
	 *
	 * @throws SpaceException
	 */
	public void endWrite() throws SpaceException {
		CallContext.attach(storedContext);
		try {
			largeObject.endWrite();
			CallContext.commit();
		} catch (SpaceException ex) {
			cancel();
			throw ex;
		} catch (Throwable th) {
			th.printStackTrace();
			throw new SystemException("end write error", new RuntimeException(th));
		} finally {
			CallContext.end();
		}
	}

	/**
	 * Откат длинной транзакции.
	 * После выполнения этого метода stateful bean должен быть удалён. 
	 * Для удаления bean необходимо в классе реализации перед методом указать декларацию Remove.
	 */
	public void cancel() {
		if (storedContext != null) {
			CallContext.attach(storedContext);
		}
		try {
			if (largeObject != null) {
				largeObject.cancel();
			}
			CallContext.rollback();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				CallContext.end();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}