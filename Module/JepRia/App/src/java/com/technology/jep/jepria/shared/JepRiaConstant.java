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
   * Формат даты по умолчанию.- только месяц и год
   */
  public static final String DEFAULT_DATE_MONTH_AND_YEAR_ONLY_FORMAT = "MM.yyyy";
  
  /**
   * Формат даты по умолчанию.- только год
   */
  public static final String DEFAULT_DATE_YEAR_ONLY_FORMAT = "yyyy";
  
  /**
   * Формат времени по умолчанию.
   */
  public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
  
  /**
   * Сокращённый формат времени.
   */
  public static final String SHORT_TIME_FORMAT = "HH:mm";
  
  /**
   * Формат десятичных чисел по умолчанию.
   */
  public static final String DEFAULT_DECIMAL_SEPARATOR = ".";
  public static final String DEFAULT_DECIMAL_FORMAT = "###,###,##0" + DEFAULT_DECIMAL_SEPARATOR + "00";
  
  /**
   * Ограничение на возвращаемое количество записей, в случае, если пользователь не указал количество записей явно.
   */
  public static final Integer DEFAULT_MAX_ROW_COUNT = 100;

  public static final String JEP_USER_NAME_FIELD_NAME = "userName";
  public static final String JEP_USER_ROLES_FIELD_NAME = "userRoles";
  
  public static final String DOWNLOAD_FIELD_NAME = "fieldName";
  public static final String DOWNLOAD_RECORD_KEY = "recordKey";
  public static final String DOWNLOAD_MIME_TYPE = "mimeType";
  public static final String DOWNLOAD_EXTENSION = "ext";
  public static final String DOWNLOAD_CONTENT_DISPOSITION = "contentDisposition";
  public static final String DOWNLOAD_CONTENT_DISPOSITION_INLINE = "inline";
  public static final String DOWNLOAD_CONTENT_DISPOSITION_ATTACHMENT = "attachment";
  public static final String DOWNLOAD_FILE_NAME = "fileName";
  public static final String DOWNLOAD_FILE_NAME_PREFIX = "fileNamePrefix";
  public static final String DOWNLOAD_ID = "downloadId";
  
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
   * Наименование скрытого поля компонента LargeField, предназначенного для хранения информации о первичном ключе и его значении
   */
  public static final String PRIMARY_KEY_HIDDEN_FIELD_NAME = "primaryKey";
  
  /**
   * Наименование скрытого поля компонента LargeField, предназначенного для хранения информации о максимально допустимом размере загружаемого файла
   */
  public static final String FILE_SIZE_HIDDEN_FIELD_NAME = "fileSize";
  
  /**
   * Наименование скрытого поля компонента LargeField, предназначенного для хранения информации о необходимости удаления файла
   */
  public static final String IS_DELETED_FILE_HIDDEN_FIELD_NAME = "isDeleted";
  
  /**
   * Имя параметра http-запроса: локаль текущей межмодульной сессии
   */
  public static final String HTTP_REQUEST_PARAMETER_LOCALE = "locale";
}
