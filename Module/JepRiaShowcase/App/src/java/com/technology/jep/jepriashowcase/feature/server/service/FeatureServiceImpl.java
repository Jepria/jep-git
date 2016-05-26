package com.technology.jep.jepriashowcase.feature.server.service;
 
import static com.technology.jep.jepriashowcase.feature.server.FeatureServerConstant.DATA_SOURCE_JNDI_NAME;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepriashowcase.feature.server.dao.Feature;
import com.technology.jep.jepriashowcase.feature.server.dao.FeatureDao;
import com.technology.jep.jepriashowcase.feature.shared.record.FeatureRecordDefinition;
import com.technology.jep.jepriashowcase.feature.shared.service.FeatureService;
 
@RemoteServiceRelativePath("FeatureService")
public class FeatureServiceImpl extends JepDataServiceServlet<Feature> implements FeatureService  {
 
	private static final long serialVersionUID = 1L;
 
	public FeatureServiceImpl() {
		super(FeatureRecordDefinition.instance, new ServerFactory<Feature>(new FeatureDao(), DATA_SOURCE_JNDI_NAME));
	}
}
