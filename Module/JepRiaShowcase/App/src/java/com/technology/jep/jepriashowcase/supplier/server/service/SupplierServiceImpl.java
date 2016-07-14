package com.technology.jep.jepriashowcase.supplier.server.service;
 
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepriashowcase.supplier.server.SupplierServerFactory;
import com.technology.jep.jepriashowcase.supplier.server.dao.Supplier;
import com.technology.jep.jepriashowcase.supplier.shared.record.SupplierRecordDefinition;
import com.technology.jep.jepriashowcase.supplier.shared.service.SupplierService;
 
@RemoteServiceRelativePath("SupplierService")
public class SupplierServiceImpl extends JepDataServiceServlet<Supplier> implements SupplierService  {
 
  private static final long serialVersionUID = 1L;
 
  public SupplierServiceImpl() {
    super(SupplierRecordDefinition.instance, SupplierServerFactory.instance);
  }

  @Override
  public List<JepOption> getBank(String bankBic, Integer maxRowCount) throws ApplicationException {
    List<JepOption> result = null;
    try {
      result = dao.getBank(bankBic, maxRowCount);          
    } catch (Throwable th) {
      throw new ApplicationException(th.getLocalizedMessage(), th);
    }
    return result;
  }
  
  @Override
  public String getSupplierNameById(Integer id) throws ApplicationException {
    String result = null;
    try {
      result = dao.getSupplierNameById(id, super.getOperatorId());          
    } catch (Throwable th) {
      throw new ApplicationException(th.getLocalizedMessage(), th);
    }
    return result;
  }
}
