package com.technology.jep.jepria.server.upload.blob;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.NotSupportedException;

import oracle.j2ee.ejb.StatefulDeployment;

import com.technology.jep.jepria.server.db.blob.BinaryLargeObject;
import com.technology.jep.jepria.server.ejb.CallContext;
import com.technology.jep.jepria.server.exceptions.SpaceException;
import com.technology.jep.jepria.server.upload.AbstractFileUploadBean;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * FileUpload Stateful Session EJB 3 для записи в BINARY_FILE
 */
@Local( { BinaryFileUploadLocal.class })
@Remote( { BinaryFileUploadRemote.class })
@StatefulDeployment
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class BinaryFileUploadBean extends AbstractFileUploadBean implements BinaryFileUpload {

	public int beginWrite(
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
			result = ((BinaryLargeObject)super.largeObject).beginWrite();
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
	
	public void continueWrite(byte[] dataBlock) throws SpaceException {
		CallContext.attach(storedContext);
		boolean cancelled = false;
		try {
			((BinaryLargeObject)super.largeObject).continueWrite(dataBlock);
		} catch (Throwable ex) {
			cancelled = true;
			if (ex instanceof SpaceException) {
				throw (SpaceException) ex;
			} else if (ex instanceof Exception) {
				throw new SpaceException("continue write error", (Exception) ex);
			} else {
				throw new SpaceException("continue write error", new RuntimeException(ex));
			}
		} finally {
			if (cancelled) {
				try {
					cancel();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			storedContext = CallContext.detach();
		}
	}
}
