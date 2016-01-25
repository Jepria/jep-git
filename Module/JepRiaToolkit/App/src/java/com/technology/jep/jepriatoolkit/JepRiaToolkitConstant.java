package com.technology.jep.jepriatoolkit;

import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public interface JepRiaToolkitConstant {	
	
	/**
	 * Наименование конфигурационного файла сборки проекта
	 */
	String BUILD_FILE = "build.xml";
	
	/**
	 * Наименование папки библиотеки
	 */
	String LIB_DIR_NAME = "lib"; 
	
	/**
	 * Наименование директории для хранения структуры навигации проекта
	 */
	String NAVIGATION_MENU_DIR_NAME = "navigation/menu";
	
	/**
	 * Наименование директории для хранения текстовых ресурсов навигации
	 */
	String NAVIGATION_TEXT_DIR_NAME = "navigation/text";
	
	/**
	 * Наименование папки исходных кодов
	 */
	String SOURCE_DIR_NAME = "src"; 
	
	/**
	 * Префикс пути до папки с java-кодом
	 */
	String PREFIX_DESTINATION_JAVA_CODE = "src/java"; 
	/**
	 * Префикс пути до исходного кода
	 */
	String PREFIX_DESTINATION_SOURCE_CODE = PREFIX_DESTINATION_JAVA_CODE + "/com/technology/";
	
	/**
	 * Префикс пути до исходного кода
	 */
	String PREFIX_DESTINATION_RESOURCE = "src/resources/com/technology/";
	
	/**
	 * Путь до основной jsp и страницы приветствия
	 */
	String WELCOME_PAGE_DIR_NAME = "src/html";	
	
	/**
	 * Название файла-шаблона css
	 */
	String CSS_TEMPLATE_NAME = "template.css";	

	/**
	 * Название файла-шаблона css
	 */
	String JSP_TEMPLATE_NAME = "template.jsp";	

	/**
	 * Наименование Логгера
	 */
	String LOGGER_SERVLET_NAME = "remoteLoggerServiceImpl";
	
	/**
	 * Наименование полного имени класса Логгера, включая имя пакета
	 */
	String LOGGER_SERVLET_CLASS = "com.allen_sauer.gwt.log.server.RemoteLoggerServiceImpl";
	
	/**
	 * Наименование сервлета основного сервиса
	 */
	String MAIN_SERVICE_SERVLET_NAME = "MainServiceServlet";
	
	/**
	 * Наименование полного имени класса сервлета основного сервиса
	 */
	String MAIN_SERVICE_SERVLET_CLASS = "com.technology.jep.jepria.server.service.JepMainServiceServlet";
	
	/**
	 * Наименование сервлета, отвечающего за версионность приложений
	 */
	String VERSION_SERVLET_NAME = "VersionServlet";
	
	/**
	 * Наименование полного имени класса сервлета, отвечающего за версионность приложений
	 */
	String VERSION_SERVLET_CLASS = "com.technology.jep.jepria.server.version.VersionServlet";
	
	/**
	 * Наименование свойства для таска productionBuildChecker, хранящее в себе описание ошибки, возникаемой во время продукционной сборки проекта
	 */
	String PRODUCTION_BUILD_CHECKER_ERROR = "productionBuildCheckerError";
	
	/**
	 * Открывающая скобка
	 */
	String LEFT_BRACKET = "(";
	
	/**
	 * Закрывающая скобка
	 */
	String RIGHT_BRACKET = ")";
		
	/**
	 * Разделитель форм
	 */
	String SEPARATOR = ",";
	
	/**
	 * Разделитель в пути
	 */
	String PATH_SEPARATOR = "/";
	
	/**
	 * Идентифицирующий атрибут элемента
	 */
	String ID_ATTRIBUTE = "id";
		
	/**
	 * Атрибут наименования элемента
	 */
	String NAME_ATTRIBUTE = "name";
	
	/**
	 * Атрибут англоязычного наименования элемента
	 */
	String NAME_EN_ATTRIBUTE = "nameEn";
	
	/**
	 * Атрибут значения элемента
	 */
	String VALUE_ATTRIBUTE = "value";
	
	/**
	 * Атрибут значений элемента
	 */
	String VALUES_ATTRIBUTE = "values";
	
	/**
	 * Источник данных по умолчанию
	 */
	String DEFAULT_DATASOURCE = "RFInfoDS";
	
	/**
	 * Значения атрибута, выключающего некое свойство (доступность тулбара, статусбара и т.д.)
	 */
	String OFF = "off";
			
	/**
	 * Наименование тэга, отвечающего за идентификацию GWT-модуля
	 */
	String APPLICATION_TAG_NAME = "application";
	
	/**
	 * Наименование файла по сборке приложения
	 */
	String APPLICATION_SETTING_XML = "JepApplication.xml";
	
	/**
	 * Наименование атрибута, отвечающего за наименование приложения
	 */
	String APPLICATION_NAME_ATTRIBUTE = NAME_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за наименование пакета
	 */
	String PROJECT_PACKAGE_ATTRIBUTE = "projectPackage";
	
	/**
	 * Наименование атрибута, отвечающего за наименование источника данных по умолчанию
	 */
	String APPLICATION_DATASOURCE_ATTRIBUTE = "defaultDatasource";
	
	/**
	 * Наименование атрибута, отвечающего за наименование ролей GWT-модуля
	 */
	String MODULE_ROLES_ATTRIBUTE = "roles";
	
	/**
	 * Наименование атрибута, отвечающего за наименование роли GWT-модуля
	 */
	String MODULE_ROLE_ATTRIBUTE = "role";
	
	/**
	 * Наименование тэга, отвечающего за идентификацию GWT-модуля
	 */
	String MODULE_TAG_NAME = "module";
	
	/**
	 * Наименование атрибута, отвечающего за идентификатор GWT-модуля
	 */
	String MODULE_ID_ATTRIBUTE = ID_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за русское наименование GWT-модуля
	 */
	String MODULE_NAME_ATTRIBUTE = NAME_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за англоязычное наименование GWT-модуля
	 */
	String MODULE_NAME_EN_ATTRIBUTE = NAME_EN_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за источник данных GWT-модуля
	 */
	String MODULE_DATASOURCE_ATTRIBUTE = "datasource";
	
	/**
	 * Наименование атрибута, отвечающего за первичный ключ GWT-модуля
	 */
	String MODULE_PRIMARY_KEY_ATTRIBUTE = "primaryKey";
	
	/**
	 * Наименование атрибута, отвечающего за имя таблицы JepFileRecordDefinition
	 */
	String MODULE_TABLE_NAME_ATTRIBUTE = "table";
	
	/**
	 * Атрибут доступности тулбара для GWT-модуля
	 */
	String MODULE_TOOLBAR_ATTRIBUTE = "toolbar";
	
	/**
	 * Атрибут доступности статусбара для GWT-модуля
	 */
	String MODULE_STATUSBAR_ATTRIBUTE = "statusbar";
	
	/**
	 * Атрибут пересборки модуля
	 */
	String MODULE_BUILD_ATTRIBUTE = "isBuild";
	
	/**
	 * Наименование атрибута, отвечающего за пакет БД GWT-модуля
	 */
	String DB_PACKAGE_ATTRIBUTE = "package";	
	
	/**
	 * Наименование тэга, отвечающего за идентификацию поля GWT-модуля
	 */
	String FIELD_TAG_NAME = "field";
	
	/**
	 * Наименование атрибута, отвечающего за идентификатор поля GWT-модуля
	 */
	String FIELD_ID_ATTRIBUTE = ID_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за русское наименование поля GWT-модуля
	 */
	String FIELD_NAME_ATTRIBUTE = NAME_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за англоязычное наименование поля GWT-модуля
	 */
	String FIELD_NAME_EN_ATTRIBUTE = NAME_EN_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за тип поля GWT-модуля
	 */
	String FIELD_TYPE_ATTRIBUTE = "type";
	
	/**
	 * Наименование атрибута, отвечающего за тип поиска вхождений поля GWT-модуля
	 */
	String FIELD_LIKE_ATTRIBUTE = "like";
	
	/**
	 * Наименование атрибута, отвечающего за виджет детальной формы
	 */
	String FIELD_WIDGET_ATTRIBUTE = "widget";
	
	/**
	 * Наименование атрибута, отвечающего за максимально возможное количество символов для поля детальной формы
	 */
	String FIELD_MAX_LENGTH_ATTRIBUTE = "maxLength";
	
	/**
	 * Наименование атрибута, отвечающего за ширину поля детальной формы
	 */
	String FIELD_WIDTH_ATTRIBUTE = "width";
		
	/**
	 * Наименование атрибута, отвечающего за высоту поля детальной формы
	 */
	String FIELD_HEIGHT_ATTRIBUTE = "height";
	
	/**
	 * Наименование атрибута, отвечающего за ширину лейбла поля детальной формы
	 */
	String FIELD_LABEL_WIDTH_ATTRIBUTE = "label-width";
	
	/**
	 * Наименование атрибута, отвечающего за список состояний, в которых доступно поле
	 */
	String FIELD_VISIBLE_WORKSTATES_ATTRIBUTE = "visibleWorkstates";
	
	/**
	 * Наименование атрибута, отвечающего за список состояний, в которых поле является обязательным
	 */
	String FIELD_MANDATORY_WORKSTATES_ATTRIBUTE = "mandatoryWorkstates";
	
	/**
	 * Наименование атрибута, отвечающего за список состояний, в которых поле является редактируемым
	 */
	String FIELD_EDITABLE_WORKSTATES_ATTRIBUTE = "editableWorkstates";
	
	/**
	 * Наименование атрибута, отвечающего за список состояний, в которых поле является доступным
	 */
	String FIELD_ENABLE_WORKSTATES_ATTRIBUTE = "enableWorkstates";
	
	/**
	 * Наименование тэга, отвечающего за список форм : детальной и списочной
	 */
	String FORMS_TAG_NAME = "forms";
	
	/**
	 * Наименование тэга, отвечающего за списочную форму
	 */
	String LIST_FORM_TAG_NAME = "form-list";
	
	/**
	 * Наименование атрибута списочной формы, отвечающего за группировку представления
	 */
	String LIST_FORM_GROUP_FIELD_NAME_ATTRIBUTE = "groupField";
	
	/**
	 * Наименование атрибута списочной формы, отвечающего вкл./выкл. Drag And Drop
	 */
	String LIST_FORM_DND_NAME_ATTRIBUTE = "dnd";
	
	/**
	 * Наименованеи атрибута, отвечающего за выключение двойного клика на списочной форме (значение OFF делает недоступным двойной клик)
	 */
	String DBL_CLICK_NAME_ATTRIBUTE = "dblClick";
	
	/**
	 * Наименованеи атрибута, указывающего на необходимость создания презентера списочной формы (значение, отличное от OFF - признак необходимости создания) 
	 */
	String LIST_FORM_PRESENTER_ATTRIBUTE = "presenter";
	
	/**
	 * Наименование тэга, отвечающего за детальную форму
	 */
	String DETAIL_FORM_TAG_NAME = "form-detail";
	
	/**
	 * Если не задано значение наименования, то выводим вопросы
	 */
	String NO_NAME = "???";
	
	/**
	 * Наименование тэга, отвечающего за описание БД
	 */
	String DATABASE_TAG_NAME = "db";
	
	/**
	 * Наименование атрибута, отвечающего за префикс параметра EJB
	 */
	String DATABASE_PREFIX_ATTRIBUTE_NAME = "prefix";
	
	/**
	 * Наименование тэга, отвечающего за наименование атрибута параметров
	 */
	String DATABASE_PARAMETERS_TAG_NAME = "parameters";
	
	/**
	 * Наименование тэга, отвечающего за описание инструментальной панели
	 */
	String TOOLBAR_TAG_NAME = MODULE_TOOLBAR_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, указывающего на необходимость создания презентера инструментальной панели
	 */
	String TOOLBAR_PRESENTER_ATTRIBUTE = LIST_FORM_PRESENTER_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, указывающего на необходимость создания представления (view) инструментальной панели
	 */
	String TOOLBAR_VIEW_ATTRIBUTE = "view";
	
	/**
	 * Наименование тэга, отвечающего за описание кнопки инструментальной панели
	 */
	String BUTTON_TAG_NAME = "button";
	
	/**
	 * Наименование тэга, отвечающего за описание разделителя инструментальной панели
	 */
	String SEPARATOR_TAG_NAME = "separator";
	
	/**
	 * Наименование атрибута, отвечающего за идентификатор кнопки тулбара
	 */
	String BUTTON_ID_ATTRIBUTE = ID_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за список доступных рабочих состояний для кнопки тулбара
	 */
	String BUTTON_ENABLE_STATES_ATTRIBUTE = FIELD_ENABLE_WORKSTATES_ATTRIBUTE; //"enableStates";
	
	/**
	 * Наименование атрибута, отвечающего за изображение для кнопки тулбара
	 */
	String BUTTON_IMAGE_ATTRIBUTE = "image";
	
	/**
	 * Наименование атрибута, отвечающего за изображение для недоступной кнопки тулбара
	 */
	String BUTTON_IMAGE_DISABLED_ATTRIBUTE = "imageDisabled";
	
	/**
	 * Наименование атрибута, отвечающего за событие EventBus, привязанное к данной кнопке тулбара
	 */
	String BUTTON_EVENT_ATTRIBUTE = "event";
	
	/**
	 * Наименование атрибута, отвечающего за надпись на кнопке тулбара
	 */
	String BUTTON_TEXT_ATTRIBUTE = "text";
	
	/**
	 * Наименование атрибута, отвечающего за русскоязычное значение надписи на кнопке тулбара
	 */
	String BUTTON_NAME_ATTRIBUTE = NAME_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за англоязычное значение надписи на кнопке тулбара
	 */
	String BUTTON_NAME_EN_ATTRIBUTE = NAME_EN_ATTRIBUTE;
	
	/**
	 * Идентификатор поля, необходимого при работе с фаловой загрузкой/выгрузкой
	 */
	String FILE_NAME_FIELD_ID = "FILE_NAME";
	
	/**
	 * Идентификатор поля, необходимого при работе с фаловой загрузкой/выгрузкой
	 */
	String MIME_TYPE_FIELD_ID = "MIME_TYPE";
	
	/**
	 * Идентификатор поля, необходимого при работе с фаловой загрузкой/выгрузкой
	 */
	String EXTENSION_FIELD_ID = "EXTENSION";
	
	/**
	 * Окончание для идентификаторов
	 */
	String IDENTIFICATOR_SUFFIX = "_ID";
	
	/**
	 * Окончание для идентификаторов кнопок
	 */
	String BUTTON_IDENTIFICATOR_SUFFIX = "_BUTTON" + IDENTIFICATOR_SUFFIX;
	
	/**
	 * Окончание для идентификаторов
	 */
	String IDENTIFICATOR_CODE_SUFFIX = "_CODE";
	
	/**
	 * Окончание для отображаемых значений комбобокса
	 */
	String DISPLAY_VALUE_SUFFIX = "_NAME";
	
	/**
	 * Перенос строки
	 */
	String END_OF_LINE = "\n";
	
	/**
	 * Табуляция
	 */
	String TAB = "\t";
	
	/**
	 * Пробельный символ
	 */
	String WHITE_SPACE = " ";	
	
	/**
	 * Префикс в полном наименовании полей
	 */
	final String FIELD_PREFIX = "com.technology.jep.jepria.client.widget.field.multistate.";
	
	
	//Список виджетов JepRia
	
	final String JEP_FIELD_PREFIX = "Jep";
	
	/**
	 * Виджет даты
	 */
	String JEP_DATE_FIELD = JEP_FIELD_PREFIX + "DateField";
	
	/**
	 * Денежный виджет
	 */
	String JEP_MONEY_FIELD = JEP_FIELD_PREFIX + "MoneyField";
	
	/**
	 * Виджет ввода чисел
	 */
	String JEP_NUMBER_FIELD = JEP_FIELD_PREFIX + "NumberField";

	/**
	 * Виджет ввода целых чисел
	 */
	String JEP_INTEGER_FIELD = JEP_FIELD_PREFIX + "IntegerField";

	/** 
	 * Виджет ввода чисел большой разрядности
	 */
	String JEP_LONG_FIELD = JEP_FIELD_PREFIX + "LongField";

	/**
	 * Виджет текстовой области
	 */
	String JEP_TEXT_AREA_FIELD = JEP_FIELD_PREFIX + "TextAreaField";
	
	/**
	 * Виджет для ввода текста
	 */
	String JEP_TEXT_FIELD = JEP_FIELD_PREFIX + "TextField";

	/**
	 * Виджет для ввода текста по шаблону
	 */
	String JEP_MASKED_TEXT_FIELD = JEP_FIELD_PREFIX + "MaskedTextField";

	/**
	 * Виджет времени
	 */
	String JEP_TIME_FIELD = JEP_FIELD_PREFIX + "TimeField";
	
	/**
	 * Виджет загрузки файла
	 */
	String JEP_FILE_FIELD = JEP_FIELD_PREFIX + "FileField";
	
	/**
	 * Виджет загрузки изображений
	 */
	String JEP_IMAGE_FIELD = JEP_FIELD_PREFIX + "ImageField";
	
	/**
	 * Виджет выбора
	 */
	String JEP_CHECKBOX_FIELD = JEP_FIELD_PREFIX + "CheckBoxField";
	
	/**
	 * Виджет группы выбора
	 */
	String JEP_CHECKBOX_GROUP_FIELD = JEP_FIELD_PREFIX + "CheckBoxGroupField";
	
	/**
	 * Виджет выпадающего списка
	 */
	String JEP_COMBOBOX_FIELD = JEP_FIELD_PREFIX + "ComboBoxField";
	
	/**
	 * Виджет список группы выбора
	 */
	String JEP_LIST_FIELD = JEP_FIELD_PREFIX + "ListField";
	
	/**
	 * Виджет дерева
	 */
	String JEP_TREE_FIELD = JEP_FIELD_PREFIX + "TreeField";
	
	/**
	 * Виджет двойного списка с возможностью переноса значений
	 */
	String JEP_DUAL_LIST_FIELD = JEP_FIELD_PREFIX + "DualListField";
	
	/**
	 * Виджет по умолчанию
	 */
	String DEFAULT_WIDGET = JEP_TEXT_FIELD;

	//Список стандартных идентификатор для кнопок тулбара
	
	/**
	 * Идентификатор кнопки назад
	 */
	String BACK_BUTTON_ID = "BACK_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки добавления
	 */
	String ADD_BUTTON_ID = "ADD_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки сохранения
	 */
	String SAVE_BUTTON_ID = "SAVE_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки редактирования
	 */
	String EDIT_BUTTON_ID = "EDIT_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки удаления
	 */
	String DELETE_BUTTON_ID = "DELETE_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки просмотра
	 */
	String VIEW_DETAILS_BUTTON_ID = "VIEW_DETAILS_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки попадания на списочную форму
	 */
	String LIST_BUTTON_ID = "LIST_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки поиска
	 */
	String SEARCH_BUTTON_ID = "SEARCH_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки найти
	 */
	String FIND_BUTTON_ID = "FIND_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки обновления
	 */
	String REFRESH_BUTTON_ID = "REFRESH_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки выбора
	 */
	String SELECT_BUTTON_ID = "SELECT_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки генерации отчетов
	 */
	String REPORT_BUTTON_ID = "REPORT_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки генерации Excel-отчета
	 */
	String EXCEL_BUTTON_ID = "EXCEL_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки печати
	 */
	String PRINT_BUTTON_ID = "PRINT_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки помощи
	 */
	String HELP_BUTTON_ID = "HELP_BUTTON_ID";
	
	
	//Разделители тулбара	
	String BACK_RIGHT_SEPARATOR_ID = "BACK_RIGHT_SEPARATOR_ID";
	String ADD_SEPARATOR_ID = "ADD_SEPARATOR_ID";
	String DELETE_SEPARATOR_ID = "DELETE_SEPARATOR_ID";
	String SEARCH_SEPARATOR_ID = "SEARCH_SEPARATOR_ID";
	String REFRESH_SEPARATOR_ID = "REFRESH_SEPARATOR_ID";
	String HELP_SEPARATOR_ID = "HELP_SEPARATOR_ID";
	
	//Логирование
	
	/**
	 * Префикс полного пути для логгеров log4j
	 */
	String LOG4J_LOGGER_PREFIX = "log4j.logger.";
	
	/**
	 * Префикс в полном пути аппендеров для log4j
	 */
	String LOG4J_APPENDER_PREFIX = "log4j.appender.";
	
	/**
	 * Значение для логгеров в log4j
	 */
	String LOG4J_LOGGER_VALUE = "INFO, FILE, gwt-log";
	
	/**
	 * Обозначение логического ИЛИ
	 */
	String OR = "||";
	
	/**
	 * Английский алфавит в верхнем регистре
	 */
	String ALPHABET_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	/**
	 * Английский алфавит в нижнем регистре
	 */
	String ALPHABET_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
	
	/**
	 * Наименование первичного ключа
	 */
	String RECORD_ID = "RECORD_ID";
	
	/**
	 * HTTP-протокол
	 */
	String HTTP_PROTOCOL = "http";
	
	/**
	 * Стандартный порт http-протокола
	 */
	String DEFAULT_HTTP_PORT = "80";
	
	/**
	 * OPMN-протокол
	 */
	String OPMN_PROTOCOL = "opmn";
	
	String INHERITS_MAIN_GWT_XML_TAG_NAME = "inherits";
	String EXTEND_PROPERTY_MAIN_GWT_XML_TAG_NAME = "extend-property";
	String SET_PROPERTY_MAIN_GWT_XML_TAG_NAME = "set-property";
	String SET_PROPERTY_FALLBACK_MAIN_GWT_XML_TAG_NAME = "set-property-fallback";
	String JAVA_BUILD_XML_TAG_NAME = "java";
	String ARG_BUILD_XML_TAG_NAME = "arg";
	
	
	String INFO_LOG_LEVEL = "INFO";
	String DEBUG_LOG_LEVEL = "DEBUG";
	
	/**
	 * Кодировка по умолчанию
	 */
	String UTF_8 = "UTF-8";
	
	/**
	 * Идентификатор панели для вывода тестового уведомления
	 */
	String TEST_BUILD_MESSAGE_DIV_ID = "testBuildMessage";
	
	/**
	 * Идентификатор панели для вывода сообщения о загрузке модуля
	 */
	String LOADING_DIV_ID = "loading";
	
	/**
	 * Сообщение, выводимое в случае тестовой сборки
	 */
	String TEST_BUILD_MESSAGE = JepRiaToolkitUtil.multipleConcat(
  	 	  "<div id=\"", TEST_BUILD_MESSAGE_DIV_ID, "\" class=\"testBuildMessage\">", END_OF_LINE
  	 	, "						<div class=\"testBuildMessage-notification error\">", END_OF_LINE
  	 	, "							<div class=\"testBuildMessage-close\" onclick=\"document.getElementById('testBuildMessage').style.display = 'none';\">X</div>", END_OF_LINE
  	 	, "							<div class=\"testBuildMessage-header\">Attention please!</div>", END_OF_LINE
  	 	, "							<div class=\"testBuildMessage-message\">This is test build!</div>", END_OF_LINE
  	 	, "						</div>", END_OF_LINE
  	 	, "					</div>", END_OF_LINE, WHITE_SPACE, END_OF_LINE);
	/**
	 * Стиль для отображения сообщения о тестовой сборке
	 */
	String TEST_BUILD_MESSAGE_STYLE_VISIBLE = "div.testBuildMessage { display: block; }";
	/**
	 * Стиль для скрытия сообщения о тестовой сборке
	 */
	String TEST_BUILD_MESSAGE_STYLE_HIDDEN = "div.testBuildMessage { display: none; }"; 
	/**
	 * Размерность отступа
	 */
	int INDENT_AMOUNT = 4;
	
	/**
	 * Префикс пакетов
	 */
	String PKG_PREFIX = "pkg_";
	
	/**
	 * Целочисленная величина, значение которой считаем неопределенным.
	 */
	int UNDEFINED_INT = Integer.MIN_VALUE;
	
	/**
	 * Нижнее подчеркивание
	 */
	String UNDERSCORE = "_";
	
	/**
	 * Регулярное выражение для пустых символов
	 */
	String REGEXP_FOR_BLANK = "\\s";
	
	/**
	 * Префикс для вывода сообщения об ошибке
	 */
	String ERROR_PREFIX = "[ERROR] ";
	
	/**
	 * Префикс для вывода сообщения о предупреждении
	 */
	String WARNING_PREFIX = "[WARNING] ";
	
	public static String BUILD_CONFIG_PATH_PREFIX = "config\\"; 
	public static String BUILD_CONFIG_FILE_NAME = "build.config";
	public static String DEBUG_BUILD_CONFIG_NAME = "debug";
	public static String RELEASE_BUILD_CONFIG_NAME = "production";

}