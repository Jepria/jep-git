package com.technology.jep.jepriareport.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import com.technology.jep.jepria.shared.exceptions.SystemException;

@SuppressWarnings("serial")
public class HtmlServlet extends HttpServlet
{
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		try
		{
			JasperPrint jasperPrint = (JasperPrint) request.getSession().getAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
			JRHtmlExporter htmlExporter = new JRHtmlExporter();
			htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);			
		    response.setContentType("text/html; charset=UTF-8");       
		    
			PrintWriter out = response.getWriter();

			htmlExporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
			htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "../servlets/image?image=");
			
			htmlExporter.exportReport();
		}
		catch (JRException jrex)
		{
			throw new SystemException("HtmlServlet error", jrex);
		}			
	}


}
