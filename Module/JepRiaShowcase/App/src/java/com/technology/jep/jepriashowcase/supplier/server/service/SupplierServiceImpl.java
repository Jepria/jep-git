package com.technology.jep.jepriashowcase.supplier.server.service;
 
import static com.technology.jep.jepriashowcase.supplier.server.SupplierServerConstant.DATA_SOURCE_JNDI_NAME;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepriashowcase.supplier.server.dao.Supplier;
import com.technology.jep.jepriashowcase.supplier.server.dao.SupplierDao;
import com.technology.jep.jepriashowcase.supplier.shared.record.SupplierRecordDefinition;
import com.technology.jep.jepriashowcase.supplier.shared.service.SupplierService;
 
@RemoteServiceRelativePath("SupplierService")
public class SupplierServiceImpl extends JepDataServiceServlet<Supplier> implements SupplierService  {
 
  private static final long serialVersionUID = 1L;
 
  public SupplierServiceImpl() {
    super(SupplierRecordDefinition.instance, new ServerFactory<Supplier>(new SupplierDao(), DATA_SOURCE_JNDI_NAME));
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
