package com.technology.jep.jepria.server.download;

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
 * Абстрактный базовый класс для FileDownload Stateful Session EJB 3
 */
public abstract class AbstractFileDownloadBean implements FileDownload {
	@Resource
	protected SessionContext sessionContext;
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
