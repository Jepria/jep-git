package com.technology.jep.jepriashowcase.request.server.service;
 
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepriashowcase.request.server.RequestServerFactory;
import com.technology.jep.jepriashowcase.request.server.dao.Request;
import com.technology.jep.jepriashowcase.request.shared.record.RequestRecordDefinition;
import com.technology.jep.jepriashowcase.request.shared.service.RequestService;
 
@RemoteServiceRelativePath("RequestService")
public class RequestServiceImpl extends JepDataServiceServlet<Request> implements RequestService  {
 
	private static final long serialVersionUID = 1L;
 
	public RequestServiceImpl() {
		super(RequestRecordDefinition.instance,
				RequestServerFactory.instance);
	}
	
	public List<JepOption> getRequestStatus() throws ApplicationException {
		List<JepOption> result = null;
		try {
			result = dao.getRequestStatus();
		} catch (Throwable th) {
			throw new ApplicationException(th.getLocalizedMessage(), th);
		}
		return result;
	}
}
