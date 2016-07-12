package com.technology.jep.jepriashowcase.supplier.shared.service;
 
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
 
public interface SupplierServiceAsync extends JepDataServiceAsync {
  void getBank(String bankBic, Integer maxRowCount, AsyncCallback<List<JepOption>> callback);
  void getSupplierNameById(Integer id, AsyncCallback<String> callback);
}
