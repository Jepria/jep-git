package com.technology.jep.jepriashowcase.requestprocess.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.requestprocess.client.RequestProcessClientConstant.requestProcessText;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.DATE_INS;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.OPERATOR_ID;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.OPERATOR_NAME;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.PROCESS_COMMENT;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.REQUEST_ID;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.REQUEST_PROCESS_ID;

import com.technology.jep.jepria.client.ui.form.detail.StandardDetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepDateField;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;
 
public class RequestProcessDetailFormViewImpl
	extends StandardDetailFormViewImpl 
	implements RequestProcessDetailFormView {	
 
	public RequestProcessDetailFormViewImpl() {
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
		
		fields.put(REQUEST_PROCESS_ID, requestProcessIdIntegerField);
		fields.put(REQUEST_ID, requestIdIntegerField);
		fields.put(PROCESS_COMMENT, processCommentTextField);
		fields.put(DATE_INS, dateInsDateField);
		fields.put(OPERATOR_ID, operatorIdIntegerField);
		fields.put(OPERATOR_NAME, operatorNameTextField);
	}
 
}
