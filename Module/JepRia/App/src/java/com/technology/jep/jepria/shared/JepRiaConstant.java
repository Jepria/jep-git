package com.technology.jep.jepria.shared;

public class JepRiaConstant {
	
	/**
	 * Язык, который считается основным для пользователей.
	 */
	public static final String LOCAL_LANG = "ru";

	/**
	 * Целочисленная величина, значение которой считаем неопределенным.
	 */
	public static final int UNDEFINED_INT = Integer.MIN_VALUE;
	
	/**
	 * Формат даты по умолчанию.
	 */
	public static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";
	
	/**
	 * Формат времени по умолчанию.
	 */
	public static String DEFAULT_TIME_FORMAT = "HH:mm:ss";
	
	/**
	 * Сокращённый формат времени.
	 */
	public static String SHORT_TIME_FORMAT = "HH:mm";
	
	/**
	 * Формат десятичных чисел по умолчанию.
	 */
	public static final String DEFAULT_DECIMAL_FORMAT = "###,###,##0.00";
	
	/**
	 * Ограничение на возвращаемое количество записей, в случае, если пользователь не указал количество записей явно.
	 */
	public static final Integer DEFAULT_MAX_ROW_COUNT = 100;

	public static final String JEP_USER_NAME_FIELD_NAME = "userName";
	public static final String JEP_USER_ROLES_FIELD_NAME = "userRoles";
	
	public static final String DOWNLOAD_FIELD_NAME_REQUEST_PARAMETER = "fieldName";
	public static final String DOWNLOAD_RECORD_KEY_REQUEST_PARAMETER = "recordKey";
	public static final String DOWNLOAD_MIME_TYPE_REQUEST_PARAMETER = "mimeType";
	public static final String DOWNLOAD_EXTENSION_REQUEST_PARAMETER = "ext";
	public static final String DOWNLOAD_CONTENT_DISPOSITION_REQUEST_PARAMETER = "contentDisposition";
	public static final String DOWNLOAD_CONTENT_DISPOSITION_INLINE_REQUEST_PARAMETER_VALUE = "inline";
	public static final String DOWNLOAD_CONTENT_DISPOSITION_ATTACHMENT_REQUEST_PARAMETER_VALUE = "attachment";
	public static final String DOWNLOAD_FILE_NAME_REQUEST_PARAMETER = "fileName";
	public static final String DOWNLOAD_FILE_NAME_PREFIX_REQUEST_PARAMETER = "fileNamePrefix";
	
	/**
	 * Имя параметра запроса, в котором передается значение идентификатора выбранного пользователем набора данных
	 * {@link com.technology.jep.jepria.client.widget.list.ListManager#uid}.
	 */
	public static final String LIST_UID_REQUEST_PARAMETER = "listUID";
	
	/**
	 * Имя параметра запроса, в котором передаётся имя выгружаемого Excel-файла.
	 */
	public static final String EXCEL_FILE_NAME_PARAMETER = "fileName";
	
	/**
	 * Имя выгружаемого Excel-файла по умолчанию.
	 */
	public static final String EXCEL_DEFAULT_FILE_NAME = "excelReport.xls";
	
	/**
	 * Относительный путь к Excel-сервлету по умолчанию.
	 */
	public static final String EXCEL_DEFAULT_SERVLET = "showExcel";
	
	/**
	 * Имя cookie, являющегося признаком прошедшей интеграции с SSO OC4J для WebLogic
	 */
	public static final String ORA_OC4J_SSO_COOKIE_NAME = "ORA_OC4J_SSO";
	
	/**
	 * Константы, предначенные для именования cookie, отражающих состояние интеграции SSO WebLogic и OC4J
	 */
	public static final String ORA_WL_INTEGRATION_SSO_COOKIE_NAME_PREFIX = "ORA_WL_SSO_INTEGRATION";
	public static final String ORA_WL_INTEGRATION_SSO_COOKIE_NAME_OC4J_SUFFIX = "OC4J";
	public static final String ORA_WL_INTEGRATION_SSO_COOKIE_NAME_WEBLOGIC_SUFFIX = "WEBLOGIC";
	public static final String ORA_WL_INTEGRATION_SSO_COOKIE_NAME_WEBLOGIC = ORA_WL_INTEGRATION_SSO_COOKIE_NAME_PREFIX + "_" + ORA_WL_INTEGRATION_SSO_COOKIE_NAME_WEBLOGIC_SUFFIX;
	public static final String ORA_WL_INTEGRATION_SSO_COOKIE_NAME_OC4J = ORA_WL_INTEGRATION_SSO_COOKIE_NAME_PREFIX + "_" + ORA_WL_INTEGRATION_SSO_COOKIE_NAME_OC4J_SUFFIX;
	
	/**
	 * Наименование скрытого поля компонента LargeField, предназначенного для хранения информации о первичном ключе и его значении
	 */
	public static final String PRIMARY_KEY_HIDDEN_FIELD_NAME = "primaryKey";
	
	/**
	 * Наименование скрытого поля компонента LargeField, предназначенного для хранения информации о максимально допустимом размере загружаемого файла
	 */
	public static final String FILE_SIZE_HIDDEN_FIELD_NAME = "fileSize";
}
