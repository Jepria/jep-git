package com.technology.jep.jepria.shared.field.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

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

  
  /*
   * TODO: Тесты ниже написаны не по правилам. Требуется рефакторинг.
   */
  @Test
  public void equalTest() {
    assertTrue(new JepOption("abc", "123").equals(new JepOption("abc", "123")));
    assertTrue(new JepOption("zzz", 1).equals(new JepOption("zzz", 1)));
    assertFalse(new JepOption("abc", "123").equals(new JepOption("abc", 123)));
    assertFalse(new JepOption("abc", 1).equals(new JepOption("abc", 2)));
    assertFalse(new JepOption("abc", 1).equals(new JepOption("zzz", 1)));   
  }
  
  @Test
  public void containsTest() {
    List<JepOption> options = new ArrayList<JepOption>();
    options.add(new JepOption("abc", 123));
    options.add(new JepOption("option 2", "opt2"));
    assertTrue(options.contains(new JepOption("abc", 123)));
    assertTrue(options.contains(new JepOption("option 2", "opt2")));
    assertFalse(options.contains(new JepOption("option 2", "opt1")));
    assertFalse(options.contains(new JepOption("abc", "123")));
    assertFalse(options.contains(new JepOption("zzz", 123)));
    
  }
}
