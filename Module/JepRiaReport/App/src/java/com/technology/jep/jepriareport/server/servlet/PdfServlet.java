package com.technology.jep.jepriareport.server.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.technology.jep.jepriareport.server.JepReportConstant;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

public class PdfServlet extends BaseHttpServlet {
  
  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    
    List<JasperPrint> jasperPrintList = BaseHttpServlet.getJasperPrintList(request);

    if (jasperPrintList == null) {
      throw new ServletException(
          "No JasperPrint documents found on the HTTP session.");
    }
    response.setContentType("application/pdf");
    response.setHeader("Cache-Control", "cache"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // prevents caching at the proxy server
    response.setDateHeader("Last-Modified", System.currentTimeMillis());

    String reportNameParameter = 
        request.getParameter(JepReportConstant.DOWNLOAD_FILE_NAME_PARAMETER) != null ? 
            request.getParameter(JepReportConstant.DOWNLOAD_FILE_NAME_PARAMETER) : 
                "report";
    String attachmentFileName = 
        reportNameParameter.indexOf(".") >= 0 ? 
          reportNameParameter.substring(0, reportNameParameter.indexOf(".") - 1) : 
            reportNameParameter;
    response.setHeader("Content-disposition", "attachment; filename=" + attachmentFileName + ".pdf");
    
    Boolean isBuffered = Boolean.valueOf(request.getParameter(BaseHttpServlet.BUFFERED_OUTPUT_REQUEST_PARAMETER));
    if (isBuffered.booleanValue()) {
      FileBufferedOutputStream fbos = new FileBufferedOutputStream();
      JRPdfExporter exporter = new JRPdfExporter();
      exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
      exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos));
      SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
      configuration.setCreatingBatchModeBookmarks(true);
      exporter.setConfiguration(configuration);

      try {
        exporter.exportReport();
        fbos.close();

        if (fbos.size() > 0) {
          response.setContentLength(fbos.size());
          ServletOutputStream ouputStream = response.getOutputStream();

          try {
            fbos.writeData(ouputStream);
            fbos.dispose();
            ouputStream.flush();
          } finally {
            if (ouputStream != null) {
              try {
                ouputStream.close();
              } catch (IOException ex) {
              }
            }
          }
        }
      } catch (JRException e) {
        throw new ServletException(e);
      } finally {
        fbos.close();
        fbos.dispose();
      }
    } else {
      JRPdfExporter exporter = new JRPdfExporter();
      exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));

      OutputStream ouputStream = response.getOutputStream();
      exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ouputStream));
      SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
      configuration.setCreatingBatchModeBookmarks(true);
      exporter.setConfiguration(configuration);

      try {
        exporter.exportReport();
      } catch (JRException e) {
        throw new ServletException(e);
      } finally {
        if (ouputStream != null) {
          try {
            ouputStream.close();
          } catch (IOException ex) {
          }
        }
      }
    }
  }

}
