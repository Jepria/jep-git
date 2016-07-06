package com.technology.jep.jepriashowcase.arsenic.server.dao;
 
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_CHECKBOX_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_DATE_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_LONG_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_MONEY_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_NUMBER_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.DETAILFORM_JEP_TEXT_FIELD;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.JEP_COMBOBOX_FIELD_SIMPLE_NAME;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.PRIMARY_KEY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.technology.jep.jepria.server.dao.JepDaoStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;

public class ArsenicDao extends JepDaoStandard implements Arsenic {

	private static int id = 1;
	private static final Map<Integer, JepRecord> database = new HashMap<Integer, JepRecord>();
	
	@Override
	public List<JepRecord> find(JepRecord templateRecord,
			Mutable<Boolean> autoRefreshFlag, Integer maxRowCount,
			Integer operatorId) throws ApplicationException {

		Integer tmpId = templateRecord.get(PRIMARY_KEY);
		if (tmpId != null) {
			return Arrays.asList(database.get(tmpId));
		}
		
		
		List<JepRecord> ret = new ArrayList<JepRecord>();
		
		String dbText, tmpText = templateRecord.get(DETAILFORM_JEP_TEXT_FIELD);
		Long dbLong, tmpLong = templateRecord.get(DETAILFORM_JEP_LONG_FIELD);
		BigDecimal dbMoney, tmpMoney = templateRecord.get(DETAILFORM_JEP_MONEY_FIELD);
		Double dbNumber, tmpNumber = templateRecord.get(DETAILFORM_JEP_NUMBER_FIELD);
		Date dbDate, tmpDate = templateRecord.get(DETAILFORM_JEP_DATE_FIELD);
		JepOption dbCombo, tmpCombo = templateRecord.get(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE);
		Boolean dbCheck, tmpCheck = templateRecord.get(DETAILFORM_JEP_CHECKBOX_FIELD);
		
		for (JepRecord dbrec: database.values()) {
			dbText = dbrec.get(DETAILFORM_JEP_TEXT_FIELD);
			dbLong = dbrec.get(DETAILFORM_JEP_LONG_FIELD);
			dbMoney = dbrec.get(DETAILFORM_JEP_MONEY_FIELD);
			dbNumber = dbrec.get(DETAILFORM_JEP_NUMBER_FIELD);
			dbDate = dbrec.get(DETAILFORM_JEP_DATE_FIELD);
			dbCombo = dbrec.get(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE);
			dbCheck = dbrec.get(DETAILFORM_JEP_CHECKBOX_FIELD);
			
			if (tmpText != null) {
				if (dbText == null || !dbText.contains(tmpText)) {
					break;
				}
			}
			if (tmpLong != null) {
				if (dbLong == null || !dbLong.equals(tmpLong)) {
					break;
				}
			}
			if (tmpMoney != null) {
				if (dbMoney == null || !dbMoney.equals(tmpMoney)) {
					break;
				}
			}
			if (tmpNumber != null) {
				if (dbNumber == null || !dbNumber.equals(tmpNumber)) {
					break;
				}
			}
			if (tmpDate != null) {
				if (dbDate == null || dbDate.getYear() != tmpDate.getYear() ||
						dbDate.getMonth() != tmpDate.getMonth() || dbDate.getDay() != tmpDate.getDay()) {
					break;
				}
			}
			
			if (tmpCombo != null) {
				if (dbCombo == null || !dbCombo.equals(tmpCombo)) {
					break;
				}
			}
			if (dbCombo != null) {
				dbrec.put(JEP_COMBOBOX_FIELD_SIMPLE_NAME, dbCombo.getName());
			}
			
			if (tmpCheck != null && tmpCheck) {
				if (dbCheck == null || !dbCheck) {
					break;
				}
			}
			
			ret.add(dbrec);
			
			// retrieve no more than maxRowCount records
			if (ret.size() >= maxRowCount) {
				break;
			}
		}
		
		return ret;
	}

	@Override
	public Object create(JepRecord record, Integer operatorId)
			throws ApplicationException {
		final int id_ = id++;
		record.put(PRIMARY_KEY, id_);
		database.put(id_, record);
		return (Integer)id_;
	}

	@Override
	public void update(JepRecord record, Integer operatorId)
			throws ApplicationException {
		final Integer oldId = record.get(PRIMARY_KEY);
		JepRecord jr = database.get(oldId);
		if (jr != null) {
			jr.update(record);
			// do not change the id during update
			jr.set(PRIMARY_KEY, oldId);
		}
		
	}

	@Override
	public void delete(JepRecord record, Integer operatorId)
			throws ApplicationException {
		database.remove(record.get(PRIMARY_KEY));
	}

	@Override
	public void durableFetch(long msec) throws ApplicationException {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
 
}
