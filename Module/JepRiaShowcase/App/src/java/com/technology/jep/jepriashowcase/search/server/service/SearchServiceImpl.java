package com.technology.jep.jepriashowcase.search.server.service;

import static com.technology.jep.jepriashowcase.search.server.SearchServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepriashowcase.search.server.SearchServerConstant.RESOURCE_BUNDLE_NAME;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.shared.exceptions.SystemException;
import com.technology.jep.jepriashowcase.search.server.dao.Search;
import com.technology.jep.jepriashowcase.search.server.dao.SearchDao;
import com.technology.jep.jepriashowcase.search.shared.record.SearchRecordDefinition;
import com.technology.jep.jepriashowcase.search.shared.service.SearchService;

/**
 * Реализация gwt-сервиса для Модуля с произвольным располажением элементов.
 */
@RemoteServiceRelativePath("SearchService")
public class SearchServiceImpl extends JepDataServiceServlet<Search> implements SearchService {
	private static final long serialVersionUID = 1L;

	public SearchServiceImpl() {
		super(
			SearchRecordDefinition.instance
			, new ServerFactory<Search>(new SearchDao(), DATA_SOURCE_JNDI_NAME)
			,	RESOURCE_BUNDLE_NAME);
	}

	public String getOperatorName(
		Integer operatorId) {

		String result = null;

		try {
			result = dao.getOperatorName(operatorId);
		} catch (Throwable th) {
			throw new SystemException(th.getLocalizedMessage(), th);
		}

		return result;
	}
	
}
