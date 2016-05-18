package com.technology.jep.jepriashowcase.featurerequest.auto;

import static com.technology.jep.jepriashowcase.featurerequest.client.FeatureRequestAutomationConstant.FEATUREREQUEST_DESCRIPTION_DETAILFORM_FIELD_ID_INPUT;
import static com.technology.jep.jepriashowcase.featurerequest.client.FeatureRequestAutomationConstant.FEATUREREQUEST_FEATUREID_DETAILFORM_FIELD_ID_INPUT;
import static com.technology.jep.jepriashowcase.featurerequest.client.FeatureRequestAutomationConstant.FEATUREREQUEST_FEATURENAMEEN_DETAILFORM_FIELD_ID_INPUT;
import static com.technology.jep.jepriashowcase.featurerequest.client.FeatureRequestAutomationConstant.FEATUREREQUEST_FEATURENAME_DETAILFORM_FIELD_ID_INPUT;
import static com.technology.jep.jepriashowcase.featurerequest.client.FeatureRequestAutomationConstant.FEATUREREQUEST_MAXROWCOUNT_DETAILFORM_FIELD_ID_INPUT;

import com.technology.jep.jepria.auto.JepRiaModuleAutoImpl;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAuto;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcasePageManager;

public class FeatureRequestAutoImpl<A extends JepRiaShowcaseAuto, P extends JepRiaShowcasePageManager> 
		extends JepRiaModuleAutoImpl<A, P>
		implements FeatureRequestAuto {

	public FeatureRequestAutoImpl(A app, P pageManager) {
		super(app, pageManager);
	}

	@Override
	public void setFeatureId(String featureId) {
		pages.featureRequestPage.ensurePageLoaded();
		setFieldValue(FEATUREREQUEST_FEATUREID_DETAILFORM_FIELD_ID_INPUT, featureId);
	}

	@Override
	public void setFeatureName(String featureName) {
		pages.featureRequestPage.ensurePageLoaded();
		setFieldValue(FEATUREREQUEST_FEATURENAME_DETAILFORM_FIELD_ID_INPUT, featureName);
	}

	@Override
	public void setFeatureNameEn(String featureNameEn) {
		pages.featureRequestPage.ensurePageLoaded();
		setFieldValue(FEATUREREQUEST_FEATURENAMEEN_DETAILFORM_FIELD_ID_INPUT, featureNameEn);
	}

	@Override
	public void setRowCount(String rowCount) {
		pages.featureRequestPage.ensurePageLoaded();
		setFieldValue(FEATUREREQUEST_MAXROWCOUNT_DETAILFORM_FIELD_ID_INPUT, rowCount);
	}

	@Override
	public void setDescription(String description) {
		pages.featureRequestPage.ensurePageLoaded();
		setFieldValue(FEATUREREQUEST_DESCRIPTION_DETAILFORM_FIELD_ID_INPUT, description);
	}

	@Override
	public String getFeatureId() {
		return getFieldValue(FEATUREREQUEST_FEATUREID_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public String getFeatureName() {
		return getFieldValue(FEATUREREQUEST_FEATURENAME_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public String getFeatureNameEn() {
		return getFieldValue(FEATUREREQUEST_FEATURENAMEEN_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public String getRowCount() {
		return getFieldValue(FEATUREREQUEST_MAXROWCOUNT_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public String getDescription() {
		return getFieldValue(FEATUREREQUEST_DESCRIPTION_DETAILFORM_FIELD_ID_INPUT);
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
