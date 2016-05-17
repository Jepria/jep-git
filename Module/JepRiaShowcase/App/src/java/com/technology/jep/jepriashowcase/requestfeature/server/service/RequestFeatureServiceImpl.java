package com.technology.jep.jepriashowcase.requestfeature.server.service;
 
import static com.technology.jep.jepriashowcase.requestfeature.server.RequestFeatureServerConstant.DATA_SOURCE_JNDI_NAME;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepriashowcase.request.server.dao.Request;
import com.technology.jep.jepriashowcase.request.server.dao.RequestDao;
import com.technology.jep.jepriashowcase.request.shared.record.RequestRecordDefinition;
import com.technology.jep.jepriashowcase.request.shared.service.RequestService;
import com.technology.jep.jepriashowcase.requestfeature.server.dao.RequestFeature;
import com.technology.jep.jepriashowcase.requestfeature.server.dao.RequestFeatureDao;
import com.technology.jep.jepriashowcase.requestfeature.shared.record.RequestFeatureRecordDefinition;
import com.technology.jep.jepriashowcase.requestfeature.shared.service.RequestFeatureService;
 
@RemoteServiceRelativePath("RequestFeatureService")
public class RequestFeatureServiceImpl extends JepDataServiceServlet<RequestFeature> implements RequestFeatureService  {
 
	private static final long serialVersionUID = 1L;
 
	public RequestFeatureServiceImpl() {
		super(RequestFeatureRecordDefinition.instance, new ServerFactory<RequestFeature>(new RequestFeatureDao(), DATA_SOURCE_JNDI_NAME));
	}
	
//	public List<JepOption> getRequestStatus() throws ApplicationException {
//		List<JepOption> result = null;
//		try {
//			result = dao.getRequestStatus();
//		} catch (Throwable th) {
//			throw new ApplicationException(th.getLocalizedMessage(), th);
//		}
//		return result;
//	}
}
