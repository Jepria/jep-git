package com.technology.jep.jepriashowcase.requestfeature.auto;

import com.technology.jep.jepria.auto.JepRiaModuleAuto;

public interface RequestFeatureAuto extends JepRiaModuleAuto {

	/**
	 * Заполнение полей и получение их значений 
	 */
	void setFeatureId(String featureId);
	void setFeatureName(String featureName);
	void setFeatureNameEn(String featureNameEn);
	void setRowCount(String rowCount);
	void setDescription(String description);
	
	String getFeatureId();
	String getFeatureName();
	String getFeatureNameEn();
	String getRowCount();
	String getDescription();

	/**
	 * Заполнение детальных форм
	 */
	void fillSearchForm(
			String featureId,
			String featureName,
			String featureNameEn,
			String rowCount);
	
	void fillCreateForm(
			String featureName,
			String featureNameEn,
			String description);

	void fillEditForm(
			String featureId,
			String featureName,
			String featureNameEn,
			String description);
}
