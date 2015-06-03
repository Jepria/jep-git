package com.technology.jep.jepria.server;

import com.technology.jep.jepria.shared.JepRiaConstant;

public class JepRiaServerConstant extends JepRiaConstant {
	
	public static final String LOCALE_KEY = "com.technology.jep.jepria.server.LOCALE";
	
	public static final String JEP_RIA_RESOURCE_BUNDLE_NAME = "com.technology.jep.jepria.shared.text.JepRiaText";
	
	/**
	 * JNDI-имя источника данных модуля.
	 */
	public static final String DATA_SOURCE_JNDI_NAME = "jdbc/RFInfoDS";
	
	public static final String VERSION_BEAN_JNDI_NAME = "VersionBean";
	/**
	 * Имя параметра http-запроса: язык текущей межмодульной сессии
	 */
	public static final String HTTP_REQUEST_PARAMETER_LANG = "lang";

	/**
	 * Имя параметра http-запроса: локаль текущей межмодульной сессии
	 */
	public static final String HTTP_REQUEST_PARAMETER_LOCALE = "locale";
	
	public static final String BINARY_FILE_UPLOAD_BEAN_JNDI_NAME = "BinaryFileUploadBean";

	public static final String BINARY_FILE_DOWNLOAD_BEAN_JNDI_NAME = "BinaryFileDownloadBean";

	public static final String TEXT_FILE_UPLOAD_BEAN_JNDI_NAME = "TextFileUploadBean";
	
	public static final String TEXT_FILE_DOWNLOAD_BEAN_JNDI_NAME = "TextFileDownloadBean";
	
	/**
	 * Префикс имени аттрибута сессии, в котором сохраняется найденный набор записей. 
	 */
	public static final String FOUND_RECORDS_SESSION_ATTRIBUTE = "foundRecordsSessionAttribute";

	/**
	 * Префикс имени аттрибута сессии, в котором сохраняется выбранный набор записей. 
	 */
	public final static String SELECTED_RECORDS_SESSION_ATTRIBUTE = "selectedRecordsSessionAttribute";

	/**
	 * Префикс имени аттрибута сессии, в котором сохраняется список содержащий названия колонок Excel-отчета.
	 */
	public static final String EXCEL_REPORT_HEADERS_SESSION_ATTRIBUTE = "excelReportHeaders";

	/**
	 * Префикс имени аттрибута сессии, в котором сохраняется список содержащий идентификаторы полей, из которых брать данные для колонок Excel-отчета.
	 */
	public static final String EXCEL_REPORT_FIELDS_SESSION_ATTRIBUTE = "excelReportFields";
	
	/**
	 * Имя атрибута сессии, в котором хранится флаг автообновления.
	 */
	public static final String IS_REFRESH_NEEDED = "isRefreshNeeded";
	
	/**
	 * Суффикс, подставляемый в логин для указания того, что авторизация будет осуществлена по хэшу пароля
	 */
	public static final String LOGIN_SUFFIX_FOR_HASH_AUTHORIZATION = String.valueOf((char) 2).concat("hashed");
}
