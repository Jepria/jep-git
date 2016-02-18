package com.technology.jep.jepriashowcase.requestprocess.server.dao;
 
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.DATE_INS;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.OPERATOR_ID;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.OPERATOR_NAME;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.PROCESS_COMMENT;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.REQUEST_ID;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.REQUEST_PROCESS_ID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.technology.jep.jepria.server.dao.JepDaoStandard;
import com.technology.jep.jepria.server.dao.ResultSetMapper;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;
 
public class RequestProcessDao extends JepDaoStandard implements RequestProcess {
 
	public List<JepRecord> find( JepRecord templateRecord, Mutable<Boolean> autoRefreshFlag, Integer maxRowCount, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin  " 
			  +	"? := pkg_jepriashowcase.findRequestProcess(" 
				  	+ "requestProcessId => ? " 
				  	+ ", requestId => ? " 
					+ ", maxRowCount => ? " 
					+ ", operatorId => ? " 
			  + ");"
		 + " end;";
		return super.find( sqlQuery,
				new ResultSetMapper<JepRecord>() {
					public void map(ResultSet rs, JepRecord record) throws SQLException {
						record.set(REQUEST_PROCESS_ID, getInteger(rs, REQUEST_PROCESS_ID));
						record.set(PROCESS_COMMENT, rs.getString(PROCESS_COMMENT));
						record.set(DATE_INS, getDate(rs, DATE_INS));
						record.set(OPERATOR_ID, getInteger(rs, OPERATOR_ID));
						record.set(OPERATOR_NAME, rs.getString(OPERATOR_NAME));
					}
				}
				, templateRecord.get(REQUEST_PROCESS_ID)
				, templateRecord.get(REQUEST_ID)
				, maxRowCount 
				, operatorId);
	}
	public void delete(JepRecord record, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin " 
			  + "pkg_jepriashowcase.deleteRequestProcess(" 
				  	+ "requestProcessId => ? " 
					+ ", operatorId => ? " 
			  + ");"
		  + "end;";
		super.delete(sqlQuery 
				, record.get(REQUEST_PROCESS_ID) 
				, operatorId);
	}
 
	public void update(JepRecord record, Integer operatorId) throws ApplicationException {
	}
 
	public Integer create(JepRecord record, Integer operatorId) throws ApplicationException {
		return null;
	}
 
}
