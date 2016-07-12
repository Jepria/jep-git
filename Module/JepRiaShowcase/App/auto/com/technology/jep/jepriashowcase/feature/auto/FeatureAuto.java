package com.technology.jep.jepriashowcase.feature.auto;

import com.technology.jep.jepria.auto.JepRiaModuleAuto;

public interface FeatureAuto extends JepRiaModuleAuto {

  /**
   * Заполнение полей и получение их значений 
   */
  void setFeatureId(String featureId);
  void setFeatureName(String featureName);
  void setFeatureNameEn(String featureNameEn);
  void setFromDateIns(String fromDateIns);
  void setToDateIns(String toDateIns);
  void setDescription(String description);
  void setMaxRowCount(String maxRowCount);
  
  String getFeatureId();
  String getFeatureName();
  String getFeatureNameEn();
  String getFromDateIns();
  String getToDateIns();
  String getDescription();
  String getMaxRowCount();

  /**
   * Заполнение детальных форм
   */
  void fillSearchForm(
      String featureId,
      String featureName,
      String featureNameEn,
      String fromDateIns,
      String toDateIns,
      String maxRowCount);
  
  void fillCreateForm(
      String featureName,
      String featureNameEn,
      String description);

  void fillEditForm(
      String featureName,
      String featureNameEn);
}
