package com.technology.jep.jepriashowcase.feature.auto;

import static com.technology.jep.jepriashowcase.feature.client.FeatureAutomationConstant.*;

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
    setFieldValue(FEATURE_FEATUREID_DETAILFORM_FIELD_ID, featureId);
  }

  @Override
  public void setFeatureName(String featureName) {
    pages.featurePage.ensurePageLoaded();
    setFieldValue(FEATURE_FEATURENAME_DETAILFORM_FIELD_ID, featureName);
  }

  @Override
  public void setFeatureNameEn(String featureNameEn) {
    pages.featurePage.ensurePageLoaded();
    setFieldValue(FEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID, featureNameEn);
  }
  
  @Override
  public void setFromDateIns(String fromDateIns) {
    pages.featurePage.ensurePageLoaded();
    setFieldValue(FEATURE_FROMDATEINS_DETAILFORM_FIELD_ID, fromDateIns);
  }
  
  @Override
  public void setToDateIns(String toDateIns) {
    pages.featurePage.ensurePageLoaded();
    setFieldValue(FEATURE_TODATEINS_DETAILFORM_FIELD_ID, toDateIns);
  }
  
  @Override
  public void setDescription(String description) {
    pages.featurePage.ensurePageLoaded();
    setFieldValue(FEATURE_DESCRIPTION_DETAILFORM_FIELD_ID, description);
  }

  @Override
  public void setMaxRowCount(String maxRowCount) {
    pages.featurePage.ensurePageLoaded();
    setFieldValue(FEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID, maxRowCount);
  }

  @Override
  public String getFeatureId() {
    return getFieldValue(FEATURE_FEATUREID_DETAILFORM_FIELD_ID);
  }

  @Override
  public String getFeatureName() {
    return getFieldValue(FEATURE_FEATURENAME_DETAILFORM_FIELD_ID);
  }

  @Override
  public String getFeatureNameEn() {
    return getFieldValue(FEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID);
  }
  
  @Override
  public String getFromDateIns() {
    return getFieldValue(FEATURE_FROMDATEINS_DETAILFORM_FIELD_ID);
  }
  
  @Override
  public String getToDateIns() {
    return getFieldValue(FEATURE_TODATEINS_DETAILFORM_FIELD_ID);
  }
  
  @Override
  public String getDescription() {
    return getFieldValue(FEATURE_DESCRIPTION_DETAILFORM_FIELD_ID);
  }

  @Override
  public String getMaxRowCount() {
    return getFieldValue(FEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID);
  }

  @Override
  public void fillSearchForm(
      String featureId,
      String featureName,
      String featureNameEn,
      String fromDateIns,
      String toDateIns,
      String maxRowCount) {
    setFeatureId(featureId);
    setFeatureName(featureName);
    setFeatureNameEn(featureNameEn);
    setFromDateIns(fromDateIns);
    setToDateIns(toDateIns);
    setMaxRowCount(maxRowCount);
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
      String featureName,
      String featureNameEn) {
    setFeatureName(featureName);
    setFeatureNameEn(featureNameEn);
  }
}
