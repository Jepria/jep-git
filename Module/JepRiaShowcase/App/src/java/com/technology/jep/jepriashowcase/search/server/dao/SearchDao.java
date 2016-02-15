package com.technology.jep.jepriashowcase.search.server.dao;

import static com.technology.jep.jepriashowcase.search.shared.field.SearchFieldNames.*;
import static com.technology.jep.jepriashowcase.search.server.SearchServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepriashowcase.search.server.SearchServerConstant.RESOURCE_BUNDLE_NAME;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.server.dao.JepDao;
import com.technology.jep.jepria.server.dao.ResultSetMapper;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.record.lob.JepFileReference;
import com.technology.jep.jepria.shared.util.Mutable;

public class SearchDao extends JepDao implements Search {
	protected static Logger logger = Logger.getLogger(SearchDao.class.getName());	

	public SearchDao() {
		super(DATA_SOURCE_JNDI_NAME, RESOURCE_BUNDLE_NAME);
	}
	
	/**
	 * Создание // TODO 8.0: 
	 * 
	 * @param record				Содержит следующие поля:<br/>
	 *			CITY_ID												Город<br/>
	 * @param operatorId			Идентификатор оператора добавляющего запись
	 * @return первичный ключ созданной записи
	 * @throws ApplicationException
	 */
	public Object create(JepRecord record, Integer operatorId)	throws ApplicationException {
		// TODO 8.0: 
		return null;
	}
	
	/**
	 * Редактирование // TODO 8.0: 
	 * 
	 * @param record				Содержит следующие поля:<br/>
	 *			CITY_ID												Город<br/>
	 * @param operatorId			Идентификатор оператора изменяющего запись
	 * @throws ApplicationException
	 */
	public void update(JepRecord record, Integer operatorId) throws ApplicationException {
		// TODO 8.0: 
	}	
	
	/**
	 * Удаление // TODO 8.0: 
	 * 
	 * @param record				Содержит следующие поля:<br/>
	 *			CITY_ID												Город<br/>
	 * @param operatorId 			Идентификатор оператора удаляющего запись
	 * @throws ApplicationException
	 */
	public void delete(JepRecord record, Integer operatorId) throws ApplicationException {
		// TODO 8.0: 
	}


	public List<JepRecord> find( JepRecord templateRecord, Mutable<Boolean> autoRefreshFlag, Integer maxRowCount, Integer operatorId) throws ApplicationException {
		String sqlQuery = 
			"begin  " 
			  +	"? := pkg_jepriashowcase.findGoods(" 
				  	+ "goodsIdList => ? " 
				  	+ ", supplierId => ? " 
				  	+ ", goodsName => ? " 
				  	+ ", goodsTypeCode => ? " 
				  	+ ", goodsSegmentCodeList => ? " 
				  	+ ", goodsCatalogIdList => ? " 
					+ ", maxRowCount => ? " 
					+ ", operatorId => ? " 
			  + ");"
		 + " end;";
		
		String goodsIdList = templateRecord.get(GOODS_ID) != null ? templateRecord.get(GOODS_ID).toString() : (String) templateRecord.get(GOODS_ID_LIST);
		
		return super.find( sqlQuery,
				new ResultSetMapper<JepRecord>() {
					public void map(ResultSet rs, JepRecord record) throws SQLException {
						Integer goodsId = getInteger(rs, GOODS_ID);
						record.set(GOODS_ID, goodsId);
						record.set(GOODS_NAME, rs.getString(GOODS_NAME));
						record.set(UNIT_NAME, rs.getString(UNIT_NAME));
						record.set(PURCHASING_PRICE, rs.getBigDecimal(PURCHASING_PRICE));
					}
				}
				, goodsIdList
				, null
				, templateRecord.get(GOODS_NAME)
				, null
				, null
				, null
				, maxRowCount 
				, operatorId);
	}
	
	public String getOperatorName(
		Integer operatorId)
		throws ApplicationException {
		
		String sqlQuery = 
			" begin ? := pkg_Operator.getOperatorName("
				+ " operatorId => ?"
				+ " );"
			+ " end;";

		return super.execute(sqlQuery, String.class, 
			operatorId
		);
		
	}
	
}
