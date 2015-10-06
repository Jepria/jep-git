package com.technology.jep.jepriashowcase.request.server.service;
 
import static com.technology.jep.jepriashowcase.request.server.RequestServerConstant.BEAN_JNDI_NAME;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.server.util.JepServerUtil;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepriashowcase.request.server.ejb.Request;
import com.technology.jep.jepriashowcase.request.shared.record.RequestRecordDefinition;
import com.technology.jep.jepriashowcase.request.shared.service.RequestService;
 
@RemoteServiceRelativePath("RequestService")
public class RequestServiceImpl extends JepDataServiceServlet implements RequestService  {
 
	private static final long serialVersionUID = 1L;
 
	public RequestServiceImpl() {
		super(RequestRecordDefinition.instance, BEAN_JNDI_NAME);
	}
	
	public List<JepOption> getRequestStatus() throws ApplicationException {
		List<JepOption> result = null;
		try {
			Request request = (Request) JepServerUtil.ejbLookup(ejbName);
			result = request.getRequestStatus();
		} catch (Throwable th) {
			throw new ApplicationException(th.getLocalizedMessage(), th);
		}
		return result;
	}
}
