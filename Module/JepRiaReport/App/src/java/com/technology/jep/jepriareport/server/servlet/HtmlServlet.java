package com.technology.jep.jepriareport.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.technology.jep.jepria.shared.exceptions.SystemException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

@SuppressWarnings("serial")
public class HtmlServlet extends HttpServlet {
  
  @Override
  public void service(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    try {
      JasperPrint jasperPrint = (JasperPrint) request.getSession().getAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
      HtmlExporter htmlExporter = new HtmlExporter();
      htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
      
      request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
      response.setContentType("text/html; charset=UTF-8");

      htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));

      htmlExporter.exportReport();
    } catch (JRException jrex) {
      throw new SystemException("HtmlServlet error", jrex);
    }
  }

}