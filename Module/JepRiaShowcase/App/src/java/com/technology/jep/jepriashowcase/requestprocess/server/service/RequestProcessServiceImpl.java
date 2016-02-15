package com.technology.jep.jepriashowcase.requestprocess.server.service;
 
import com.technology.jep.jepriashowcase.requestprocess.server.dao.RequestProcess;
import com.technology.jep.jepriashowcase.requestprocess.server.dao.RequestProcessDao;
import com.technology.jep.jepriashowcase.requestprocess.shared.service.RequestProcessService;
import com.technology.jep.jepria.server.dao.transaction.TransactionFactory;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepriashowcase.requestprocess.shared.record.RequestProcessRecordDefinition;

import static com.technology.jep.jepriashowcase.requestprocess.server.RequestProcessServerConstant.DATA_SOURCE_JNDI_NAME;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
 
@RemoteServiceRelativePath("RequestProcessService")
public class RequestProcessServiceImpl extends JepDataServiceServlet<RequestProcess> implements RequestProcessService  {
 
	private static final long serialVersionUID = 1L;
 
	public RequestProcessServiceImpl() {
		super(RequestProcessRecordDefinition.instance,
				TransactionFactory.process(new RequestProcessDao(), DATA_SOURCE_JNDI_NAME));
	}
}
