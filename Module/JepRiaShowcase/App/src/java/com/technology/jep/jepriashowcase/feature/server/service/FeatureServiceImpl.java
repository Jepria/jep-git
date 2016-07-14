package com.technology.jep.jepriashowcase.feature.server.service;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepriashowcase.feature.server.FeatureServerFactory;
import com.technology.jep.jepriashowcase.feature.server.dao.Feature;
import com.technology.jep.jepriashowcase.feature.shared.record.FeatureRecordDefinition;
import com.technology.jep.jepriashowcase.feature.shared.service.FeatureService;
 
@RemoteServiceRelativePath("FeatureService")
public class FeatureServiceImpl extends JepDataServiceServlet<Feature> implements FeatureService  {
 
  private static final long serialVersionUID = 1L;
 
  public FeatureServiceImpl() {
    super(FeatureRecordDefinition.instance, FeatureServerFactory.instance);
  }
}
