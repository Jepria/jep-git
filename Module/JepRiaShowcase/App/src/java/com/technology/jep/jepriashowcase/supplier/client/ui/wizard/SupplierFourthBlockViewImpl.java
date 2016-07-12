package com.technology.jep.jepriashowcase.supplier.client.ui.wizard;

import static com.technology.jep.jepriashowcase.supplier.client.SupplierClientConstant.supplierText;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.FAX_NUMBER;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.PHONE_NUMBER;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.wizard.BlockPositionEnum;
import com.technology.jep.jepria.client.ui.wizard.BlockViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepMaskedTextField;

public class SupplierFourthBlockViewImpl extends BlockViewImpl {

  public SupplierFourthBlockViewImpl(){
    
    VerticalPanel innerPanel = new VerticalPanel();

    JepMaskedTextField phoneNumberMaskedTextField = new JepMaskedTextField(supplierText.supplier_detail_phoneNumber(), "(000)000-00-00");
    JepMaskedTextField faxNumberMaskedTextField = new JepMaskedTextField(supplierText.supplier_detail_faxNumber(), "(000)000-00-00");
    
    innerPanel.add(phoneNumberMaskedTextField);
    innerPanel.add(faxNumberMaskedTextField);
    
    // init main widget
    setWidget(innerPanel, BlockPositionEnum.DOWN);
    // initialize fieldset caption
    asWidget().setCaptionText(supplierText.supplier_detail_contactsFieldSet_title());
    
    fields.put(PHONE_NUMBER, phoneNumberMaskedTextField);
    fields.put(FAX_NUMBER, faxNumberMaskedTextField);
  }
}
