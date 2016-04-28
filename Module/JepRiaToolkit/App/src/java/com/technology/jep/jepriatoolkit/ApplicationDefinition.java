package com.technology.jep.jepriatoolkit;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;

import java.util.ResourceBundle;

public enum ApplicationDefinition {
	
	JEPRIA_7(JEPRIA7_APPLICATION_DEFINITION_RESOURCE_BUNDLE_NAME), 
	JEPRIA_8(JEPRIA8_APPLICATION_DEFINITION_RESOURCE_BUNDLE_NAME), 
	JEPRIA_9(JEPRIA9_APPLICATION_DEFINITION_RESOURCE_BUNDLE_NAME);
	
	String resourceBundlePath;
	ResourceBundle resourceBundle;
	
	public static ApplicationDefinition LAST = JEPRIA_9;
	
	ApplicationDefinition(String path){
		this.resourceBundlePath = path;
		this.resourceBundle = ResourceBundle.getBundle(path);
	}
	
	public ResourceBundle getResource(){
		return this.resourceBundle;
	}
}
