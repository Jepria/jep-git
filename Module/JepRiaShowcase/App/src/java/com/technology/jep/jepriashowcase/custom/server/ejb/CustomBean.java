package com.technology.jep.jepriashowcase.custom.server.ejb;

import static com.technology.jep.jepriashowcase.custom.server.CustomServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepriashowcase.custom.server.CustomServerConstant.RESOURCE_BUNDLE_NAME;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.j2ee.ejb.StatelessDeployment;

import com.technology.jep.jepria.server.ejb.JepDataBean;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;

/**
 * Custom Stateless Session EJB 3.
 */
@Local({CustomLocal.class})
@Remote({CustomRemote.class})
@StatelessDeployment
@Stateless
public class CustomBean extends JepDataBean implements Custom {
	protected static Logger logger = Logger.getLogger(CustomBean.class.getName());	

	public CustomBean() {
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

	/**
	 * Поиск // TODO 8.0: 
	 * 
	 * @param template				Содержит следующие поля:<br/>
	 *			CITY_ID												Город<br/>
	 * @param autoRefreshFlag	Флаг автообновления
	 * @param rowCount				Максимальное количество возвращаемых записей
	 * @param operatorId			Пользователь, осуществляющий поиск
	 *
	 * @return список моделей // TODO 8.0: в виде List&lt; com.technology.jep.jepria.shared.record.JepRecord &gt;
	 * @throws ApplicationException
	 */
	public List<JepRecord> find(
		final JepRecord template,
		Mutable<Boolean> autoRefreshFlag,
		Integer rowCount,
		final Integer operatorId) 
		throws ApplicationException {
		
		// TODO 8.0: 
					
		return null;
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
