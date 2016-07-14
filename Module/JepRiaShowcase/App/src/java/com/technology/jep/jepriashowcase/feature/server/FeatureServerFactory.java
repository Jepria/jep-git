package com.technology.jep.jepriashowcase.feature.server;

import static com.technology.jep.jepriashowcase.feature.server.FeatureServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepriashowcase.feature.server.dao.Feature;
import com.technology.jep.jepriashowcase.feature.server.dao.FeatureDao;

public class FeatureServerFactory extends ServerFactory<Feature> {

  private FeatureServerFactory() {
    super(new FeatureDao(), DATA_SOURCE_JNDI_NAME);
  }

  public static final FeatureServerFactory instance = new FeatureServerFactory();
  
}
