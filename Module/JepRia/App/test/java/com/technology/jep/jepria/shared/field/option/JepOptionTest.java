package com.technology.jep.jepria.shared.field.option;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JepOptionTest {

  @Test
  public void toStringTestIntegerValue() throws Exception {
    assertTrue("{123,abc}".equals(new JepOption("abc", 123).toString()));
  }

  @Test
  public void toStringTestStringValue() throws Exception {
    assertTrue("{0123,abc}".equals(new JepOption("abc", "0123").toString()));
  }

  @Test
  public void toStringTestBooleanValue() throws Exception {
    assertTrue("{true,abc}".equals(new JepOption("abc", true).toString()));
  }

  @Test
  public void toStringTestNullNameAndValue() throws Exception {
    assertTrue("{null,null}".equals(new JepOption(null,null).toString()));
  }
}
