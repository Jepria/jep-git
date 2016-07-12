package com.technology.jep.test.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for feeding arguments to methods conforming to the "@DataProvider"
 * annotation type.
 * 
 * @author jharen
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DataProviderArguments {
  /**
   * String array of key-value pairs fed to a dynamic data provider. Should be
   * in the form of key=value, e.g., <br />
   * args={"foo=bar", "biz=baz"}
   */
  String[] value();
}