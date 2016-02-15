package com.technology.jep.jepria.server.dao;

import java.util.List;

import com.technology.jep.jepria.server.dao.DaoSupport;
import com.technology.jep.jepria.server.dao.ResultSetMapper;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Общий предок EJB стандартной работы с данными.
 */
public class JepDaoStandard {

	protected final String dataSourceJndiName;
	protected final String resourceBundleName;

	public JepDaoStandard(String dataSourceJndiName, String resourceBundleName) {
		this.dataSourceJndiName = dataSourceJndiName;
		this.resourceBundleName = resourceBundleName;
	}

	public List<JepRecord> find(
			String sqlQuery,
			ResultSetMapper<JepRecord> resultSetMapper,
			Object... params) throws ApplicationException {
		return DaoSupport.find(
				sqlQuery,
				dataSourceJndiName,
				resultSetMapper,
				JepRecord.class,
				params); 
	}
	
	public List<JepOption> getOptions (
			String sqlQuery,
			ResultSetMapper<JepOption> resultSetMapper,
			Object... params) throws ApplicationException {
		return DaoSupport.find(
				sqlQuery,
				dataSourceJndiName,
				resultSetMapper,
				JepOption.class,
				params); 
	}

	public <T> T create(
			String sqlQuery,
			Class<T> resultTypeClass,			
			Object... params) throws ApplicationException {
		return DaoSupport.<T> create(sqlQuery,
				dataSourceJndiName,
				resultTypeClass,
				params);
	}

	public void update(
			String sqlQuery,
			Object... params) throws ApplicationException {
		DaoSupport.update(
				sqlQuery,
				dataSourceJndiName,
				params);
	}

	public void delete(
			String sqlQuery,
			Object... params) throws ApplicationException {
		DaoSupport.delete(
				sqlQuery,
				dataSourceJndiName,
				params);
	}

}
