package com.technology.jep.jepriashowcase.requestprocess.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.requestprocess.client.RequestProcessClientConstant.requestProcessText;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.DATE_INS;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.OPERATOR_ID;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.OPERATOR_NAME;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.PROCESS_COMMENT;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.REQUEST_ID;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.REQUEST_PROCESS_ID;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.form.detail.StandardDetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepDateField;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;
 
public class RequestProcessDetailFormViewImpl
	extends StandardDetailFormViewImpl 
	implements RequestProcessDetailFormView {	
 
	@Override
	protected LinkedHashMap<String, Widget> getFieldConfigurations() {
		return new LinkedHashMap<String, Widget>() {{
			put(REQUEST_PROCESS_ID, new JepIntegerField(requestProcessText.requestProcess_detail_request_process_id()));
			put(REQUEST_ID, new JepIntegerField(requestProcessText.requestProcess_detail_request_id()));
			put(PROCESS_COMMENT, new JepTextField(requestProcessText.requestProcess_detail_process_comment()));
			put(DATE_INS, new JepDateField(requestProcessText.requestProcess_detail_date_ins()));
			put(OPERATOR_ID, new JepIntegerField(requestProcessText.requestProcess_detail_operator_id()));
			put(OPERATOR_NAME, new JepTextField(requestProcessText.requestProcess_detail_operator_name()));
		}};
	}
}
