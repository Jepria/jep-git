package com.technology.jep.jepria.client.widget.field.multistate.customized;

import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_DAYS_AND_MONTH_AND_YEAR;

import java.util.Date;

import com.google.gwt.user.datepicker.client.CalendarModel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DefaultCalendarView;

public abstract class JepDatePicker extends DatePicker {

    private MonthAndYearSelectorWithYear monthSelector;
    
    public JepDatePicker(int showPanelElements) {
        super(new MonthAndYearSelectorWithYear(showPanelElements), new DefaultCalendarView(), new CalendarModel());
        monthSelector = (MonthAndYearSelectorWithYear) this.getMonthSelector();
        monthSelector.setPicker(this);
        monthSelector.setModel(this.getModel());
    }
    
    public JepDatePicker() {
        this(PANEL_OF_DAYS_AND_MONTH_AND_YEAR);
    }

    public void refreshComponents() {
        super.refreshAll();
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
    
    public Integer getMinYear() {
        return ((MonthAndYearSelectorWithYear) getMonthSelector()).getMinYear();
    }

    public void setMinYear(Integer minYear) {
        ((MonthAndYearSelectorWithYear) getMonthSelector()).setMinYear(minYear);
    }

    public Integer getMaxYear() {
        return ((MonthAndYearSelectorWithYear) getMonthSelector()).getMaxYear();
    }

    public void setMaxYear(Integer maxYear) {
        ((MonthAndYearSelectorWithYear) getMonthSelector()).setMaxYear(maxYear);
    }
    
    protected abstract void changeYearWhenBakwards();
    protected abstract void changeYearWhenForwards();
    protected abstract void changeMonthWhenBakwards();
    protected abstract void changeMonthWhenForwards();
    
    protected abstract void doWhenFireEventYearListBox();
    protected abstract void doWhenFireEventMonthListBox();
}