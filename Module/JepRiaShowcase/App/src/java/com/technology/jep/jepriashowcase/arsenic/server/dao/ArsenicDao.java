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
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.TREE_FIELD_HAS_CHILDREN_FLAG;

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
import com.technology.jep.jepria.shared.field.option.JepParentOption;
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

	/**
	 * The tree in form of '(id)name', * on leaves:
	 * 
	 * (1119)Options_11_19
	 * 		(11*)Option 11
	 * 		(1214)Options_12_14
	 * 			(12*)Option12
	 * 			(13*)Option13
	 * 			(14*)Option14
	 * 		(1519)Options_15_19
	 * 			(15191)Options_15
	 * 				(15192)Options_15
	 * 					(15*)Option15
	 * 			(1618)Options_16_18
	 * 				(16*)Option16
	 * 				(17*)Option17
	 * 				(18*)Option18
	 * 			(19*)Option19
	 * (2027)Options_20_27
	 * 		(2022)Options_20_22
	 * 			(20*)Option20
	 * 			(21*)Option21
	 * 			(22*)Option22
	 * 		(2327)Options_23_27
	 * 		(2325)Options_23_25
	 * 			(2324)Options_23_24
	 * 				(23*)Option23
	 * 				(24*)Option24
	 * 			(25*)Option25
	 * 			(2627)Options_26_27
	 * 				(26*)Option26
	 * 				(27*)Option27
	 * (28*)Option28
	 * (29*)Option29
	 * (30*)Option30
	 * (3131)EmptyAndOptions_31
	 * 		(31311)EmptyAndOptions_31
	 * 			(31312)Empty
	 * 				(31313)Empty
	 * 			(31*)Option31
	 */
	@Override
	public List<JepOption> getTreeCatalog(Integer parentCatalogId) throws ApplicationException {
		
		List<JepOption> ret = new ArrayList<JepOption>();
		JepOption tmp;
		
		if (parentCatalogId == null) {
			//root
			tmp = new JepParentOption("Options_11_19", 1119);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
			
			tmp = new JepParentOption("Options_20_27", 2027);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
			
			ret.add(new JepOption("Option28", 28));
			ret.add(new JepOption("Option29", 29));
			ret.add(new JepOption("Option30", 30));
			
			tmp = new JepParentOption("EmptyAndOptions_31", 3131);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
		} else if (parentCatalogId == 1119) {
			ret.add(new JepOption("Option11", 11));
			
			tmp = new JepParentOption("Options_12_14", 1214);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
			
			tmp = new JepParentOption("Options_15_19", 1519);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
		} else if (parentCatalogId == 1214) {
			ret.add(new JepOption("Option12", 12));
			ret.add(new JepOption("Option13", 13));
			ret.add(new JepOption("Option14", 14));
		} else if (parentCatalogId == 1519) {
			tmp = new JepParentOption("Options_15", 15191);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
			
			tmp = new JepParentOption("Options_16_18", 1618);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);

			ret.add(new JepOption("Option19", 19));
		} else if (parentCatalogId == 15191) {
			tmp = new JepParentOption("Options_15", 15192);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
		} else if (parentCatalogId == 15192) {
			ret.add(new JepOption("Option15", 15));
		} else if (parentCatalogId == 1618) {
			ret.add(new JepOption("Option16", 16));
			ret.add(new JepOption("Option17", 17));
			ret.add(new JepOption("Option18", 18));
		} else if (parentCatalogId == 2027) {
			tmp = new JepParentOption("Options_20_22", 2022);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
			
			tmp = new JepParentOption("Options_23_27", 2327);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
		} else if (parentCatalogId == 2022) {
			ret.add(new JepOption("Option20", 20));
			ret.add(new JepOption("Option21", 21));
			ret.add(new JepOption("Option22", 22));
		} else if (parentCatalogId == 2327) {
			tmp = new JepParentOption("Options_23_24", 2324);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
			
			ret.add(new JepOption("Option25", 25));
			
			tmp = new JepParentOption("Options_26_27", 2627);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
		} else if (parentCatalogId == 2324) {
			ret.add(new JepOption("Option23", 23));
			ret.add(new JepOption("Option24", 24));
		} else if (parentCatalogId == 2627) {
			ret.add(new JepOption("Option26", 26));
			ret.add(new JepOption("Option27", 27));
		} else if (parentCatalogId == 3131) {
			tmp = new JepParentOption("EmptyAndOptions_31", 31311);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
		}  else if (parentCatalogId == 31311) {
			tmp = new JepParentOption("EmptyAndOptions_31", 31312);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
			
			ret.add(new JepOption("Option31", 31));
		} else if (parentCatalogId == 31312) {
			tmp = new JepParentOption("Empty", 31313);
			tmp.put(TREE_FIELD_HAS_CHILDREN_FLAG, true);
			ret.add(tmp);
		}  else if (parentCatalogId == 31313) {
			// empty folder
		} 
		return ret;
	}
 
}
