package com.technology.jep.jepriashowcase.feature.auto;

import com.technology.jep.jepria.auto.JepRiaModuleAuto;

public interface FeatureAuto extends JepRiaModuleAuto {

	/**
	 * Заполнение полей и получение их значений 
	 */
	void setFeatureId(String featureId);
	void setFeatureName(String featureName);
	void setFeatureNameEn(String featureNameEn);
	void setMaxRowCount(String maxRowCount);
	void setDescription(String description);
	
	String getFeatureId();
	String getFeatureName();
	String getFeatureNameEn();
	String getMaxRowCount();
	String getDescription();

	/**
	 * Заполнение детальных форм
	 */
	void fillSearchForm(
			String featureId,
			String featureName,
			String featureNameEn,
			String maxRowCount);
	
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
