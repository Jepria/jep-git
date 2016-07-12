package com.technology.jep.jepcommon;

import java.awt.AWTEvent;
import java.nio.charset.Charset;

public interface JepConstant {
  //Общие.
  public static final String LOCAL_LANG = "ru";//Язык, который считается основным для пользователей.
  public static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";//Формат даты по умолчанию.
  public static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");

  //Целочисленная величина, значение которой считаем неопределенным.
  public static final int UNDEFINED_INT = Integer.MIN_VALUE;
  //Пользователь Гость.
  public static final String GUEST_LOGIN = "Guest";//Логин.
  public static final String GUEST_PASSWORD = "Guest";//Пароль.

  //Тип формы.
  public static final int FT_LIST = 0;//Форма списочной структуры.
  public static final int FT_EDIT = 1;//Форма редактирования.

  //Режимы форм.
  public static final int EM_BROWSE = 0;//Просмотр.
  public static final int EM_ADD = 1;//Добавление новой записи.
  public static final int EM_EDIT = 2;//Редактирование.
  public static final int EM_SEARCH = 3;//Поиск.
  public static final int EM_DEFAULT = EM_SEARCH;//Режим по умолчанию
  
  //События форм (нажатие кнопок и т.п.).
  //Стандартные.
  public static final int EV_BACK = 0;//Вернуться на предыдущую страницу.
  public static final int EV_BROWSE = 1;//Просмотреть.
  public static final int EV_ADD = 2;//Добавить новую запись.
  public static final int EV_SAVE = 3;//Сохранить изменения.
  public static final int EV_EDIT = 4;//Редактировать.
  public static final int EV_DELETE = 5;//Удалить.
  public static final int EV_REFRESH = 6;//Обновить.
  public static final int EV_ORDER_BY = 7;//Упорядочить.
  public static final int EV_SEARCH = 8;//Поиск.
  public static final int EV_FIND = 9;//Найти.
  public static final int EV_FIND_ALL = 10;//Отобразить все записи.
  public static final int EV_SCROLL_UP = 11;//Прокрутить данные вверх.
  public static final int EV_SCROLL_DOWN = 12;//Прокрутить данные вниз.
  public static final int EV_PRINT = 13;//Печать.
  public static final int EV_CLEAR = 14;//Очистить.
  public static final int EV_FILL = 15;//Заполнить.
  public static final int EV_TAB = 16;//Перейти на страницу диалога-книжки.
  public static final int EV_EXCEL = 17;//Вывести в Excel.
  public static final int EV_SELECT = 18;//Выбрать.
  public static final int EV_HELP = 19;//Отобразить справку.
  public static final int EV_EXPIRED = 20;//Устаревание сессии. 

  /**
   * Пользовательские события. С этого идентификатора идут события,
   * определяемые пользователем. В константах модуля, в котором используется
   * пользовательское события, определяем, например:<br/>
   * <br/>
   * <code>
   * public static final int EV_CREATE_TO_OUTBOUND_LIST = EV_CUSTOM + 1;//Сформировать список на исходящий обзвон.<br/>
   * public static final int EV_TO_OUTBOUND = EV_CUSTOM + 2;//Отправить на исходящий обзвон.
  * </code>
   */
  public static final int EV_CUSTOM = AWTEvent.RESERVED_ID_MAX + 20000;//С этого идентификатора идут события, определяемые пользователем.

  // Имена атрибутов Web-сессий и Web-запросов

  /**
   * Имя атрибута сессии, в котором хранится объект класса JepSecurityModule.
   * 
   * @see com.technology.jep.jepcommon.security.JepSecurityModule
   */
  public static final String JEP_SECURITY_MODULE_ATTRIBUTE_NAME = "jepSecurityModule";

  /**
   * Идентификатор файла ресурсов.
   */
  public static final String JEP_RESOURCE_BUNDLE_NAME = "com.technology.jep.jepcommon.text.JepCommon";

  /**
   * Имя (Key), под которым хранятся ресурсы с НЕуказанным параметром Key в
   * struts-config.xml (тег &lt;message-resources/&gt;).
   */
  public static final String MESSAGE_RESOURCES_UNDEFINED_KEY = "org.apache.struts.action.MESSAGE";

  /**
   * Параметр / JNDI-имя, в котором передается URL контекст для формирования
   * полного URL для вызова EJB модуля. Например, для полного URL вызова EJB
   * вида "ormi://localhost:23791/Ejb3Template" в данном параметре будет
   * содержаться "ormi://localhost:23791". Данная константа используется для
   * вызова EJB расположенного на том же инстансе OC4J, что и вызывающий
   * модуль.<br/>
   * Если необходимо вызвать EJB расположенный на другом инстансе и (или)
   * Application сервере, то в константах модуля создается дополнительная
   * (-ые) константы вида: <br/>
   * <code>
   * static final String ENVIRONMENT_EJB_URL_CONTEXT_XXXX = "ejbUrlContextXxxx";
   * </code> <br/>
   * , где Xxxx - "логическое" (осмысленное) имя другого инстанса и (или)
   * Application сервера.
   */
  public static final String ENVIRONMENT_EJB_URL_CONTEXT = "ejbUrlContext";

  /**
   * Префикс-признак зашифрованного текста. Во избежание зависимостей между модулями
   * константа дублируется с JepApplet
   */
  public static final String ENCODING_PREFIX = "ENCODED_";

  /**
   * Имя параметра http-запроса: mtSID
   */
  public static final String HTTP_REQUEST_PARAMETER_MTSID = "mtSID";

  /**
   * Имя параметра http-запроса: язык текущей межмодульной сессии
   */
  public static final String HTTP_REQUEST_PARAMETER_LANG = "lang";

  /**
   * Имя параметра http-запроса: локаль текущей межмодульной сессии
   */
  public static final String HTTP_REQUEST_PARAMETER_LOCALE = "locale";

  /**
   * Имя параметра http-запроса: последнее событие HTTP-запроса
   */
  public static final String HTTP_REQUEST_PARAMETER_LAST_EVENT_IN_REQUEST = "lastEventInRequest";

  /**
   * Параметр, в котором передается наименование фрейма, который необходимо
   * открыть при отображении апплета.
   */
  public static final String APPLET_PARAMETER_FRAME = "frame";

  /**
   * Имя параметра апплета: lang
   */
  public static final String APPLET_PARAMETER_LANG = HTTP_REQUEST_PARAMETER_LANG;

  /**
   * Имя параметра апплета: roles
   */
  public static final String APPLET_PARAMETER_ROLES = "roles";

  /**
   * Имя параметра апплета: operatorId
   */
  public static final String APPLET_PARAMETER_OPERATOR_ID = "operatorId";

    /**
   * Имя параметра апплета: последнее событие HTTP-запроса
   */
  public static final String APPLET_PARAMETER_LAST_EVENT_IN_REQUEST = HTTP_REQUEST_PARAMETER_LAST_EVENT_IN_REQUEST;

  /**
   * Роль, через которую работают апплеты, взаимодействующие с EJB.
   * Должна быть сконфигурирована на сервере приложений (см. JavaDoc пакета com.technology.jep.tag.applet).
   */
  public static final String APPLET_USER_ROLE = "JepAppletUser";

  /**
   * Способ шифрования DEFAULT_ENCRYPTION_METHOD
   */
  public static final String DEFAULT_ENCRYPTION_METHOD = "default";

  /**
   * JNDI-имя EJB BinaryFileUpload.
   */
  public static final String BINARY_FILE_UPLOAD_BEAN_JNDI_NAME = "BinaryFileUploadBean";

  /**
   * JNDI-имя EJB TextFileUpload.
   */
  public static final String TEXT_FILE_UPLOAD_BEAN_JNDI_NAME = "TextFileUploadBean";

  /**
   * JNDI-имя EJB BinaryFileDownload.
   */
  public static final String BINARY_FILE_DOWNLOAD_BEAN_JNDI_NAME = "BinaryFileDownloadBean";

  /**
   * JNDI-имя EJB TextFileDownload.
   */
  public static final String TEXT_FILE_DOWNLOAD_BEAN_JNDI_NAME = "TextFileDownloadBean";
  
  /**
   * Базовая директория всех систем
   */
  public static final String BASE_DIRECTORY = "resources/com/technology/";
  
  /**
   * Базовый пакет всех систем
   */
  public static final String BASE_PACKAGE = "com.technology";

  /**
   * Имя свойства принципала, содержащего operatorId
   */
  public static final String PRINCIPAL_PROPERTY_NAME_OPERATOR_ID = "operatorId";

  /**
   * Имя свойства принципала, содержащего идентификатор межмодульной сессии (imsId)
   */
  public static final String PRINCIPAL_PROPERTY_NAME_IMS_ID = "imsId";
  
  /**
   * Атрибут, отслеживающий актуальность сессии 
   */
  public static final String IS_SESSION_ACTUAL_ATTRIBUTE = "isSessionActual";
  
  /**
   * Наименование HTTP header'а, который хранит оригинальный ip-адрес клиента, если используется proxy-сервер
   */
  static final String HEADER_X_FORWARDED_FOR = "X-FORWARDED-FOR";
    
  /**
   * Расширение файлов - текстовых ресурсов
   */
  static final String PROPERTY_EXTENSION = ".properties";
  
  /**
   * Суффикс, подставляемый в логин для указания того, что авторизация будет осуществлена по хэшу пароля
   */
  static final String LOGIN_SUFFIX_FOR_HASH_AUTHORIZATION = String.valueOf((char) 2).concat("hashed");
  
  
  /**
   * JNDI-имя источника данных модуля.
   */
  static final String DATA_SOURCE_JNDI_NAME = "jdbc/RFInfoDS";
}