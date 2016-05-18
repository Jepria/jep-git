package com.technology.jep.jepriashowcase.featurerequest.client.ui.form.detail;

import static com.technology.jep.jepriashowcase.featurerequest.client.FeatureRequestAutomationConstant.FEATUREREQUEST_DESCRIPTION_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.featurerequest.client.FeatureRequestAutomationConstant.FEATUREREQUEST_FEATUREID_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.featurerequest.client.FeatureRequestAutomationConstant.FEATUREREQUEST_FEATURENAMEEN_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.featurerequest.client.FeatureRequestAutomationConstant.FEATUREREQUEST_FEATURENAME_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.featurerequest.client.FeatureRequestAutomationConstant.FEATUREREQUEST_MAXROWCOUNT_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.featurerequest.shared.field.FeatureRequestFieldNames.DATE_INS;
import static com.technology.jep.jepriashowcase.featurerequest.shared.field.FeatureRequestFieldNames.DESCRIPTION;
import static com.technology.jep.jepriashowcase.featurerequest.shared.field.FeatureRequestFieldNames.FEATURE_ID;
import static com.technology.jep.jepriashowcase.featurerequest.shared.field.FeatureRequestFieldNames.FEATURE_NAME;
import static com.technology.jep.jepriashowcase.featurerequest.shared.field.FeatureRequestFieldNames.FEATURE_NAME_EN;
import static com.technology.jep.jepriashowcase.featurerequest.shared.field.FeatureRequestFieldNames.FROM_DATE_INS;
import static com.technology.jep.jepriashowcase.featurerequest.shared.field.FeatureRequestFieldNames.MAX_ROW_COUNT;
import static com.technology.jep.jepriashowcase.featurerequest.shared.field.FeatureRequestFieldNames.OPERATOR_NAME;
import static com.technology.jep.jepriashowcase.featurerequest.shared.field.FeatureRequestFieldNames.TO_DATE_INS;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.client.widget.field.multistate.JepDateField;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;

public class FeatureRequestDetailFormViewImpl extends DetailFormViewImpl {

	public FeatureRequestDetailFormViewImpl() {
		super(new FieldManager());

		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "100%");
		
		VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setMarginTop(5, Unit.PX);
		scrollPanel.add(panel);
 
		JepIntegerField featureIdIntegerField = new JepIntegerField(FEATUREREQUEST_FEATUREID_DETAILFORM_FIELD_ID, "TODO featureId");
		JepTextField featureNameTextField = new JepTextField(FEATUREREQUEST_FEATURENAME_DETAILFORM_FIELD_ID, "TODO featureName");
		JepTextField featureNameEnTextField = new JepTextField(FEATUREREQUEST_FEATURENAMEEN_DETAILFORM_FIELD_ID, "TODO featureNameEn");
		JepDateField fromDateInsTextField = new JepDateField("TODO fromDateIns");
		JepDateField toDateInsTextField = new JepDateField("TODO toDateIns");
		JepDateField dateInsTextField = new JepDateField("TODO dateIns");
		JepTextField descriptionTextField = new JepTextField(FEATUREREQUEST_DESCRIPTION_DETAILFORM_FIELD_ID, "TODO description");
		JepTextField operatorNameTextField = new JepTextField("TODO operatorName");
		JepIntegerField maxRowCountIntegerField = new JepIntegerField(FEATUREREQUEST_MAXROWCOUNT_DETAILFORM_FIELD_ID, "TODO maxRowCount");
		
		panel.add(featureIdIntegerField);
		panel.add(featureNameTextField);
		panel.add(featureNameEnTextField);
		panel.add(fromDateInsTextField);
		panel.add(toDateInsTextField);
		panel.add(dateInsTextField);
		panel.add(descriptionTextField);
		panel.add(operatorNameTextField);
		panel.add(maxRowCountIntegerField);
		
		setWidget(scrollPanel);
 
		fields.put(FEATURE_ID, featureIdIntegerField);
		fields.put(FEATURE_NAME, featureNameTextField);
		fields.put(FEATURE_NAME_EN, featureNameEnTextField);
		fields.put(FROM_DATE_INS, fromDateInsTextField);
		fields.put(TO_DATE_INS, toDateInsTextField);
		fields.put(DATE_INS, dateInsTextField);
		fields.put(DESCRIPTION, descriptionTextField);
		fields.put(OPERATOR_NAME, operatorNameTextField);
		fields.put(MAX_ROW_COUNT, maxRowCountIntegerField);
	}
}
