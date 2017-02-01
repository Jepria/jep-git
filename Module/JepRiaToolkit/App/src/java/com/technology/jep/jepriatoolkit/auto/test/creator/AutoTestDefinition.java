package com.technology.jep.jepriatoolkit.auto.test.creator;

import static com.technology.jep.jepriatoolkit.auto.test.creator.AutoTestConstant.*;

import java.util.ResourceBundle;

public enum AutoTestDefinition {
  
  JEPRIA_9_AUTO_TEST(JEPRIA9_AUTO_TEST_DEFINITION_RESOURCE_BUNDLE_NAME);
  
  String resourceBundlePath;
  ResourceBundle resourceBundle;
  
  public static AutoTestDefinition LAST_AUTO_TEST = JEPRIA_9_AUTO_TEST;
  
  AutoTestDefinition(String path){
    this.resourceBundlePath = path;
    this.resourceBundle = ResourceBundle.getBundle(path);
  }
  
  public ResourceBundle getResource(){
    return this.resourceBundle;
  }
  
  public static String getAutoTestDefinitionProperty(String property) {
    return getAutoTestDefinitionProperty(property, null);
  }
  
  public static String getAutoTestDefinitionProperty(String property, ResourceBundle autoTestResourceBundle) {
    if (autoTestResourceBundle == null) autoTestResourceBundle = LAST_AUTO_TEST.getResource();
    return autoTestResourceBundle.getString(property);
  }
}
