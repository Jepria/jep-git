package com.technology.jep.jepriareport.server.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

import com.technology.jep.jepriareport.server.JepReportConstant;

public class PdfServlet extends BaseHttpServlet {
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		super.service(request, response);
		
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
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);

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
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);

			OutputStream ouputStream = response.getOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);

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
