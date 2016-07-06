package com.technology.jep.jepriashowcase.arsenic.server.service;
 
import static com.technology.jep.jepriashowcase.arsenic.server.ArsenicServerConstant.DATA_SOURCE_JNDI_NAME;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepriashowcase.arsenic.server.dao.Arsenic;
import com.technology.jep.jepriashowcase.arsenic.server.dao.ArsenicDao;
import com.technology.jep.jepriashowcase.arsenic.shared.record.ArsenicRecordDefinition;
import com.technology.jep.jepriashowcase.arsenic.shared.service.ArsenicService;

@RemoteServiceRelativePath("ArsenicService")
public class ArsenicServiceImpl extends JepDataServiceServlet<Arsenic> implements ArsenicService  {

	public ArsenicServiceImpl() {
		super(ArsenicRecordDefinition.instance, new ServerFactory<Arsenic>(new ArsenicDao(), DATA_SOURCE_JNDI_NAME));
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void durableFetch(long msec) throws ApplicationException {
		try {
			dao.durableFetch(msec);
		} catch (Throwable th) {
			throw new ApplicationException(th.getLocalizedMessage(), th);
		}
	}

	@Override
	public List<JepOption> getTreeCatalog(Integer parentGoodsCatalogId)
			throws ApplicationException {
		List<JepOption> result = null;

		try {
			result = dao.getTreeCatalog(parentGoodsCatalogId);
		} catch (Throwable th) {
			throw new ApplicationException(th.getLocalizedMessage(), th);
		}
		return result;
	}
 
}