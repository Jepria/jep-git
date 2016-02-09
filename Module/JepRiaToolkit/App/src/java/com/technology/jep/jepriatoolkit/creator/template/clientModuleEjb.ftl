package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.ejb;
 
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.${form.formName}ServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.${form.formName}ServerConstant.RESOURCE_BUNDLE_NAME;
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.field.${form.formName}FieldNames.*;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import oracle.j2ee.ejb.StatelessDeployment;
import com.technology.jep.jepria.server.ejb.JepDataBean;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.ejb.${form.formName};
import com.technology.jep.jepria.server.dao.ResultSetMapper;
<#if form.hasOptionField>
import com.technology.jep.jepria.shared.field.option.JepOption;
</#if>
<#list form.fields as field><#t>
<#if field.isOptionField>
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.field.${field.fieldIdAsParameter}Options;
</#if>
</#list>
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
 
@Local( { ${form.formName}Local.class })
@Remote( { ${form.formName}Remote.class })
@StatelessDeployment
@Stateless
public class ${form.formName}Bean extends JepDataBean implements ${form.formName} {
 
	public ${form.formName}Bean() {
		super(DATA_SOURCE_JNDI_NAME, RESOURCE_BUNDLE_NAME);
	}
 
	public List<JepRecord> find(JepRecord templateRecord, Mutable<Boolean> autoRefreshFlag, Integer maxRowCount, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin  " 
			  +	"? := pkg_evaluation.find${form.formName}(" 
				  	+ "interviewId => ? " 
				  	+ ", interviewBeginDateFrom => ? " 
				  	+ ", interviewBeginDateTo => ? " 
				  	+ ", interviewEndDateFrom => ? " 
				  	+ ", interviewEndDateTo => ? " 
				  	+ ", departmentId => ? " 
				  	+ ", positionId => ? " 
				  	+ ", employeeId => ? " 
				  	+ ", categoryId => ? " 
				  	+ ", isActivate => ? " 
				  	+ ", isClose => ? " 
					+ ", maxRowCount => ? " 
					+ ", operatorId => ? " 
			  + ");"
		 + " end;";
		return super.find(sqlQuery,
				new ResultSetMapper<JepRecord>() {
					public void map(ResultSet rs, JepRecord record) throws SQLException {
						record.set(INTERVIEW_ID, getInteger(rs, INTERVIEW_ID));
						record.set(INTERVIEW_BEGIN_DATE, getDate(rs, INTERVIEW_BEGIN_DATE));
						record.set(INTERVIEW_END_DATE, getDate(rs, INTERVIEW_END_DATE));
						record.set(DEPARTMENT_NAME, rs.getString(DEPARTMENT_NAME));
						record.set(POSITION_NAME, rs.getString(POSITION_NAME));
						record.set(EMPLOYEE_NAME, rs.getString(EMPLOYEE_NAME));
						record.set(WORK_CITY_NAME, rs.getString(WORK_CITY_NAME));
						record.set(CATEGORY_NAME, rs.getString(CATEGORY_NAME));
						record.set(ACTIVATE_STATUS, rs.getString(ACTIVATE_STATUS));
						record.set(STATUS_PERCENT, rs.getString(STATUS_PERCENT));
						record.set(CLOSE_STATUS, rs.getString(CLOSE_STATUS));
					}
				}
				, templateRecord.get(INTERVIEW_ID)
				, templateRecord.get(INTERVIEW_BEGIN_DATE_FROM)
				, templateRecord.get(INTERVIEW_BEGIN_DATE_TO)
				, templateRecord.get(INTERVIEW_END_DATE_FROM)
				, templateRecord.get(INTERVIEW_END_DATE_TO)
				, JepOption.<String>getValue(templateRecord.get(DEPARTMENT_ID))
				, JepOption.<String>getValue(templateRecord.get(POSITION_ID))
				, JepOption.<String>getValue(templateRecord.get(EMPLOYEE_ID))
				, JepOption.<String>getValue(templateRecord.get(CATEGORY_ID))
				, JepOption.<String>getValue(templateRecord.get(IS_ACTIVATE))
				, JepOption.<String>getValue(templateRecord.get(IS_CLOSE))
				, maxRowCount 
				, operatorId);
	}
 
	public void delete(JepRecord record, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin " 
			  + "pkg_evaluation.delete${form.formName}(" 
				  	+ "interviewId => ? " 
					+ ", operatorId => ? " 
			  + ");"
		  + "end;";
		super.delete(sqlQuery 
				, record.get(INTERVIEW_ID) 
				, operatorId);
	}
 
	public void update(JepRecord record, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin " 
			+	"pkg_evaluation.update${form.formName}(" 
				  	+ "interviewId => ? " 
				  	+ ", interviewBeginDate => ? " 
				  	+ ", interviewEndDate => ? " 
					+ ", operatorId => ? " 
			+ ");"
		 + "end;";
		super.update(sqlQuery 
				, record.get(INTERVIEW_ID)
				, record.get(INTERVIEW_BEGIN_DATE)
				, record.get(INTERVIEW_END_DATE)
				, operatorId);
	}
 
	public Integer create(JepRecord record, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin " 
			  + "? := pkg_evaluation.create${form.formName}(" 
				  	+ "interviewBeginDate => ? " 
				  	+ ", interviewEndDate => ? " 
				  	+ ", employeeId => ? " 
				  	+ ", categoryId => ? " 
					+ ", operatorId => ? " 
			  + ");"
			+ "end;";
		return super.create(sqlQuery, 
				Integer.class 
				, record.get(INTERVIEW_BEGIN_DATE)
				, record.get(INTERVIEW_END_DATE)
				, JepOption.<String>getValue(record.get(EMPLOYEE_ID))
				, JepOption.<String>getValue(record.get(CATEGORY_ID))
				, operatorId);
	}
 
 
	public List<JepOption> getDepartment() throws ApplicationException {
		String sqlQuery = 
			" begin " 
			+ " ? := pkg_evaluation.getDepartment;" 
			+ " end;";
 
		return super.getOptions(
				sqlQuery,
				new ResultSetMapper<JepOption>() {
					public void map(ResultSet rs, JepOption dto) throws SQLException {
						dto.setValue(getInteger(rs, DepartmentOptions.DEPARTMENT_ID));
						dto.setName(rs.getString(DepartmentOptions.DEPARTMENT_NAME));
					}
				}
		);
	}
 
	public List<JepOption> getPosition() throws ApplicationException {
		String sqlQuery = 
			" begin " 
			+ " ? := pkg_evaluation.getPosition;" 
			+ " end;";
 
		return super.getOptions(
				sqlQuery,
				new ResultSetMapper<JepOption>() {
					public void map(ResultSet rs, JepOption dto) throws SQLException {
						dto.setValue(getInteger(rs, PositionOptions.POSITION_ID));
						dto.setName(rs.getString(PositionOptions.POSITION_NAME));
					}
				}
		);
	}
 
	public List<JepOption> getEmployee() throws ApplicationException {
		String sqlQuery = 
			" begin " 
			+ " ? := pkg_evaluation.getEmployee;" 
			+ " end;";
 
		return super.getOptions(
				sqlQuery,
				new ResultSetMapper<JepOption>() {
					public void map(ResultSet rs, JepOption dto) throws SQLException {
						dto.setValue(getInteger(rs, EmployeeOptions.EMPLOYEE_ID));
						dto.setName(rs.getString(EmployeeOptions.EMPLOYEE_NAME));
					}
				}
		);
	}
 
	public List<JepOption> getWorkCityName() throws ApplicationException {
		String sqlQuery = 
			" begin " 
			+ " ? := pkg_evaluation.getWorkCityName;" 
			+ " end;";
 
		return super.getOptions(
				sqlQuery,
				new ResultSetMapper<JepOption>() {
					public void map(ResultSet rs, JepOption dto) throws SQLException {
						dto.setValue(rs.getString(WorkCityNameOptions.WORK_CITY_NAME));
						dto.setName(rs.getString(WorkCityNameOptions.WORK_CITY_NAME_NAME));
					}
				}
		);
	}
 
	public List<JepOption> getCategory() throws ApplicationException {
		String sqlQuery = 
			" begin " 
			+ " ? := pkg_evaluation.getCategory;" 
			+ " end;";
 
		return super.getOptions(
				sqlQuery,
				new ResultSetMapper<JepOption>() {
					public void map(ResultSet rs, JepOption dto) throws SQLException {
						dto.setValue(getInteger(rs, CategoryOptions.CATEGORY_ID));
						dto.setName(rs.getString(CategoryOptions.CATEGORY_NAME));
					}
				}
		);
	}
 
	public List<JepOption> getIsActivate() throws ApplicationException {
		String sqlQuery = 
			" begin " 
			+ " ? := pkg_evaluation.getIsActivate;" 
			+ " end;";
 
		return super.getOptions(
				sqlQuery,
				new ResultSetMapper<JepOption>() {
					public void map(ResultSet rs, JepOption dto) throws SQLException {
						dto.setValue(getInteger(rs, IsActivateOptions.IS_ACTIVATE));
						dto.setName(rs.getString(IsActivateOptions.IS_ACTIVATE_NAME));
					}
				}
		);
	}
 
	public List<JepOption> getIsClose() throws ApplicationException {
		String sqlQuery = 
			" begin " 
			+ " ? := pkg_evaluation.getIsClose;" 
			+ " end;";
 
		return super.getOptions(
				sqlQuery,
				new ResultSetMapper<JepOption>() {
					public void map(ResultSet rs, JepOption dto) throws SQLException {
						dto.setValue(getInteger(rs, IsCloseOptions.IS_CLOSE));
						dto.setName(rs.getString(IsCloseOptions.IS_CLOSE_NAME));
					}
				}
		);
	}
}
