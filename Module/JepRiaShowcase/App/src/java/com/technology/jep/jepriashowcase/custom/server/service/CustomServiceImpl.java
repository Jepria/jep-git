package com.technology.jep.jepriashowcase.custom.server.service;

import static com.technology.jep.jepriashowcase.custom.server.CustomServerConstant.BEAN_JNDI_NAME;
import static com.technology.jep.jepriashowcase.custom.server.CustomServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepriashowcase.custom.server.CustomServerConstant.RESOURCE_BUNDLE_NAME;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.server.util.JepServerUtil;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;
import com.technology.jep.jepriashowcase.custom.server.dao.CustomDao;
import com.technology.jep.jepriashowcase.custom.server.dao.CustomDaoImpl;
import com.technology.jep.jepriashowcase.custom.server.dao.annotation.TransactionFactory;
import com.technology.jep.jepriashowcase.custom.server.ejb.Custom;
import com.technology.jep.jepriashowcase.custom.shared.service.CustomService;

/**
 * Реализация gwt-сервиса для Модуля с произвольным располажением элементов.
 */
@RemoteServiceRelativePath("CustomService")
public class CustomServiceImpl extends JepDataServiceServlet implements CustomService {
	private static final long serialVersionUID = 1L;

	private CustomDao custom;
	
	public CustomServiceImpl() {
		super(
			null // TODO 8.0: CustomRecordDefinition.instance
			, BEAN_JNDI_NAME
			,	DATA_SOURCE_JNDI_NAME
			,	RESOURCE_BUNDLE_NAME);
		
		this.custom = TransactionFactory.process(new CustomDaoImpl());
	}

	public String getOperatorName(
		Integer operatorId) {
		String result = null;
		try {
			logger.trace("BEGIN TRANSACTION getOperatorName(" + operatorId + ")");
			result = custom.getOperatorName(operatorId);
			
		} catch (Throwable th) {
			throw new SystemException(th.getLocalizedMessage(), th);
		}
		logger.trace("END TRANSACTION getOperatorName(" + operatorId + ")");
		return result;
	}
	
	@Override
	public void transaction() throws ApplicationException {
		try {
//			CustomDao custom = TransactionFactory.process(new CustomDaoImpl());
			logger.trace("BEGIN TRANSACTION transaction()");
			custom.transaction();
		} catch (Throwable th) {
			throw new ApplicationException(th.getLocalizedMessage(), th);
		}
		logger.trace("END TRANSACTION transaction()");
	}
	
}
