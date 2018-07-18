package com.technology.jep.jepria.client.widget.field.multistate.customized;

import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_DAYS_AND_MONTH_AND_YEAR;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.CalendarModel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DefaultCalendarView;

public abstract class JepDatePicker extends DatePicker {

  private MonthAndYearSelector monthSelector;
  
  public JepDatePicker(int showPanelElements) {
    super(new MonthAndYearSelector(showPanelElements), new DefaultCalendarView(), new CalendarModel());
    monthSelector = (MonthAndYearSelector) this.getMonthSelector();
    monthSelector.setPicker(this);
    monthSelector.setModel(this.getModel());
    
    if (showPanelElements == PANEL_OF_DAYS_AND_MONTH_AND_YEAR) {
      addButtonToday((VerticalPanel) getWidget());
    }
  }
  
  protected void addButtonToday(VerticalPanel panel) {
    LocaleInfo locale = LocaleInfo.getCurrentLocale();
    Button todayButton = new Button();
    if (locale.getLocaleName().equalsIgnoreCase("ru")) {
      todayButton.setHTML("<b>Сегодня</b>");
    } else {
      todayButton.setHTML("<b>Today</b>");
    }
    todayButton.setStyleName("datePickerMonthSelector");
    todayButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickevent) {
        refresh(new Date());
      }
    });
    panel.add(todayButton);
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
  }
  
  public void setVisibleDaysPanel(boolean visible) {
    getView().setVisible(visible);
  }
  
  public void setVisibleNavigationPanel(boolean visible) {
    monthSelector.setVisibleNavigationPanel(visible);
  }
  
  public Date getActualDate() {
    return getModel().getCurrentMonth();
  }
  
  public Date getCurrentDate() {
    return getValue();
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
}