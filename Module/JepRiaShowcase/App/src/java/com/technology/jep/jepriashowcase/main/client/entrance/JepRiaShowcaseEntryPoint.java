package com.technology.jep.jepriashowcase.main.client.entrance;

import com.technology.jep.jepria.client.entrance.JepEntryPoint;
import com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientFactoryImpl;

public class JepRiaShowcaseEntryPoint extends JepEntryPoint {

	JepRiaShowcaseEntryPoint() {
 		super(JepRiaShowcaseClientFactoryImpl.getInstance());
	}
	
}
