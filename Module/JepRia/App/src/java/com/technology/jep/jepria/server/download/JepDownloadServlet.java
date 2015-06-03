package com.technology.jep.jepria.server.download;

import static com.technology.jep.jepria.server.JepRiaServerConstant.BINARY_FILE_DOWNLOAD_BEAN_JNDI_NAME;
import static com.technology.jep.jepria.server.JepRiaServerConstant.TEXT_FILE_DOWNLOAD_BEAN_JNDI_NAME;
import static com.technology.jep.jepria.server.download.clob.FileDownloadReader.DEFAULT_ENCODING;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION_ATTACHMENT_REQUEST_PARAMETER_VALUE;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION_INLINE_REQUEST_PARAMETER_VALUE;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION_REQUEST_PARAMETER;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_EXTENSION_REQUEST_PARAMETER;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_FIELD_NAME_REQUEST_PARAMETER;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_FILE_NAME_PREFIX_REQUEST_PARAMETER;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_FILE_NAME_REQUEST_PARAMETER;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_MIME_TYPE_REQUEST_PARAMETER;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_RECORD_KEY_REQUEST_PARAMETER;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.BINARY_FILE;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.TEXT_FILE;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.server.download.blob.BinaryFileDownloadLocal;
import com.technology.jep.jepria.server.download.blob.FileDownloadStream;
import com.technology.jep.jepria.server.download.clob.FileDownloadReader;
import com.technology.jep.jepria.server.download.clob.TextFileDownloadLocal;
import com.technology.jep.jepria.server.util.JepServerUtil;
import com.technology.jep.jepria.shared.exceptions.UnsupportedException;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.lob.JepLobRecordDefinition;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

@SuppressWarnings("serial")
public class JepDownloadServlet extends HttpServlet {
	/**
	 * Логгер.
	 */
	protected static Logger logger = Logger.getLogger(JepDownloadServlet.class.getName());	
	/**
	 * Определение записи.
	 */
	private JepLobRecordDefinition fileRecordDefinition = null;
	/**
	 * JNDI-имя источника данных.
	 */
	private String dataSourceJndiName;
	/**
	 * Имя текстовых ресурсов.
	 */
	private String resourceBundleName;
	/**
	 * Кодировка текстовых файлов.
	 */
	private final Charset textFileCharset;
	
	/**
	 * Создаёт сервлет для загрузки файлов с сервера.
	 * @param fileRecordDefinition определение записи
	 * @param dataSourceJndiName JNDI-имя источника данных
	 * @param resourceBundleName имя текстовых ресурсов
	 * @param textFileCharset кодировка текстовых файлов
	 */
	public JepDownloadServlet(
		JepLobRecordDefinition fileRecordDefinition,
		String dataSourceJndiName,
		String resourceBundleName,
		Charset textFileCharset) {
		
		this.fileRecordDefinition = fileRecordDefinition;
		this.dataSourceJndiName = dataSourceJndiName;
		this.resourceBundleName = resourceBundleName;
		this.textFileCharset = textFileCharset;
	}

	/**
	 * Создаёт сервлет для загрузки файлов с сервера.<br/>
	 * Для текстовых файлов используется кодировка по умолчанию - UTF-8.
	 * @param fileRecordDefinition определение записи
	 * @param dataSourceJndiName JNDI-имя источника данных
	 * @param resourceBundleName имя текстовых ресурсов
	 */
	public JepDownloadServlet(
		JepLobRecordDefinition fileRecordDefinition,
		String dataSourceJndiName,
		String resourceBundleName){
		
		this(fileRecordDefinition, dataSourceJndiName, resourceBundleName, DEFAULT_ENCODING);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.debug("doGet() request.getQueryString() = " + request.getQueryString());
		
		try {
			response.reset();
			String mimeType = request.getParameter(DOWNLOAD_MIME_TYPE_REQUEST_PARAMETER);
				response.setContentType(mimeType + ";charset=" + textFileCharset);
				
				addAntiCachingHeaders(response);
				
				String recordKey = request.getParameter(DOWNLOAD_RECORD_KEY_REQUEST_PARAMETER);
			String contentDisposition = request.getParameter(DOWNLOAD_CONTENT_DISPOSITION_REQUEST_PARAMETER);
			if(DOWNLOAD_CONTENT_DISPOSITION_INLINE_REQUEST_PARAMETER_VALUE.equals(contentDisposition)){
				response.setHeader("Content-disposition", "inline");
			} else if(DOWNLOAD_CONTENT_DISPOSITION_ATTACHMENT_REQUEST_PARAMETER_VALUE.equals(contentDisposition)){
				setAttachedFileName(response, request, recordKey);
			}
						
			String fieldName = request.getParameter(DOWNLOAD_FIELD_NAME_REQUEST_PARAMETER);
			String fileFieldName = fileRecordDefinition.getFieldMap().get(fieldName);
			JepTypeEnum fileFieldType = fileRecordDefinition.getTypeMap().get(fieldName);
			String tableName = fileRecordDefinition.getTableName();
			ServletOutputStream outputStream = response.getOutputStream();
			
			if(fileFieldType == BINARY_FILE) {
				downloadBinary(outputStream,
					tableName,
					fileFieldName,
					recordKey);
			} else if(fileFieldType == TEXT_FILE){
				downloadText(outputStream,
					tableName,
					fileFieldName,
					recordKey);
			} else {
				throw new UnsupportedException(this.getClass() + ".doGet(): " + fileFieldType + " field type does not supported for download.");
			}
		} catch (Throwable th) {
			logger.error("doGet() threw exception: ", th);
			onError(response,
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				"doGet(): " + "Download error: " + th.getMessage());
		}
	}

	/**
	 * Установка имени выгружаемого файла (в случае, если файл выгружается как вложение).
	 * @param response ответ сервлета
	 * @param request запрос к сервлету
	 * @param recordKey ключ записи
	 * @throws UnsupportedEncodingException 
	 */
	protected void setAttachedFileName(HttpServletResponse response,
			HttpServletRequest request, String recordKey) throws UnsupportedEncodingException {
		String fileName = request.getParameter(DOWNLOAD_FILE_NAME_REQUEST_PARAMETER);
		String fileNamePrefix = request.getParameter(DOWNLOAD_FILE_NAME_PREFIX_REQUEST_PARAMETER);
		String fileExtension = request.getParameter(DOWNLOAD_EXTENSION_REQUEST_PARAMETER);
		String downloadFileName;
		if(!JepRiaUtil.isEmpty(fileName)){
			downloadFileName = fileName;
		} else if(!JepRiaUtil.isEmpty(fileNamePrefix)){
			downloadFileName = fileNamePrefix + "_" + recordKey;
		} else {
			downloadFileName = recordKey;
		}
		
		if(!JepRiaUtil.isEmpty(fileExtension)){
			fileExtension = "." + fileExtension;
		} else {
			fileExtension = "";
		}
		
		response.setHeader("Content-disposition", "attachment; filename=\"" + URLEncoder.encode(downloadFileName, textFileCharset + "") + fileExtension + "\"");
	}

	/**
	 * Добавление &quot;типового набора&quot; заголовков для борьбы с кэшированием в разных браузерах.
	 * @param response ответ сервлета
	 */
	private static void addAntiCachingHeaders(HttpServletResponse response) {
		response.setHeader("Cache-Control", "cache"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0);
		response.setDateHeader("Last-Modified", System.currentTimeMillis());
	}

	/**
	 * Загрузка бинарного файла из базы данных.
	 * @param outputStream выходной поток
	 * @param tableName имя таблицы
	 * @param fileFieldName имя поля в таблице
	 * @param recordKey ключ записи
	 * @throws IOException
	 * @throws NamingException
	 */
	private void downloadBinary(
		OutputStream outputStream
		, String tableName
		, String fileFieldName
		, String recordKey
		) throws IOException, NamingException {
		
		FileDownloadStream.downloadFile(
			outputStream,
			(BinaryFileDownloadLocal) JepServerUtil.ejbLookup(BINARY_FILE_DOWNLOAD_BEAN_JNDI_NAME),
			tableName,
			fileFieldName,
			fileRecordDefinition.getKeyFieldName(),
			recordKey,
			this.dataSourceJndiName,
			this.resourceBundleName);
	}

	/**
	 * Загрузка текстового файла из базы данных.
	 * @param outputStream выходной поток
	 * @param tableName имя таблицы
	 * @param fileFieldName имя поля
	 * @param recordKey ключ записи
	 * @throws IOException
	 * @throws NamingException
	 */
	private void downloadText(
		OutputStream outputStream
		, String tableName
		, String fileFieldName
		, String recordKey
		) throws IOException, NamingException {
		
		FileDownloadReader.downloadFile(
			outputStream,
			(TextFileDownloadLocal) JepServerUtil.ejbLookup(TEXT_FILE_DOWNLOAD_BEAN_JNDI_NAME),
			tableName,
			fileFieldName,
			fileRecordDefinition.getKeyFieldName(),
			recordKey,
			this.dataSourceJndiName,
			this.resourceBundleName,
			textFileCharset);
	}
	
	/**
	 * Отправка сообщения об ошибке в случае ошибки при загрузке файла.<br/>
	 * При необходимости данный метод может быть переопределён в классе-наследнике.
	 * @param response результат работы сервлета (ответ)
	 * @param error HTTP-код ошибки
	 * @param message текст сообщения об ошибке
	 * @throws IOException
	 */
	protected void onError(HttpServletResponse response, int error, String message) throws IOException {
		logger.error(message);		
		response.sendError(error, message);
	}
}
