package com.technology.jep.jepriashowcase.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.technology.jep.jepriashowcase.main.client.entrance.JepRiaShowcaseEntryPointTest;

public class JepRiaShowcaseTestSuite extends com.google.gwt.junit.tools.GWTTestSuite {
  public static Test suite() {
  TestSuite suite = new TestSuite("Test for a JepRiaShowcase Gwt parts");
  suite.addTestSuite(JepRiaShowcaseEntryPointTest.class);
  return suite;
  }
}