package com.technology.jep.jepriashowcase.supplier.server.dao;
 
import java.util.List;

import com.technology.jep.jepria.server.ejb.JepDataStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
 
public interface Supplier extends JepDataStandard {
	List<JepOption> getBank(String bankBic, Integer maxRowCount) throws ApplicationException;
}
