package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.DEFAULT_DATE_FORMAT_MASK;
import static com.technology.jep.jepria.client.JepRiaClientConstant.DEFAULT_DATE_MONTH_AND_YEARS_ONLY_FORMAT_MASK;
import static com.technology.jep.jepria.client.JepRiaClientConstant.DEFAULT_DATE_YEARS_ONLY_FORMAT_MASK;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_MONTH_AND_YEAR_ONLY_FORMAT;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_YEAR_ONLY_FORMAT;

import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_DAYS_AND_MONTH_AND_YEAR;
import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_MONTH_AND_YEAR_ONLY;
import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_YEAR_ONLY;

import java.util.Date;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.masked.Mask;
import com.technology.jep.jepria.client.widget.field.masked.MaskedDateBox;
import com.technology.jep.jepria.client.widget.field.masked.MaskedDateBox.XDefaultFormat;
import com.technology.jep.jepria.client.widget.field.multistate.customized.JepDatePicker;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле для ввода даты.<br>
 * Использует маску ввода.<br>
 * Для задания нестандартного (отличного от {@code dd.MM.yyyy}) формата ввода и вывода
 * даты необходимо в классе-наследнике переопределить метод {@code addEditableCard()}
 * следующим образом (задаётся формат {@code dd.MM.yyyy HH:mm:ss}):
 * <pre>
 *   JepDateField dateField = new JepDateField(&quot;Date field&quot;){
 *     {@literal @}Override
 *     protected void addEditableCard() {
 *       format = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT + &quot; &quot; + DEFAULT_TIME_FORMAT);
 *       Mask mask = new Mask(DEFAULT_DATE_FORMAT_MASK + &quot; &quot; + DEFAULT_TIME_FORMAT_MASK);
 *       editableCard = new MaskedDateBox(new DatePicker(), null, new XDefaultFormat(format), mask);
 *       editablePanel.add(editableCard);
 *     }};
 * </pre>
 */
public class JepDateField extends JepMultiStateField<MaskedDateBox, HTML> {
  
  /**
   * Текущее значение поля, для проверки действительности изменения перед уведомлением слушателей
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_VALUE_EVENT CHANGE_VALUE_EVENT}.
  */
  private Date currentDate = null;
  
  /**
   * Формат представления даты.<br>
   * Влияет на отображение даты на карте просмотра и на сравнение дат.
   */
  protected DateTimeFormat format;

  /**
   * Создаёт поле для ввода даты с пустой меткой в стандартном формате (dd.MM.yyyy).
   */
  public JepDateField() {
    this(null);
  }
  
  /**
   * Создаёт поле для ввода даты в стандартном формате (dd.MM.yyyy).
   * @param fieldLabel метка
   */
  public JepDateField(String fieldLabel) {
    this(null, fieldLabel);
  }
  
  public JepDateField(String fieldIdAsWebEl, String fieldLabel) {
    super(fieldIdAsWebEl, fieldLabel);
  }
  
  public static final int FORMAT_DAYS_AND_MONTH_AND_YEAR = PANEL_OF_DAYS_AND_MONTH_AND_YEAR;
  public static final int FORMAT_MONTH_AND_YEAR_ONLY = PANEL_OF_MONTH_AND_YEAR_ONLY;
  public static final int FORMAT_YEAR_ONLY = PANEL_OF_YEAR_ONLY;
  
  private boolean isVisibleNavigationPanel = false;
  
  public void setMaxYear(int maxYear) {
      if (isVisibleNavigationPanel) {
          ((JepDatePicker)editableCard.getDatePicker()).setMaxYear(maxYear);
      }
  }
  
  public void setMinYear(int minYear) {
      if (isVisibleNavigationPanel) {
          ((JepDatePicker)editableCard.getDatePicker()).setMinYear(minYear);
      }
  }
  
  public int getMaxYear() {
      return ((JepDatePicker)editableCard.getDatePicker()).getMaxYear();
  }
  
  public int getMinYear() {
      return ((JepDatePicker)editableCard.getDatePicker()).getMinYear();
  }
  
  /**
   * Метод для управления панелью навигации в календаре
   * @param typeViewPanelOfCalendar - тип формата ввода даты, может принимать знаяения:  FORMAT_DAYS_AND_MONTH_AND_YEAR - стандартный календарь (dd.MM.yyyy), <br>FORMAT_MONTH_AND_YEAR_ONLY - Навигация только месяц и год (mm.YYYY), <br>FORMAT_YEAR_ONLY - Навигация только год (yyyy)
   * <br>любые другие значения приводятся к FORMAT_DAYS_AND_MONTH_AND_YEAR
   * @param visibleNavigationPanel -управляет отображением панели навигации в календаре: true- отображет, false - не отображет
   * <br><b>Примечание:</b>
   * <br>Следующая комбинация параметров приводит к отображению стандартного календаря:
   *  typeViewPanelOfCalendar = FORMAT_DAYS_AND_MONTH_AND_YEAR and visibleNavigationPanel = null
   */
  public void setNavigationPanelOfCalendar(int typeViewPanelOfCalendar, Boolean visibleNavigationPanel) {
      editableCard.removeFromParent();
      
      if (typeViewPanelOfCalendar == FORMAT_DAYS_AND_MONTH_AND_YEAR && visibleNavigationPanel == null) {
          addEditableCard();
          isVisibleNavigationPanel = false;
      } else {
          createDatePicker(typeViewPanelOfCalendar);
          if (typeViewPanelOfCalendar == FORMAT_DAYS_AND_MONTH_AND_YEAR 
                  && visibleNavigationPanel) {
              ((JepDatePicker)editableCard.getDatePicker()).setVisibleDaysPanel(true);
          } else {
              ((JepDatePicker)editableCard.getDatePicker()).setVisibleDaysPanel(false);
          }
          
          isVisibleNavigationPanel = visibleNavigationPanel == null ? false : visibleNavigationPanel;
          
          ((JepDatePicker)editableCard.getDatePicker()).setVisibleNavigationPanel(isVisibleNavigationPanel);
          
          
      }
      addChangeValueListener();
  }
  
  private void createDatePicker(Integer typeViewPanelOfCalendar) {
      Mask mask = null;
      
      if (typeViewPanelOfCalendar == null 
              || typeViewPanelOfCalendar > PANEL_OF_YEAR_ONLY)
          typeViewPanelOfCalendar = PANEL_OF_DAYS_AND_MONTH_AND_YEAR;
      
      switch (typeViewPanelOfCalendar.intValue()) {
          case PANEL_OF_MONTH_AND_YEAR_ONLY:
              this.format = DateTimeFormat.getFormat(DEFAULT_DATE_MONTH_AND_YEAR_ONLY_FORMAT);
              mask = new Mask(DEFAULT_DATE_MONTH_AND_YEARS_ONLY_FORMAT_MASK);
              break;
          case PANEL_OF_YEAR_ONLY:
              this.format = DateTimeFormat.getFormat(DEFAULT_DATE_YEAR_ONLY_FORMAT);
              mask = new Mask(DEFAULT_DATE_YEARS_ONLY_FORMAT_MASK);
              break;
          case PANEL_OF_DAYS_AND_MONTH_AND_YEAR:
          default:
              this.format = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT);
              mask = new Mask(DEFAULT_DATE_FORMAT_MASK);
      }
      
      editableCard = new MaskedDateBox(new JepDatePicker(typeViewPanelOfCalendar) {
                      @Override
                      protected void changeYearWhenBakwards() {
                          handlerChangeDatePicker();
                      }
                      
                      @Override
                      protected void changeYearWhenForwards() {
                          handlerChangeDatePicker();
                      }
                      
                      @Override
                      public void changeMonthWhenBakwards() {
                          handlerChangeDatePicker();
                      }
                      
                      @Override
                      public void changeMonthWhenForwards() {
                          handlerChangeDatePicker();
                      }

                      @Override
                      public void doWhenFireEventYearListBox() {
                          handlerChangeDatePicker();
                      }

                      @Override
                      public void doWhenFireEventMonthListBox() {
                          handlerChangeDatePicker();
                      }
                 }, null, new XDefaultFormat(format), mask);
      editablePanel.add(editableCard);
  }
  
  /*
   * Обработчик событий в панели навигации календаря
   * 
   */
  protected void handlerChangeDatePicker() {
      
      Date newDate = ((JepDatePicker) editableCard.getDatePicker()).getActualDate() == null ? new Date() : ((JepDatePicker) editableCard.getDatePicker()).getActualDate();
      Date oldDate = getValue();

      if (!equalsWithFormat(oldDate, newDate)) {
          setValue(newDate);
          notifyListeners(JepEventType.CHANGE_VALUE_EVENT, new JepEvent(JepDateField.this, newDate));
          currentDate = newDate;
      }
  }
  
  /**
   * Создание и добавление карты редактирования.<br>
   * Также задаёт формат даты. Если требуется изменить формат ввода и вывода даты
   * (например, с учётом времени), данный метод необходимо перегрузить в классе-наследнике.
   */
  @Override
  protected void addEditableCard() {
    this.format = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT);
    Mask mask = new Mask(DEFAULT_DATE_FORMAT_MASK);
    editableCard = new MaskedDateBox(new DatePicker(), null, new XDefaultFormat(format), mask);
    editablePanel.add(editableCard);
  }
  
  /**
   * Установка значения поля.<br/>
   * @param value значение поля
   */
  @Override
  public void setValue(Object value) {
    Date oldValue = getValue();
    if(!equalsWithFormat(oldValue, (Date)value)) {
      editableCard.setValue((Date) value);
      clearInvalid();
      currentDate = (Date) value;
      setViewValue(value);
    }
  }
  
  /**
   * {@inheritDoc}
   */
  public void setEnabled(boolean enabled) {
    getInputElement().setPropertyBoolean("disabled", !enabled);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    super.clear();
    editableCard.setValue(null);
  }
  
  /**
   * Установка значения карты Просмотра.<br>
   * Значение форматируется в соответствии с заданным форматом ввода даты.
   * @param value значение
   */
  @Override
  protected void setViewValue(Object value) {
    viewCard.setHTML(value != null ? format.format((Date) value) : null);
  }
  
  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Date getValue() {
    return editableCard.getValue();
  }
  
  public int getValueDay() {
      return getValue().getDay();
  }
  
  public int getValueMonth() {
      return getValue().getMonth();
  }
  
  public int getValueYear() {
      return getValue().getYear();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addListener(JepEventType eventType, JepListener listener) {
    switch(eventType) {
      case CHANGE_VALUE_EVENT:
        addChangeValueListener();
        break;
        
      default:;
    }
    
    super.addListener(eventType, listener);
  }
  
  /**
   * Добавление прослушивателей для реализации прослушивания события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_VALUE_EVENT}.
   */
  protected void addChangeValueListener() {
    editableCard.getDatePicker().addValueChangeHandler(
        new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
                Date newDate = getValue();
                if (!equalsWithFormat(currentDate, newDate)) {
                  notifyListeners(JepEventType.CHANGE_VALUE_EVENT, new JepEvent(JepDateField.this, newDate));
                  currentDate = newDate;
                }
            }
        }
      );
    editableCard.addValueChangeHandler(
        new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
                Date newDate = getValue();
                if (!equalsWithFormat(currentDate, newDate)) {
                  notifyListeners(JepEventType.CHANGE_VALUE_EVENT, new JepEvent(JepDateField.this, newDate));
                  currentDate = newDate;
                }
            }
        }
      );
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid() {
    clearInvalid();
    if (!editableCard.isValid()) {
      markInvalid(JepClientUtil.substitute(JepTexts.dateField_invalidText(), getRawValue()));
      return false;
    }
    if (!allowBlank && JepRiaUtil.isEmpty(getValue())) {
      markInvalid(JepTexts.field_blankText());
      return false;
    }
    return true;
  }
  
  /**
   * Сравнивает две даты с учётом формата их представления.<br>
   * Возвращает true, если оба значения null либо оба не null и совпадают,
   * будучи представленными в формате поля.
   * @param date1 первое сравниваемое значение
   * @param date2 второе сравниваемое значение
   * @return результат сравнения
   */
  private boolean equalsWithFormat(Date date1, Date date2) {
    return date1 == date2 || (date1 != null && date2 != null && format.format(date1).equals(format.format(date2)));
  }
}
