package com.technology.${packageName?lower_case}.${moduleName?lower_case}.main.client.entrance;
 
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.main.client.${moduleName}ClientFactoryImpl;
import com.technology.jep.jepria.client.entrance.JepEntryPoint;

public class ${moduleName}EntryPoint extends JepEntryPoint {
		
	${moduleName}EntryPoint() {
		super(${moduleName}ClientFactoryImpl.getInstance());
	}
}
