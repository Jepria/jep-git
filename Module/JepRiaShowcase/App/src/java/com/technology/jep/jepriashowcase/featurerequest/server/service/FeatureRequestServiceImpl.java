package com.technology.jep.jepriashowcase.featurerequest.server.service;
 
import static com.technology.jep.jepriashowcase.featurerequest.server.FeatureRequestServerConstant.DATA_SOURCE_JNDI_NAME;

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
import com.technology.jep.jepriashowcase.featurerequest.server.dao.FeatureRequest;
import com.technology.jep.jepriashowcase.featurerequest.server.dao.FeatureRequestDao;
import com.technology.jep.jepriashowcase.featurerequest.shared.record.FeatureRequestRecordDefinition;
import com.technology.jep.jepriashowcase.featurerequest.shared.service.FeatureRequestService;
 
@RemoteServiceRelativePath("FeatureRequestService")
public class FeatureRequestServiceImpl extends JepDataServiceServlet<FeatureRequest> implements FeatureRequestService  {
 
	private static final long serialVersionUID = 1L;
 
	public FeatureRequestServiceImpl() {
		super(FeatureRequestRecordDefinition.instance, new ServerFactory<FeatureRequest>(new FeatureRequestDao(), DATA_SOURCE_JNDI_NAME));
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
