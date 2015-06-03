package com.technology.jep.jepria.server.download.blob;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.NotSupportedException;

import oracle.j2ee.ejb.StatefulDeployment;

import com.technology.jep.jepria.server.db.blob.BinaryLargeObject;
import com.technology.jep.jepria.server.download.AbstractFileDownloadBean;
import com.technology.jep.jepria.server.ejb.CallContext;
import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * FileDownload Stateful Session EJB 3 для чтения из BINARY_FILE.
 */
@Local( { BinaryFileDownloadLocal.class })
@Remote( { BinaryFileDownloadRemote.class })
@StatefulDeployment
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class BinaryFileDownloadBean extends AbstractFileDownloadBean implements BinaryFileDownload {
	
	/**
	 * Метод начинает чтение данных из LOB. 
	 * 
	 * @param rowId идентификатор строки таблицы
	 * @return рекомендуемая величина буфера
	 * @throws ApplicationException
	 */
	public int beginRead(
			String tableName
			, String fileFieldName
			, String keyFieldName
			, Object rowId
			, String dataSourceJndiName
			, String resourceBundleName) 
			throws ApplicationException {

		int result = -1;
		try {
			sessionContext.getUserTransaction().begin();
			CallContext.begin(sessionContext, dataSourceJndiName, resourceBundleName);

			super.largeObject = new BinaryLargeObject(tableName, fileFieldName, keyFieldName, rowId);
			result = ((BinaryLargeObject)super.largeObject).beginRead();
		} catch (ApplicationException ex) {
			cancel();
			throw ex;
		} catch (javax.transaction.SystemException ex) {
			throw new SystemException("begin write error", ex);
		} catch (IllegalStateException ex) {
			ex.printStackTrace();
			throw new SystemException("begin write error", ex);
		} catch (NotSupportedException ex) {
			ex.printStackTrace();
			throw new SystemException("begin write error", ex);
		} finally {
			storedContext = CallContext.detach();
		}

		return result;
	}
	
	/**
	 * Чтение очередного блока данных из BINARY_FILE.
	 * 
	 * @param dataBlock блок данных
	 * @throws SpaceException
	 */
	public int continueRead(byte[] dataBlock) throws SpaceException {
		CallContext.attach(storedContext);
		boolean cancelled = true;
		int result = 0;
		try {
			result = ((BinaryLargeObject)super.largeObject).continueRead(dataBlock);
			cancelled = false;
		} catch (SpaceException e) {
			throw e;
		} catch (Exception e) {
			throw new SpaceException("continue read error", (Exception) e);
		} finally {
			if (cancelled) {
				cancel();
			}
			storedContext = CallContext.detach();
		}
		
		return result;
	}
}
