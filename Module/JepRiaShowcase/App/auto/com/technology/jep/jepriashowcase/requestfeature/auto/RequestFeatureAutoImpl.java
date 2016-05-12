package com.technology.jep.jepriashowcase.requestfeature.auto;

import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_DESCRIPTION_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FROMDATEINS_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_ROWCOUNT_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_TODATEINS_DETAILFORM_FIELD_INPUT_ID;

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
		setFieldValue(REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_INPUT_ID, featureId);
	}

	@Override
	public void setFeatureName(String featureName) {
		pages.requestFeaturePage.ensurePageLoaded();
		setFieldValue(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID, featureName);
	}

	@Override
	public void setFeatureNameEn(String featureNameEn) {
		pages.requestFeaturePage.ensurePageLoaded();
		setFieldValue(REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_INPUT_ID, featureNameEn);
	}

	@Override
	public void setFromDateIns(String fromDateIns) {
		pages.requestFeaturePage.ensurePageLoaded();
		setFieldValue(REQUESTFEATURE_FROMDATEINS_DETAILFORM_FIELD_INPUT_ID, fromDateIns);
	}

	@Override
	public void setToDateIns(String toDateIns) {
		pages.requestFeaturePage.ensurePageLoaded();
		setFieldValue(REQUESTFEATURE_TODATEINS_DETAILFORM_FIELD_INPUT_ID, toDateIns);
	}

	@Override
	public void setRowCount(String rowCount) {
		pages.requestFeaturePage.ensurePageLoaded();
		setFieldValue(REQUESTFEATURE_ROWCOUNT_DETAILFORM_FIELD_INPUT_ID, rowCount);
	}

	@Override
	public void setDescription(String description) {
		pages.requestFeaturePage.ensurePageLoaded();
		setFieldValue(REQUESTFEATURE_DESCRIPTION_DETAILFORM_FIELD_INPUT_ID, description);
	}

	@Override
	public String getFeatureId() {
		return getFieldValue(REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_INPUT_ID);
	}

	@Override
	public String getFeatureName() {
		return getFieldValue(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_INPUT_ID);
	}

	@Override
	public String getFeatureNameEn() {
		return getFieldValue(REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_INPUT_ID);
	}

	@Override
	public String getFromDateIns() {
		return getFieldValue(REQUESTFEATURE_FROMDATEINS_DETAILFORM_FIELD_INPUT_ID);
	}

	@Override
	public String getToDateIns() {
		return getFieldValue(REQUESTFEATURE_TODATEINS_DETAILFORM_FIELD_INPUT_ID);
	}

	@Override
	public String getRowCount() {
		return getFieldValue(REQUESTFEATURE_ROWCOUNT_DETAILFORM_FIELD_INPUT_ID);
	}

	@Override
	public String getDescription() {
		return getFieldValue(REQUESTFEATURE_DESCRIPTION_DETAILFORM_FIELD_INPUT_ID);
	}

	@Override
	public void fillSearchForm(
			String featureId,
			String featureName,
			String featureNameEn,
			String fromDateIns,
			String toDateIns,
			String rowCount) {
		setFeatureId(featureId);
		setFeatureName(featureNameEn);
		setFeatureNameEn(featureNameEn);
		setFromDateIns(fromDateIns);
		setToDateIns(toDateIns);
		setRowCount(rowCount);
	}

	@Override
	public void fillCreateForm(
			String featureName,
			String featureNameEn,
			String description) {
		setFeatureName(featureNameEn);
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
		setFeatureName(featureNameEn);
		setFeatureNameEn(featureNameEn);
		setDescription(description);
	}
}
