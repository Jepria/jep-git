package com.technology.jep.jepria.client.ui.eventbus.plain;

import com.technology.jep.jepria.client.ui.eventbus.JepEventBus;
import com.technology.jep.jepria.client.ui.eventbus.event.ExitScopeEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.AdjustExitScopeEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.DeleteEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.DoDeleteEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.DoGetRecordEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.DoSearchEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.ListEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.PagingEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.PrepareReportEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.RefreshFieldsEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.RefreshListEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SaveEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SaveSearchTemplateEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SearchEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SetCurrentRecordEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SetListUIDEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SetSaveButtonEnabledEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.ShowExcelEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.ShowHelpEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SortEvent;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.load.SortConfig;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.report.JepReportParameters;

public class PlainEventBus extends JepEventBus {

  public static final String CHANGE_WORKSTATE_EVENT_NAME = "cw";

  public PlainEventBus() {
    super();
  }

  public void setCurrentRecord(JepRecord newRecord) {
    fireEvent(new SetCurrentRecordEvent(newRecord));
  }
  
  public void setListUID(Integer uid) {
    fireEvent(new SetListUIDEvent(uid));
  }

  public void doSearch() {
    fireEvent(new DoSearchEvent());
  }

  public void search(PagingConfig pagingConfig) {
    fireEvent(new SearchEvent(pagingConfig));
  }

  public void sort(SortConfig sortConfig) {
    fireEvent(new SortEvent(sortConfig));
  }

  public void paging(PagingConfig pagingConfig) {
    fireEvent(new PagingEvent(pagingConfig));
  }

  public void list() {
    fireEvent(new ListEvent());
  }

  /**
   * После вызова данного метода в состоянии {@code SELECTED},
   * предполагается сброс сотояния вручную:<br>
   * {@code placeController.goTo(new JepViewListPlace());}. 
   */
  public void refreshList() {
    fireEvent(new RefreshListEvent());
  }
  
  public void refreshFields() {
    fireEvent(new RefreshFieldsEvent());
  }

  public void save() {
    fireEvent(new SaveEvent());
  }

  public void saveSearchTemplate(JepRecord searchTemplate) {
    fireEvent(new SaveSearchTemplateEvent(searchTemplate));
  }

  public void doDelete() {
    fireEvent(new DoDeleteEvent());
  }

  public void delete(JepRecord record) {
    fireEvent(new DeleteEvent(record));
  }

  public void adjustExitScope() {
    fireEvent(new AdjustExitScopeEvent());
  }

  public void showExcel() {
    fireEvent(new ShowExcelEvent(null, null));
  }
  
  public void showExcel(String fileName, String excelServlet) {
    fireEvent(new ShowExcelEvent(fileName, excelServlet));
  }

  public void prepareReport(JepReportParameters reportParameters, String reportServlet) {
    fireEvent(new PrepareReportEvent(reportParameters, reportServlet));
  }

  public void showHelp() {
    fireEvent(new ShowHelpEvent());
  }

  public void doGetRecord(PagingConfig pagingConfig) {
    fireEvent(new DoGetRecordEvent(pagingConfig));
  }

  public void exitScope() {
    fireEvent(new ExitScopeEvent());
  }
  
  public void setSaveButtonEnabled(boolean enabled) {
    fireEvent(new SetSaveButtonEnabledEvent(enabled));
  }
}
