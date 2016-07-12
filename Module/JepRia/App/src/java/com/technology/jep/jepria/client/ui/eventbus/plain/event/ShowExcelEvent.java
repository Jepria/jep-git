package com.technology.jep.jepria.client.ui.eventbus.plain.event;

import com.google.gwt.event.shared.EventHandler;
import com.technology.jep.jepria.client.ui.eventbus.ActionEvent;

public class ShowExcelEvent extends ActionEvent<ShowExcelEvent.Handler> {

  /**
   * Implemented by handlers of ShowExcelEvent.
   */
  public interface Handler extends EventHandler {
    /**
     * Called when a {@link ShowExcelEvent} is fired.
     * 
     * @param event
     *            the {@link ShowExcelEvent}
     */
    void onShowExcel(ShowExcelEvent event);
  }

  /**
   * A singleton instance of Type&lt;ShowExcelHandler&gt;.
   */
  public static final Type<Handler> TYPE = new Type<Handler>();

  /**
   * Относительный путь к сервлету Excel-выгрузки.
   */
  private String excelServlet;
  /**
   * Имя выгружаемого файла.
   */
  private String fileName;

  /**
   * Создаёт событие Excel-выгрузки.
   * @param fileName имя выгружаемого файла
   * @param excelServlet относительный путь до сервлета выгрузки
   */
  public ShowExcelEvent(String fileName, String excelServlet) {
    this.fileName = fileName;
    this.excelServlet = excelServlet;
  }

  /**
   * Возвращает относительный путь к сервлету Excel-выгрузки.
   * @return относительный путь к сервлету
   */
  public String getExcelServlet() {
    return excelServlet;
  }

  /**
   * Возвращает имя выгружаемого файла.
   * @return имя выгружаемого файла
   */
  public String getFileName() {
    return fileName;
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onShowExcel(this);
  }
}
