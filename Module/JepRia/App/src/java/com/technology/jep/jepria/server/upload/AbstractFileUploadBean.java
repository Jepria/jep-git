package com.technology.jep.jepria.server.upload;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import com.technology.jep.jepria.server.db.LargeObject;
import com.technology.jep.jepria.server.ejb.CallContext;
import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Абстрактный базовый класс для FileUpload Stateful Session EJB 3
 * 
 */
public abstract class AbstractFileUploadBean implements FileUpload {
	@Resource
	protected SessionContext sessionContext;
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
			sessionContext.getUserTransaction().commit();
		} catch (SpaceException ex) {
			cancel();
			throw ex;
		} catch (javax.transaction.SystemException ex) {
			throw new SystemException("end write error", ex);
		} catch (HeuristicRollbackException ex) {
			throw new SystemException("end write error", ex);
		} catch (HeuristicMixedException ex) {
			throw new SystemException("end write error", ex);
		} catch (RollbackException ex) {
			throw new SystemException("end write error", ex);
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
			UserTransaction transaction = sessionContext.getUserTransaction();
			if (transaction.getStatus() == Status.STATUS_ACTIVE) {
				transaction.rollback();
			}
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