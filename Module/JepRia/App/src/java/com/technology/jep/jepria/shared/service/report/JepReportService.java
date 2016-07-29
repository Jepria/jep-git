package com.technology.jep.jepria.shared.service.report;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.report.JepReportParameters;

/**
 * Сервис отчётов.
 */
public interface JepReportService {

  /**
   * Подготовка отчёта.
   * 
   * @param reportParameters параметры отчёта
   */
  void prepareReport(JepReportParameters reportParameters) throws ApplicationException;
}
