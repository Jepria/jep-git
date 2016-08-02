package com.technology.jep.jepriashowcase.arsenic.auto;

import java.util.List;

import com.technology.jep.jepria.auto.JepRiaModuleAuto;

public interface ArsenicAuto extends JepRiaModuleAuto {
  void setCheckBox_switchVsbl(boolean checked);
  boolean getCheckBox_switchVsbl();
  
  void setCheckBox_switchEnbl(boolean checked);
  boolean getCheckBox_switchEnbl();
  
  void setCheckBox_switchEdtb(boolean checked);
  boolean getCheckBox_switchEdtb();
  
  void setCheckBox_switchAlbl(boolean checked);
  boolean getCheckBox_switchAlbl();
  
  void setJepTextField(String value);
  void setJepTextAreaField(String value);
  void setJepIntegerField_maxRowCount(String value);
  void setJepLongField(String value);
  void setJepMoneyField(String value);
  void setJepNumberField(String value);
  void setJepDateField(String value);
  void setJepComboBoxFieldNotLazy(String value);
  void setJepComboBoxFieldSimple(String value);
  void setJepComboBoxFieldDurable(String value);
  void setJepComboBoxFieldReloading(String value);
  void setJepComboBoxField3chReloading(String value);
  void setJepDualListField(String[] values);
  void setJepCheckBoxField(boolean checked);
  void changeJepCheckBoxField();
  void setJepListField(String[] values);
  void setJepListFieldCheckAll(String[] values);
  void setJepTreeField(String[] values);
  void setJepTreeField_nodes(String[] values);
  boolean selectAllJepTreeField_nodes(boolean selectAll);
  void setJepTreeField_casc(String[] values);
  
  String getJepTextField();
  String getJepTextAreaField();
  String getJepIntegerField_maxRowCount();
  String getJepLongField();
  String getJepMoneyField();
  String getJepNumberField();
  String getJepDateField();
  String getJepComboBoxFieldNotLazy();
  String getJepComboBoxFieldSimple();
  String getJepComboBoxFieldDurable();
  String getJepComboBoxFieldReloading();
  String getJepComboBoxField3chReloading();
  String[] getJepDualListField();
  boolean getJepCheckBoxField();
  String[] getJepListField();
  String[] getJepListFieldCheckAll();
  String[] getJepTreeField_checked();
  String[] getJepTreeField_nodes_checked();
  String[] getJepTreeField_nodes_visible();
  String[] getJepTreeField_casc_checked();
  
  /**
   * Проверяет, действительно ли все заданные поля имеют visiblity=expected?
   * @param expected
   * @return
   */
  boolean checkAllFieldsVisibility(boolean expected);
  
  /**
   * Проверяет, действительно ли все заданные поля имеют enability=expected?
   * @param expected
   * @return
   */
  boolean checkAllFieldsEnability(boolean expected);
  
  /**
   * Проверяет, действительно ли все заданные поля имеют редактируемость=expected?
   * @param expected
   * @return
   */
  boolean checkAllFieldsEditability(boolean expected);
  
  /**
   * Проверяет, действительно ли все заданные поля имеют обязательность=expected?
   * @param expected
   * @return
   */
  boolean checkAllFieldsAllowBlank(boolean expected);
  
  List<String> getGridHeaders();
  List<List<Object>> getGridData();
  
  void doGridColumnSettings(String[] columns);
  
  void refreshPage();
}
