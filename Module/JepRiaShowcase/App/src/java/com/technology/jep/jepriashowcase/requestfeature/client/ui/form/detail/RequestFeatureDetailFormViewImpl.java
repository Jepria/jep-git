package com.technology.jep.jepriashowcase.requestfeature.client.ui.form.detail;

import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_DESCRIPTION_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.REQUESTFEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.requestfeature.shared.field.RequestFeatureFieldNames.DATE_INS;
import static com.technology.jep.jepriashowcase.requestfeature.shared.field.RequestFeatureFieldNames.DESCRIPTION;
import static com.technology.jep.jepriashowcase.requestfeature.shared.field.RequestFeatureFieldNames.FEATURE_ID;
import static com.technology.jep.jepriashowcase.requestfeature.shared.field.RequestFeatureFieldNames.FEATURE_NAME;
import static com.technology.jep.jepriashowcase.requestfeature.shared.field.RequestFeatureFieldNames.FEATURE_NAME_EN;
import static com.technology.jep.jepriashowcase.requestfeature.shared.field.RequestFeatureFieldNames.FROM_DATE_INS;
import static com.technology.jep.jepriashowcase.requestfeature.shared.field.RequestFeatureFieldNames.MAX_ROW_COUNT;
import static com.technology.jep.jepriashowcase.requestfeature.shared.field.RequestFeatureFieldNames.OPERATOR_NAME;
import static com.technology.jep.jepriashowcase.requestfeature.shared.field.RequestFeatureFieldNames.TO_DATE_INS;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.client.widget.field.multistate.JepDateField;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;

public class RequestFeatureDetailFormViewImpl extends DetailFormViewImpl {

	public RequestFeatureDetailFormViewImpl() {
		super(new FieldManager());

		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "100%");
		
		VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setMarginTop(5, Unit.PX);
		scrollPanel.add(panel);
 
		JepIntegerField featureIdIntegerField = new JepIntegerField(REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_ID, "TODO featureId");
		JepTextField featureNameTextField = new JepTextField(REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID, "TODO featureName");
		JepTextField featureNameEnTextField = new JepTextField(REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID, "TODO featureNameEn");
		JepDateField fromDateInsTextField = new JepDateField("TODO fromDateIns");
		JepDateField toDateInsTextField = new JepDateField("TODO toDateIns");
		JepDateField dateInsTextField = new JepDateField("TODO dateIns");
		JepTextField descriptionTextField = new JepTextField(REQUESTFEATURE_DESCRIPTION_DETAILFORM_FIELD_ID, "TODO description");
		JepTextField operatorNameTextField = new JepTextField("TODO operatorName");
		JepIntegerField maxRowCountIntegerField = new JepIntegerField(REQUESTFEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID, "TODO maxRowCount");
		
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
