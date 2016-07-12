package com.technology.jep.jepria.shared.service.report;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.report.JepReportParameters;

/**
 * Асинхронный интерфейс сервиса отчётов.
 */
public interface JepReportServiceAsync {
  /**
   * Подготовка отчёта.
   * 
   * @param reportParameters параметры отчёта
   * @param callback пустой обратный вызов (для сигнализации, что асинхронный метод отработал)
   */
  void prepareReport(JepReportParameters reportParameters, AsyncCallback<Void> callback);
}
