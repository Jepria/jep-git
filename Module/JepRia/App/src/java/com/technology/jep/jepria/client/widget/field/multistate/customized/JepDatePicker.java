package com.technology.jep.jepria.client.widget.field.multistate.customized;

import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_DAYS_AND_MONTH_AND_YEAR;
import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_DAYS_AND_MONTH_AND_YEAR_TIME;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarModel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DefaultCalendarView;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.shared.text.JepRiaText;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

public abstract class JepDatePicker extends DatePicker {

  public static final JepRiaText JepTexts = (JepRiaText) GWT.create(JepRiaText.class);
  
  private MonthAndYearSelector monthSelector;
  
  public JepDatePicker() {
    this(PANEL_OF_DAYS_AND_MONTH_AND_YEAR);
  }
  
  public JepDatePicker(int showPanelElements) {
    super(new MonthAndYearSelector(showPanelElements), new DefaultCalendarView(), new CalendarModel());
    monthSelector = (MonthAndYearSelector) this.getMonthSelector();
    monthSelector.setPicker(this);
    monthSelector.setModel(this.getModel());
    
    if (showPanelElements == PANEL_OF_DAYS_AND_MONTH_AND_YEAR_TIME
        || showPanelElements == PANEL_OF_DAYS_AND_MONTH_AND_YEAR) {
      HorizontalPanel hPanel = new HorizontalPanel();
      ((VerticalPanel)getWidget()).getElement().getStyle().setBackgroundColor("white");
      hPanel.getElement().setAttribute("align", "right");
      hPanel.getElement().setAttribute("cellpadding", "5px");
      if (showPanelElements == PANEL_OF_DAYS_AND_MONTH_AND_YEAR_TIME) {
        hPanel.add(createTime());
      }
      hPanel.add(createButtonToday(showPanelElements));
      ((VerticalPanel)getWidget()).add(hPanel);
    }
  }
  
  /**
   * Создаем кнопку под панелью календаря
   * @param kindPresentPanelElements вид панели элементов навигации для календаря
   * @return widget
   */
  
  protected Widget createButtonToday(int kindPresentPanelElements) {
    LocaleInfo locale = LocaleInfo.getCurrentLocale();
    
    HorizontalPanel vPanel = new HorizontalPanel();
    
    Button todayButton = new Button();
    
    if (locale.getLocaleName().equalsIgnoreCase("ru")) {
      if (kindPresentPanelElements == PANEL_OF_DAYS_AND_MONTH_AND_YEAR_TIME) {
        todayButton.setHTML("<b>" + JepTexts.button_now() + "</b>");
      } else if (kindPresentPanelElements == PANEL_OF_DAYS_AND_MONTH_AND_YEAR) {
        todayButton.setHTML("<b>" + JepTexts.button_today() + "</b>");
      }
      
    } else {
      if (kindPresentPanelElements == PANEL_OF_DAYS_AND_MONTH_AND_YEAR_TIME) {
        todayButton.setHTML("<b>" + JepTexts.button_now() + "</b>");
      } else if (kindPresentPanelElements == PANEL_OF_DAYS_AND_MONTH_AND_YEAR) {
        todayButton.setHTML("<b>" + JepTexts.button_today() + "</b>");
      }
      
    }
    todayButton.setStyleName("datePickerMonthSelector");
    todayButton.getElement().getStyle().setHeight(21, Unit.PX);
    
    todayButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickevent) {
        refresh(new Date());
        doFireEventClickDatePicker();
      }
    });
    vPanel.add(todayButton);
    return vPanel;
  }
  
  /**
   * Обновляем содержимое панели календаря
   */
  
  public void refreshComponents() {
    super.refreshAll();
  }
  
  /**
   * Обновляем навигационной панели календаря 
   * @param newDate
   */
  public void refresh(Date newDate) {
    monthSelector.setMonth(newDate.getMonth());
    monthSelector.setYear(newDate.getYear());
    this.setValue(newDate);
    setTime(newDate);
  }
  
  private TextBox hours;
  private TextBox minutes;
  private TextBox seconds;
  
  /**
   * Создаем поля редактирования для ввода времени 
   * @return widget
   */
  protected Widget createTime() {
    HorizontalPanel vPanel = new HorizontalPanel();
    vPanel.getElement().getStyle().setHeight(22, Unit.PX);
    vPanel.add(hours = initHoursField());
    vPanel.add(new Label(":"));
    vPanel.add(minutes = initMinuteField());
    vPanel.add(new Label(":"));
    vPanel.add(seconds = initSecondsField());
    
    initFocusHandlers();
    
    return vPanel;
  }
  
  private TextBox initHoursField() {
    TextBox  widget = new TextBox();
    widget.setMaxLength(2);
    Date date = new Date();
    widget.setText(String.valueOf(date.getHours()));
    widget.getElement().getStyle().setWidth(18, Unit.PX);
    appendHandlers(widget, 23, minutes);
    
    return widget;
  }
  
  private TextBox initMinuteField() {
    TextBox  widget = new TextBox();
    widget.setMaxLength(2);
    Date date = new Date();
    widget.setText(String.valueOf(date.getMinutes()));
    widget.getElement().getStyle().setWidth(18, Unit.PX);
    appendHandlers(widget, 59, seconds);
    return widget;
  }
  
  private TextBox initSecondsField() {
    final TextBox  widget = new TextBox();
    widget.setMaxLength(2);
    Date date = new Date();
    widget.setText(String.valueOf(date.getSeconds()));
    widget.getElement().getStyle().setWidth(18, Unit.PX);
    appendHandlers(widget, 59, hours);
    return widget;
  }
  
  /**
   * Выделяем все содержимое соответствующего элемента
   * @param element
   */
  private native void selectAll(Element element) /*-{
    element.setSelectionRange(0, element.value.length);
  }-*/;
  
  /**
   * Устанавливаем время
   * @param newTime
   */
  protected void setTime(Date newTime) {
    if (hours != null && minutes != null && seconds != null && newTime != null) {
      hours.setValue(String.valueOf(newTime.getHours()));
      minutes.setValue(String.valueOf(newTime.getMinutes()));
      seconds.setValue(String.valueOf(newTime.getSeconds()));
    }
  }
  
  /**
   * Устанавливаем видимость панели навигации по дням месяца   
   * @param visible
   */
  public void setVisibleDaysPanel(boolean visible) {
    getView().setVisible(visible);
  }
  
  /**
   * Видимость панели с календарем
   * @return
   */
  public boolean isVisibleDaysPanel() {
    return getView().isVisible();
  }
  
  /**
   * Устанавливаем видимость панели календаря
   * @param visible
   */
  public void setVisibleNavigationPanel(boolean visible) {
    monthSelector.setVisibleNavigationPanel(visible);
  }
  
  /**
   * @return получить актуальную дату из панели календаря
   */
  public Date getActualDate() {
    Date date = getValue();

    if (hours != null && minutes != null && seconds != null && date != null) {
      date.setHours(Integer.valueOf(JepRiaUtil.isEmpty(hours.getText()) ? defaultValue : hours.getText()));
      date.setMinutes(Integer.valueOf(JepRiaUtil.isEmpty(minutes.getText()) ? defaultValue : minutes.getText()));
      date.setSeconds(Integer.valueOf(JepRiaUtil.isEmpty(seconds.getText()) ? defaultValue : seconds.getText()));
    }
    
    if (monthSelector.getModel().getCurrentMonth() != null && date != null) {
      date.setMonth(monthSelector.getModel().getCurrentMonth().getMonth());
      date.setYear(monthSelector.getModel().getCurrentMonth().getYear());
    }
    
    return date;
  }
  
  /**
   * Проверка на нажатие допустимых клавиш с клавиатуры 
   * @param key код нажатой клавиши
   * @return Истина - допустимо, Ложь - недопусимо
   */
  
  protected boolean isPermitKey(int key) {
    return isDecimalKey(key) 
        || key >= 96 && key <= 105 || key == KeyCodes.KEY_BACKSPACE || key ==KeyCodes.KEY_DELETE 
        || key ==KeyCodes.KEY_RIGHT || key ==KeyCodes.KEY_LEFT || key ==KeyCodes.KEY_TAB;
  }
  
  /**
   * Проверка на допустимость символов
   * @param chs массив проверяемых символов
   * @return Истина - допустимо, Ложь - недопусимо
   */
  protected boolean isPermitValue(char[] chs) {
    boolean result = chs != null && chs.length > 0 ? true : false;
    for (char ch : chs) {
      if (!(isDecimalKey((int)ch) || ch >= 96 && ch <= 105)) {
        result = false;
        break;
      }
    }
    return result;
  }
  
  /**
   * Проверка, что код символа соответствует коду числа
   * @param key код символа
   * @return Истина - допустимо, Ложь - недопусимо
   */
  protected boolean isDecimalKey(int key) {
    return key >= KeyCodes.KEY_ZERO && key <= KeyCodes.KEY_NINE;
  }
  
  
  final String defaultValue = "0";
  private String preValue;
  
  /**
   * Обработчик событий воода с клавиатру, обработчик обеспечивает обработку событий в стационарных и мобильных броузерах 
   * @param widget
   * @param maxValue определяет верхнюю границу диапазона числа
   * @param nextWidget определяет виджет на который надо переключиться при нажатии клавишив TAB
   */
  protected void appendHandlers(TextBox widget, int maxValue, TextBox nextWidget) {
    
    if (JepClientUtil.isMobile()) {
      
      widget.addKeyDownHandler(keydownevent -> {
        preValue = widget.getText();
      });
    
      widget.addKeyUpHandler(keyuphandler -> handlerEventKeyUpMobileDevice(widget, keyuphandler.getNativeKeyCode(), maxValue));
    
    } else {
      widget.addKeyDownHandler(keydownevent -> {
        int keyCode = keydownevent.getNativeKeyCode();
        if (!isPermitKey(keyCode)) {
          widget.cancelKey();
        } else {
          if (widget.getText() == null) {
            widget.setValue(defaultValue);
          } else {
            try {
              Integer.valueOf(widget.getText());
            } catch(Exception e) {
              widget.setValue(defaultValue);
            }
          }
        }
      });
    
    widget.addKeyUpHandler(keyuphandler -> {
      int keyCode = keyuphandler.getNativeKeyCode();
      if (!isPermitKey(keyCode)) {
        widget.cancelKey();
      } else {
        Integer value = 0;
        if (widget.getText() == null) {
          value = new Integer(defaultValue);
        } else {
          try {
            if (isDecimalKey(keyCode)) {
              value = Integer.valueOf(widget.getText());
            } else {
              value = Integer.valueOf(widget.getText());
            }
          } catch(Exception e) {
            value = new Integer(defaultValue);
          }
        }
        
        if (value < 0) {
          widget.setValue(defaultValue);
        } else if (value > maxValue) {
          widget.setValue(String.valueOf(maxValue));
        } else if (value == null) {
          widget.setValue(defaultValue);
        }
        
        if (JepRiaUtil.isEmpty(widget.getValue())) {
          widget.setValue(defaultValue);
        }
        doFireEventChangeTime();
      }
    });
    }
  }
  
  private void initFocusHandlers() {
    hours.addFocusHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent focusevent) {
        selectAll(hours.getElement());
      }
    });
    
    minutes.addFocusHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent focusevent) {
        selectAll(minutes.getElement());
      }
    });
    
    seconds.addFocusHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent focusevent) {
        selectAll(seconds.getElement());
      }
    });
  }
  
  /**
   * Получить минимально допустимый год
   * @return
   */
  public Integer getMinYear() {
    return ((MonthAndYearSelector) getMonthSelector()).getMinYear();
  }

  /**
   * Установить минимально допустимый год
   * @param minYear
   * @return Если попали в допустимый диапахон (01.01.1900 - 31.12.2100) - Истина, иначе - Ложь
   */
  public boolean setMinYear(Integer minYear) {
    return ((MonthAndYearSelector) getMonthSelector()).setMinYear(minYear);
    
  }

  /**
   * Получить максимально допустимый год
   * @return год
   */
  public Integer getMaxYear() {
    return ((MonthAndYearSelector) getMonthSelector()).getMaxYear();
  }

  /**
   * Установить максимально допустимый год
   * @param maxYear
   * @return Если попали в допустимый диапахон (01.01.1900 - 31.12.2100) - Истина, иначе - Ложь
   */
  public boolean setMaxYear(Integer maxYear) {
    return  ((MonthAndYearSelector) getMonthSelector()).setMaxYear(maxYear);
  }
  
  /**
   * Обработка ввода с клавиатуры для мобильных платформ
   * @param widget
   * @param keyCode
   * @param maxValue
   */
  
  protected void handlerEventKeyUpMobileDevice(Widget widget, int keyCode, int maxValue) {
    TextBox textBox = (TextBox)widget;
    Integer value = 0;
    String postValue = textBox.getText();
    value = isPermitValue(postValue.toCharArray()) ? new Integer(postValue) 
        : isPermitValue(preValue.toCharArray()) ? new Integer(preValue) : new Integer(defaultValue);
      
    if (value < 0) {
      textBox.setValue(defaultValue);
    } else if (value > maxValue) {
      textBox.setValue(String.valueOf(maxValue));
    } else if (value == null) {
      textBox.setValue(defaultValue);
    }
    
    if (JepRiaUtil.isEmpty(textBox.getValue())) {
      textBox.setValue(defaultValue);
    }
    doFireEventChangeTime();
  }
  
  /**
   * Пропаганда события уменьшения года 
   */
  protected abstract void changeYearWhenBakwards();
  
  /**
   * Пропаганда события увеличение года 
   */
  protected abstract void changeYearWhenForwards();
  
  /**
   * Пропаганда события уменьшения месяца 
   */
  protected abstract void changeMonthWhenBakwards();
  
  /**
   * Пропаганда события увеличение месяца 
   */
  protected abstract void changeMonthWhenForwards();
  
  /**
   * Пропаганда события выбора года из списка 
   */
  protected abstract void doWhenFireEventYearListBox();
  
  /**
   * Пропаганда события выбора месяца из списка 
   */
  protected abstract void doWhenFireEventMonthListBox();
  
  /**
   * Пропаганда события нажатия кнопки на панели календаря
   */
  protected abstract void doFireEventClickDatePicker();
  
  /**
   * Пропаганда события измеения времени на панели календаря
   */
  protected abstract void doFireEventChangeTime();
}