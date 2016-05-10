package com.technology.jep.jepriashowcase.requestfunct.auto;

import static com.technology.jep.jepriashowcase.requestfunct.client.RequestFunctAutomationConstant.REQUESTFUNCT_DESCRIPTION_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfunct.client.RequestFunctAutomationConstant.REQUESTFUNCT_FEATUREID_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfunct.client.RequestFunctAutomationConstant.REQUESTFUNCT_FEATURENAMEEN_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfunct.client.RequestFunctAutomationConstant.REQUESTFUNCT_FEATURENAME_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfunct.client.RequestFunctAutomationConstant.REQUESTFUNCT_FROMDATEINS_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfunct.client.RequestFunctAutomationConstant.REQUESTFUNCT_ROWCOUNT_DETAILFORM_FIELD_INPUT_ID;
import static com.technology.jep.jepriashowcase.requestfunct.client.RequestFunctAutomationConstant.REQUESTFUNCT_TODATEINS_DETAILFORM_FIELD_INPUT_ID;

import com.technology.jep.jepria.auto.JepRiaModuleAutoImpl;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAuto;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcasePageManager;

public class RequestFunctAutoImpl<A extends JepRiaShowcaseAuto, P extends JepRiaShowcasePageManager> 
		extends JepRiaModuleAutoImpl<A, P>
		implements RequestFunctAuto {

	public RequestFunctAutoImpl(A app, P pageManager) {
		super(app, pageManager);
	}

	@Override
	public void setFeatureId(String featureId) {
		pages.requestFunctPage.ensurePageLoaded();
		setFieldValue(REQUESTFUNCT_FEATUREID_DETAILFORM_FIELD_INPUT_ID, featureId);
	}

	@Override
	public void setFeatureName(String featureName) {
		pages.requestFunctPage.ensurePageLoaded();
		setFieldValue(REQUESTFUNCT_FEATURENAME_DETAILFORM_FIELD_INPUT_ID, featureName);
	}

	@Override
	public void setFeatureNameEn(String featureNameEn) {
		pages.requestFunctPage.ensurePageLoaded();
		setFieldValue(REQUESTFUNCT_FEATURENAMEEN_DETAILFORM_FIELD_INPUT_ID, featureNameEn);
	}

	@Override
	public void setFromDateIns(String fromDateIns) {
		pages.requestFunctPage.ensurePageLoaded();
		setFieldValue(REQUESTFUNCT_FROMDATEINS_DETAILFORM_FIELD_INPUT_ID, fromDateIns);
	}

	@Override
	public void setToDateIns(String toDateIns) {
		pages.requestFunctPage.ensurePageLoaded();
		setFieldValue(REQUESTFUNCT_TODATEINS_DETAILFORM_FIELD_INPUT_ID, toDateIns);
	}

	@Override
	public void setRowCount(String rowCount) {
		pages.requestFunctPage.ensurePageLoaded();
		setFieldValue(REQUESTFUNCT_ROWCOUNT_DETAILFORM_FIELD_INPUT_ID, rowCount);
	}

	@Override
	public void setDescription(String description) {
		pages.requestFunctPage.ensurePageLoaded();
		setFieldValue(REQUESTFUNCT_DESCRIPTION_DETAILFORM_FIELD_INPUT_ID, description);
	}

	@Override
	public String getFeatureId() {
		return getFieldValue(REQUESTFUNCT_FEATUREID_DETAILFORM_FIELD_INPUT_ID);
	}

	@Override
	public String getFeatureName() {
		return getFieldValue(REQUESTFUNCT_FEATURENAME_DETAILFORM_FIELD_INPUT_ID);
	}

	@Override
	public String getFeatureNameEn() {
		return getFieldValue(REQUESTFUNCT_FEATURENAMEEN_DETAILFORM_FIELD_INPUT_ID);
	}

	@Override
	public String getFromDateIns() {
		return getFieldValue(REQUESTFUNCT_FROMDATEINS_DETAILFORM_FIELD_INPUT_ID);
	}

	@Override
	public String getToDateIns() {
		return getFieldValue(REQUESTFUNCT_TODATEINS_DETAILFORM_FIELD_INPUT_ID);
	}

	@Override
	public String getRowCount() {
		return getFieldValue(REQUESTFUNCT_ROWCOUNT_DETAILFORM_FIELD_INPUT_ID);
	}

	@Override
	public String getDescription() {
		return getFieldValue(REQUESTFUNCT_DESCRIPTION_DETAILFORM_FIELD_INPUT_ID);
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
