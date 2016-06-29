package com.technology.jep.jepriashowcase.arsenic.auto;

import static com.technology.jep.jepriashowcase.arsenic.client.ArsenicAutomationConstant.*;

import com.technology.jep.jepria.auto.JepRiaModuleAutoImpl;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAuto;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcasePageManager;

public class ArsenicAutoImpl<A extends JepRiaShowcaseAuto, P extends JepRiaShowcasePageManager> extends JepRiaModuleAutoImpl<A, P> implements ArsenicAuto {

	public ArsenicAutoImpl(A app, P pageManager) {
		super(app, pageManager);
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
	public void setJepIntegerField(String value) {
		setFieldValue(ARSENIC_JEP_INTEGER_FIELD_ID, value);
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
	public void setJepCheckBoxField(boolean value) {
		setCheckBoxFieldValue(ARSENIC_JEP_CHECKBOX_FIELD_ID, value);
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
	public String getJepIntegerField() {
		return getFieldValue(ARSENIC_JEP_INTEGER_FIELD_ID);
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

}
