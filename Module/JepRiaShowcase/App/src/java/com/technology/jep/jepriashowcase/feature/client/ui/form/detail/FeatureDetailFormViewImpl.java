package com.technology.jep.jepriashowcase.feature.client.ui.form.detail;

import static com.technology.jep.jepriashowcase.feature.client.FeatureAutomationConstant.FEATURE_FEATUREID_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.feature.client.FeatureAutomationConstant.FEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.feature.client.FeatureAutomationConstant.FEATURE_FEATURENAME_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.feature.client.FeatureAutomationConstant.FEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID;
import static com.technology.jep.jepriashowcase.feature.client.FeatureClientConstant.featureText;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DATE_INS;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DATE_INS_FROM;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DATE_INS_TO;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.DESCRIPTION;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_ID;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.FEATURE_NAME_EN;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.MAX_ROW_COUNT;
import static com.technology.jep.jepriashowcase.feature.shared.field.FeatureFieldNames.OPERATOR_NAME;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.client.widget.field.multistate.JepDateField;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextAreaField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;

public class FeatureDetailFormViewImpl extends DetailFormViewImpl {

	public FeatureDetailFormViewImpl() {
		super(new FieldManager());

		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "100%");
		
		VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setMarginTop(5, Unit.PX);
		scrollPanel.add(panel);
 
		JepIntegerField featureIdIntegerField = new JepIntegerField(FEATURE_FEATUREID_DETAILFORM_FIELD_ID, featureText.feature_detail_feature_id());
		JepTextField featureNameTextField = new JepTextField(FEATURE_FEATURENAME_DETAILFORM_FIELD_ID, featureText.feature_detail_feature_name());
		featureNameTextField.setMaxLength(255);
		JepTextField featureNameEnTextField = new JepTextField(FEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID, featureText.feature_detail_feature_name_en());
		featureNameEnTextField.setMaxLength(255);
		JepDateField dateInsFromTextField = new JepDateField(featureText.feature_detail_date_ins_from());
		JepDateField dateInsToTextField = new JepDateField(featureText.feature_detail_date_ins_to());
		JepDateField dateInsTextField = new JepDateField(featureText.feature_detail_date_ins());
		JepTextAreaField descriptionTextAreaField = new JepTextAreaField(featureText.feature_detail_description());
		descriptionTextAreaField.setFieldHeight(120);
		descriptionTextAreaField.setFieldWidth(400);
		JepTextField operatorNameTextField = new JepTextField(featureText.feature_detail_operator_name());
		JepIntegerField maxRowCountIntegerField = new JepIntegerField(FEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID, featureText.feature_detail_max_row_count());
		
		panel.add(featureIdIntegerField);
		panel.add(featureNameTextField);
		panel.add(featureNameEnTextField);
		panel.add(dateInsFromTextField);
		panel.add(dateInsToTextField);
		panel.add(dateInsTextField);
		panel.add(descriptionTextAreaField);
		panel.add(operatorNameTextField);
		panel.add(maxRowCountIntegerField);
		
		setWidget(scrollPanel);
 
		fields.put(FEATURE_ID, featureIdIntegerField);
		fields.put(FEATURE_NAME, featureNameTextField);
		fields.put(FEATURE_NAME_EN, featureNameEnTextField);
		fields.put(DATE_INS_FROM, dateInsFromTextField);
		fields.put(DATE_INS_TO, dateInsToTextField);
		fields.put(DATE_INS, dateInsTextField);
		fields.put(DESCRIPTION, descriptionTextAreaField);
		fields.put(OPERATOR_NAME, operatorNameTextField);
		fields.put(MAX_ROW_COUNT, maxRowCountIntegerField);
	}
}
