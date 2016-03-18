package com.technology.jep.jepriashowcase.supplier.shared.service;
 
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.service.data.JepDataService;
 
@RemoteServiceRelativePath("SupplierService")
public interface SupplierService extends JepDataService {
	List<JepOption> getBank(String bankBic, Integer maxRowCount) throws ApplicationException;
	String getSupplierNameById(Integer id) throws ApplicationException;
}
