package com.technology.jep.jepriashowcase.requestfeature.auto;

import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_DESCRIPTION_DETAILFORM_FIELD_ID_INPUT;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_ID_INPUT;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID_INPUT;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID_INPUT;

import com.technology.jep.jepria.auto.JepRiaModuleAutoImpl;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAuto;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcasePageManager;

public class RequestFeatureAutoImpl<A extends JepRiaShowcaseAuto, P extends JepRiaShowcasePageManager> 
		extends JepRiaModuleAutoImpl<A, P>
		implements RequestFeatureAuto {

	public RequestFeatureAutoImpl(A app, P pageManager) {
		super(app, pageManager);
	}

	@Override
	public void setFeatureId(String featureId) {
		pages.requestFeaturePage.ensurePageLoaded();
		setFieldValue(REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_ID_INPUT, featureId);
	}

	@Override
	public void setFeatureName(String featureName) {
		pages.requestFeaturePage.ensurePageLoaded();
		setFieldValue(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT, featureName);
	}

	@Override
	public void setFeatureNameEn(String featureNameEn) {
		pages.requestFeaturePage.ensurePageLoaded();
		setFieldValue(REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID_INPUT, featureNameEn);
	}

	@Override
	public void setRowCount(String rowCount) {
		pages.requestFeaturePage.ensurePageLoaded();
		setFieldValue(REQUESTFEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID_INPUT, rowCount);
	}

	@Override
	public void setDescription(String description) {
		pages.requestFeaturePage.ensurePageLoaded();
		setFieldValue(REQUESTFEATURE_DESCRIPTION_DETAILFORM_FIELD_ID_INPUT, description);
	}

	@Override
	public String getFeatureId() {
		return getFieldValue(REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public String getFeatureName() {
		return getFieldValue(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public String getFeatureNameEn() {
		return getFieldValue(REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public String getRowCount() {
		return getFieldValue(REQUESTFEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID_INPUT);
	}

	@Override
	public String getDescription() {
		return getFieldValue(REQUESTFEATURE_DESCRIPTION_DETAILFORM_FIELD_ID_INPUT);
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
