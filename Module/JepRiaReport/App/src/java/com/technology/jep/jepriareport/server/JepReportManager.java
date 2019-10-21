package com.technology.jep.jepriareport.server;

import static com.technology.jep.jepriareport.server.JepReportConstant.LIST_IN_FILES_MODE;
import static com.technology.jep.jepriareport.server.JepReportConstant.LIST_IN_MEMORY_MODE;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JRAbstractLRUVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.fill.JRTemplatePrintFrame;
import net.sf.jasperreports.engine.fill.JRTemplatePrintRectangle;
import net.sf.jasperreports.engine.fill.JRTemplatePrintText;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.report.JepReportParameters;
import com.technology.jep.jepriareport.server.large.JepLargeReportDataSource;

/**
 * Модуль поддержки построения отчётов Jep
 */
public class JepReportManager {

  /**
   * Подготовка отчёта для печатной (несписочной) формы в оперативной памяти
   * 
   * @param request Http-запрос
   * @param applicationParameters параметры бизнес-модуля
   * @param reportDefinitionPath месторасположение шаблона отчёта
   * @throws JRException
   * @throws ApplicationException
   */
  public static void preparePrintForm(
      HttpServletRequest request,
      JepReportParameters applicationParameters,
      String reportDefinitionPath) throws JRException, ApplicationException {
    
    JasperPrint jasperPrint = fillReport(request, applicationParameters, reportDefinitionPath, null);
    
    request.getSession().setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
  }
  
  /**
   * Подготовка отчета и сохранения в сессионной переменной с передачей подключения к бд
   * @param request
   * @param applicationParameters
   * @param reportDefinitionPath
   * @param conn
   * @throws JRException
   * @throws ApplicationException
   */
  
  public static void preparePrintForm(HttpServletRequest request,
      JepReportParameters applicationParameters,
      String reportDefinitionPath, Connection conn) throws JRException, ApplicationException {
    
    JasperPrint jasperPrint = fillReport(request, applicationParameters, reportDefinitionPath, conn);
    
    request.getSession().setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
    
  }

  /**
   * ffill jasper report
   * @param request
   * @param applicationParameters
   * @param reportDefinitionPath
   * @return JasperPrint
   * @throws JRException
   * @throws ApplicationException
   */
  private static JasperPrint fillReport(HttpServletRequest request,
      JepReportParameters applicationParameters,
      String reportDefinitionPath, Connection conn) throws JRException, ApplicationException {
    HttpSession session = request.getSession();
    ServletContext servletContext = session.getServletContext();
    String realPath = servletContext.getRealPath(reportDefinitionPath);
    JasperReport jasperReport = getJasperReportCompiled(request, realPath);
    
    Map<String, Object> parameters = fillStandardParameters(request.getLocale());
    parameters = fillApplicationParameters(parameters, applicationParameters);

    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
    
    removeBlankPage(jasperPrint.getPages());  // Удаление последней пустой страницы (обход возможных баг)
    
    return jasperPrint;
  }

  /**
   * Подготовка отчёта для списочных форм в оперативной памяти
   * 
   * @param request Http-запрос
   * @param applicationParameters параметры бизнес-модуля
   * @param reportDefinitionPath месторасположение шаблона отчёта
   * @param resultRecords список результирующих записей отчёта
   * @throws JRException
   * @throws ApplicationException
   */
  public static void prepareReport(
      HttpServletRequest request,
      JepReportParameters applicationParameters,
      String reportDefinitionPath,
      List<JepRecord> resultRecords) throws JRException, ApplicationException {
    Locale locale = request.getLocale();
    JepReportRecords reportRecords = new JepReportRecords(resultRecords);
    prepareReport(request,
            applicationParameters,
            locale,
            request.getSession().getServletContext().getRealPath(reportDefinitionPath),
            new JepReportDataSource(reportRecords),
            JepReportConstant.DEFAULT_MODE);
  }

  /**
   * Подготовка отчёта для списочных форм с указанием способа построения
   * отчёта: в оперативной памяти или с использованием файловой системы
   * 
   * @param request Http-запрос
   * @param applicationParameters параметры бизнес-модуля
   * @param reportDefinitionPath месторасположение шаблона отчёта
   * @param reportTitleKey ключ названия отчёта
   * @param resultRecords список результирующих записей отчёта
   * @param useFiles если true, то отчёты строятся с использование файловой системы, если false - в оперативной памяти
   * @throws JRException
   * @throws ApplicationException
   */
  public static void prepareReport(
      HttpServletRequest request,
      JepReportParameters applicationParameters,
      String reportDefinitionPath,
      List<JepRecord> resultRecords,
      boolean useFiles
  ) throws JRException, ApplicationException {
    JepReportRecords reportRecords = new JepReportRecords(resultRecords);
    prepareReport(request,
            applicationParameters,
            request.getLocale(),
            request.getSession().getServletContext().getRealPath(reportDefinitionPath),
            new JepReportDataSource(reportRecords),
            useFiles ? JepReportConstant.LIST_IN_FILES_MODE : JepReportConstant.LIST_IN_MEMORY_MODE);
  }

  /**
   * Подготовка большого отчёта для списочных форм с использованием файловой системы и специального источника данных
   * 
   * @param request Http-запрос
   * @param  applicationParameters параметры бизнес-модуля
   * @param reportDefinitionPath месторасположение шаблона отчёта
   * @param beanJndiName JNDI-имя EJB-модуля поддержки большого отчёта, реализующего интерфейс JepLargeReport
   * @throws JRException
   * @throws ApplicationException
   */
  public static void prepareLargeReport(
      HttpServletRequest request,
      JepReportParameters applicationParameters,
      String reportDefinitionPath,
      String beanJndiName
  ) throws JRException, ApplicationException {
    Locale locale = request.getLocale();
    
    prepareReport(request,
      applicationParameters,
      locale,
      request.getSession().getServletContext().getRealPath(reportDefinitionPath),
      new JepLargeReportDataSource(beanJndiName, applicationParameters), JepReportConstant.LIST_IN_FILES_MODE);
  }


  /**
   * Подготовка отчёта (компиляция шаблона, если нужно, и заполнение его данными)
   * Результирующий объект типа JasperPrint помещается в сессию для последующей обработки процедурами экспорта.
   * 
   * @param request Http-запрос
   * @param  applicationParameters параметры бизнес-модуля
   * @param locale Локаль
   * @param reportDefinitionPath месторасположение шаблона отчёта
   * @param reportTitle название отчёта
   * @param jrDataSource источник данных
   * @param mode режим построения отчёта (в памяти или с использованием файловой системы)
   * @throws JRException
   * @throws ApplicationException
   */
  private static void prepareReport (
      HttpServletRequest request,
      JepReportParameters applicationParameters,
      Locale locale,
      String reportDefinitionPath,
      JRDataSource jrDataSource,
      int mode
  ) throws JRException, ApplicationException {
    // Получение скомпилированного определения отчёта
    JasperReport jasperReport = getJasperReportCompiled(request, reportDefinitionPath);

    // Заполнение отчёта
    Map<String, Object> parameters = fillStandardParameters(locale);
    parameters = fillApplicationParameters(parameters, applicationParameters);
    
    JasperPrint jasperPrint = null;
    switch(mode) {
    case LIST_IN_MEMORY_MODE:
      jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrDataSource);
      break;
    case LIST_IN_FILES_MODE:
      // Создание виртуализатора
      JRSwapFile swapFile = new JRSwapFile("", 1024, 1024);
      JRAbstractLRUVirtualizer virtualizer = new JRSwapFileVirtualizer(2, swapFile, true);
      parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);  
      File reportDefinitionResultFile = getJasperReportFile(request, reportDefinitionPath);
      String srcFileName = reportDefinitionResultFile.getPath();
      jasperPrint = JasperFillManager.fillReport(srcFileName, parameters, jrDataSource);
      
      removeBlankPage(jasperPrint.getPages());
      
      if (virtualizer != null)
      {
        virtualizer.setReadOnly(true);      
      }
      break;
    default:
      throw new UnsupportedOperationException("Unknown report creation mode: " + mode);
    }
    
    // Экспорт сервлетом
    request.getSession().setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
  }

  /**
   * Получение скомпилированного определения отчёта
   * 
   * @param request Http-запрос
   * @param reportDefinitionPath местонахождение определения отчёта
   * @return скомпилированное определение отчёта
   * @throws JRException
   * @throws ApplicationException
   */
  private static JasperReport getJasperReportCompiled(HttpServletRequest request, String reportDefinitionPath) throws JRException, ApplicationException {
    JasperReport jasperReport = null;
    File reportDefinitionResultFile = getJasperReportFile(request, reportDefinitionPath);
      if (reportDefinitionResultFile.exists()) {
        jasperReport = (JasperReport)JRLoader.loadObject(new File(reportDefinitionResultFile.getPath()));          
      } else {
      throw new ApplicationException("File '" + reportDefinitionResultFile.getAbsolutePath() + "' not found.", null);
      }
    return jasperReport;
  }

  /**
   * Получение пути к скомпилированному определению отчёта
   * 
   * @param request Http-запрос
   * @param reportDefinitionPath местонахождение определения отчёта
   * @return файл скомпилированного определения отчёта
   * @throws JRException
   * @throws ApplicationException
   */
  private static File getJasperReportFile(HttpServletRequest request, String reportDefinitionPath) throws JRException, ApplicationException {
    String reportDefinitionSourceName = reportDefinitionPath + ".jrxml";
    if(new File(reportDefinitionSourceName).exists()) {
      // Компилируем, если есть шаблон отчёта
      // Run-time компиляция используется главным образом для design-time-поддержки,
      // поэтому результат не кешируется в сессии. Это позволяет "на месте" обновлять шаблон отчёта и проверять
      // его в работе.
      JasperCompileManager.compileReportToFile(reportDefinitionSourceName);
    }
    String reportDefinitionResultName = reportDefinitionPath + ".jasper";
    return new File(reportDefinitionResultName);
  }
  

  /**
   * Задание стандартных параметров
   * 
   * @param reportTitle название отчёта
   * @param locale локаль
   * @return HashMap, заполненную стандартными параметрами
   */
  private static Map<String, Object> fillStandardParameters(Locale locale) {
    HashMap<String, Object> parameters = new HashMap<String, Object>();
    // Иначе будет использована серверная локаль (на момент запуска сервера) 
    parameters.put(JRParameter.REPORT_LOCALE, locale);
    return parameters;
  }

  /**
   * Задание параметров приложения
   * 
   * @param parameters контейнер всех параметров
   * @param applicationParameters параметры приложения
   * @return HashMap, дополненную параметрами
   */
  private static Map<String, Object> fillApplicationParameters(Map<String, Object> parameters, JepReportParameters applicationParameters) {
    if(applicationParameters != null) {
      Map<String, Object> parameterMap = applicationParameters.getParameterMap();
      Set<String> keys = parameterMap.keySet();
      for(String key : keys) {
        parameters.put(key, applicationParameters.getParameter(key));
      }
    }
    return parameters;
  }
  
  private static void removeBlankPage(List<JRPrintPage> pages) {
    for (Iterator<JRPrintPage> i = pages.iterator(); i.hasNext();) {
      JRPrintPage page = i.next();
      List<JRPrintElement> elements = page.getElements(); 
      int size = elements.size(); 
      Object element = null;
      switch(size) {
      case 0:
        i.remove();
        break;
      case 1:
        element = elements.get(0);
        if(element instanceof JRTemplatePrintText) {  // Проверка на background (key верхнего элемента Background должен иметь значение "Background")
          JRTemplatePrintText jrTemplatePrintText = (JRTemplatePrintText) element;
          if("Background".equals(jrTemplatePrintText.getKey())) {
            i.remove();
          }
        } else if (element instanceof JRTemplatePrintFrame) {
          JRTemplatePrintFrame jrTemplatePrintFrame = (JRTemplatePrintFrame) element;
          if(jrTemplatePrintFrame.getElements().size() == 0) {
            i.remove();
          }
        }
        break;
      case 2:
        element = elements.get(0);
        if(element instanceof JRTemplatePrintRectangle) {  // Проверка на background (key верхнего элемента Background должен иметь значение "Background")
          element = elements.get(1);
          if(element instanceof JRTemplatePrintText) {  // Проверка на background (key верхнего элемента Background должен иметь значение "Background")
            JRTemplatePrintText jrTemplatePrintText = (JRTemplatePrintText) element;
            if("Background".equals(jrTemplatePrintText.getKey())) {
              i.remove();
            }
          }
        } else if (element instanceof JRTemplatePrintFrame) {
          JRTemplatePrintFrame jrTemplatePrintFrame = (JRTemplatePrintFrame) element;
          if(jrTemplatePrintFrame.getElements().size() == 0) {
            element = elements.get(1);
            if (element instanceof JRTemplatePrintFrame) {
              jrTemplatePrintFrame = (JRTemplatePrintFrame) element;
              if(jrTemplatePrintFrame.getElements().size() == 0) {
                i.remove();
              }
            }
          }
        }
        break;
      }
    }
  }
}
