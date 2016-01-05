package com.technology.jep.jepriashowcase.custom.server.dao;

import static com.technology.jep.jepriashowcase.custom.server.CustomServerConstant.*;

import java.util.List;
import java.util.Random;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;

public class CustomDaoImpl extends JepDaoImpl implements CustomDao {
	
	public CustomDaoImpl() {
		super(DATA_SOURCE_JNDI_NAME, RESOURCE_BUNDLE_NAME);
	}

	@Override
	public String getOperatorName(Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			" begin ? := pkg_Operator.getOperatorName("
				+ " operatorId => ?"
				+ " );"
			+ " end;";

		return super.execute(sqlQuery, String.class, 
			operatorId
		);
	}

	@Override
	public void transaction() throws ApplicationException {
		startTransaction();
		
		try {
			for (int i = 0; i < 3; i++) {
				testQuery(i);
			}
			commit();
		} catch (Throwable th) {
			rollback();
			throw new ApplicationException(th.getMessage(), th);
		} finally {
			endTransaction();			
		}		
	}
	
	public void testQuery(Integer value) throws ApplicationException {
		String sqlQuery = 
			"insert into ejb_test(test_id, value) values (null, ?)";
		super.update(sqlQuery, value);
		simulateRandomError();		
	}

	private void simulateRandomError() {
		// имитация ошибки с вероятностью 20%
		int r = (new Random()).nextInt(5);
		if (r == 1) {
			throw new SystemException("Непредвиденная ошибка!");
		}
	}

	@Override
	public List<JepRecord> find(JepRecord templateRecord,
			Mutable<Boolean> autoRefreshFlag, Integer maxRowCount,
			Integer operatorId) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object create(JepRecord record, Integer operatorId)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(JepRecord record, Integer operatorId)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(JepRecord record, Integer operatorId)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

}
