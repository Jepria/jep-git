package com.technology.jep.jepriashowcase.request.shared.service;
 
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.service.data.JepDataService;
 
@RemoteServiceRelativePath("RequestService")
public interface RequestService extends JepDataService {
	List<JepOption> getRequestStatus() throws ApplicationException;
}
