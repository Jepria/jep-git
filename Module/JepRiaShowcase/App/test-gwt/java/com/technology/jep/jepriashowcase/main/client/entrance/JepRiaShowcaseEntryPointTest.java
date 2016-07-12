package com.technology.jep.jepriashowcase.main.client.entrance;

import com.google.gwt.junit.client.GWTTestCase;

import com.technology.jep.jepria.client.entrance.JepEntryPoint;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;

public class JepRiaShowcaseEntryPointTest extends GWTTestCase {

  public String getModuleName() {
    return "com.technology.jep.jepriashowcase.main.JepRiaShowcase";
  }

  public void gwtSetUp() {

  }

  public void testStartApplication() {
    JepEntryPoint entryPoint = new JepRiaShowcaseEntryPoint() {
      public void initHistory() {
        super.initHistory();
        
        assertNotNull("MainClientFactory not initialized properly.", JepScopeStack.instance.getMainClientFactory());
        
        finishTest();
      }
    };
    
    delayTestFinish(3000);
    
    entryPoint.onModuleLoad();
  }
  
  
}
