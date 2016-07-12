package com.technology.jep.jepriareport.server;

import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Запись отчёта
 */
public class JepReportRecord extends JepRecord {
  private static final long serialVersionUID = 1L;

//  private HashMap<String, Object> recordMap = new HashMap<String, Object>();

  public Object getFieldValue(String name) {
    return super.get(name);
//    return recordMap.get(name);
  }

  public void setFieldValue(String name, Object value) {
//    recordMap.put(name, value);
    super.set(name, value);
  }
}
