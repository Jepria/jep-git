package com.technology.jep.jepriashowcase.request.server.dao;
 
import java.util.List;

import com.technology.jep.jepria.server.ejb.JepDataStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
 
public interface Request extends JepDataStandard {
	List<JepOption> getRequestStatus() throws ApplicationException;
}
