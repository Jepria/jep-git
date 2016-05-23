package com.technology.jep.jepriashowcase.feature.auto;

import static com.technology.jep.jepriashowcase.feature.client.FeatureAutomationConstant.FEATURE_DESCRIPTION_DETAILFORM_FIELD_ID_INPUT;
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
	public void setRowCount(String rowCount) {
		pages.featurePage.ensurePageLoaded();
		setFieldValue(FEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID_INPUT, rowCount);
	}

	@Override
	public void setDescription(String description) {
		pages.featurePage.ensurePageLoaded();
		setFieldValue(FEATURE_DESCRIPTION_DETAILFORM_FIELD_ID_INPUT, description);
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
	public String getRowCount() {
		return getFieldValue(FEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public String getDescription() {
		return getFieldValue(FEATURE_DESCRIPTION_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public void fillSearchForm(
			String featureId,
			String featureName,
			String featureNameEn,
			String rowCount) {
		setFeatureId(featureId);
		setFeatureName(featureName);
		setFeatureNameEn(featureNameEn);
		setRowCount(rowCount);
	}

	@Override
	public void fillCreateForm(
			String featureName,
			String featureNameEn,
			String description) {
		setFeatureName(featureName);
		setFeatureNameEn(featureNameEn);
		setDescription(description);
	}

	@Override
	public void fillEditForm(
			String featureId,
			String featureName,
			String featureNameEn,
			String description) {
		setFeatureId(featureId);
		setFeatureName(featureName);
		setFeatureNameEn(featureNameEn);
		setDescription(description);
	}
}
