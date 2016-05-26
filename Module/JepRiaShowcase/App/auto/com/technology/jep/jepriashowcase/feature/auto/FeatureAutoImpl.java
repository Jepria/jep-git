package com.technology.jep.jepriashowcase.feature.auto;

import static com.technology.jep.jepriashowcase.feature.client.FeatureAutomationConstant.FEATURE_FEATUREID_DETAILFORM_FIELD_ID_INPUT;
import static com.technology.jep.jepriashowcase.feature.client.FeatureAutomationConstant.FEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID_INPUT;
import static com.technology.jep.jepriashowcase.feature.client.FeatureAutomationConstant.FEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT;
import static com.technology.jep.jepriashowcase.feature.client.FeatureAutomationConstant.FEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID_INPUT;

import com.technology.jep.jepria.auto.JepRiaModuleAutoImpl;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAuto;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcasePageManager;

public class FeatureAutoImpl<A extends JepRiaShowcaseAuto, P extends JepRiaShowcasePageManager> 
		extends JepRiaModuleAutoImpl<A, P>
		implements FeatureAuto {

	public FeatureAutoImpl(A app, P pageManager) {
		super(app, pageManager);
	}

	@Override
	public void setFeatureId(String featureId) {
		pages.featurePage.ensurePageLoaded();
		setFieldValue(FEATURE_FEATUREID_DETAILFORM_FIELD_ID_INPUT, featureId);
	}

	@Override
	public void setFeatureName(String featureName) {
		pages.featurePage.ensurePageLoaded();
		setFieldValue(FEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT, featureName);
	}

	@Override
	public void setFeatureNameEn(String featureNameEn) {
		pages.featurePage.ensurePageLoaded();
		setFieldValue(FEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID_INPUT, featureNameEn);
	}

	@Override
	public void setMaxRowCount(String maxRowCount) {
		pages.featurePage.ensurePageLoaded();
		setFieldValue(FEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID_INPUT, maxRowCount);
	}

	@Override
	public String getFeatureId() {
		return getFieldValue(FEATURE_FEATUREID_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public String getFeatureName() {
		return getFieldValue(FEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public String getFeatureNameEn() {
		return getFieldValue(FEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public String getMaxRowCount() {
		return getFieldValue(FEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public void fillSearchForm(
			String featureId,
			String featureName,
			String featureNameEn,
			String maxRowCount) {
		setFeatureId(featureId);
		setFeatureName(featureName);
		setFeatureNameEn(featureNameEn);
		setMaxRowCount(maxRowCount);
	}

	@Override
	public void fillCreateForm(
			String featureName,
			String featureNameEn) {
		setFeatureName(featureName);
		setFeatureNameEn(featureNameEn);
	}

	@Override
	public void fillEditForm(
			String featureName,
			String featureNameEn) {
		setFeatureName(featureName);
		setFeatureNameEn(featureNameEn);
	}
}
