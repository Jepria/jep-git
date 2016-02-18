package com.technology.jep.jepriatoolkit;

import java.util.ResourceBundle;

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
	 * Окончание в наименовании конфигурационного файла приложения, хранящего его структуру
	 */
	String APPLICATION_SETTING_FILE_ENDING = "Definition.xml";
	
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
	
	String BUILD_CONFIG_PATH_PREFIX = "config\\"; 
	String BUILD_CONFIG_FILE_NAME = "build.config";
	String DEBUG_BUILD_CONFIG_NAME = "debug";
	String RELEASE_BUILD_CONFIG_NAME = "production";
	
	String APPLICATION_DEFINITION_RESOURCE_BUNDLE_NAME = "com.technology.jep.jepriatoolkit.creator.ApplicationDefinition";
	
	ResourceBundle applicationResourceBundle = ResourceBundle.getBundle(APPLICATION_DEFINITION_RESOURCE_BUNDLE_NAME);
	
	String LIB_DIRECTORY_PROPERTY = "fileStructure.libraryDirectory";
	String HTML_DIRECTORY_PROPERTY = "fileStructure.htmlDirectory";
	String RESOURCE_DIRECTORY_PROPERTY = "fileStructure.resourceDirectory";
	String MAIN_MODULE_DIRECTORY_PROPERTY = "fileStructure.mainModuleDirectory";
	String ENTRANCE_DIRECTORY_PROPERTY = "fileStructure.entranceDirectory";
	String MAIN_TEXT_RESOURCE_DIRECTORY_PROPERTY = "fileStructure.mainTextResourceDirectory";
	String CLIENT_MODULE_PLAIN_FORM_DIRECTORY_PROPERTY = "fileStructure.clientModulePlainFormDirectory";
	String CLIENT_MODULE_DETAIL_FORM_DIRECTORY_PROPERTY = "fileStructure.clientModuleDetailFormDirectory";
	String CLIENT_MODULE_LIST_FORM_DIRECTORY_PROPERTY = "fileStructure.clientModuleListFormDirectory";
	String CLIENT_MODULE_EVENTBUS_DIRECTORY_PROPERTY = "fileStructure.clientModuleEventBusDirectory";
	String CLIENT_MODULE_TOOLBAR_DIRECTORY_PROPERTY = "fileStructure.clientModuleToolBarDirectory";
	String CLIENT_MODULE_STATUSBAR_DIRECTORY_PROPERTY = "fileStructure.clientModuleStatusBarDirectory";
	String CLIENT_MODULE_EJB_DIRECTORY_PROPERTY = "fileStructure.clientModuleEjbDirectory";
	String CLIENT_MODULE_SERVICE_IMPL_DIRECTORY_PROPERTY = "fileStructure.clientModuleServiceImplDirectory";
	String CLIENT_MODULE_FIELD_DIRECTORY_PROPERTY = "fileStructure.clientModuleFieldDirectory";
	String CLIENT_MODULE_RECORD_DIRECTORY_PROPERTY = "fileStructure.clientModuleRecordDirectory";
	String CLIENT_MODULE_SERVICE_DIRECTORY_PROPERTY = "fileStructure.clientModuleServiceDirectory";
	String CLIENT_MODULE_TEXT_RESOURCE_DIRECTORY_PROPERTY = "fileStructure.clientTextResourceDirectory";
	String CONFIG_HTML_DIRECTORY_PROPERTY = "fileStructure.configHtmlDirectory";
	String CONFIG_MAIN_PACKAGE_DIRECTORY_PROPERTY = "fileStructure.configMainPackageDirectory";
	
	String DEPLOY_PROPERTIES_SOURCE_PATH_TEMPLATE_PROPERTY = "fileTemplate.deployProperties.path.source";
	String DEPLOY_PROPERTIES_DESTINATION_PATH_TEMPLATE_PROPERTY = "fileTemplate.deployProperties.path.destination";
	
	String LOG4J_PROPERTIES_SOURCE_PATH_TEMPLATE_PROPERTY = "fileTemplate.log4jProperties.path.source";
	String LOG4J_PROPERTIES_DESTINATION_PATH_TEMPLATE_PROPERTY = "fileTemplate.log4jProperties.path.destination";
	String LOG4J_PROPERTIES_CODE_DESTINATION_PATH_TEMPLATE_PROPERTY = "fileTemplate.log4jProperties.path.codeDestination";
	
	String WEB_XML_TEMPLATE_PROPERTY = "fileTemplate.webXml";
	String WEB_XML_PATH_TEMPLATE_PROPERTY = "fileTemplate.webXml.path";
	
	String APPLICATION_XML_TEMPLATE_PROPERTY = "fileTemplate.applicationXml";
	String APPLICATION_XML_PATH_TEMPLATE_PROPERTY = "fileTemplate.applicationXml.path";
	
	String ORION_APPLICATION_XML_TEMPLATE_PROPERTY = "fileTemplate.orionApplicationXml";
	String ORION_APPLICATION_XML_PATH_TEMPLATE_PROPERTY = "fileTemplate.orionApplicationXml.path";
	
	String MAIN_GWT_XML_DEBUG_TEMPLATE_PROPERTY = "fileTemplate.mainGwtXml.debug";
	String MAIN_GWT_XML_PRODUCTION_TEMPLATE_PROPERTY = "fileTemplate.mainGwtXml.production";
	String MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainGwtXml.path";
	
	String CLIENT_MODULE_GWT_XML_TEMPLATE_PROPERTY = "fileTemplate.clientModuleGwtXml";
	String CLIENT_MODULE_GWT_XML_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleGwtXml.path";
	
	String APPLICATION_JSP_TEMPLATE_PROPERTY = "fileTemplate.applicationJsp";
	String APPLICATION_JSP_PATH_TEMPLATE_PROPERTY = "fileTemplate.applicationJsp.path";
	
	String APPLICATION_CSS_PATH_TEMPLATE_PROPERTY = "fileTemplate.applicationCss.path";
	
	String MAIN_TEXT_RESOURCE_TEMPLATE_PROPERTY = "fileTemplate.mainText";
	String MAIN_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainText.path";
	
	String MAIN_TEXT_RESOURCE_EN_TEMPLATE_PROPERTY = "fileTemplate.mainTextEn";
	String MAIN_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainTextEn.path";
	
	String CLIENT_MODULE_TEXT_RESOURCE_TEMPLATE_PROPERTY = "fileTemplate.clientModuleText";
	String CLIENT_MODULE_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleText.path";
	
	String CLIENT_MODULE_TEXT_RESOURCE_EN_TEMPLATE_PROPERTY = "fileTemplate.clientModuleTextEn";
	String CLIENT_MODULE_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleTextEn.path";
	
	String OVERVIEW_TEMPLATE_PROPERTY = "fileTemplate.overview";
	String OVERVIEW_PATH_TEMPLATE_PROPERTY = "fileTemplate.overview.path";
	
	String CLIENT_MODULE_CONSTANT_TEMPLATE_PROPERTY = "fileTemplate.clientConstant";
	String CLIENT_MODULE_CONSTANT_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientConstant.path";
	
	String MAIN_MODULE_CONSTANT_TEMPLATE_PROPERTY = "fileTemplate.mainConstant";
	String MAIN_MODULE_CONSTANT_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainConstant.path";
	
	String CLIENT_MODULE_FACTORY_TEMPLATE_PROPERTY = "fileTemplate.clientFactory";
	String CLIENT_MODULE_FACTORY_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientFactory.path";
	
	String MAIN_MODULE_FACTORY_TEMPLATE_PROPERTY = "fileTemplate.mainFactory";
	String MAIN_MODULE_FACTORY_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainFactory.path";
	
	String MODULE_ENTRY_POINT_TEMPLATE_PROPERTY = "fileTemplate.moduleEntryPoint";
	String MODULE_ENTRY_POINT_PATH_TEMPLATE_PROPERTY = "fileTemplate.moduleEntryPoint.path";
	
	String CLIENT_MODULE_DETAIL_FORM_PRESENTER_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDetailFormPresenter";
	String CLIENT_MODULE_DETAIL_FORM_PRESENTER_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDetailFormPresenter.path";
	
	String CLIENT_MODULE_DETAIL_FORM_VIEW_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDetailFormView";
	String CLIENT_MODULE_DETAIL_FORM_VIEW_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDetailFormView.path";
	
	String CLIENT_MODULE_DETAIL_FORM_VIEW_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDetailFormViewImpl";
	String CLIENT_MODULE_DETAIL_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDetailFormViewImpl.path";
	
	String CLIENT_MODULE_LIST_FORM_PRESENTER_TEMPLATE_PROPERTY = "fileTemplate.clientModuleListFormPresenter";
	String CLIENT_MODULE_LIST_FORM_PRESENTER_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleListFormPresenter.path";
	
	String CLIENT_MODULE_LIST_FORM_VIEW_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleListFormViewImpl";
	String CLIENT_MODULE_LIST_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleListFormViewImpl.path";
	
	String CLIENT_MODULE_PRESENTER_TEMPLATE_PROPERTY = "fileTemplate.clientModulePresenter";
	String CLIENT_MODULE_PRESENTER_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModulePresenter.path";
	
	String MAIN_MODULE_PRESENTER_TEMPLATE_PROPERTY = "fileTemplate.mainModulePresenter";
	String MAIN_MODULE_PRESENTER_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainModulePresenter.path";
	
	String CLIENT_MODULE_SERVICE_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleServiceImpl";
	String CLIENT_MODULE_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleServiceImpl.path";
	
	String CLIENT_MODULE_UPLOAD_SERVICE_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleUploadServiceImpl";
	String CLIENT_MODULE_UPLOAD_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleUploadServiceImpl.path";
	
	String CLIENT_MODULE_DOWNLOAD_SERVICE_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDownloadServiceImpl";
	String CLIENT_MODULE_DOWNLOAD_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDownloadServiceImpl.path";
	
	String CLIENT_MODULE_EXCEL_SERVICE_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleExcelServiceImpl";
	String CLIENT_MODULE_EXCEL_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleExcelServiceImpl.path";
	
	String CLIENT_MODULE_SERVER_CONSTANT_TEMPLATE_PROPERTY = "fileTemplate.clientModuleServerConstant";
	String CLIENT_MODULE_SERVER_CONSTANT_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleServerConstant.path";
	
	String CLIENT_MODULE_LOCAL_EJB_TEMPLATE_PROPERTY = "fileTemplate.clientModuleLocalEjb";
	String CLIENT_MODULE_LOCAL_EJB_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleLocalEjb.path";
	
	String CLIENT_MODULE_REMOTE_EJB_TEMPLATE_PROPERTY = "fileTemplate.clientModuleRemoteEjb";
	String CLIENT_MODULE_REMOTE_EJB_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleRemoteEjb.path";
	
	String CLIENT_MODULE_EJB_INTERFACE_TEMPLATE_PROPERTY = "fileTemplate.clientModuleEjbInterface";
	String CLIENT_MODULE_EJB_INTERFACE_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleEjbInterface.path";
	
	String CLIENT_MODULE_EJB_TEMPLATE_PROPERTY = "fileTemplate.clientModuleEjb";
	String CLIENT_MODULE_EJB_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleEjb.path";
	
	String CLIENT_MODULE_FIELDS_TEMPLATE_PROPERTY = "fileTemplate.clientModuleFields";
	String CLIENT_MODULE_FIELDS_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleFields.path";
	
	String CLIENT_MODULE_OPTIONS_TEMPLATE_PROPERTY = "fileTemplate.clientModuleOptions";
	String CLIENT_MODULE_OPTIONS_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleOptions.path";
	
	String CLIENT_MODULE_RECORD_DEFINITION_TEMPLATE_PROPERTY = "fileTemplate.clientModuleRecordDefinition";
	String CLIENT_MODULE_RECORD_DEFINITION_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleRecordDefinition.path";
	
	String CLIENT_MODULE_SERVICE_TEMPLATE_PROPERTY = "fileTemplate.clientModuleService";
	String CLIENT_MODULE_SERVICE_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleService.path";
	
	String CLIENT_MODULE_SERVICE_ASYNC_TEMPLATE_PROPERTY = "fileTemplate.clientModuleServiceAsync";
	String CLIENT_MODULE_SERVICE_ASYNC_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleServiceAsync.path";
	
	String CLIENT_MODULE_SHARED_CONSTANT_TEMPLATE_PROPERTY = "fileTemplate.clientModuleSharedConstant";
	String CLIENT_MODULE_SHARED_CONSTANT_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleSharedConstant.path";
	
	String MAIN_MODULE_SHARED_CONSTANT_TEMPLATE_PROPERTY = "fileTemplate.mainModuleSharedConstant";
	String MAIN_MODULE_SHARED_CONSTANT_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainModuleSharedConstant.path";
	
	String CLIENT_MODULE_STATUSBAR_TEMPLATE_PROPERTY = "fileTemplate.clientModuleStatusBar";
	String CLIENT_MODULE_STATUSBAR_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleStatusBar.path";
	
	String CLIENT_MODULE_TOOLBAR_VIEW_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarView";
	String CLIENT_MODULE_TOOLBAR_VIEW_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarView.path";
	
	String CLIENT_MODULE_TOOLBAR_VIEW_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarViewImpl";
	String CLIENT_MODULE_TOOLBAR_VIEW_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarViewImpl.path";
	
	String CLIENT_MODULE_TOOLBAR_PRESENTER_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarPresenter";
	String CLIENT_MODULE_TOOLBAR_PRESENTER_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarPresenter.path";
	
	String CLIENT_MODULE_EVENTBUS_TEMPLATE_PROPERTY = "fileTemplate.clientModuleEventBus";
	String CLIENT_MODULE_EVENTBUS_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleEventBus.path";
	
	String CLIENT_MODULE_EVENT_TEMPLATE_PROPERTY = "fileTemplate.clientModuleEvent";
	String CLIENT_MODULE_EVENT_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleEvent.path";
	
	String CLIENT_MODULE_IMAGES_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarImages";
	String CLIENT_MODULE_IMAGES_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarImages.path";
	
	String CURRENT_DIRECTORY_ENVIRONMENT_VARIABLE = "user.dir";
	
	String MODULE_NAME_TEMPLATE_PARAMETER = "moduleName";
	String PACKAGE_NAME_TEMPLATE_PARAMETER = "packageName";
	String SECURITY_ROLES_TEMPLATE_PARAMETER = "securityRoles";
	String FORMS_TEMPLATE_PARAMETER = "forms";
	String FORM_TEMPLATE_PARAMETER = "form";
	String FIELD_TEMPLATE_PARAMETER = "field";
	String BUTTON_TEMPLATE_PARAMETER = "button";
	String HAS_TEXT_FILE_TEMPLATE_PARAMETER = "hasTextFile";
	String HAS_BINARY_FILE_TEMPLATE_PARAMETER = "hasBinaryFile";
	String FORM_NAME_TEMPLATE_PARAMETER = "formName";
}