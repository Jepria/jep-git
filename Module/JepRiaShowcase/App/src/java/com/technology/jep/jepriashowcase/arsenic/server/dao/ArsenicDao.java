package com.technology.jep.jepriashowcase.arsenic.server.dao;
 
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.*;

import com.technology.jep.jepria.server.dao.JepDaoStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;

public class ArsenicDao extends JepDaoStandard implements Arsenic {

	@Override
	public List<JepRecord> find(JepRecord templateRecord,
			Mutable<Boolean> autoRefreshFlag, Integer maxRowCount,
			Integer operatorId) throws ApplicationException {
		
		List<JepOption> list;
		String h;
		JepOption jo;
		
		List<JepRecord> ret = new ArrayList<JepRecord>();
		for (int i = 0; i < maxRowCount; i++) {
			JepRecord record = new JepRecord();
			
			record.put(DETAILFORM_JEP_TEXT_FIELD, templateRecord.get(DETAILFORM_JEP_TEXT_FIELD) + " ("+i+")");
			record.put(DETAILFORM_JEP_TEXT_AREA_FIELD, templateRecord.get(DETAILFORM_JEP_TEXT_AREA_FIELD) + " ("+i+")");
			
			Long loong = (Long)templateRecord.get(DETAILFORM_JEP_LONG_FIELD);
			record.put(DETAILFORM_JEP_LONG_FIELD, loong != null ? loong + i : null);
			
			BigDecimal bigDecimal = (BigDecimal)templateRecord.get(DETAILFORM_JEP_MONEY_FIELD);
			record.put(DETAILFORM_JEP_MONEY_FIELD, bigDecimal != null ? bigDecimal.add(BigDecimal.valueOf(i)) : null);

			Double doub = (Double)templateRecord.get(DETAILFORM_JEP_NUMBER_FIELD);
			record.put(DETAILFORM_JEP_NUMBER_FIELD, doub != null ? doub + i : null);
			
			Date date = (Date)templateRecord.get(DETAILFORM_JEP_DATE_FIELD);
			record.put(DETAILFORM_JEP_DATE_FIELD, date != null ? new Date(date.getTime() + i * 86400000) : null);
			
			jo = templateRecord.get(DETAILFORM_JEP_COMBOBOX_FIELD_NOTLAZY);
			record.put(DETAILFORM_JEP_COMBOBOX_FIELD_NOTLAZY, jo != null ? jo.getName() + " ("+i+")" : null);
			
			jo = templateRecord.get(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE);
			record.put(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE, jo != null ? jo.getName() + " ("+i+")" : null);
			
			jo = templateRecord.get(DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE);
			record.put(DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE, jo != null ? jo.getName() + " ("+i+")" : null);
			
			jo = templateRecord.get(DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING);
			record.put(DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING, jo != null ? jo.getName() + " ("+i+")" : null);
			
			jo = templateRecord.get(DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING);
			record.put(DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING, jo != null ? jo.getName() + " ("+i+")" : null);
			
			list = templateRecord.get(DETAILFORM_JEP_DUAL_LIST_FIELD);
			h = ""; for (JepOption jo2: list) h += jo2.getName() + ";";
			record.put(DETAILFORM_JEP_DUAL_LIST_FIELD, "".equals(h) ? null : h + " ("+i+")");
			
			Boolean b = (Boolean)templateRecord.get(DETAILFORM_JEP_CHECKBOX_FIELD);
			record.put(DETAILFORM_JEP_CHECKBOX_FIELD, b != null ? b && (i % 2 == 0) || !b && !(i % 2 == 0) : false);
			
			list = templateRecord.get(DETAILFORM_JEP_LIST_FIELD);
			h = ""; for (JepOption jo2: list) h += jo2.getName() + ";";
			record.put(DETAILFORM_JEP_LIST_FIELD, "".equals(h) ? null : h + " ("+i+")");
			
			list = templateRecord.get(DETAILFORM_JEP_LIST_FIELD_CHECKALL);
			h = ""; for (JepOption jo2: list) h += jo2.getName() + ";";
			record.put(DETAILFORM_JEP_LIST_FIELD_CHECKALL, "".equals(h) ? null : h + " ("+i+")");
			
			ret.add(record);
		}
		return ret;
	}

	@Override
	public Object create(JepRecord record, Integer operatorId)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(JepRecord record, Integer operatorId)
			throws ApplicationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(JepRecord record, Integer operatorId)
			throws ApplicationException {
		// TODO Auto-generated method stub
		
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
