package com.technology.jep.jepriashowcase.supplier.server;

import static com.technology.jep.jepriashowcase.supplier.server.SupplierServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepriashowcase.supplier.server.dao.Supplier;
import com.technology.jep.jepriashowcase.supplier.server.dao.SupplierDao;

public class SupplierServerFactory extends ServerFactory<Supplier> {

  private SupplierServerFactory() {
    super(new SupplierDao(), DATA_SOURCE_JNDI_NAME);
  }

  public static final SupplierServerFactory instance = new SupplierServerFactory();
  
}
