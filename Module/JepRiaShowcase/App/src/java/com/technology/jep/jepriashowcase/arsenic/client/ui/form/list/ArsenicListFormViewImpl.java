package com.technology.jep.jepriashowcase.arsenic.client.ui.form.list;
 
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicClientConstant.arsenicText;
import static com.technology.jep.jepriashowcase.arsenic.shared.field.ArsenicFieldNames.*;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.*;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.technology.jep.jepria.client.ui.form.list.StandardListFormViewImpl;
import com.technology.jep.jepria.client.widget.list.JepColumn;
import com.technology.jep.jepria.client.widget.list.cell.JepCheckBoxCell;
 
public class ArsenicListFormViewImpl extends StandardListFormViewImpl {
 
	private static NumberFormat defaultNumberFormatter = NumberFormat.getFormat("#");
	private static NumberFormat moneyNumberFormatter = NumberFormat.getFormat("$#.##");
	private static NumberFormat doubleNumberFormatter = NumberFormat.getFormat("#.#");
	private static DateTimeFormat defaultDateFormatter = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT);
 
	public ArsenicListFormViewImpl() {
		super(ARSENIC_GRID_ID);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	@Override
	protected List<JepColumn> getColumnConfigurations() {
		return new ArrayList<JepColumn>() {{
			add(new JepColumn(DETAILFORM_JEP_TEXT_FIELD, arsenicText.list_jepTextField(), 80));
			add(new JepColumn(DETAILFORM_JEP_TEXT_AREA_FIELD, arsenicText.list_jepTextAreaField(), 150));
			add(new JepColumn(DETAILFORM_JEP_LONG_FIELD, arsenicText.list_jepLongField(), 80, new NumberCell(defaultNumberFormatter)));
			add(new JepColumn(DETAILFORM_JEP_MONEY_FIELD, arsenicText.list_jepMoneyField(), 80, new NumberCell(moneyNumberFormatter)));
			add(new JepColumn(DETAILFORM_JEP_NUMBER_FIELD, arsenicText.list_jepNumberField(), 80, new NumberCell(doubleNumberFormatter)));
			add(new JepColumn(DETAILFORM_JEP_DATE_FIELD, arsenicText.list_jepDateField(), 80, new DateCell(defaultDateFormatter)));
			add(new JepColumn(DETAILFORM_JEP_COMBOBOX_FIELD_NOTLAZY, arsenicText.list_jepComboBoxField_notlazy(), 80));
			add(new JepColumn(DETAILFORM_JEP_COMBOBOX_FIELD_SIMPLE, arsenicText.list_jepComboBoxField_simple(), 80));
			add(new JepColumn(DETAILFORM_JEP_COMBOBOX_FIELD_DURABLE, arsenicText.list_jepComboBoxField_durable(), 80));
			add(new JepColumn(DETAILFORM_JEP_COMBOBOX_FIELD_RELOADING, arsenicText.list_jepComboBoxField_reloading(), 80));
			add(new JepColumn(DETAILFORM_JEP_COMBOBOX_FIELD_3CH_RELOADING, arsenicText.list_jepComboBoxField_3ch_reloading(), 80));
			add(new JepColumn(DETAILFORM_JEP_DUAL_LIST_FIELD, arsenicText.list_jepDualListField(), 150));
			add(new JepColumn(DETAILFORM_JEP_CHECKBOX_FIELD, arsenicText.list_jepCheckBoxField(), 30, new JepCheckBoxCell()));
			add(new JepColumn(DETAILFORM_JEP_LIST_FIELD, arsenicText.list_jepListField(), 150));
			add(new JepColumn(DETAILFORM_JEP_LIST_FIELD_CHECKALL, arsenicText.list_jepListField_checkAll(), 150));
		}};
	}
}
