package com.technology.jep.jepria.server.download;

import java.sql.SQLException;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.db.LargeObject;
import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Абстрактный базовый класс для реализаций выгрузки файла.
 */
public abstract class AbstractFileDownload implements FileDownload {

	protected CallContext storedContext;
	protected LargeObject largeObject = null;
	
	/**
	 * Метод начинает чтение данных из LOB. 
	 * 
	 * @param rowId идентификатор строки таблицы
	 * @return рекомендуемая величина буфера
	 * @throws ApplicationException
	 */
	public int beginRead(Object rowId) 
		throws ApplicationException {

		return 0;
	}
	
	/**
	 * Метод завершает чтение данных из LOB.
	 * 
	 * @throws SpaceException
	 */
	public void endRead() throws SpaceException {
		CallContext.attach(storedContext);
		try {
			largeObject.endRead();
			CallContext.commit();
		} catch (SpaceException ex) {
			cancel();
			throw ex;
		} catch (SQLException ex) {
			throw new SystemException("end write error", ex);
		} catch (Throwable th) {
			th.printStackTrace();
			throw new SystemException("end write error", new RuntimeException(th));
		} finally {
			CallContext.end();
		}
	}
	
	/**
	 * Метод отменяет текущую операцию и откатывает транзакцию.
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
			CallContext.end();
		}
	}

}
