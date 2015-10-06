package com.technology.jep.jepriashowcase.requestprocess.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.requestprocess.client.RequestProcessClientConstant.requestProcessText;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.*;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.client.widget.field.multistate.JepDateField;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;
 
public class RequestProcessDetailFormViewImpl
	extends DetailFormViewImpl 
	implements RequestProcessDetailFormView {	
 
	public RequestProcessDetailFormViewImpl() {
		super(new FieldManager());
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "100%");
		
		VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setMarginTop(5, Unit.PX);
		scrollPanel.add(panel);
 
		JepIntegerField requestProcessIdIntegerField = new JepIntegerField(requestProcessText.requestProcess_detail_request_process_id());
		JepIntegerField requestIdIntegerField = new JepIntegerField(requestProcessText.requestProcess_detail_request_id());
		JepTextField processCommentTextField = new JepTextField(requestProcessText.requestProcess_detail_process_comment());
		JepDateField dateInsDateField = new JepDateField(requestProcessText.requestProcess_detail_date_ins());
		JepIntegerField operatorIdIntegerField = new JepIntegerField(requestProcessText.requestProcess_detail_operator_id());
		JepTextField operatorNameTextField = new JepTextField(requestProcessText.requestProcess_detail_operator_name());
		panel.add(requestProcessIdIntegerField);
		panel.add(requestIdIntegerField);
		panel.add(processCommentTextField);
		panel.add(dateInsDateField);
		panel.add(operatorIdIntegerField);
		panel.add(operatorNameTextField);
		
		setWidget(scrollPanel);
 
		fields.put(REQUEST_PROCESS_ID, requestProcessIdIntegerField);
		fields.put(REQUEST_ID, requestIdIntegerField);
		fields.put(PROCESS_COMMENT, processCommentTextField);
		fields.put(DATE_INS, dateInsDateField);
		fields.put(OPERATOR_ID, operatorIdIntegerField);
		fields.put(OPERATOR_NAME, operatorNameTextField);
	}
 
}
