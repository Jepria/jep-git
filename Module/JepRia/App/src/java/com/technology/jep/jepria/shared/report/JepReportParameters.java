package com.technology.jep.jepria.shared.report;

import java.util.Map;

import com.technology.jep.jepria.shared.dto.JepDto;

/**
 * Параметры отчёта.
 */
public class JepReportParameters extends JepDto {
  private static final long serialVersionUID = 1L;
  
  public JepReportParameters() {
  }
  
  /**
   * Клонирующий конструктор.
   *
   * @param properties клонируемые параметры
   */
  public JepReportParameters(JepDto properties) {
    super(properties);
  }

  public Object getParameter(String name) {
    return super.get(name);
  }

  public void setParameter(String name, Object value) {
    super.set(name, value);
  }

  public Map<String, Object> getParameterMap() {
    return super.getProperties();
  }
}
