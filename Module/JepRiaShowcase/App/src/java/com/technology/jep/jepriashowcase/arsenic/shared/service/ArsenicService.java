package com.technology.jep.jepriashowcase.arsenic.shared.service;
 
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.service.data.JepDataService;
 
@RemoteServiceRelativePath("ArsenicService")
public interface ArsenicService extends JepDataService {
  void durableFetch(long msec) throws ApplicationException;
  List<JepOption> getTreeCatalog(Integer parentGoodsCatalogId) throws ApplicationException;
}
