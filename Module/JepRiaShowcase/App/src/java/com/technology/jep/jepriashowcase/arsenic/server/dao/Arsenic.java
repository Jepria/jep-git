package com.technology.jep.jepriashowcase.arsenic.server.dao;
 
import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
 
public interface Arsenic extends JepDataStandard {
	void durableFetch(long msec) throws ApplicationException;
}
