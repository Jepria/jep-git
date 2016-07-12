package com.technology.jep.jepria.server.download.excel;

import static com.technology.jep.jepria.server.JepRiaServerConstant.EXCEL_REPORT_FIELDS_SESSION_ATTRIBUTE;
import static com.technology.jep.jepria.server.JepRiaServerConstant.EXCEL_REPORT_HEADERS_SESSION_ATTRIBUTE;
import static com.technology.jep.jepria.server.JepRiaServerConstant.FOUND_RECORDS_SESSION_ATTRIBUTE;
import static com.technology.jep.jepria.server.JepRiaServerConstant.SELECTED_RECORDS_SESSION_ATTRIBUTE;
import static com.technology.jep.jepria.shared.JepRiaConstant.EXCEL_DEFAULT_FILE_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.EXCEL_FILE_NAME_PARAMETER;
import static com.technology.jep.jepria.shared.JepRiaConstant.LIST_UID_REQUEST_PARAMETER;
import static com.technology.jep.jepria.shared.util.JepRiaUtil.isEmpty;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;

/**
 * Сервлет для отображения набора данных в Excel.<br/>
 * Для использования в прикладном модуле необходимо:
 * <ul>
 *   <li>унаследовать в прикладном модуле сервлет от данного класса вызвав в <code>public</code> конструкторе без параметров конструктор данного
 *   класса {@link #ExcelServlet(JepRecordDefinition recordDefinition)} с указанием 
 *   {@link com.technology.jep.jepria.shared.record.JepRecordDefinition определения записи}. Пример:
 *     <pre>
 *       ...
 *       public PrintActExcelServlet() {
 *         super(PartnerActRecordDefinition.instance);
 *       }
 *       ...
 *     </pre>
 *   </li>
 *   <li>при необходимости переопределить <code>protected</code>-метод {@link #createExcelReport(List, List, List)}.
 *   </li>
 *   <li>указать в <code>web.xml</code> определение для вызова прикладного сервлета. Пример:
 *     <pre>
 *       ...
 *       &lt;servlet&gt;
 *         &lt;servlet-name&gt;PrintActExcelServlet&lt;/servlet-name&gt;
 *         &lt;servlet-class&gt;com.technology.rfi.outofstaffasria.partneract.server.PrintActExcelServlet&lt;/servlet-class&gt;
 *       &lt;/servlet&gt;
 *       &lt;servlet-mapping&gt;
 *         &lt;servlet-name&gt;PrintActExcelServlet&lt;/servlet-name&gt;
 *         &lt;url-pattern&gt;/OutOfStaffAsRia/printActExcel&lt;/url-pattern&gt;
 *       &lt;/servlet-mapping&gt;
 *       ...
 *     </pre>
 *   </li>
 * </ul>
 * <b>Важно:</b> При добавлении новых полей необходимо внести их в 
 * {@link com.technology.jep.jepria.shared.record.JepRecordDefinition определение записи}.
 */
@SuppressWarnings("serial")
public class ExcelServlet extends HttpServlet {

  protected static final Logger logger = Logger.getLogger(ExcelServlet.class.getName());
  
  /**
   * Определение записи набора данных.
   */
  protected JepRecordDefinition recordDefinition = null;
  
  /**
   * Создает сервлет для отображения набора данных в Excel.<br/>
   * Конструктор вызывается с указанием {@link com.technology.jep.jepria.shared.record.JepRecordDefinition определения записи} в прикладных модулях 
   * из <code>public</code> конструктора без параметров.
   *
   * @param recordDefinition определение записи набора данных
   */
  public ExcelServlet(JepRecordDefinition recordDefinition) {
    this.recordDefinition = recordDefinition;    
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
    logger.trace("BEGIN Generate Excel Report");
    
    String listUIDParameter = request.getParameter(LIST_UID_REQUEST_PARAMETER);
    if (isEmpty(listUIDParameter)){
      response.setContentType("text/html;charset=UTF-8");
      response.getOutputStream().print("<b>Request parameter '" + LIST_UID_REQUEST_PARAMETER +"' is mandatory!</b>");
      return;
    }
    
    Integer listUID = Integer.valueOf(listUIDParameter);
    HttpSession session = request.getSession();
    
    String fileName = request.getParameter(EXCEL_FILE_NAME_PARAMETER);
    if (fileName == null) {
      fileName = EXCEL_DEFAULT_FILE_NAME;
    }
    logger.trace("fileName=" + fileName);
    
    @SuppressWarnings("unchecked")
    List<String> reportHeaders = (List<String>)session.getAttribute(EXCEL_REPORT_HEADERS_SESSION_ATTRIBUTE + listUID);
    logger.trace("reportHeaders = " + reportHeaders);
    
    @SuppressWarnings("unchecked")
    List<String> reportFields = (List<String>)session.getAttribute(EXCEL_REPORT_FIELDS_SESSION_ATTRIBUTE + listUID);
    logger.trace("reportFields = " + reportFields);

    @SuppressWarnings("unchecked")
    List<JepRecord> selectedRecords = (List<JepRecord>) session.getAttribute(SELECTED_RECORDS_SESSION_ATTRIBUTE + listUID);
    logger.trace("seletedRecords = " + selectedRecords);

    @SuppressWarnings("unchecked")
    List<JepRecord> resultRecords = (List<JepRecord>)session.getAttribute(FOUND_RECORDS_SESSION_ATTRIBUTE + listUID);
    logger.trace("resultRecords = " + resultRecords);    
    
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Cache-Control", "cache"); //HTTP 1.1
    response.setHeader("Pragma", "no-cache"); //HTTP 1.0
    response.setDateHeader("Expires", 0); //prevents caching at the proxy server
    response.setDateHeader("Last-Modified", System.currentTimeMillis());
    response.setContentType("application/vnd.ms-excel; charset=UTF-8");
    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    
    PrintWriter pw = response.getWriter();    
    
    try {
      ExcelReport report = createExcelReport(
        reportFields, 
        reportHeaders, 
        selectedRecords != null ? selectedRecords : resultRecords);
      report.print(pw);
      
      pw.flush();
      response.flushBuffer();
    }
    catch (Throwable th) {
      onError(response, th);
    }
    
    logger.trace("END Generate Excel Report");
  }

  /**
   * Фабричный метод, формирующий объект Excel-отчёта.<br/>
   * По умолчанию создаёт объект класса ExcelReport. Если в прикладном модуле для этих цедей
   * используется собственный класс, то данный метод необходимо переопределить.
   *
   * @param reportFields список идентификаторов полей для формирования выгрузки
   * @param reportHeaders список заголовков таблицы в Excel-файле
   * @param records спиок записей для выгрузки
   * @return объект Excel-отчёта
   */
  protected ExcelReport createExcelReport(List<String> reportFields, List<String> reportHeaders, List<JepRecord> records) {
    return new ExcelReport(recordDefinition, reportFields, reportHeaders, records);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }
  
  /**
   * Отправка сообщения об ошибке в случае её возникновения.<br/>
   * При необходимости данный метод может быть переопределён в классе-наследнике.
   * @param response результат работы сервлета (ответ)
   * @param th возникшее исключение
   * @throws IOException
   */
  protected void onError(HttpServletResponse response, Throwable th) throws IOException {
    logger.error(th.getMessage(), th);
    response.setContentType("text/html; charset=UTF-8");
    response.setHeader("Content-Disposition", "");
    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, th.getMessage());
  }
}
