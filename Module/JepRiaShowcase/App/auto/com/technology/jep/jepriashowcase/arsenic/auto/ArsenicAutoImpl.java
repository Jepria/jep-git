package com.technology.jep.jepriashowcase.arsenic.auto;

import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_CHECKBOX_SWITCH_ALBL_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_CHECKBOX_SWITCH_EDTB_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_CHECKBOX_SWITCH_ENBL_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_CHECKBOX_SWITCH_VSBL_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_GRID_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_CHECKBOX_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_DATE_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_DUAL_LIST_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_LIST_FIELD_CHECKALL_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_LIST_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_LONG_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_MONEY_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_NUMBER_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_TEXT_AREA_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_JEP_TEXT_FIELD_ID;
import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.ARSENIC_MAX_ROW_COUNT_ID;

import java.util.List;

import com.technology.jep.jepria.auto.JepRiaModuleAutoImpl;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAuto;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcasePageManager;

public class ArsenicAutoImpl<A extends JepRiaShowcaseAuto, P extends JepRiaShowcasePageManager> extends JepRiaModuleAutoImpl<A, P> implements ArsenicAuto {

	public ArsenicAutoImpl(A app, P pageManager) {
		super(app, pageManager);
	}

	@Override
	public void setCheckBox_switchVsbl(boolean checked) {
		setCheckBoxFieldValue(ARSENIC_CHECKBOX_SWITCH_VSBL_ID, checked);
	}
	
	@Override
	public boolean getCheckBox_switchVsbl() {
		return getCheckBoxFieldValue(ARSENIC_CHECKBOX_SWITCH_VSBL_ID);
	}
	
	
	@Override
	public void setCheckBox_switchEnbl(boolean checked) {
		setCheckBoxFieldValue(ARSENIC_CHECKBOX_SWITCH_ENBL_ID, checked);
	}

	@Override
	public boolean getCheckBox_switchEnbl() {
		return getCheckBoxFieldValue(ARSENIC_CHECKBOX_SWITCH_ENBL_ID);
	}
	
	@Override
	public void setCheckBox_switchEdtb(boolean checked) {
		setCheckBoxFieldValue(ARSENIC_CHECKBOX_SWITCH_EDTB_ID, checked);
	}
	
	@Override
	public boolean getCheckBox_switchEdtb() {
		return getCheckBoxFieldValue(ARSENIC_CHECKBOX_SWITCH_EDTB_ID);
	}
	
	@Override
	public void setCheckBox_switchAlbl(boolean checked) {
		setCheckBoxFieldValue(ARSENIC_CHECKBOX_SWITCH_ALBL_ID, checked);
	}

	@Override
	public boolean getCheckBox_switchAlbl() {
		return getCheckBoxFieldValue(ARSENIC_CHECKBOX_SWITCH_ALBL_ID);
	}
	
	
	
	
	
	
	
	@Override
	public void setJepTextField(String value) {
		setFieldValue(ARSENIC_JEP_TEXT_FIELD_ID, value);
	}

	@Override
	public void setJepTextAreaField(String value) {
		setFieldValue(ARSENIC_JEP_TEXT_AREA_FIELD_ID, value);
	}

	@Override
	public void setJepIntegerField_maxRowCount(String value) {
		setFieldValue(ARSENIC_MAX_ROW_COUNT_ID, value);
	}

	@Override
	public void setJepLongField(String value) {
		setFieldValue(ARSENIC_JEP_LONG_FIELD_ID, value);
	}

	@Override
	public void setJepMoneyField(String value) {
		setFieldValue(ARSENIC_JEP_MONEY_FIELD_ID, value);
	}

	@Override
	public void setJepNumberField(String value) {
		setFieldValue(ARSENIC_JEP_NUMBER_FIELD_ID, value);
	}

	@Override
	public void setJepDateField(String value) {
		setFieldValue(ARSENIC_JEP_DATE_FIELD_ID, value);
	}

	@Override
	public void setJepComboBoxFieldNotLazy(String value) {
		selectComboBoxMenuItem(ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID, value);
	}
	
	@Override
	public void setJepComboBoxFieldSimple(String value) {
		selectComboBoxMenuItem(ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID, value);
	}

	@Override
	public void setJepComboBoxFieldDurable(String value) {
		selectComboBoxMenuItem(ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID, value);
	}

	@Override
	public void setJepComboBoxFieldReloading(String value) {
		selectComboBoxMenuItemWithCharByCharReloadingOptions(ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID, value, 0);
	}

	@Override
	public void setJepComboBoxField3chReloading(String value) {
		selectComboBoxMenuItemWithCharByCharReloadingOptions(ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID, value, 3);
	}
	
	@Override
	public void setJepDualListField(String[] value) {
		selectDualListMenuItems(ARSENIC_JEP_DUAL_LIST_FIELD_ID, value);
	}
	
	@Override
	public void setJepCheckBoxField(boolean checked) {
		setCheckBoxFieldValue(ARSENIC_JEP_CHECKBOX_FIELD_ID, checked);
	}

	@Override
	public void changeJepCheckBoxField() {
		changeCheckBoxFieldValue(ARSENIC_JEP_CHECKBOX_FIELD_ID);
	}

	@Override
	public void setJepListField(String[] value) {
		selectListMenuItems(ARSENIC_JEP_LIST_FIELD_ID, value);
	}
	
	@Override
	public void setJepListFieldCheckAll(String[] value) {
		selectListMenuItems(ARSENIC_JEP_LIST_FIELD_CHECKALL_ID, value);
	}

	
	
	
	
	@Override
	public String getJepTextField() {
		return getFieldValue(ARSENIC_JEP_TEXT_FIELD_ID);
	}

	@Override
	public String getJepTextAreaField() {
		return getFieldValue(ARSENIC_JEP_TEXT_AREA_FIELD_ID);
	}

	@Override
	public String getJepIntegerField_maxRowCount() {
		return getFieldValue(ARSENIC_MAX_ROW_COUNT_ID);
	}

	@Override
	public String getJepLongField() {
		return getFieldValue(ARSENIC_JEP_LONG_FIELD_ID);
	}

	@Override
	public String getJepMoneyField() {
		return getFieldValue(ARSENIC_JEP_MONEY_FIELD_ID);
	}

	@Override
	public String getJepNumberField() {
		return getFieldValue(ARSENIC_JEP_NUMBER_FIELD_ID);
	}

	@Override
	public String getJepDateField() {
		return getFieldValue(ARSENIC_JEP_DATE_FIELD_ID);
	}
	
	@Override
	public String getJepComboBoxFieldNotLazy() {
		return getFieldValue(ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID);
	}

	@Override
	public String getJepComboBoxFieldSimple() {
		return getFieldValue(ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID);
	}

	@Override
	public String getJepComboBoxFieldDurable() {
		return getFieldValue(ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID);
	}

	@Override
	public String getJepComboBoxFieldReloading() {
		return getFieldValue(ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID);
	}

	@Override
	public String getJepComboBoxField3chReloading() {
		return getFieldValue(ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID);
	}
	
	@Override
	public String[] getJepDualListField() {
		return getDualListFieldValues(ARSENIC_JEP_DUAL_LIST_FIELD_ID);
	}
	
	@Override
	public boolean getJepCheckBoxField() {
		return getCheckBoxFieldValue(ARSENIC_JEP_CHECKBOX_FIELD_ID);
	}

	@Override
	public String[] getJepListField() {
		return getListFieldValues(ARSENIC_JEP_LIST_FIELD_ID);
	}
	
	@Override
	public String[] getJepListFieldCheckAll() {
		return getListFieldValues(ARSENIC_JEP_LIST_FIELD_CHECKALL_ID);
	}

	
	
	
	@Override
	public boolean checkAllFieldsVisibility(boolean expected) {
		if (expected) {
			return isFieldVisible(ARSENIC_JEP_TEXT_FIELD_ID) &&
					isFieldVisible(ARSENIC_JEP_TEXT_AREA_FIELD_ID) &&
					isFieldVisible(ARSENIC_JEP_LONG_FIELD_ID) &&
					isFieldVisible(ARSENIC_JEP_MONEY_FIELD_ID) &&
					isFieldVisible(ARSENIC_JEP_NUMBER_FIELD_ID) &&
					isFieldVisible(ARSENIC_JEP_DATE_FIELD_ID) &&
					isFieldVisible(ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID) &&
					isFieldVisible(ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID) &&
					isFieldVisible(ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID) &&
					isFieldVisible(ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID) &&
					isFieldVisible(ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID) &&
					isFieldVisible(ARSENIC_JEP_DUAL_LIST_FIELD_ID) &&
					isFieldVisible(ARSENIC_JEP_CHECKBOX_FIELD_ID) &&
					isFieldVisible(ARSENIC_JEP_LIST_FIELD_ID) &&
					isFieldVisible(ARSENIC_JEP_LIST_FIELD_CHECKALL_ID);
		} else {
			return !isFieldVisible(ARSENIC_JEP_TEXT_FIELD_ID) &&
					!isFieldVisible(ARSENIC_JEP_TEXT_AREA_FIELD_ID) &&
					!isFieldVisible(ARSENIC_JEP_LONG_FIELD_ID) &&
					!isFieldVisible(ARSENIC_JEP_MONEY_FIELD_ID) &&
					!isFieldVisible(ARSENIC_JEP_NUMBER_FIELD_ID) &&
					!isFieldVisible(ARSENIC_JEP_DATE_FIELD_ID) &&
					!isFieldVisible(ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID) &&
					!isFieldVisible(ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID) &&
					!isFieldVisible(ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID) &&
					!isFieldVisible(ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID) &&
					!isFieldVisible(ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID) &&
					!isFieldVisible(ARSENIC_JEP_DUAL_LIST_FIELD_ID) &&
					!isFieldVisible(ARSENIC_JEP_CHECKBOX_FIELD_ID) &&
					!isFieldVisible(ARSENIC_JEP_LIST_FIELD_ID) &&
					!isFieldVisible(ARSENIC_JEP_LIST_FIELD_CHECKALL_ID);
		}
	}
	
	@Override
	public boolean checkAllFieldsEnability(boolean expected) {
		if (expected) {
			return isFieldEnabled(ARSENIC_JEP_TEXT_FIELD_ID) &&
					isFieldEnabled(ARSENIC_JEP_TEXT_AREA_FIELD_ID) &&
					isFieldEnabled(ARSENIC_JEP_LONG_FIELD_ID) &&
					isFieldEnabled(ARSENIC_JEP_MONEY_FIELD_ID) &&
					isFieldEnabled(ARSENIC_JEP_NUMBER_FIELD_ID) &&
					isFieldEnabled(ARSENIC_JEP_DATE_FIELD_ID) &&
					isFieldEnabled(ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID) &&
					isFieldEnabled(ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID) &&
					isFieldEnabled(ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID) &&
					isFieldEnabled(ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID) &&
					isFieldEnabled(ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID) &&
					isFieldEnabled(ARSENIC_JEP_DUAL_LIST_FIELD_ID) &&
					isFieldEnabled(ARSENIC_JEP_CHECKBOX_FIELD_ID) &&
					isFieldEnabled(ARSENIC_JEP_LIST_FIELD_ID) &&
					isFieldEnabled(ARSENIC_JEP_LIST_FIELD_CHECKALL_ID);
		} else {
			return !isFieldEnabled(ARSENIC_JEP_TEXT_FIELD_ID) &&
					!isFieldEnabled(ARSENIC_JEP_TEXT_AREA_FIELD_ID) &&
					!isFieldEnabled(ARSENIC_JEP_LONG_FIELD_ID) &&
					!isFieldEnabled(ARSENIC_JEP_MONEY_FIELD_ID) &&
					!isFieldEnabled(ARSENIC_JEP_NUMBER_FIELD_ID) &&
					!isFieldEnabled(ARSENIC_JEP_DATE_FIELD_ID) &&
					!isFieldEnabled(ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID) &&
					!isFieldEnabled(ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID) &&
					!isFieldEnabled(ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID) &&
					!isFieldEnabled(ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID) &&
					!isFieldEnabled(ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID) &&
					!isFieldEnabled(ARSENIC_JEP_DUAL_LIST_FIELD_ID) &&
					!isFieldEnabled(ARSENIC_JEP_CHECKBOX_FIELD_ID) &&
					!isFieldEnabled(ARSENIC_JEP_LIST_FIELD_ID) &&
					!isFieldEnabled(ARSENIC_JEP_LIST_FIELD_CHECKALL_ID);
		}
	}

	@Override
	public boolean checkAllFieldsEditability(boolean expected) {
		if (expected) {
			return isFieldEditable(ARSENIC_JEP_TEXT_FIELD_ID) &&
					isFieldEditable(ARSENIC_JEP_TEXT_AREA_FIELD_ID) &&
					isFieldEditable(ARSENIC_JEP_LONG_FIELD_ID) &&
					isFieldEditable(ARSENIC_JEP_MONEY_FIELD_ID) &&
					isFieldEditable(ARSENIC_JEP_NUMBER_FIELD_ID) &&
					isFieldEditable(ARSENIC_JEP_DATE_FIELD_ID) &&
					isFieldEditable(ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID) &&
					isFieldEditable(ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID) &&
					isFieldEditable(ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID) &&
					isFieldEditable(ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID) &&
					isFieldEditable(ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID) &&
					isFieldEditable(ARSENIC_JEP_DUAL_LIST_FIELD_ID) &&
					isFieldEditable(ARSENIC_JEP_CHECKBOX_FIELD_ID) &&
					isFieldEditable(ARSENIC_JEP_LIST_FIELD_ID) &&
					isFieldEditable(ARSENIC_JEP_LIST_FIELD_CHECKALL_ID);
		} else {
			return !isFieldEditable(ARSENIC_JEP_TEXT_FIELD_ID) &&
					!isFieldEditable(ARSENIC_JEP_TEXT_AREA_FIELD_ID) &&
					!isFieldEditable(ARSENIC_JEP_LONG_FIELD_ID) &&
					!isFieldEditable(ARSENIC_JEP_MONEY_FIELD_ID) &&
					!isFieldEditable(ARSENIC_JEP_NUMBER_FIELD_ID) &&
					!isFieldEditable(ARSENIC_JEP_DATE_FIELD_ID) &&
					!isFieldEditable(ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID) &&
					!isFieldEditable(ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID) &&
					!isFieldEditable(ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID) &&
					!isFieldEditable(ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID) &&
					!isFieldEditable(ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID) &&
					!isFieldEditable(ARSENIC_JEP_DUAL_LIST_FIELD_ID) &&
					!isFieldEditable(ARSENIC_JEP_CHECKBOX_FIELD_ID) &&
					!isFieldEditable(ARSENIC_JEP_LIST_FIELD_ID) &&
					!isFieldEditable(ARSENIC_JEP_LIST_FIELD_CHECKALL_ID);
		}
	}
	
	@Override
	public boolean checkAllFieldsAllowBlank(boolean expected) {
		if (expected) {
			return isFieldAllowBlank(ARSENIC_JEP_TEXT_FIELD_ID) &&
					isFieldAllowBlank(ARSENIC_JEP_TEXT_AREA_FIELD_ID) &&
					isFieldAllowBlank(ARSENIC_JEP_LONG_FIELD_ID) &&
					isFieldAllowBlank(ARSENIC_JEP_MONEY_FIELD_ID) &&
					isFieldAllowBlank(ARSENIC_JEP_NUMBER_FIELD_ID) &&
					isFieldAllowBlank(ARSENIC_JEP_DATE_FIELD_ID) &&
					isFieldAllowBlank(ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID) &&
					isFieldAllowBlank(ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID) &&
					isFieldAllowBlank(ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID) &&
					isFieldAllowBlank(ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID) &&
					isFieldAllowBlank(ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID) &&
					isFieldAllowBlank(ARSENIC_JEP_DUAL_LIST_FIELD_ID) &&
					isFieldAllowBlank(ARSENIC_JEP_LIST_FIELD_ID) &&
					isFieldAllowBlank(ARSENIC_JEP_LIST_FIELD_CHECKALL_ID);
		} else {
			return !isFieldAllowBlank(ARSENIC_JEP_TEXT_FIELD_ID) &&
					!isFieldAllowBlank(ARSENIC_JEP_TEXT_AREA_FIELD_ID) &&
					!isFieldAllowBlank(ARSENIC_JEP_LONG_FIELD_ID) &&
					!isFieldAllowBlank(ARSENIC_JEP_MONEY_FIELD_ID) &&
					!isFieldAllowBlank(ARSENIC_JEP_NUMBER_FIELD_ID) &&
					!isFieldAllowBlank(ARSENIC_JEP_DATE_FIELD_ID) &&
					!isFieldAllowBlank(ARSENIC_JEP_COMBOBOX_FIELD_NOTLAZY_ID) &&
					!isFieldAllowBlank(ARSENIC_JEP_COMBOBOX_FIELD_SIMPLE_ID) &&
					!isFieldAllowBlank(ARSENIC_JEP_COMBOBOX_FIELD_DURABLE_ID) &&
					!isFieldAllowBlank(ARSENIC_JEP_COMBOBOX_FIELD_RELOADING_ID) &&
					!isFieldAllowBlank(ARSENIC_JEP_COMBOBOX_FIELD_3CH_RELOADING_ID) &&
					!isFieldAllowBlank(ARSENIC_JEP_DUAL_LIST_FIELD_ID) &&
					!isFieldAllowBlank(ARSENIC_JEP_LIST_FIELD_ID) &&
					!isFieldAllowBlank(ARSENIC_JEP_LIST_FIELD_CHECKALL_ID);
		}
	}

	
	
	@Override
	public List<String> getGridHeaders() {
		return getGridHeaders(ARSENIC_GRID_ID);
	}

	
	@Override
	public List<List<Object>> getGridData() {
		return getGridDataRowwise(ARSENIC_GRID_ID);
	}


}
