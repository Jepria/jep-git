package com.technology.jep.jepriashowcase.feature.server.dao;
 
import static com.technology.jep.jepria.shared.field.JepFieldNames.OPERATOR_ID;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DATE_INS;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DESCRIPTION;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_ID;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME_EN;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DATE_INS_FROM;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.OPERATOR_NAME;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DATE_INS_TO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.technology.jep.jepria.server.dao.JepDaoStandard;
import com.technology.jep.jepria.server.dao.ResultSetMapper;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;

public class FeatureDao extends JepDaoStandard implements Feature {
 
	public List<JepRecord> find( JepRecord templateRecord, Mutable<Boolean> autoRefreshFlag, Integer maxRowCount, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin  " 
			  +	"? := pkg_jepriashowcase.findFeature(" 
				  	+ "featureid => ? " 
				  	+ ", featureName => ? " 
				  	+ ", featureNameEn => ? " 
				  	+ ", dateInsFrom => ? " 
				  	+ ", dateInsTo => ? " 
				  	+ ", maxRowCount => ? " 
				  	+ ", operatorId => ? " 
			  + ");"
		 + " end;";
		return super.find( sqlQuery,
				new ResultSetMapper<JepRecord>() {
					public void map(ResultSet rs, JepRecord record) throws SQLException {
						record.set(FEATURE_ID, getInteger(rs, FEATURE_ID));
						record.set(FEATURE_NAME, rs.getString(FEATURE_NAME));
						record.set(FEATURE_NAME_EN, rs.getString(FEATURE_NAME_EN));
						record.set(DESCRIPTION, rs.getString(DESCRIPTION));
						record.set(DATE_INS, getDate(rs, DATE_INS));
						record.set(OPERATOR_ID, getInteger(rs, OPERATOR_ID));
						record.set(OPERATOR_NAME, rs.getString(OPERATOR_NAME));
					}
				}
				, templateRecord.get(FEATURE_ID)
				, templateRecord.get(FEATURE_NAME)
				, templateRecord.get(FEATURE_NAME_EN)
				, templateRecord.get(DATE_INS_FROM)
				, templateRecord.get(DATE_INS_TO)
				, maxRowCount 
				, operatorId);
	}
	public void delete(JepRecord record, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin " 
			  + "pkg_jepriashowcase.deleteFeature(" 
				  	+ "featureId => ? " 
					+ ", operatorId => ? " 
			  + ");"
		  + "end;";
		super.delete(sqlQuery 
				, record.get(FEATURE_ID) 
				, operatorId);
	}
 
	public void update(JepRecord record, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin " 
			+	"pkg_jepriashowcase.updateFeature(" 
				  	+ "featureId => ? " 
				  	+ ", featureName => ? "
				  	+ ", featureNameEn => ? "
				  	+ ", operatorId => ? " 
			+ ");"
		 + "end;";
		super.update(sqlQuery 
				, record.get(FEATURE_ID)
				, record.get(FEATURE_NAME)
				, record.get(FEATURE_NAME_EN)
				, operatorId);
	}
 
	public Integer create(JepRecord record, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin  " 
			  +	"? := pkg_jepriashowcase.createFeature(" 
				  	+ "featureName => ? " 
				  	+ ", featureNameEn => ? " 
				  	+ ", operatorId => ? " 
			  + ");"
		 + " end;";
		return super.create(sqlQuery, 
				Integer.class 
				, record.get(FEATURE_NAME)
				, record.get(FEATURE_NAME_EN)
				, operatorId);
	}
}
