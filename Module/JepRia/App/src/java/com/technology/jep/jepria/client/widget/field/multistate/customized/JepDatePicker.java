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
import com.sun.javafx.webkit.KeyCodeMap;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.field.masked.MaskedTextBoxMobile;
import com.technology.jep.jepria.shared.text.JepRiaText;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

public abstract class JepDatePicker extends DatePicker {

  public static final JepRiaText JepTexts = (JepRiaText) GWT.create(JepRiaText.class);
  
  private MonthAndYearSelector monthSelector;
  
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
  
  protected Widget createButtonToday(int showPanelElements) {
    LocaleInfo locale = LocaleInfo.getCurrentLocale();
    
    HorizontalPanel vPanel = new HorizontalPanel();
    
    Button todayButton = new Button();
    
    if (locale.getLocaleName().equalsIgnoreCase("ru")) {
      if (showPanelElements == PANEL_OF_DAYS_AND_MONTH_AND_YEAR_TIME) {
        todayButton.setHTML("<b>" + JepTexts.button_now() + "</b>");
      } else if (showPanelElements == PANEL_OF_DAYS_AND_MONTH_AND_YEAR) {
        todayButton.setHTML("<b>" + JepTexts.button_today() + "</b>");
      }
      
    } else {
      if (showPanelElements == PANEL_OF_DAYS_AND_MONTH_AND_YEAR_TIME) {
        todayButton.setHTML("<b>" + JepTexts.button_now() + "</b>");
      } else if (showPanelElements == PANEL_OF_DAYS_AND_MONTH_AND_YEAR) {
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
  
  public JepDatePicker() {
    this(PANEL_OF_DAYS_AND_MONTH_AND_YEAR);
  }

  public void refreshComponents() {
    super.refreshAll();
  }
  
  public void refresh(Date newDate) {
    this.setValue(newDate);
    monthSelector.setMonth(newDate.getMonth());
    monthSelector.setYear(newDate.getYear());
    setTime(newDate);
  }
  
  private TextBox hours;
  private TextBox minutes;
  private TextBox seconds;
  
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
  
  private native void selectAll(Element element) /*-{
    element.setSelectionRange(0, element.value.length);
  }-*/;
  
  protected void setTime(Date newDate) {
    if (hours != null && minutes != null && seconds != null && newDate != null) {
      hours.setValue(String.valueOf(newDate.getHours()));
      minutes.setValue(String.valueOf(newDate.getMinutes()));
      seconds.setValue(String.valueOf(newDate.getSeconds()));
    }
  }
  
  public void setVisibleDaysPanel(boolean visible) {
    getView().setVisible(visible);
  }
  
  public void setVisibleNavigationPanel(boolean visible) {
    monthSelector.setVisibleNavigationPanel(visible);
  }
  
  public Date getActualDate() {
    Date date = getValue();
    
    if (getCurrentMonth() != null && date != null) {
      date.setMonth(getCurrentMonth().getMonth());
      date.setYear(getCurrentMonth().getYear());
    }
    
    if (hours != null && minutes != null && seconds != null && date != null) {
      date.setHours(Integer.valueOf(JepRiaUtil.isEmpty(hours.getText()) ? defaultValue : hours.getText()));
      date.setMinutes(Integer.valueOf(JepRiaUtil.isEmpty(minutes.getText()) ? defaultValue : minutes.getText()));
      date.setSeconds(Integer.valueOf(JepRiaUtil.isEmpty(seconds.getText()) ? defaultValue : seconds.getText()));
    }
    
    return date;
  }
  
  protected boolean isPermitKey(int key) {
    return isDecimalKey(key) 
        || key >= 96 && key <= 105 || key == KeyCodes.KEY_BACKSPACE || key ==KeyCodes.KEY_DELETE 
        || key ==KeyCodes.KEY_RIGHT || key ==KeyCodes.KEY_LEFT || key ==KeyCodes.KEY_TAB;
  }
  
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
  
  protected boolean isDecimalKey(int key) {
    return key >= KeyCodes.KEY_ZERO && key <= KeyCodes.KEY_NINE;
  }
  
  final String defaultValue = "0";
  private String preValue;
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
  
  public Integer getMinYear() {
    return ((MonthAndYearSelector) getMonthSelector()).getMinYear();
  }

  public boolean setMinYear(Integer minYear) {
    return ((MonthAndYearSelector) getMonthSelector()).setMinYear(minYear);
    
  }

  public Integer getMaxYear() {
    return ((MonthAndYearSelector) getMonthSelector()).getMaxYear();
  }

  public boolean setMaxYear(Integer maxYear) {
    return  ((MonthAndYearSelector) getMonthSelector()).setMaxYear(maxYear);
  }
  
  protected abstract void changeYearWhenBakwards();
  protected abstract void changeYearWhenForwards();
  protected abstract void changeMonthWhenBakwards();
  protected abstract void changeMonthWhenForwards();
  
  protected abstract void doWhenFireEventYearListBox();
  protected abstract void doWhenFireEventMonthListBox();
  
  protected abstract void doFireEventClickDatePicker();
  protected abstract void doFireEventChangeTime();
  
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
}