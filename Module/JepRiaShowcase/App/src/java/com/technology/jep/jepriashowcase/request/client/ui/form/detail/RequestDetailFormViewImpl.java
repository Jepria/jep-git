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
      private static final long serialVersionUID = 1L;

      @Override
      public boolean isValid(){
        return super.isValid() & customDatesValidator.isValid();
      }
    });
    
    JepDateField requestDateDateField = new JepDateField(requestText.request_detail_request_date()){
      @Override
      public boolean isValid(){
        return super.isValid() && customRequestDateValidator.isValid();
      }
    };
    JepDateField requestDateFromDateField = new JepDateField(requestText.request_detail_request_date_from());
    JepDateField requestDateToDateField = new JepDateField(requestText.request_detail_request_date_to());
    JepComboBoxField requestStatusCodeComboBoxField = new JepComboBoxField(requestText.request_detail_request_status_code());
    JepIntegerField shopIdIntegerField = new JepIntegerField(requestText.request_detail_shop_id());
    JepTextField shopNameTextField = new JepTextField(requestText.request_detail_shop_name());
    JepIntegerField goodsIdIntegerField = new JepIntegerField(requestText.request_detail_goods_id());
    JepTextField goodsNameTextField = new JepTextField(requestText.request_detail_goods_name());
    JepMoneyField goodsQuantityMoneyField = new JepMoneyField(requestText.request_detail_goods_id());
    goodsQuantityMoneyField.setMaxNumberCharactersAfterDecimalSeparator(4);
    goodsQuantityMoneyField.setMaxLength(20);
    
    panel.add(requestDateDateField);
    panel.add(requestDateFromDateField);
    panel.add(requestDateToDateField);
    panel.add(requestStatusCodeComboBoxField);
    panel.add(shopIdIntegerField);
    panel.add(shopNameTextField);
    panel.add(goodsIdIntegerField);
    panel.add(goodsNameTextField);
    panel.add(goodsQuantityMoneyField);
    
    fields.put(REQUEST_DATE, requestDateDateField);
    fields.put(REQUEST_DATE_FROM, requestDateFromDateField);
    fields.put(REQUEST_DATE_TO, requestDateToDateField);
    fields.put(REQUEST_STATUS_CODE, requestStatusCodeComboBoxField);
    fields.put(SHOP_ID, shopIdIntegerField);
    fields.put(SHOP_NAME, shopNameTextField);
    fields.put(GOODS_ID, goodsIdIntegerField);
    fields.put(GOODS_NAME, goodsNameTextField);
    fields.put(GOODS_QUANTITY, goodsQuantityMoneyField);
  }

  
  
  public void setCustomValidator(String validatorId, Validator customValidator) {
    if (BEGIN_END_DATE_CUSTOM_VALIDATOR_ID.equalsIgnoreCase(validatorId)){
      RequestDetailFormViewImpl.customDatesValidator = customValidator; 
    } else if (REQUEST_DATE_CUSTOM_VALIDATOR_ID.equalsIgnoreCase(validatorId)){
      RequestDetailFormViewImpl.customRequestDateValidator = customValidator; 
    }
  }
}
