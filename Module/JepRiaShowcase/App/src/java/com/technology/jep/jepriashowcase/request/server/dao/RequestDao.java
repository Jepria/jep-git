package com.technology.jep.jepriashowcase.request.server.dao;
 
import static com.technology.jep.jepriashowcase.request.server.RequestServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepriashowcase.request.server.RequestServerConstant.RESOURCE_BUNDLE_NAME;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.GOODS_ID;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.GOODS_NAME;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.GOODS_QUANTITY;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_DATE;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_DATE_FROM;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_DATE_TO;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_ID;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_STATUS_CODE;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_STATUS_NAME;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.SHOP_ID;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.SHOP_NAME;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.technology.jep.jepria.server.dao.JepDaoStandard;
import com.technology.jep.jepria.server.dao.ResultSetMapper;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;

public class RequestDao extends JepDaoStandard implements Request {
 
	public RequestDao() {
		super(DATA_SOURCE_JNDI_NAME, RESOURCE_BUNDLE_NAME);
	}
 
	public List<JepRecord> find( JepRecord templateRecord, Mutable<Boolean> autoRefreshFlag, Integer maxRowCount, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin  " 
			  +	"? := pkg_jepriashowcase.findRequest(" 
				  	+ "requestId => ? " 
				  	+ ", shopId => ? " 
				  	+ ", goodsId => ? " 
				  	+ ", requestDateFrom => ? " 
				  	+ ", requestDateTo => ? " 
				  	+ ", requestStatusCode => ? " 
					+ ", maxRowCount => ? " 
					+ ", operatorId => ? " 
			  + ");"
		 + " end;";
		return super.find( sqlQuery,
				new ResultSetMapper<JepRecord>() {
					public void map(ResultSet rs, JepRecord record) throws SQLException {
						record.set(REQUEST_ID, getInteger(rs, REQUEST_ID));
						record.set(SHOP_ID, getInteger(rs, SHOP_ID));
						record.set(SHOP_NAME, rs.getString(SHOP_NAME));
						record.set(GOODS_ID, getInteger(rs, GOODS_ID));
						record.set(GOODS_NAME, rs.getString(GOODS_NAME));
						record.set(REQUEST_DATE, getDate(rs, REQUEST_DATE));
						
						JepOption option = getOption(rs, REQUEST_STATUS_CODE, REQUEST_STATUS_NAME);
						record.set(REQUEST_STATUS_CODE, option);
						record.set(REQUEST_STATUS_NAME, option.getName());
						
						record.set(GOODS_QUANTITY, rs.getBigDecimal(GOODS_QUANTITY));
					}
				}
				, templateRecord.get(REQUEST_ID)
				, templateRecord.get(SHOP_ID)
				, templateRecord.get(GOODS_ID)
				, templateRecord.get(REQUEST_DATE_FROM)
				, templateRecord.get(REQUEST_DATE_TO)
				, JepOption.<String>getValue(templateRecord.get(REQUEST_STATUS_CODE))
				, maxRowCount 
				, operatorId);
	}
	public void delete(JepRecord record, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin " 
			  + "pkg_jepriashowcase.deleteRequest(" 
				  	+ "requestId => ? " 
					+ ", operatorId => ? " 
			  + ");"
		  + "end;";
		super.delete(sqlQuery 
				, record.get(REQUEST_ID) 
				, operatorId);
	}
 
	public void update(JepRecord record, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin " 
			+	"pkg_jepriashowcase.updateRequest(" 
				  	+ "requestId => ? " 
				  	+ ", requestStatusCode => ? "
				  	+ ", goodsId => ? "
				  	+ ", goodsQuantity => ? " 
				  	+ ", operatorId => ? " 
			+ ");"
		 + "end;";
		super.update(sqlQuery 
				, record.get(REQUEST_ID)
				, JepOption.<String>getValue(record.get(REQUEST_STATUS_CODE))
				, record.get(GOODS_ID)
				, record.get(GOODS_QUANTITY)
				, operatorId);
	}
 
	public Integer create(JepRecord record, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin " 
			  + "? := pkg_jepriashowcase.createRequest(" 
				  	+ "shopId => ? " 
				  	+ ", goodsId => ? " 
				  	+ ", goodsQuantity => ? "
					+ ", operatorId => ? " 
			  + ");"
			+ "end;";
		return super.create(sqlQuery, 
				Integer.class 
				, JepOption.<Integer>getValue(record.get(SHOP_ID))
				, record.get(GOODS_ID)
				, record.get(GOODS_QUANTITY)
				, operatorId);
	}
 
	public List<JepOption> getRequestStatus() throws ApplicationException {
		String sqlQuery = 
			" begin " 
			+ " ? := pkg_jepriashowcase.getRequestStatus;" 
			+ " end;";
 
		return super.getOptions(
				sqlQuery,
				new ResultSetMapper<JepOption>() {
					public void map(ResultSet rs, JepOption dto) throws SQLException {
						dto.setValue(rs.getString(REQUEST_STATUS_CODE));
						dto.setName(rs.getString(REQUEST_STATUS_NAME));
					}
				}
		);
	}
}
