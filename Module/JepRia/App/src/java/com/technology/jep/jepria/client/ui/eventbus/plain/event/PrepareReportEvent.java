package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;
import com.technology.jep.jepria.shared.report.JepReportParameters;

public class PrepareReportEvent extends
    BusEvent<PrepareReportEvent.Handler> {

  /**
   * Implemented by handlers of PrepareReportEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link PrepareReportEvent} is fired.
     * 
     * @param event
     *            the {@link PrepareReportEvent}
     */
    void onPrepareReport(PrepareReportEvent event);
  }

  /**
   * A singleton instance of Type&lt;PrepareReportHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();
  
  private final JepReportParameters reportParameters;

  private String reportServlet;

  public String getReportServlet() {
    return reportServlet;
  }

  public JepReportParameters getReportParameters() {
    return reportParameters;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  public PrepareReportEvent(JepReportParameters reportParameters, String reportServlet) {
    this.reportParameters = reportParameters;
    this.reportServlet = reportServlet;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onPrepareReport(this);
  }
}
