package com.technology.jep.jepriashowcase.arsenic.auto;

import com.technology.jep.jepria.auto.JepRiaModuleAuto;

public interface ArsenicAuto extends JepRiaModuleAuto {
	void setJepTextField(String value);
	void setJepTextAreaField(String value);
	void setJepIntegerField(String value);
	void setJepLongField(String value);
	void setJepMoneyField(String value);
	void setJepNumberField(String value);
	void setJepDateField(String value);
	void setJepComboBoxFieldSimple(String value);
	void setJepComboBoxFieldDurable(String value);
	void setJepComboBoxFieldReloading(String value);
	void setJepComboBoxField3chReloading(String value);
	
	String getJepTextField();
	String getJepTextAreaField();
	String getJepIntegerField();
	String getJepLongField();
	String getJepMoneyField();
	String getJepNumberField();
	String getJepDateField();
	String getJepComboBoxFieldSimple();
	String getJepComboBoxFieldDurable();
	String getJepComboBoxFieldReloading();
	String getJepComboBoxField3chReloading();
}
