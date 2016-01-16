package com.technology.jep.jepria.server.ejb;

import static com.technology.jep.jepria.server.JepRiaServerConstant.JEP_RIA_RESOURCE_BUNDLE_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.technology.jep.jepria.server.dao.DaoSupport;
import com.technology.jep.jepria.server.dao.ResultSetMapper;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Общий предок EJB, требующих расширенную работу с данными.
 */
public class JepDataBean extends JepDataStandardBean {

	public JepDataBean(String dataSourceJndiName, String resourceBundleName) {
		super(dataSourceJndiName, resourceBundleName);
	}

	public List<JepRecord> find(
			String sqlQuery,
			String dataSourceJndiName,
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
	
	public List<JepOption> getOptions(
			String sqlQuery,
			String dataSourceJndiName,
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
			String dataSourceJndiName,
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
			String dataSourceJndiName,
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
			String dataSourceJndiName,
			Object... params) throws ApplicationException {
		DaoSupport.delete(
				sqlQuery,
				sessionContext,
				dataSourceJndiName,
				resourceBundleName,
				params);
	}

	public void execute(
			String sqlQuery,
			Object... params) throws ApplicationException {
		DaoSupport.execute(
				sqlQuery,
				sessionContext,
				dataSourceJndiName,
				resourceBundleName,
				params);
	}

	public void execute(
			String sqlQuery,
			String dataSourceJndiName,
			Object... params) throws ApplicationException {
		DaoSupport.execute(
				sqlQuery,
				sessionContext,
				dataSourceJndiName,
				resourceBundleName,
				params);
	}

	public <T> T execute(
			String sqlQuery,
			Class<T> resultTypeClass,
			Object... params) throws ApplicationException {
		return DaoSupport.execute(
				sqlQuery,
				sessionContext,
				dataSourceJndiName,
				resourceBundleName,
				resultTypeClass,
				params);
	}
	
	public <T> T execute(
			String sqlQuery,
			String dataSourceJndiName,
			Class<T> resultTypeClass,
			Object... params) throws ApplicationException {
		return DaoSupport.execute(
				sqlQuery,
				sessionContext,
				dataSourceJndiName,
				resourceBundleName,
				resultTypeClass,
				params);
	}
	
	public List<JepRecord> select(
			String sqlQuery,
			ResultSetMapper<JepRecord> resultSetMapper,
			Object... params) throws ApplicationException {
		return DaoSupport.select(
				sqlQuery,
				sessionContext,
				dataSourceJndiName,
				resourceBundleName,
				resultSetMapper,
				JepRecord.class,
				params); 
	}

	public List<JepRecord> select(
			String sqlQuery,
			String dataSourceJndiName,
			ResultSetMapper<JepRecord> resultSetMapper,
			Object... params) throws ApplicationException {
		return DaoSupport.select(
				sqlQuery,
				sessionContext,
				dataSourceJndiName,
				resourceBundleName,
				resultSetMapper,
				JepRecord.class,
				params); 
	}

	/**
	 * @deprecated Данный метод будет удалён в JepRia 9. Вместо него следует использовать
	 * метод {@link com.technology.jep.jepria.shared.field.option.JepOption#getValue(Object option)}
	 * <br>
	 * Получение значения из опции {@link com.technology.jep.jepria.shared.field.option.JepOption}.<br/>
	 * В реализации метод вызывает {@link com.technology.jep.jepria.shared.field.option.JepOption#getValue(Object option)}.<br/>
	 * Пример использования в прикладном модуле:
	 * <pre>
	 *   ...
	 *   &lt;Integer&gt;getFieldValueFromOption(template.get(CITY_ID)); // Получение значения типа Integer или null.
	 *   ...
	 *   &lt;String&gt;getFieldValueFromOption(template.get(COMPANY_CODE)); // Получение значения типа String или null.
	 *   ...
	 * </pre>
	 * @param option опция {@link com.technology.jep.jepria.shared.field.option.JepOption}
	 * @return значение опции {@link com.technology.jep.jepria.shared.field.option.JepOption#getValue(Object option)}
	 */	
	@Deprecated
	protected <X> X getValueFromOption(Object option) {
		return JepOption.<X>getValue(option);
	}

	/**
	 * Возвращает название месяца по его номеру.
	 * @param month номер месяца (от 1 до 12)
	 * @return название месяца
	 */
	protected String getMonthName(int month) {
		ResourceBundle bundle = ResourceBundle.getBundle(JEP_RIA_RESOURCE_BUNDLE_NAME);
		switch(month) {
			case 1: return bundle.getString("month.january");
			case 2: return bundle.getString("month.february");
			case 3: return bundle.getString("month.march");
			case 4: return bundle.getString("month.april");
			case 5: return bundle.getString("month.may");
			case 6: return bundle.getString("month.june");
			case 7: return bundle.getString("month.july");
			case 8: return bundle.getString("month.august");
			case 9: return bundle.getString("month.september");
			case 10: return bundle.getString("month.october");
			case 11: return bundle.getString("month.november");
			case 12: return bundle.getString("month.december");
			default: throw new IllegalArgumentException(bundle.getString("errors.server.illegalMonthNumber"));
		}
	}
	
	/**
	 * Возвращает название дня недели по номеру.
	 * @param weekDay номер дня недели (от 1 до 7)
	 * @return название дня недели
	 */
	protected String getWeekDayName(int weekDay) {
		ResourceBundle bundle = ResourceBundle.getBundle(JEP_RIA_RESOURCE_BUNDLE_NAME);
		switch(weekDay) {
			case 1: return bundle.getString("weekDay.monday");
			case 2: return bundle.getString("weekDay.tuesday");
			case 3: return bundle.getString("weekDay.wednesday");
			case 4: return bundle.getString("weekDay.thursday");
			case 5: return bundle.getString("weekDay.friday");
			case 6: return bundle.getString("weekDay.saturday");
			case 7: return bundle.getString("weekDay.sunday");
			default: throw new IllegalArgumentException(bundle.getString("errors.server.illegalWeekDayNumber"));
		}
	}
	
	/**
	 * Возвращает список из двух опций, "Да" и "Нет".
	 * @return список опций
	 */
	protected List<JepOption> getYesNo() {
		ResourceBundle bundle = ResourceBundle.getBundle(JEP_RIA_RESOURCE_BUNDLE_NAME);
		List<JepOption> result = new ArrayList<JepOption>();
		
		result.add(new JepOption(bundle.getString("yes"), true));
		result.add(new JepOption(bundle.getString("no"), false));
		
		return result;
	}

}
