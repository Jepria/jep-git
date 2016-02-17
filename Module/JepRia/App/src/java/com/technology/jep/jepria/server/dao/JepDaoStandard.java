package com.technology.jep.jepria.server.dao;

import java.util.List;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Общий предок EJB стандартной работы с данными.
 */
public class JepDaoStandard {

	public List<JepRecord> find(
			String sqlQuery,
			ResultSetMapper<JepRecord> resultSetMapper,
			Object... params) throws ApplicationException {
		return NewDaoSupport.find(
				sqlQuery,
				resultSetMapper,
				JepRecord.class,
				params); 
	}
	
	public List<JepOption> getOptions (
			String sqlQuery,
			ResultSetMapper<JepOption> resultSetMapper,
			Object... params) throws ApplicationException {
		return NewDaoSupport.find(
				sqlQuery,
				resultSetMapper,
				JepOption.class,
				params); 
	}

	public <T> T create(
			String sqlQuery,
			Class<T> resultTypeClass,			
			Object... params) throws ApplicationException {
		return NewDaoSupport.<T> create(sqlQuery,
				resultTypeClass,
				params);
	}

	public void update(
			String sqlQuery,
			Object... params) throws ApplicationException {
		NewDaoSupport.update(
				sqlQuery,
				params);
	}

	public void delete(
			String sqlQuery,
			Object... params) throws ApplicationException {
		NewDaoSupport.delete(
				sqlQuery,
				params);
	}

}
