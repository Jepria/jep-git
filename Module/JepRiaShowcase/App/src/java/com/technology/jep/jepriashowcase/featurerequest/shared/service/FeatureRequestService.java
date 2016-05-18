package com.technology.jep.jepriashowcase.featurerequest.shared.service;
 
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.service.data.JepDataService;
 
@RemoteServiceRelativePath("FeatureRequestService")
public interface FeatureRequestService extends JepDataService {
//	List<JepOption> getRequestStatus() throws ApplicationException;
}
