package com.technology.jep.jepriatoolkit;

import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class JepRiaToolkitConstant {	
	
	/**
	 * Наименование конфигурационного файла сборки проекта
	 */
	public static final String BUILD_FILE = "build.xml";
	
	/**
	 * Наименование папки библиотеки
	 */
	public static final String LIB_DIR_NAME = "lib"; 
	
	/**
	 * Наименование директории для хранения структуры навигации проекта
	 */
	public static final String NAVIGATION_MENU_DIR_NAME = "navigation/menu";
	
	/**
	 * Наименование директории для хранения текстовых ресурсов навигации
	 */
	public static final String NAVIGATION_TEXT_DIR_NAME = "navigation/text";
	
	/**
	 * Наименование папки исходных кодов
	 */
	public static final String SOURCE_DIR_NAME = "src"; 
	
	/**
	 * Префикс пути до папки с java-кодом
	 */
	public static final String PREFIX_DESTINATION_JAVA_CODE = "src/java"; 
	
	/**
	 * Префикс пути до исходного кода
	 */
	public static final String PREFIX_DESTINATION_SOURCE_CODE = PREFIX_DESTINATION_JAVA_CODE + "/com/technology/";
	
	/**
	 * Префикс пути до исходного кода
	 */
	public static final String PREFIX_DESTINATION_RESOURCE = "src/resources/com/technology/";
	
	/**
	 * Путь до основной jsp и страницы приветствия
	 */
	public static final String WELCOME_PAGE_DIR_NAME = "src/html";	
	
	/**
	 * Название файла-шаблона css
	 */
	public static final String CSS_TEMPLATE_NAME = "template.css";	

	/**
	 * Название файла-шаблона css
	 */
	public static final String JSP_TEMPLATE_NAME = "template.jsp";	

	/**
	 * Наименование Логгера
	 */
	public static final String LOGGER_SERVLET_NAME = "remoteLoggerServiceImpl";
	
	/**
	 * Наименование полного имени класса Логгера, включая имя пакета
	 */
	public static final String LOGGER_SERVLET_CLASS = "com.allen_sauer.gwt.log.server.RemoteLoggerServiceImpl";
	
	/**
	 * Наименование сервлета основного сервиса
	 */
	public static final String MAIN_SERVICE_SERVLET_NAME = "MainServiceServlet";
	
	/**
	 * Наименование полного имени класса сервлета основного сервиса
	 */
	public static final String MAIN_SERVICE_SERVLET_CLASS = "com.technology.jep.jepria.server.service.JepMainServiceServlet";
	
	/**
	 * Наименование сервлета, отвечающего за версионность приложений
	 */
	public static final String VERSION_SERVLET_NAME = "VersionServlet";
	
	/**
	 * Наименование полного имени класса сервлета, отвечающего за версионность приложений
	 */
	public static final String VERSION_SERVLET_CLASS = "com.technology.jep.jepria.server.version.VersionServlet";
	
	/**
	 * Наименование свойства для таска productionBuildChecker, хранящее в себе описание ошибки, возникаемой во время продукционной сборки проекта
	 */
	public static final String PRODUCTION_BUILD_CHECKER_ERROR = "productionBuildCheckerError";
	
	/**
	 * Открывающая скобка
	 */
	public static final String LEFT_BRACKET = "(";
	
	/**
	 * Закрывающая скобка
	 */
	public static final String RIGHT_BRACKET = ")";
		
	/**
	 * Разделитель форм
	 */
	public static final String SEPARATOR = ",";
	
	/**
	 * Разделитель в пути
	 */
	public static final String PATH_SEPARATOR = "/";
	
	/**
	 * Идентифицирующий атрибут элемента
	 */
	public static final String ID_ATTRIBUTE = "id";
		
	/**
	 * Атрибут наименования элемента
	 */
	public static final String NAME_ATTRIBUTE = "name";
	
	/**
	 * Атрибут англоязычного наименования элемента
	 */
	public static final String NAME_EN_ATTRIBUTE = "nameEn";
	
	/**
	 * Атрибут значения элемента
	 */
	public static final String VALUE_ATTRIBUTE = "value";
	
	/**
	 * Атрибут значений элемента
	 */
	public static final String VALUES_ATTRIBUTE = "values";
	
	/**
	 * Источник данных по умолчанию
	 */
	public static final String DEFAULT_DATASOURCE = "RFInfoDS";
	
	/**
	 * Пакет для исходных кодов по умолчанию
	 */
	public static final String DEFAULT_PROJECT_PACKAGE = "rfi";
	
	/**
	 * Значения атрибута, выключающего некое свойство (доступность тулбара, статусбара и т.д.)
	 */
	public static final String OFF = "off";
			
	/**
	 * Наименование тэга, отвечающего за идентификацию GWT-модуля
	 */
	public static final String APPLICATION_TAG_NAME = "application";
	
	/**
	 * Окончание в наименовании конфигурационного файла приложения, хранящего его структуру
	 */
	public static final String APPLICATION_SETTING_FILE_ENDING = "Definition.xml";
	
	/**
	 * Наименование атрибута, отвечающего за наименование приложения
	 */
	public static final String APPLICATION_NAME_ATTRIBUTE = NAME_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за наименование пакета
	 */
	public static final String PROJECT_PACKAGE_ATTRIBUTE = "projectPackage";
	
	/**
	 * Наименование атрибута, отвечающего за наименование источника данных по умолчанию
	 */
	public static final String APPLICATION_DATASOURCE_ATTRIBUTE = "defaultDatasource";
	
	/**
	 * Наименование атрибута, отвечающего за наименование ролей GWT-модуля
	 */
	public static final String MODULE_ROLES_ATTRIBUTE = "roles";
	
	/**
	 * Наименование атрибута, отвечающего за наименование роли GWT-модуля
	 */
	public static final String MODULE_ROLE_ATTRIBUTE = "role";
	
	/**
	 * Наименование тэга, отвечающего за определение списка GWT-модулей в приложении
	 */
	public static final String MODULES_TAG_NAME = "modules";
	
	/**
	 * Наименование тэга, отвечающего за идентификацию GWT-модуля
	 */
	public static final String MODULE_TAG_NAME = "module";
	
	/**
	 * Наименование атрибута, отвечающего за идентификатор GWT-модуля
	 */
	public static final String MODULE_ID_ATTRIBUTE = ID_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за русское наименование GWT-модуля
	 */
	public static final String MODULE_NAME_ATTRIBUTE = NAME_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за англоязычное наименование GWT-модуля
	 */
	public static final String MODULE_NAME_EN_ATTRIBUTE = NAME_EN_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за источник данных GWT-модуля
	 */
	public static final String MODULE_DATASOURCE_ATTRIBUTE = "datasource";
	
	/**
	 * Наименование атрибута, отвечающего за первичный ключ GWT-модуля
	 */
	public static final String MODULE_PRIMARY_KEY_ATTRIBUTE = "primaryKey";
	
	/**
	 * Наименование атрибута, отвечающего за имя таблицы JepFileRecordDefinition
	 */
	public static final String MODULE_TABLE_NAME_ATTRIBUTE = "table";
	
	/**
	 * Атрибут доступности тулбара для GWT-модуля
	 */
	public static final String MODULE_TOOLBAR_ATTRIBUTE = "toolbar";
	
	/**
	 * Атрибут доступности статусбара для GWT-модуля
	 */
	public static final String MODULE_STATUSBAR_ATTRIBUTE = "statusbar";
	
	/**
	 * Атрибут пересборки модуля
	 */
	public static final String MODULE_BUILD_ATTRIBUTE = "isBuild";
	
	/**
	 * Наименование атрибута, отвечающего за пакет БД GWT-модуля
	 */
	public static final String DB_PACKAGE_ATTRIBUTE = "package";	
	
	/**
	 * Наименование тэга, отвечающего за идентификацию поля GWT-модуля
	 */
	public static final String FIELD_TAG_NAME = "field";
	
	/**
	 * Наименование атрибута, отвечающего за идентификатор поля GWT-модуля
	 */
	public static final String FIELD_ID_ATTRIBUTE = ID_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за русское наименование поля GWT-модуля
	 */
	public static final String FIELD_NAME_ATTRIBUTE = NAME_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за англоязычное наименование поля GWT-модуля
	 */
	public static final String FIELD_NAME_EN_ATTRIBUTE = NAME_EN_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за тип поля GWT-модуля
	 */
	public static final String FIELD_TYPE_ATTRIBUTE = "type";
	
	/**
	 * Наименование атрибута, отвечающего за тип поиска вхождений поля GWT-модуля
	 */
	public static final String FIELD_LIKE_ATTRIBUTE = "like";
	
	/**
	 * Наименование атрибута, отвечающего за виджет детальной формы
	 */
	public static final String FIELD_WIDGET_ATTRIBUTE = "widget";
	
	/**
	 * Наименование атрибута, отвечающего за максимально возможное количество символов для поля детальной формы
	 */
	public static final String FIELD_MAX_LENGTH_ATTRIBUTE = "maxLength";
	
	/**
	 * Наименование атрибута, отвечающего за ширину поля детальной формы
	 */
	public static final String FIELD_WIDTH_ATTRIBUTE = "width";
	
	/**
	 * Значение умолчательного значения ширины колонки списочной формы формы
	 */
	public static final String DEFAULT_FIELD_WIDTH = Integer.toString(150);
		
	/**
	 * Наименование атрибута, отвечающего за высоту поля детальной формы
	 */
	public static final String FIELD_HEIGHT_ATTRIBUTE = "height";
	
	/**
	 * Наименование атрибута, отвечающего за ширину лейбла поля детальной формы
	 */
	public static final String FIELD_LABEL_WIDTH_ATTRIBUTE = "label-width";
	
	/**
	 * Наименование атрибута, отвечающего за список состояний, в которых доступно поле
	 */
	public static final String FIELD_VISIBLE_WORKSTATES_ATTRIBUTE = "visibleWorkstates";
	
	/**
	 * Наименование атрибута, отвечающего за список состояний, в которых поле является обязательным
	 */
	public static final String FIELD_MANDATORY_WORKSTATES_ATTRIBUTE = "mandatoryWorkstates";
	
	/**
	 * Наименование атрибута, отвечающего за список состояний, в которых поле является редактируемым
	 */
	public static final String FIELD_EDITABLE_WORKSTATES_ATTRIBUTE = "editableWorkstates";
	
	/**
	 * Наименование атрибута, отвечающего за список состояний, в которых поле является доступным
	 */
	public static final String FIELD_ENABLE_WORKSTATES_ATTRIBUTE = "enableWorkstates";
	
	/**
	 * Наименование тэга, отвечающего за список форм : детальной и списочной
	 */
	public static final String FORMS_TAG_NAME = "forms";
	
	/**
	 * Наименование тэга, отвечающего за списочную форму
	 */
	public static final String LIST_FORM_TAG_NAME = "form-list";
	
	/**
	 * Наименование атрибута списочной формы, отвечающего за группировку представления
	 */
	public static final String LIST_FORM_GROUP_FIELD_NAME_ATTRIBUTE = "groupField";
	
	/**
	 * Наименование атрибута списочной формы, отвечающего вкл./выкл. Drag And Drop
	 */
	public static final String LIST_FORM_DND_NAME_ATTRIBUTE = "dnd";
	
	/**
	 * Наименованеи атрибута, отвечающего за выключение двойного клика на списочной форме (значение OFF делает недоступным двойной клик)
	 */
	public static final String DBL_CLICK_NAME_ATTRIBUTE = "dblClick";
	
	/**
	 * Наименованеи атрибута, указывающего на необходимость создания презентера списочной формы (значение, отличное от OFF - признак необходимости создания) 
	 */
	public static final String LIST_FORM_PRESENTER_ATTRIBUTE = "presenter";
	
	/**
	 * Наименование тэга, отвечающего за детальную форму
	 */
	public static final String DETAIL_FORM_TAG_NAME = "form-detail";
	
	/**
	 * Наименование тэга, отвечающего за тело презентера детальной формы
	 */
	public static final String PRESENTER_BOBY_TAG_NAME = "presenterBody";
	
	/**
	 * Если не задано значение наименования, то выводим вопросы
	 */
	public static final String NO_NAME = "???";
	
	/**
	 * Наименование тэга, отвечающего за описание БД
	 */
	public static final String DATABASE_TAG_NAME = "db";
	
	/**
	 * Наименование тэга, отвечающего за описание записей модуля
	 */
	public static final String RECORD_TAG_NAME = "record";
	
	/**
	 * Наименование атрибута, отвечающего за префикс параметра DAO
	 */
	public static final String DATABASE_PREFIX_ATTRIBUTE_NAME = "prefix";
	
	/**
	 * Наименование тэга, отвечающего за наименование атрибута параметров
	 */
	public static final String DATABASE_PARAMETERS_TAG_NAME = "parameters";
	
	/**
	 * Наименование тэга, отвечающего за описание инструментальной панели
	 */
	public static final String TOOLBAR_TAG_NAME = MODULE_TOOLBAR_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, указывающего на необходимость создания презентера инструментальной панели
	 */
	public static final String TOOLBAR_PRESENTER_ATTRIBUTE = LIST_FORM_PRESENTER_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, указывающего на необходимость создания представления (view) инструментальной панели
	 */
	public static final String TOOLBAR_VIEW_ATTRIBUTE = "view";
	
	/**
	 * Наименование тэга, отвечающего за описание кнопки инструментальной панели
	 */
	public static final String BUTTON_TAG_NAME = "button";
	
	/**
	 * Наименование тэга, отвечающего за описание разделителя инструментальной панели
	 */
	public static final String SEPARATOR_TAG_NAME = "separator";
	
	/**
	 * Наименование атрибута, отвечающего за идентификатор кнопки тулбара
	 */
	public static final String BUTTON_ID_ATTRIBUTE = ID_ATTRIBUTE;
	
	/**
	 * Наименование атрибута, отвечающего за список доступных рабочих состояний для кнопки тулбара
	 */
	public static final String BUTTON_ENABLE_STATES_ATTRIBUTE = FIELD_ENABLE_WORKSTATES_ATTRIBUTE; //"enableStates";
	
	/**
	 * Наименование атрибута, отвечающего за изображение для кнопки тулбара
	 */
	public static final String BUTTON_IMAGE_ATTRIBUTE = "image";
	
	/**
	 * Наименование атрибута, отвечающего за изображение для недоступной кнопки тулбара
	 */
	public static final String BUTTON_IMAGE_DISABLED_ATTRIBUTE = "imageDisabled";
	
	/**
	 * Наименование атрибута, отвечающего за событие EventBus, привязанное к данной кнопке тулбара
	 */
	public static final String BUTTON_EVENT_ATTRIBUTE = "event";
	
	/**
	 * Наименование атрибута, отвечающего за надпись на кнопке тулбара
	 */
	public static final String BUTTON_TEXT_ATTRIBUTE = "text";
	
//	/**
//	 * Наименование атрибута, отвечающего за русскоязычное значение надписи на кнопке тулбара
//	 */
//	String BUTTON_NAME_ATTRIBUTE = NAME_ATTRIBUTE;
//	
//	/**
//	 * Наименование атрибута, отвечающего за англоязычное значение надписи на кнопке тулбара
//	 */
//	String BUTTON_NAME_EN_ATTRIBUTE = NAME_EN_ATTRIBUTE;
	
	/**
	 * Идентификатор поля, необходимого при работе с фаловой загрузкой/выгрузкой
	 */
	public static final String FILE_NAME_FIELD_ID = "FILE_NAME";
	
	/**
	 * Идентификатор поля, необходимого при работе с фаловой загрузкой/выгрузкой
	 */
	public static final String MIME_TYPE_FIELD_ID = "MIME_TYPE";
	
	/**
	 * Идентификатор поля, необходимого при работе с фаловой загрузкой/выгрузкой
	 */
	public static final String EXTENSION_FIELD_ID = "EXTENSION";
	
	/**
	 * Окончание для идентификаторов
	 */
	public static final String IDENTIFICATOR_SUFFIX = "_ID";
	
	/**
	 * Окончание для идентификаторов кнопок
	 */
	public static final String BUTTON_IDENTIFICATOR_SUFFIX = "_BUTTON" + IDENTIFICATOR_SUFFIX;
	
	/**
	 * Окончание для идентификаторов
	 */
	public static final String IDENTIFICATOR_CODE_SUFFIX = "_CODE";
	
	/**
	 * Окончание для отображаемых значений комбобокса
	 */
	public static final String DISPLAY_VALUE_SUFFIX = "_NAME";
	
	/**
	 * Перенос строки
	 */
	public static final String END_OF_LINE = "\n";
	
	/**
	 * Табуляция
	 */
	public static final String TAB = "\t";
	
	/**
	 * Пробельный символ
	 */
	public static final String WHITE_SPACE = " ";	
	
	/**
	 * Префикс в полном наименовании полей
	 */
	public static final String FIELD_PREFIX = "com.technology.jep.jepria.client.widget.field.multistate.";
	
	
	//Список виджетов JepRia
	
	public static final String JEP_FIELD_PREFIX = "Jep";
	
	/**
	 * Виджет даты
	 */
	public static final String JEP_DATE_FIELD = JEP_FIELD_PREFIX + "DateField";
	
	/**
	 * Денежный виджет
	 */
	public static final String JEP_MONEY_FIELD = JEP_FIELD_PREFIX + "MoneyField";
	
	/**
	 * Виджет ввода чисел
	 */
	public static final String JEP_NUMBER_FIELD = JEP_FIELD_PREFIX + "NumberField";

	/**
	 * Виджет ввода целых чисел
	 */
	public static final String JEP_INTEGER_FIELD = JEP_FIELD_PREFIX + "IntegerField";

	/** 
	 * Виджет ввода чисел большой разрядности
	 */
	public static final String JEP_LONG_FIELD = JEP_FIELD_PREFIX + "LongField";

	/**
	 * Виджет текстовой области
	 */
	public static final String JEP_TEXT_AREA_FIELD = JEP_FIELD_PREFIX + "TextAreaField";
	
	/**
	 * Виджет для ввода текста
	 */
	public static final String JEP_TEXT_FIELD = JEP_FIELD_PREFIX + "TextField";

	/**
	 * Виджет для ввода текста по шаблону
	 */
	public static final String JEP_MASKED_TEXT_FIELD = JEP_FIELD_PREFIX + "MaskedTextField";

	/**
	 * Виджет времени
	 */
	public static final String JEP_TIME_FIELD = JEP_FIELD_PREFIX + "TimeField";
	
	/**
	 * Виджет загрузки файла
	 */
	public static final String JEP_FILE_FIELD = JEP_FIELD_PREFIX + "FileField";
	
	/**
	 * Виджет загрузки изображений
	 */
	public static final String JEP_IMAGE_FIELD = JEP_FIELD_PREFIX + "ImageField";
	
	/**
	 * Виджет выбора
	 */
	public static final String JEP_CHECKBOX_FIELD = JEP_FIELD_PREFIX + "CheckBoxField";
	
	/**
	 * Виджет группы выбора
	 */
	public static final String JEP_CHECKBOX_GROUP_FIELD = JEP_FIELD_PREFIX + "CheckBoxGroupField";
	
	/**
	 * Виджет выпадающего списка
	 */
	public static final String JEP_COMBOBOX_FIELD = JEP_FIELD_PREFIX + "ComboBoxField";
	
	/**
	 * Виджет список группы выбора
	 */
	public static final String JEP_LIST_FIELD = JEP_FIELD_PREFIX + "ListField";
	
	/**
	 * Виджет дерева
	 */
	public static final String JEP_TREE_FIELD = JEP_FIELD_PREFIX + "TreeField";
	
	/**
	 * Виджет двойного списка с возможностью переноса значений
	 */
	public static final String JEP_DUAL_LIST_FIELD = JEP_FIELD_PREFIX + "DualListField";
	
	/**
	 * Виджет по умолчанию
	 */
	public static final String DEFAULT_WIDGET = JEP_TEXT_FIELD;

	//Список стандартных идентификатор для кнопок тулбара
	
	/**
	 * Идентификатор кнопки назад
	 */
	public static final String BACK_BUTTON_ID = "BACK_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки добавления
	 */
	public static final String ADD_BUTTON_ID = "ADD_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки сохранения
	 */
	public static final String SAVE_BUTTON_ID = "SAVE_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки редактирования
	 */
	public static final String EDIT_BUTTON_ID = "EDIT_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки удаления
	 */
	public static final String DELETE_BUTTON_ID = "DELETE_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки просмотра
	 */
	public static final String VIEW_DETAILS_BUTTON_ID = "VIEW_DETAILS_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки попадания на списочную форму
	 */
	public static final String LIST_BUTTON_ID = "LIST_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки поиска
	 */
	public static final String SEARCH_BUTTON_ID = "SEARCH_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки найти
	 */
	public static final String FIND_BUTTON_ID = "FIND_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки обновления
	 */
	public static final String REFRESH_BUTTON_ID = "REFRESH_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки выбора
	 */
	public static final String SELECT_BUTTON_ID = "SELECT_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки генерации отчетов
	 */
	public static final String REPORT_BUTTON_ID = "REPORT_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки генерации Excel-отчета
	 */
	public static final String EXCEL_BUTTON_ID = "EXCEL_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки печати
	 */
	public static final String PRINT_BUTTON_ID = "PRINT_BUTTON_ID";
	
	/**
	 * Идентификатор кнопки помощи
	 */
	public static final String HELP_BUTTON_ID = "HELP_BUTTON_ID";
	
	
	//Разделители тулбара	
	public static final String BACK_RIGHT_SEPARATOR_ID = "BACK_RIGHT_SEPARATOR_ID";
	public static final String ADD_SEPARATOR_ID = "ADD_SEPARATOR_ID";
	public static final String DELETE_SEPARATOR_ID = "DELETE_SEPARATOR_ID";
	public static final String SEARCH_SEPARATOR_ID = "SEARCH_SEPARATOR_ID";
	public static final String REFRESH_SEPARATOR_ID = "REFRESH_SEPARATOR_ID";
	public static final String HELP_SEPARATOR_ID = "HELP_SEPARATOR_ID";
	
	//Логирование
	
	/**
	 * Префикс полного пути для логгеров log4j
	 */
	public static final String LOG4J_LOGGER_PREFIX = "log4j.logger.";
	
	/**
	 * Префикс в полном пути аппендеров для log4j
	 */
	public static final String LOG4J_APPENDER_PREFIX = "log4j.appender.";
	
	/**
	 * Значение для логгеров в log4j
	 */
	public static final String LOG4J_LOGGER_VALUE = "INFO, FILE, gwt-log";
	
	/**
	 * Обозначение логического ИЛИ
	 */
	public static final String OR = "||";
	
	/**
	 * Английский алфавит в верхнем регистре
	 */
	public static final String ALPHABET_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	/**
	 * Английский алфавит в нижнем регистре
	 */
	public static final String ALPHABET_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
	
	/**
	 * Наименование первичного ключа
	 */
	public static final String RECORD_ID = "RECORD_ID";
	
	/**
	 * HTTP-протокол
	 */
	public static final String HTTP_PROTOCOL = "http";
	
	/**
	 * Стандартный порт http-протокола
	 */
	public static final String DEFAULT_HTTP_PORT = "80";	
	
	/**
	 * OPMN-протокол
	 */
	public static final String OPMN_PROTOCOL = "opmn";
	
	public static final String INHERITS_MAIN_GWT_XML_TAG_NAME = "inherits";
	public static final String EXTEND_PROPERTY_MAIN_GWT_XML_TAG_NAME = "extend-property";
	public static final String SET_PROPERTY_MAIN_GWT_XML_TAG_NAME = "set-property";
	public static final String SET_PROPERTY_FALLBACK_MAIN_GWT_XML_TAG_NAME = "set-property-fallback";
	public static final String JAVA_BUILD_XML_TAG_NAME = "java";
	public static final String ARG_BUILD_XML_TAG_NAME = "arg";
	
	
	public static final String INFO_LOG_LEVEL = "INFO";
	public static final String DEBUG_LOG_LEVEL = "DEBUG";
	
	/**
	 * Кодировка по умолчанию
	 */
	public static final String UTF_8 = "UTF-8";
	
	/**
	 * Идентификатор панели для вывода тестового уведомления
	 */
	public static final String TEST_BUILD_MESSAGE_DIV_ID = "testBuildMessage";
	
	/**
	 * Идентификатор панели для вывода сообщения о загрузке модуля
	 */
	public static final String LOADING_DIV_ID = "loading";
	
	/**
	 * Сообщение, выводимое в случае тестовой сборки
	 */
	public static final String TEST_BUILD_MESSAGE = JepRiaToolkitUtil.multipleConcat(
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
	public static final String TEST_BUILD_MESSAGE_STYLE_VISIBLE = "div.testBuildMessage { display: block; }";
	/**
	 * Стиль для скрытия сообщения о тестовой сборке
	 */
	public static final String TEST_BUILD_MESSAGE_STYLE_HIDDEN = "div.testBuildMessage { display: none; }"; 
	/**
	 * Размерность отступа
	 */
	public static final int INDENT_AMOUNT = 4;
	
	/**
	 * Префикс пакетов
	 */
	public static final String PKG_PREFIX = "pkg_";
	
	/**
	 * Целочисленная величина, значение которой считаем неопределенным.
	 */
	public static final int UNDEFINED_INT = Integer.MIN_VALUE;
	
	/**
	 * Нижнее подчеркивание
	 */
	public static final String UNDERSCORE = "_";
	
	/**
	 * Регулярное выражение для пустых символов
	 */
	public static final String REGEXP_FOR_BLANK = "\\s";
	
	/**
	 * Префикс для вывода сообщения об ошибке
	 */
	public static final String ERROR_PREFIX = "[ERROR] ";
	
	/**
	 * Префикс для вывода сообщения о предупреждении
	 */
	public static final String WARNING_PREFIX = "[WARNING] ";
	
	/**
	 * Префикс для вывода информационного сообщения
	 */
	public static final String INFO_PREFIX = "[INFO] ";
	
	public static final String BUILD_CONFIG_PATH_PREFIX = "config\\"; 
	public static final String BUILD_CONFIG_FILE_NAME = "build.config";
	public static final String DEBUG_BUILD_CONFIG_NAME = "debug";
	public static final String PRODUCTION_BUILD_CONFIG_NAME = "production";
	
	public static final String JEPRIA9_APPLICATION_DEFINITION_RESOURCE_BUNDLE_NAME = "com.technology.jep.jepriatoolkit.creator.release.jepria9.ApplicationDefinition";
	public static final String JEPRIA8_APPLICATION_DEFINITION_RESOURCE_BUNDLE_NAME = "com.technology.jep.jepriatoolkit.creator.release.jepria8.ApplicationDefinition";
	public static final String JEPRIA7_APPLICATION_DEFINITION_RESOURCE_BUNDLE_NAME = "com.technology.jep.jepriatoolkit.creator.release.jepria7.ApplicationDefinition";
	
	public static final String LIB_DIRECTORY_PROPERTY = "fileStructure.libraryDirectory";
	public static final String HTML_DIRECTORY_PROPERTY = "fileStructure.htmlDirectory";
	public static final String RESOURCE_DIRECTORY_PROPERTY = "fileStructure.resourceDirectory";
	public static final String MAIN_MODULE_DIRECTORY_PROPERTY = "fileStructure.mainModuleDirectory";
	public static final String ENTRANCE_DIRECTORY_PROPERTY = "fileStructure.entranceDirectory";
	public static final String MAIN_TEXT_RESOURCE_DIRECTORY_PROPERTY = "fileStructure.mainTextResourceDirectory";
	public static final String CLIENT_MODULE_PLAIN_FORM_DIRECTORY_PROPERTY = "fileStructure.clientModulePlainFormDirectory";
	public static final String CLIENT_MODULE_DETAIL_FORM_DIRECTORY_PROPERTY = "fileStructure.clientModuleDetailFormDirectory";
	public static final String CLIENT_MODULE_LIST_FORM_DIRECTORY_PROPERTY = "fileStructure.clientModuleListFormDirectory";
	public static final String CLIENT_MODULE_EVENTBUS_DIRECTORY_PROPERTY = "fileStructure.clientModuleEventBusDirectory";
	public static final String CLIENT_MODULE_TOOLBAR_DIRECTORY_PROPERTY = "fileStructure.clientModuleToolBarDirectory";
	public static final String CLIENT_MODULE_STATUSBAR_DIRECTORY_PROPERTY = "fileStructure.clientModuleStatusBarDirectory";
	public static final String CLIENT_MODULE_DAO_DIRECTORY_PROPERTY = "fileStructure.clientModuleDaoDirectory";
	public static final String CLIENT_MODULE_SERVICE_IMPL_DIRECTORY_PROPERTY = "fileStructure.clientModuleServiceImplDirectory";
	public static final String CLIENT_MODULE_FIELD_DIRECTORY_PROPERTY = "fileStructure.clientModuleFieldDirectory";
	public static final String CLIENT_MODULE_RECORD_DIRECTORY_PROPERTY = "fileStructure.clientModuleRecordDirectory";
	public static final String CLIENT_MODULE_SERVICE_DIRECTORY_PROPERTY = "fileStructure.clientModuleServiceDirectory";
	public static final String CLIENT_MODULE_TEXT_RESOURCE_DIRECTORY_PROPERTY = "fileStructure.clientTextResourceDirectory";
	public static final String CONFIG_HTML_DIRECTORY_PROPERTY = "fileStructure.configHtmlDirectory";
	public static final String CONFIG_MAIN_PACKAGE_DIRECTORY_PROPERTY = "fileStructure.configMainPackageDirectory";
	
	public static final String DEPLOY_PROPERTIES_SOURCE_PATH_TEMPLATE_PROPERTY = "fileTemplate.deployProperties.path.source";
	public static final String DEPLOY_PROPERTIES_DESTINATION_PATH_TEMPLATE_PROPERTY = "fileTemplate.deployProperties.path.destination";
	
	public static final String LOG4J_PROPERTIES_SOURCE_PATH_TEMPLATE_PROPERTY = "fileTemplate.log4jProperties.path.source";
	public static final String LOG4J_PROPERTIES_DESTINATION_PATH_TEMPLATE_PROPERTY = "fileTemplate.log4jProperties.path.destination";
	public static final String LOG4J_PROPERTIES_CODE_DESTINATION_PATH_TEMPLATE_PROPERTY = "fileTemplate.log4jProperties.path.codeDestination";
	
	public static final String WEB_XML_TEMPLATE_PROPERTY = "fileTemplate.webXml";
	public static final String WEB_XML_PATH_TEMPLATE_PROPERTY = "fileTemplate.webXml.path";
	
	public static final String APPLICATION_XML_TEMPLATE_PROPERTY = "fileTemplate.applicationXml";
	public static final String APPLICATION_XML_PATH_TEMPLATE_PROPERTY = "fileTemplate.applicationXml.path";
	
	public static final String ORION_APPLICATION_XML_TEMPLATE_PROPERTY = "fileTemplate.orionApplicationXml";
	public static final String ORION_APPLICATION_XML_PATH_TEMPLATE_PROPERTY = "fileTemplate.orionApplicationXml.path";
	
	public static final String MAIN_GWT_XML_DEBUG_TEMPLATE_PROPERTY = "fileTemplate.mainGwtXml.debug";
	public static final String MAIN_GWT_XML_PRODUCTION_TEMPLATE_PROPERTY = "fileTemplate.mainGwtXml.production";
	public static final String MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainGwtXml.path";
	
	public static final String CLIENT_MODULE_GWT_XML_TEMPLATE_PROPERTY = "fileTemplate.clientModuleGwtXml";
	public static final String CLIENT_MODULE_GWT_XML_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleGwtXml.path";
	
	public static final String APPLICATION_JSP_TEMPLATE_PROPERTY = "fileTemplate.applicationJsp";
	public static final String APPLICATION_JSP_PATH_TEMPLATE_PROPERTY = "fileTemplate.applicationJsp.path";
	
	public static final String APPLICATION_CSS_PATH_TEMPLATE_PROPERTY = "fileTemplate.applicationCss.path";
	
	public static final String MAIN_TEXT_RESOURCE_TEMPLATE_PROPERTY = "fileTemplate.mainText";
	public static final String MAIN_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainText.path";
	
	public static final String MAIN_TEXT_RESOURCE_EN_TEMPLATE_PROPERTY = "fileTemplate.mainTextEn";
	public static final String MAIN_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainTextEn.path";
	
	public static final String CLIENT_MODULE_TEXT_RESOURCE_TEMPLATE_PROPERTY = "fileTemplate.clientModuleText";
	public static final String CLIENT_MODULE_TEXT_RESOURCE_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleText.path";
	
	public static final String CLIENT_MODULE_TEXT_RESOURCE_EN_TEMPLATE_PROPERTY = "fileTemplate.clientModuleTextEn";
	public static final String CLIENT_MODULE_TEXT_RESOURCE_EN_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleTextEn.path";
	
	public static final String OVERVIEW_TEMPLATE_PROPERTY = "fileTemplate.overview";
	public static final String OVERVIEW_PATH_TEMPLATE_PROPERTY = "fileTemplate.overview.path";
	
	public static final String CLIENT_MODULE_CONSTANT_TEMPLATE_PROPERTY = "fileTemplate.clientConstant";
	public static final String CLIENT_MODULE_CONSTANT_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientConstant.path";
	
	public static final String MAIN_MODULE_CONSTANT_TEMPLATE_PROPERTY = "fileTemplate.mainConstant";
	public static final String MAIN_MODULE_CONSTANT_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainConstant.path";
	
	public static final String CLIENT_MODULE_FACTORY_TEMPLATE_PROPERTY = "fileTemplate.clientFactory";
	public static final String CLIENT_MODULE_FACTORY_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientFactory.path";
	
	public static final String MAIN_MODULE_FACTORY_TEMPLATE_PROPERTY = "fileTemplate.mainFactory";
	public static final String MAIN_MODULE_FACTORY_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainFactory.path";
	
	public static final String MODULE_ENTRY_POINT_TEMPLATE_PROPERTY = "fileTemplate.moduleEntryPoint";
	public static final String MODULE_ENTRY_POINT_PATH_TEMPLATE_PROPERTY = "fileTemplate.moduleEntryPoint.path";
	
	public static final String CLIENT_MODULE_DETAIL_FORM_PRESENTER_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDetailFormPresenter";
	public static final String CLIENT_MODULE_DETAIL_FORM_PRESENTER_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDetailFormPresenter.path";
	
	public static final String CLIENT_MODULE_DETAIL_FORM_VIEW_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDetailFormView";
	public static final String CLIENT_MODULE_DETAIL_FORM_VIEW_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDetailFormView.path";
	
	public static final String CLIENT_MODULE_DETAIL_FORM_VIEW_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDetailFormViewImpl";
	public static final String CLIENT_MODULE_DETAIL_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDetailFormViewImpl.path";
	
	public static final String CLIENT_MODULE_LIST_FORM_PRESENTER_TEMPLATE_PROPERTY = "fileTemplate.clientModuleListFormPresenter";
	public static final String CLIENT_MODULE_LIST_FORM_PRESENTER_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleListFormPresenter.path";
	
	public static final String CLIENT_MODULE_LIST_FORM_VIEW_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleListFormViewImpl";
	public static final String CLIENT_MODULE_LIST_FORM_VIEW_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleListFormViewImpl.path";
	
	public static final String CLIENT_MODULE_PRESENTER_TEMPLATE_PROPERTY = "fileTemplate.clientModulePresenter";
	public static final String CLIENT_MODULE_PRESENTER_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModulePresenter.path";
	
	public static final String MAIN_MODULE_PRESENTER_TEMPLATE_PROPERTY = "fileTemplate.mainModulePresenter";
	public static final String MAIN_MODULE_PRESENTER_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainModulePresenter.path";
	
	public static final String CLIENT_MODULE_SERVICE_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleServiceImpl";
	public static final String CLIENT_MODULE_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleServiceImpl.path";
	
	public static final String CLIENT_MODULE_UPLOAD_SERVICE_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleUploadServiceImpl";
	public static final String CLIENT_MODULE_UPLOAD_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleUploadServiceImpl.path";
	
	public static final String CLIENT_MODULE_DOWNLOAD_SERVICE_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDownloadServiceImpl";
	public static final String CLIENT_MODULE_DOWNLOAD_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDownloadServiceImpl.path";
	
	public static final String CLIENT_MODULE_EXCEL_SERVICE_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleExcelServiceImpl";
	public static final String CLIENT_MODULE_EXCEL_SERVICE_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleExcelServiceImpl.path";
	
	public static final String CLIENT_MODULE_SERVER_CONSTANT_TEMPLATE_PROPERTY = "fileTemplate.clientModuleServerConstant";
	public static final String CLIENT_MODULE_SERVER_CONSTANT_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleServerConstant.path";
	
	public static final String CLIENT_MODULE_DAO_INTERFACE_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDaoInterface";
	public static final String CLIENT_MODULE_DAO_INTERFACE_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDaoInterface.path";
	
	public static final String CLIENT_MODULE_DAO_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDao";
	public static final String CLIENT_MODULE_DAO_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleDao.path";
	
	public static final String CLIENT_MODULE_FIELDS_TEMPLATE_PROPERTY = "fileTemplate.clientModuleFields";
	public static final String CLIENT_MODULE_FIELDS_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleFields.path";
	
	public static final String CLIENT_MODULE_OPTIONS_TEMPLATE_PROPERTY = "fileTemplate.clientModuleOptions";
	public static final String CLIENT_MODULE_OPTIONS_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleOptions.path";
	
	public static final String CLIENT_MODULE_RECORD_DEFINITION_TEMPLATE_PROPERTY = "fileTemplate.clientModuleRecordDefinition";
	public static final String CLIENT_MODULE_RECORD_DEFINITION_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleRecordDefinition.path";
	
	public static final String CLIENT_MODULE_SERVICE_TEMPLATE_PROPERTY = "fileTemplate.clientModuleService";
	public static final String CLIENT_MODULE_SERVICE_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleService.path";
	
	public static final String CLIENT_MODULE_SERVICE_ASYNC_TEMPLATE_PROPERTY = "fileTemplate.clientModuleServiceAsync";
	public static final String CLIENT_MODULE_SERVICE_ASYNC_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleServiceAsync.path";
	
	public static final String CLIENT_MODULE_SHARED_CONSTANT_TEMPLATE_PROPERTY = "fileTemplate.clientModuleSharedConstant";
	public static final String CLIENT_MODULE_SHARED_CONSTANT_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleSharedConstant.path";
	
	public static final String MAIN_MODULE_SHARED_CONSTANT_TEMPLATE_PROPERTY = "fileTemplate.mainModuleSharedConstant";
	public static final String MAIN_MODULE_SHARED_CONSTANT_PATH_TEMPLATE_PROPERTY = "fileTemplate.mainModuleSharedConstant.path";
	
	public static final String CLIENT_MODULE_STATUSBAR_TEMPLATE_PROPERTY = "fileTemplate.clientModuleStatusBar";
	public static final String CLIENT_MODULE_STATUSBAR_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleStatusBar.path";
	
	public static final String CLIENT_MODULE_TOOLBAR_VIEW_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarView";
	public static final String CLIENT_MODULE_TOOLBAR_VIEW_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarView.path";
	
	public static final String CLIENT_MODULE_TOOLBAR_VIEW_IMPL_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarViewImpl";
	public static final String CLIENT_MODULE_TOOLBAR_VIEW_IMPL_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarViewImpl.path";
	
	public static final String CLIENT_MODULE_TOOLBAR_PRESENTER_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarPresenter";
	public static final String CLIENT_MODULE_TOOLBAR_PRESENTER_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarPresenter.path";
	
	public static final String CLIENT_MODULE_EVENTBUS_TEMPLATE_PROPERTY = "fileTemplate.clientModuleEventBus";
	public static final String CLIENT_MODULE_EVENTBUS_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleEventBus.path";
	
	public static final String CLIENT_MODULE_EVENT_TEMPLATE_PROPERTY = "fileTemplate.clientModuleEvent";
	public static final String CLIENT_MODULE_EVENT_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleEvent.path";
	
	public static final String CLIENT_MODULE_IMAGES_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarImages";
	public static final String CLIENT_MODULE_IMAGES_PATH_TEMPLATE_PROPERTY = "fileTemplate.clientModuleToolBarImages.path";
	
	public static final String CURRENT_DIRECTORY_ENVIRONMENT_VARIABLE = "user.dir";
	
	public static final String MODULE_NAME_TEMPLATE_PARAMETER = "moduleName";
	public static final String PACKAGE_NAME_TEMPLATE_PARAMETER = "packageName";
	public static final String SECURITY_ROLES_TEMPLATE_PARAMETER = "securityRoles";
	public static final String FORMS_TEMPLATE_PARAMETER = "forms";
	public static final String FORM_TEMPLATE_PARAMETER = "form";
	public static final String FIELD_TEMPLATE_PARAMETER = "field";
	public static final String BUTTON_TEMPLATE_PARAMETER = "button";
	public static final String HAS_TEXT_FILE_TEMPLATE_PARAMETER = "hasTextFile";
	public static final String HAS_BINARY_FILE_TEMPLATE_PARAMETER = "hasBinaryFile";
	public static final String FORM_NAME_TEMPLATE_PARAMETER = "formName";
	
	public static final String FILE_STRUCTURE_PATTERN = "\\{([^}]*.?)\\}";
	
	public static final String LEFT_CURLY_BRACKET = "{";
	
	public static final String DEFAULT_CHARSET = "windows-1251";
	
	public static final String DOT = ".";
	
	public static final String MODULE_NAME_TASK_ATTRIBUTE = "MODULE_NAME";
	public static final String PARENT_MODULE_NAME_TASK_ATTRIBUTE = "PARENT_MODULE_NAME";
	public static final String APPLICATION_NAME_TASK_ATTRIBUTE = "APPLICATION_NAME";
	public static final String APPLICATION_STRUCTURE_FILE_PATH_TASK_ATTRIBUTE = "APPLICATION_STRUCTURE_FILE_PATH";
	public static final String SKIP_BUILD_AND_DEPLOY_TASK_ATTRIBUTE = "SKIP_BUILD_AND_DEPLOY";
	public static final String FORMS_TASK_ATTRIBUTE = "FORMS";
	public static final String TRUE_TASK_ATTRIBUTE = "1";
	
	public static final String BUILD_CONFIG_PROPERTY = "BUILD_CONFIG";
	
	
	public static final String ALL_TEXT_ENCODE_TASK_TARGET = "all-text-encode";
	public static final String CREATE_STRUCTURE_TASK_TARGET = "create-structure";
	public static final String CLEAN_TASK_TARGET = "clean";
	public static final String BUILD_AND_DEPLOY_TASK_TARGET = "build-and-deploy";
	
	public static final String OUTPUT_LOG_FILE = "createStucture.log";
	public static final String JEP_APPLICATION_XML = "JepApplication.xml";
	public static final String YES = "y";
	public static final String NO = "n";
	public static final String XML_EXTENSION = ".xml";
	
	/**
	 * Размер создаваемого пула нитей для возможности распараллеливания выполнения задач
	 */
	public static final int THREAD_POOL_SIZE = 5;
}