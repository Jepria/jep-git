package com.technology.jep.jepriashowcase.request.client.ui.form.detail;
 
import com.technology.jep.jepria.client.ui.form.detail.DetailFormView;
import com.technology.jep.jepria.client.widget.field.validation.Validator;
 
public interface RequestDetailFormView extends DetailFormView {

  void setCustomValidator(String validatorId, Validator customValidator);
  
}
