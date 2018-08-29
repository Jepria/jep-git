package com.technology.jep.jepria.client;

import static com.google.gwt.core.client.GWT.HOSTED_MODE_PERMUTATION_STRONG_NAME;

import com.google.gwt.core.client.GWT;
import com.technology.jep.jepria.client.images.JepImages;
import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.jep.jepria.shared.text.JepRiaText;

public class JepRiaClientConstant extends JepRiaConstant {

  /**
   * Признак отладочного режима (Hosted Mode).
   */
  public static final boolean IS_HOSTED_MODE = GWT.getPermutationStrongName().equals(HOSTED_MODE_PERMUTATION_STRONG_NAME);

  /**
   * Идентификатор элемента на host-страницы, к которому привязывается ("bind'ится") корневая панель приложения (панель, на которой располагается
   * всё приложение).
   */
  public static final String APPLICATION_SLOT = "JepRiaAppSlot";

  /**
   * Ширина меток полей формы по умолчанию.
   */
  public static final int FIELD_LABEL_DEFAULT_WIDTH = 200;
  
  public static final String FIELD_INVALID_COLOR = "#c30";
  
  /**
   * Ширина полей формы по умолчанию.
   */
  public static final int FIELD_DEFAULT_WIDTH = 200;
  
  /**
   * Высота полей формы по умолчанию.
   */
  public static final int FIELD_DEFAULT_HEIGHT = 20;
  
  /**
   * Имя параметра запроса, в котором указывается имя отображаемого модуля.
   */
  public static final String ENTRY_MODULE_NAME_REQUEST_PARAMETER = "em";
  /**
   * Имя параметра запроса, в котором указывается состояние (из WorkstateEnum) отображаемого модуля.
   */
  public static final String ENTRY_STATE_NAME_REQUEST_PARAMETER = "es";

  /**
   * Тексты JepRia.
   */
  public static final JepRiaText JepTexts = (JepRiaText) GWT.create(JepRiaText.class);
  
  /**
   * Изображения JepRia.
   */
  public static final JepImages JepImages = (JepImages) GWT.create(JepImages.class);
  
  /**
   * Минимальный промежуток времени (в милисекундах), необходимый для срабатывания события {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }
   */
  public static final int TYPING_TIMEOUT_DEFAULT_VALUE = 300;
  
  /**
   * Минимальный количество символов, необходимых для срабатывания события {@link com.technology.jep.jepria.client.widget.event.JepEventType#TYPING_TIMEOUT_EVENT }
   */
  public static final int TYPING_TIMEOUT_MIN_TEXT_SIZE = 1;

  /**
   * Интервал автоматического обновления по умолчанию (в милисекундах).
   */
  public static final int DEFAULT_REFRESH_DELAY = 20000;
  
  /**
   * Подстрока, наличие которой в ответе сервлета загрузки на сервер сигнализирует об успешном окончании загрузки.
   */
  public static final String UPLOAD_SUCCESS_SUBSTRING = "success";
  
  /**
   * Префикс классов стилей. <br/>
   * <br/>TODO: заменить написание префикса во всех классах стилей на данную константу. 
   */
  public static final String STYLE_PREFIX = "jepRia-";
  
  /**
   * Наименование селектора (класса стилей) основного шрифта приложений.
   */
  public static final String MAIN_FONT_STYLE = "jepRia-FontStyle";
  
  /**
   * Наименование селектора (класса стилей) текстовой области ввода.
   */
  public static final String TEXT_AREA_STYLE = "jepRia-TextArea-Input";
  
  /**
   * Наименование селектора (класса стилей) индикатора обязательности поля.
   */
  public static final String FIELD_MANDATORY_STYLE = "jepRia-Field-Mandatory";
  
  /**
   * Признак обязательности в названии поля.
   */
  public static final String REQUIRED_MARKER = "<span {0} title='" + JepTexts.field_blankText() + "' class='" + FIELD_MANDATORY_STYLE + "'>*</span>&nbsp;";
  
  /**
   * Маска, соответствующая стандартному формату представления даты.
   */
  public static final String DEFAULT_DATE_FORMAT_MASK = "00.00.0000";
  
  /**
   * Маска, соответствующая формату представления даты и время
   */
  public static final String DEFAULT_DATE_TIME_FORMAT_MASK = "00.00.0000 00:00:00";
  
  /**
   * Маска, соответствующая дате без указания дня, только месяц и год.
   */
  public static final String DEFAULT_DATE_MONTH_AND_YEARS_ONLY_FORMAT_MASK = "00.0000";
  
  /**
   * Маска, соответствующая дате без указания дня, только год.
   */
  public static final String DEFAULT_DATE_YEARS_ONLY_FORMAT_MASK = "0000";
  
  /**
   * Маска, соответствующая стандартному формату представления времени.
   */
  public static final String DEFAULT_TIME_FORMAT_MASK = "00:00:00";
  
  /**
   * Маска, соответствующая сокращённому формату представления времени.
   */
  public static final String SHORT_TIME_FORMAT_MASK = "00:00";
  
  /**
   * Наименование CSS-класса стилей для панели инструментов по умолчанию.
   */
  public static final String TOOLBAR_DEFAULT_STYLE = "jepRia-ToolBar";
  
  /**
   * Наименование CSS-класса стилей для панели состояния по умолчанию.
   */
  public static final String STATUSBAR_DEFAULT_STYLE = "jepRia-StatusBar";
  
  /**
   * Наименование CSS-класса стилей для контейнера Test Build Message.
   */
  public static final String TEST_BUILD_MESSAGE_CLASS = "jepRia-testBuildMessage";
  
  /**
   * Свойство перемещаемого объекта, требующее инициализации при событии {@link com.technology.jep.jepria.client.widget.event.JepEventType#DRAG_START_EVENT}
   * для корректной работы DragAndDrop.
   */
  public static final String DND_DATA_PROPERTY = "Text";
  
  /*
   * Вид панели в календаре в формате dd.MM.yyyy
   */
  public static final int PANEL_OF_DAYS_AND_MONTH_AND_YEAR = 0;
  
  
  /*
   * Вид панели в календаре в формате dd.MM.yyyy HH:mm:ss
   */
  public static final int PANEL_OF_DAYS_AND_MONTH_AND_YEAR_TIME = 4;
  
  
  /*
   * Вид панели в календаре в формате MM.yyyy
   */
  public static final int PANEL_OF_MONTH_AND_YEAR_ONLY = 1;
  
  /*
   * Вид панели в календаре в формате yyyy
   */
  public static final int PANEL_OF_YEAR_ONLY = 2;
}
