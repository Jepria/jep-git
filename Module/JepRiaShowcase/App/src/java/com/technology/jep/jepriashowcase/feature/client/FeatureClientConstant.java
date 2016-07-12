package com.technology.jep.jepriashowcase.feature.client;
 
import com.google.gwt.core.client.GWT;
import com.technology.jep.jepriashowcase.feature.shared.FeatureConstant;
import com.technology.jep.jepriashowcase.feature.shared.text.FeatureText;
 
public class FeatureClientConstant extends FeatureConstant {
  public static FeatureText featureText = (FeatureText) GWT.create(FeatureText.class);
}
