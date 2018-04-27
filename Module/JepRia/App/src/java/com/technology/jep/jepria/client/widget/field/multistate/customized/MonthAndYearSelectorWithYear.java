package com.technology.jep.jepria.client.widget.field.multistate.customized;

import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_DAYS_AND_MONTH_AND_YEAR;
import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_MONTH_AND_YEAR_ONLY;
import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_YEAR_ONLY;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.datepicker.client.CalendarModel;
import com.google.gwt.user.datepicker.client.MonthSelector;

public class MonthAndYearSelectorWithYear extends MonthSelector {

    private static String BASE_NAME = "datePicker";
    private PushButton backwards;
    private PushButton forwards;
    private PushButton backwardsYear;
    private PushButton forwardsYear;
    private Grid grid;
    private int previousYearColumn = 0;
    private int previousMonthColumn = 1;

    private int nextMonthColumn = 4;
    private int nextYearColumn = 5;
    private CalendarModel model;
    private JepDatePicker picker;
    
    private String[] items_ru = { "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль",
            "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь" };
    
    private String[] items_en = { "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December" };

    private boolean isUseMonthAndYearOnly = false;
    private boolean isUseYearOnly = false;
    
    private void resetAllExeptOf(int panelElement) {
        switch(panelElement) {
            case PANEL_OF_MONTH_AND_YEAR_ONLY:
                isUseYearOnly = false;
                break;
            case PANEL_OF_YEAR_ONLY:
                isUseMonthAndYearOnly = false;
                break;
            case PANEL_OF_DAYS_AND_MONTH_AND_YEAR:
            default:
                isUseMonthAndYearOnly = false;
                isUseYearOnly = false;
        }
    }
    
    private ListBox monthListBox;
    private ListBox yearListBox;
    
    protected String[] getMonthNames() {
        return items_ru;
    }
    
    private final int MIN_YEAR = 1900;
    private final int MAX_YEAR = 2100;
    
    private Integer minYear = MIN_YEAR;
    
    public Integer getMinYear() {
        return minYear;
    }

    public void setMinYear(Integer minYear) {
        if (maxYear > minYear && minYear >= MIN_YEAR) {
            this.minYear = minYear;            
        }
        
        setYearsListBox();
    }

    
    private Integer maxYear = MAX_YEAR;

    public Integer getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(Integer maxYear) {
        if (minYear < maxYear && maxYear <= MAX_YEAR) {
            this.maxYear = maxYear;
        }
        
        setYearsListBox();
    }
    
    private void removeYearsList() {
        if (yearListBox.getItemCount() > 0) {
            int items = yearListBox.getItemCount();
            for (int i = items - 1; i >= 0; yearListBox.removeItem(i--))
                ;
        }
    }
    
    protected void setYearsListBox() {
        removeYearsList();
        for (int i = minYear; i < maxYear; i++) {
            yearListBox.addItem(i + "");
        }
    }
    
    public MonthAndYearSelectorWithYear(int showOnPanel) {
        switch (showOnPanel) {
            case PANEL_OF_MONTH_AND_YEAR_ONLY:
                isUseMonthAndYearOnly = true;
                break;
            case PANEL_OF_YEAR_ONLY:
                isUseYearOnly = true;
                break;
            case PANEL_OF_DAYS_AND_MONTH_AND_YEAR:
            default:
                resetAllExeptOf(showOnPanel);
        }
        
        yearListBox = new ListBox();
        setYearsListBox();
        
        monthListBox = new ListBox();
        for (int i = 0; i < getMonthNames().length; i++) {
            monthListBox.addItem(getMonthNames()[i]);
        }
    }
    
    public MonthAndYearSelectorWithYear() {
        this(PANEL_OF_DAYS_AND_MONTH_AND_YEAR);
    }

    public void setModel(CalendarModel model) {
        this.model = model;
    }

    public void setPicker(JepDatePicker picker) {
        this.picker = picker;
    }
    
    private int getYearIndexOfListBox(int yearIndex) {
        int resultIndex = yearIndex;
        
        int sizeList = yearListBox.getItemCount() - 1;
        if (yearIndex > sizeList) {
            Integer itemToYear = MIN_YEAR + yearIndex;
            boolean finded = false;
            
            for (int i = sizeList; i >= 0; i--) {
                if (itemToYear.intValue() == Integer.parseInt(yearListBox.getItemText(i))) {
                    resultIndex = i;
                    finded = true;
                    break;
                }
            }
            
            if (!finded) {
                resultIndex = sizeList;
            }
            
        } else if (yearIndex < 0) {
            resultIndex = 0;
        }
        
        return resultIndex;
    }

    @Override
    protected void refresh() {
        int monthIndex = getModel().getCurrentMonth().getMonth();
        monthListBox.setItemSelected(monthIndex, true);
        int yearIndex = getModel().getCurrentMonth().getYear();
        yearListBox.setItemSelected(getYearIndexOfListBox(yearIndex), true);
    }

    @Override
    protected void setup() {
        backwards = new PushButton();
        backwards.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (!(model.getCurrentMonth().getMonth() == 0 && getYearIndexOfListBox(model.getCurrentMonth().getYear()) == 0)) {
                    addMonths(-1);
                    changeMonthWhenBakwards();
                }
            }
        });

        backwards.getUpFace().setHTML("&lsaquo;");
        backwards.setStyleName(BASE_NAME + "PreviousButton");

        forwards = new PushButton();
        forwards.getUpFace().setHTML("&rsaquo;");
        forwards.setStyleName(BASE_NAME + "NextButton");
        forwards.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (getYearIndexOfListBox(model.getCurrentMonth().getYear()) < maxYear - minYear - 1) {
                    addMonths(+1);
                    changeMonthWhenForwards();
                }
                else if(model.getCurrentMonth().getMonth() < 11 && getYearIndexOfListBox(model.getCurrentMonth().getYear()) == maxYear - minYear - 1)
                {
                    addMonths(+1);
                    changeMonthWhenForwards();
                }
                
            }
        });

        backwardsYear = new PushButton();
        backwardsYear.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (!(getYearIndexOfListBox(model.getCurrentMonth().getYear()) == 0)) {
                    addMonths(-12);
                    changeYearWhenBakwards();
                    picker.refreshComponents();
                }
            }
        });

        backwardsYear.getUpFace().setHTML("&laquo;");
        backwardsYear.setStyleName(BASE_NAME + "PreviousButton");

        forwardsYear = new PushButton();
        forwardsYear.getUpFace().setHTML("&raquo;");
        forwardsYear.setStyleName(BASE_NAME + "NextButton");
        forwardsYear.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (getYearIndexOfListBox(model.getCurrentMonth().getYear()) < maxYear - minYear - 1) {
                    addMonths(+12);
                    changeYearWhenForwards();
                    picker.refreshComponents();
                }
            }
        });

        yearListBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                setYear(Integer.parseInt(yearListBox.getItemText(yearListBox.getSelectedIndex())) - MIN_YEAR);
                doWhenChangeYearListBox();
            }
        });
        monthListBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                int monthIndex = monthListBox.getSelectedIndex();
                setMonth(monthIndex);
                doWhenChangeMonthListBox();
            }
        });

        if (isUseMonthAndYearOnly) {
            grid = new Grid(1, 6);
            grid.setWidget(0, previousYearColumn, backwardsYear);
            grid.setWidget(0, previousMonthColumn, backwards);
            grid.setWidget(0, 2, monthListBox);
            grid.setWidget(0, nextMonthColumn, forwards);
            
            grid.setWidget(0, 3, yearListBox);
            grid.setWidget(0, nextYearColumn, forwardsYear);
        } else if (isUseYearOnly) {
            grid = new Grid(1, 3);
            grid.setWidget(0, previousYearColumn, backwardsYear);
            grid.setWidget(0, 1, yearListBox);
            grid.setWidget(0, 2, forwardsYear);
        } else {
            grid = new Grid(1, 6);
            grid.setWidget(0, previousYearColumn, backwardsYear);
            grid.setWidget(0, previousMonthColumn, backwards);
            grid.setWidget(0, 2, monthListBox);
            grid.setWidget(0, nextMonthColumn, forwards);
            
            grid.setWidget(0, 3, yearListBox);
            grid.setWidget(0, nextYearColumn, forwardsYear);
        }
        
        CellFormatter formatter = grid.getCellFormatter();
        if (isUseMonthAndYearOnly) {
            formatter.setWidth(0, previousYearColumn, "1");
            formatter.setWidth(0, previousMonthColumn, "1");
            formatter.setWidth(0, nextMonthColumn, "1");
            formatter.setWidth(0, nextYearColumn, "1");
        } else if (isUseYearOnly) {
            formatter.setWidth(0, previousYearColumn, "1");
            formatter.setWidth(0, 2, "1");
        } else {
            formatter.setWidth(0, previousYearColumn, "1");
            formatter.setWidth(0, previousMonthColumn, "1");
            formatter.setWidth(0, nextMonthColumn, "1");
            formatter.setWidth(0, nextYearColumn, "1");
        }
        
        grid.setStyleName(BASE_NAME + "MonthSelector");
        initWidget(grid);
    }

    public void addMonths(int numMonths) {
        model.shiftCurrentMonth(numMonths);
        picker.refreshComponents();
    }

    @SuppressWarnings("deprecation")
    public void setMonth(int month) {
        model.getCurrentMonth().setMonth(month);
        picker.refreshComponents();
    }

    @SuppressWarnings("deprecation")
    public void setYear(int year) {
        model.getCurrentMonth().setYear(year);
        picker.refreshComponents();
    }
    
    protected void setVisibleNavigationPanel(boolean visible) {
        grid.setVisible(visible);
    }
    
    public void doWhenChangeYearListBox() {
        picker.doWhenFireEventYearListBox();
    }
    
    public void doWhenChangeMonthListBox() {
        picker.doWhenFireEventMonthListBox();
    }

    private void changeYearWhenBakwards() {
        picker.changeYearWhenBakwards();
    }
    
    private void changeYearWhenForwards() {
        picker.changeYearWhenForwards();
    }
    
    private void changeMonthWhenBakwards() {
        picker.changeMonthWhenBakwards();
    }
    
    private void changeMonthWhenForwards() {
        picker.changeMonthWhenForwards();
    }
}