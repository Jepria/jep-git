package com.technology.jep.jepria.server.ejb;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;

import com.technology.jep.jepria.server.dao.DaoSupport;
import com.technology.jep.jepria.server.dao.ResultSetMapper;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Общий предок EJB стандартной работы с данными.
 */
public class JepDataStandardBean {
	@Resource
	protected SessionContext sessionContext;

	protected String dataSourceJndiName;
	protected String resourceBundleName;

	public JepDataStandardBean(String dataSourceJndiName, String resourceBundleName) {
		this.dataSourceJndiName = dataSourceJndiName;
		this.resourceBundleName = resourceBundleName;
	}

	public List<JepRecord> find(
			String sqlQuery,
			ResultSetMapper<JepRecord> resultSetMapper,
			Object... params) throws ApplicationException {
		return DaoSupport.find(
				sqlQuery,
				sessionContext,
				dataSourceJndiName,
				resourceBundleName,
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
				sessionContext,
				dataSourceJndiName,
				resourceBundleName,
				resultSetMapper,
				JepOption.class,
				params); 
	}

	public <T> T create(
			String sqlQuery,
			Class<T> resultTypeClass,			
			Object... params) throws ApplicationException {
		return DaoSupport.<T> create(sqlQuery,
				sessionContext,
				dataSourceJndiName,
				resourceBundleName,
				resultTypeClass,
				params);
	}

	public void update(
			String sqlQuery,
			Object... params) throws ApplicationException {
		DaoSupport.update(
				sqlQuery,
				sessionContext,
				dataSourceJndiName,
				resourceBundleName,
				params);
	}

	public void delete(
			String sqlQuery,
			Object... params) throws ApplicationException {
		DaoSupport.delete(
				sqlQuery,
				sessionContext,
				dataSourceJndiName,
				resourceBundleName,
				params);
	}

}
