package com.technology.jep.jepriashowcase.arsenic.server.dao;
 
import java.util.List;

import com.technology.jep.jepria.server.dao.JepDaoStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;

public class ArsenicDao extends JepDaoStandard implements Arsenic {

	@Override
	public List<JepRecord> find(JepRecord templateRecord,
			Mutable<Boolean> autoRefreshFlag, Integer maxRowCount,
			Integer operatorId) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object create(JepRecord record, Integer operatorId)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(JepRecord record, Integer operatorId)
			throws ApplicationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(JepRecord record, Integer operatorId)
			throws ApplicationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void durableFetch(long msec) throws ApplicationException {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
 
}
