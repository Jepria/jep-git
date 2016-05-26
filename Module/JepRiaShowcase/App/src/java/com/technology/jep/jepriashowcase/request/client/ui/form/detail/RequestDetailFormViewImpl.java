package com.technology.jep.jepriashowcase.request.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.request.client.RequestClientConstant.BEGIN_END_DATE_CUSTOM_VALIDATOR_ID;
import static com.technology.jep.jepriashowcase.request.client.RequestClientConstant.REQUEST_DATE_CUSTOM_VALIDATOR_ID;
import static com.technology.jep.jepriashowcase.request.client.RequestClientConstant.requestText;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.GOODS_ID;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.GOODS_NAME;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.GOODS_QUANTITY;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_DATE;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_DATE_FROM;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_DATE_TO;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.REQUEST_STATUS_CODE;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.SHOP_ID;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.SHOP_NAME;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.form.detail.StandardDetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepDateField;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepMoneyField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;
import com.technology.jep.jepria.client.widget.field.validation.Validator;
 
public class RequestDetailFormViewImpl
	extends StandardDetailFormViewImpl
	implements RequestDetailFormView {	
	
	private static Validator customDatesValidator;
	private static Validator customRequestDateValidator;
	
	public RequestDetailFormViewImpl() {
		super(new FieldManager(){
			@Override
			public boolean isValid(){
				return super.isValid() & customDatesValidator.isValid();
			}
		});
	}
	
	public void setCustomValidator(String validatorId, Validator customValidator) {
		if (BEGIN_END_DATE_CUSTOM_VALIDATOR_ID.equalsIgnoreCase(validatorId)){
			RequestDetailFormViewImpl.customDatesValidator = customValidator; 
		} else if (REQUEST_DATE_CUSTOM_VALIDATOR_ID.equalsIgnoreCase(validatorId)){
			RequestDetailFormViewImpl.customRequestDateValidator = customValidator; 
		}
	}

	@Override
	protected LinkedHashMap<String, Widget> getFieldConfigurations() {
		return new LinkedHashMap<String, Widget>() {{
			JepDateField requestDateDateField = new JepDateField(requestText.request_detail_request_date()){
				@Override
				public boolean isValid(){
					return super.isValid() && customRequestDateValidator.isValid();
				}
			};
			put(REQUEST_DATE, requestDateDateField);
			
			fields.put(REQUEST_DATE_FROM, new JepDateField(requestText.request_detail_request_date_from()));
			fields.put(REQUEST_DATE_TO, new JepDateField(requestText.request_detail_request_date_to()));
			fields.put(REQUEST_STATUS_CODE, new JepComboBoxField(requestText.request_detail_request_status_code()));
			fields.put(SHOP_ID, new JepIntegerField(requestText.request_detail_shop_id()));
			fields.put(SHOP_NAME, new JepTextField(requestText.request_detail_shop_name()));
			fields.put(GOODS_ID, new JepIntegerField(requestText.request_detail_goods_id()));
			fields.put(GOODS_NAME, new JepTextField(requestText.request_detail_goods_name()));
			
			JepMoneyField goodsQuantityMoneyField = new JepMoneyField(requestText.request_detail_goods_id());
			goodsQuantityMoneyField.setMaxNumberCharactersAfterDecimalSeparator(4);
			goodsQuantityMoneyField.setMaxLength(20);
			fields.put(GOODS_QUANTITY, goodsQuantityMoneyField);
		}};
	}
}
