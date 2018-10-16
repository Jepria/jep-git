package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.DEFAULT_DATE_FORMAT_MASK;
import static com.technology.jep.jepria.client.JepRiaClientConstant.DEFAULT_DATE_MONTH_AND_YEARS_ONLY_FORMAT_MASK;
import static com.technology.jep.jepria.client.JepRiaClientConstant.DEFAULT_DATE_TIME_FORMAT_MASK;
import static com.technology.jep.jepria.client.JepRiaClientConstant.DEFAULT_DATE_YEARS_ONLY_FORMAT_MASK;
import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_DEFAULT_HEIGHT;
import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_DEFAULT_WIDTH;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_DAYS_AND_MONTH_AND_YEAR;
import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_DAYS_AND_MONTH_AND_YEAR_TIME;
import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_MONTH_AND_YEAR_ONLY;
import static com.technology.jep.jepria.client.JepRiaClientConstant.PANEL_OF_YEAR_ONLY;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_MONTH_AND_YEAR_ONLY_FORMAT;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_TIME_FORMAT;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_YEAR_ONLY_FORMAT;

import java.util.Date;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
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
   * Формат представления даты.<br>
   * Влияет на отображение даты на карте просмотра и на сравнение дат.
   */
  protected DateTimeFormat format;
  
  /**
   * Клиент использует мобильную платформу?
   */
  private final boolean IS_CLIENT_USE_MOBILE_PLATFORM = JepClientUtil.isMobile();

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
  
  /**
   * Виды навигационной панели в календаре
   */
  public static final int FORMAT_DAYS_AND_MONTH_AND_YEAR_TIME = PANEL_OF_DAYS_AND_MONTH_AND_YEAR_TIME;
  public static final int FORMAT_DAYS_AND_MONTH_AND_YEAR = PANEL_OF_DAYS_AND_MONTH_AND_YEAR;
  public static final int FORMAT_MONTH_AND_YEAR_ONLY = PANEL_OF_MONTH_AND_YEAR_ONLY;
  public static final int FORMAT_YEAR_ONLY = PANEL_OF_YEAR_ONLY;
  
  /**
   * Граничные даты диапазона возможных значений дат
   */
  private Date minDate, maxDate = new Date();
  
  /**
   * Задать максимальный год в диапазаоне лет, если используется JepDatePicker
   * @param maxYear задается максимальный год в диапазоне в выпадающем списке, если используется JepDatePicker, иначе ограничения не устанавливаются
   */
  public void setMaxYear(int maxYear) {
    if (editableCard.getDatePicker() instanceof JepDatePicker) {
      if (((JepDatePicker)editableCard.getDatePicker()).setMaxYear(maxYear)) {
        maxDate = new Date(((JepDatePicker) editableCard.getDatePicker()).getMaxYear() - 1900, 12 - 1, 31);
      } else {
        maxDate = new Date(maxYear - 1900, 12 - 1, 31);
      }
    }
  }
  
  /**
   * Задать минимальный год в диапазоне лет, если используется JepDatePicker
   * @param minYear задается минимальный год в диапазоне в выпадающем списке, если используется JepDatePicker, иначе ограничения не устанавливаются
   */
  public void setMinYear(int minYear) {
    if (editableCard.getDatePicker() instanceof JepDatePicker) {
      if (((JepDatePicker)editableCard.getDatePicker()).setMinYear(minYear)) {
        minDate = new Date(((JepDatePicker) editableCard.getDatePicker()).getMinYear() - 1900, 1 - 1, 01);  
      } else {
        minDate = new Date(minYear - 1900, 1 - 1, 01);
      }
    }
  }
  
  /**
   * Устанавлилваем границы диапазон дат
   */
  protected void setActualFrameDate() {
    minDate = new Date(((JepDatePicker) editableCard.getDatePicker()).getMinYear() - 1900, 1 - 1, 01);
    maxDate = new Date(((JepDatePicker) editableCard.getDatePicker()).getMaxYear() - 1900, 12 - 1, 31);
  }

  /**
   * Получить минимальную граничную дату
   * @return минимальная граничная дата
   */
  public Date getMinLimitDate() {
    return minDate;
  }
  
  /**
   * Получить максимальную граничную дату
   * @return максимальная граничная дата
   */
  public Date getMaxLimitDate() {
    return maxDate;
  }
  
  private int TYPE_DATEPICKER = FORMAT_DAYS_AND_MONTH_AND_YEAR;
  
  /**
   * Метод для управления панелью навигации в календаре
   * @param typeViewPanelOfCalendar - тип формата ввода даты, может принимать значения:  FORMAT_DAYS_AND_MONTH_AND_YEAR_TIME - стандартный календарь и время(dd.MM.yyyy HH:mm:ss), <br> FORMAT_DAYS_AND_MONTH_AND_YEAR - стандартный календарь (dd.MM.yyyy), <br>FORMAT_MONTH_AND_YEAR_ONLY - Навигация только месяц и год (mm.YYYY), <br>FORMAT_YEAR_ONLY - Навигация только год (yyyy)
   * <br>любые другие значения приводятся к FORMAT_DAYS_AND_MONTH_AND_YEAR
   * @param visibleNavigationPanel -управляет отображением панели навигации в календаре: true- отображет, false - не отображет
   * <br><b>Примечание:</b>
   * <br>Следующая комбинация параметров приводит к отображению стандартного календаря:
   *  typeViewPanelOfCalendar = FORMAT_DAYS_AND_MONTH_AND_YEAR and visibleNavigationPanel = null
   */
  public void setNavigationPanel(int typeViewPanelOfCalendar, Boolean visibleNavigationPanel) {
    editableCard.removeFromParent();
    
    if (typeViewPanelOfCalendar == FORMAT_DAYS_AND_MONTH_AND_YEAR && visibleNavigationPanel == null || typeViewPanelOfCalendar > FORMAT_DAYS_AND_MONTH_AND_YEAR_TIME) {
      addEditableCard();
      
    } else {
      createDatePicker(typeViewPanelOfCalendar);
      if ((typeViewPanelOfCalendar == FORMAT_DAYS_AND_MONTH_AND_YEAR || typeViewPanelOfCalendar == FORMAT_DAYS_AND_MONTH_AND_YEAR_TIME) 
              && visibleNavigationPanel) {
        ((JepDatePicker)editableCard.getDatePicker()).setVisibleDaysPanel(true);
      } else {
        ((JepDatePicker)editableCard.getDatePicker()).setVisibleDaysPanel(false);
      }
      
      boolean isVisibleNavigationPanel = visibleNavigationPanel == null ? false : visibleNavigationPanel;
      
      ((JepDatePicker)editableCard.getDatePicker()).setVisibleNavigationPanel(isVisibleNavigationPanel);
      editableCard.setVisiblePopup(isVisibleNavigationPanel);
    }
    
    TYPE_DATEPICKER = typeViewPanelOfCalendar;
    
    // Обновляем метки элементов для новых объектах
    setWebId(getWebId());
    setCardWebAttrs();
    
    getViewCard().getElement().addClassName(VIEW_CARD_STYLE);
    getEditableCard().getElement().addClassName(EDITABLE_CARD_STYLE);
    
    setFieldWidth(FIELD_DEFAULT_WIDTH);
    setFieldHeight(FIELD_DEFAULT_HEIGHT);

    addChangeValueListener();
  }
  
  /**
   * Создание календаря с навигационной панелью с возможностью выбрать месяц и год
   * @param typeViewPanelOfCalendar
   */
  private void createDatePicker(Integer typeViewPanelOfCalendar) {
    Mask mask = null;
    
    if (typeViewPanelOfCalendar == null 
            || typeViewPanelOfCalendar > PANEL_OF_DAYS_AND_MONTH_AND_YEAR_TIME) {
      typeViewPanelOfCalendar = PANEL_OF_DAYS_AND_MONTH_AND_YEAR;
    }
    
    switch (typeViewPanelOfCalendar.intValue()) {
      case PANEL_OF_MONTH_AND_YEAR_ONLY:
        this.format = DateTimeFormat.getFormat(DEFAULT_DATE_MONTH_AND_YEAR_ONLY_FORMAT);
        mask = new Mask(DEFAULT_DATE_MONTH_AND_YEARS_ONLY_FORMAT_MASK);
        break;
      case PANEL_OF_YEAR_ONLY:
        this.format = DateTimeFormat.getFormat(DEFAULT_DATE_YEAR_ONLY_FORMAT);
        mask = new Mask(DEFAULT_DATE_YEARS_ONLY_FORMAT_MASK);
        break;
      case PANEL_OF_DAYS_AND_MONTH_AND_YEAR_TIME:
        this.format = DateTimeFormat.getFormat(DEFAULT_DATE_TIME_FORMAT);
        mask = new Mask(DEFAULT_DATE_TIME_FORMAT_MASK);
        break;
      case PANEL_OF_DAYS_AND_MONTH_AND_YEAR:
      default:
        this.format = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT);
        mask = new Mask(DEFAULT_DATE_FORMAT_MASK);
    }
    
    editableCard = new MaskedDateBox(
      new JepDatePicker(typeViewPanelOfCalendar) {
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

        @Override
        protected void doFireEventClickDatePicker() {
          handlerChangeDatePicker();
        }

        @Override
        protected void doFireEventChangeTime() {
          handlerChangeDatePicker();
        }
      }, 
      null, new XDefaultFormat(format), mask);
    
    editableCard.setStyleClassName(FIELD_AUTO_HEIGTH_STYLE);
    
    setActualFrameDate();
    
    editablePanel.add(editableCard);
  }
  
  /**
   * Обработчик событий в панели навигации календаря - при выборе дня на календаре
   * 
   */
  protected void handlerChangeDatePicker() {
    Date newDate = ((JepDatePicker) editableCard.getDatePicker()).getActualDate() == null ? new Date() : ((JepDatePicker) editableCard.getDatePicker()).getActualDate();
    
    // Отмечаем ошибкой, если вышли за пределы диапазона допустимых значений 
    inRangeDate(newDate);
    setValue(newDate);
  }
  
  /**
   * Обработчик события ввода/редактирования в поле календаря
   * 
   */
  protected void handlerKeyboardEvent() {
    Date newDate = getValue();
    ((JepDatePicker) editableCard.getDatePicker()).refresh(newDate);
    notifyListeners(JepEventType.CHANGE_VALUE_EVENT, new JepEvent(this, newDate));
  }
  
  /**
   * Проверка на принадлежность диапазона граничных дат
   * @param newDate
   */
  protected boolean inRangeDate(Date newDate) {
    boolean result = true;
    if (newDate != null && editableCard.getDatePicker() instanceof JepDatePicker) {
      if (!JepRiaUtil.isEmpty(getMinLimitDate()) && getMinLimitDate().after(newDate) && !getMinLimitDate().equals(newDate)) {
        clearInvalid();
        markInvalid(JepClientUtil.substitute(JepTexts.dateField_lessThen(), format.format(newDate), format.format(getMinLimitDate())));
        result = false;
      } else if (!JepRiaUtil.isEmpty(getMaxLimitDate()) && getMaxLimitDate().before(newDate) && !getMaxLimitDate().equals(newDate)) {
        clearInvalid();
        markInvalid(JepClientUtil.substitute(JepTexts.dateField_moreThen(), format.format(newDate), format.format(getMaxLimitDate())));
        result = false;
      } else {
        clearInvalid();
      }
    }
    return result;
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
   * Добавление прослушивателей
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_VALUE_EVENT}.
   */
  protected void addChangeValueListener() {
    
    // Срабатывает при выборе в панели календаря 
    editableCard.addValueChangeHandler(
      new ValueChangeHandler<Date>() {
        public void onValueChange(ValueChangeEvent<Date> event) {
          Date newDate = event.getValue();
          // Отмечаем ошибкой, если вышли за пределы диапазаона доступных значений и ставим граничнцю дату
          inRangeDate(newDate);

          setValue(newDate);
        }
      }
    );
    
    /**
     * Вешаем слушателя на нажатие кнопок
     */
    editableCard.addDomHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent keyupevent) {
        if (IS_CLIENT_USE_MOBILE_PLATFORM) {
          Date newDate = null;
          String value = getRawValue();
          try {
            newDate = validDate(value);
            if (newDate != null) {
              inRangeDate(newDate);
              handlerKeyboardEvent(newDate);
            }
          } catch(Exception e) {
            clearInvalid();
            markInvalid(JepClientUtil.substitute(JepTexts.dateField_invalidText(), value));
          }
        } else {
          int code = keyupevent.getNativeKeyCode();
          if (passCodeKey(code)) {
            Date newDate = null;
            try {
              if (getRawValue() != null && (getRawValue().equals("") || JepRiaUtil.isEmpty(getValue()))) {
                clearInvalid();
              } else {
                newDate = validDate(getRawValue());
                if (newDate != null) {
                  inRangeDate(newDate);
                  handlerKeyboardEvent();
                }
              }
            } catch(Exception e) {
              clearInvalid();
              markInvalid(JepClientUtil.substitute(JepTexts.dateField_invalidText(),getRawValue()));
            }
          }
        }
      }
    }, KeyUpEvent.getType());
  }
  
  /**
   *  Обработчик введенного текста с мобильного устройства
   * @param value
   */
  protected void handlerKeyboardEvent(Date newDate) {
    ((JepDatePicker) editableCard.getDatePicker()).refresh(newDate);
    notifyListeners(JepEventType.CHANGE_VALUE_EVENT, new JepEvent(this, newDate));
  }

  /**
   * Валидация введенной даты
   */
  protected Date validDate(String date) throws Exception {
    Date result = null;
    String exceptionMessage = "Incorrect date";

    if (date != null) {
      String[] dateParts = date.split("\\.");
      switch (TYPE_DATEPICKER) {
      case FORMAT_DAYS_AND_MONTH_AND_YEAR: {
        if (/* validate day */ 1 <= Integer.valueOf(dateParts[0]) && Integer.valueOf(dateParts[0]) <= 31
            /* validate month */ && 1 <= Integer.valueOf(dateParts[1]) && Integer.valueOf(dateParts[1]) <= 12
            /* validate year */ && dateParts[2].length() == 4) {
            result = format.parse(date);
        } else {
          throw new Exception(exceptionMessage);
        }
        break;
      } 
      case FORMAT_DAYS_AND_MONTH_AND_YEAR_TIME: {
        String dateYear = dateParts[2].split("\\s")[0];
        try {
          if (/* validate day */ 1 <= Integer.valueOf(dateParts[0]) && Integer.valueOf(dateParts[0]) <= 31
              /* validate month */ && 1 <= Integer.valueOf(dateParts[1]) && Integer.valueOf(dateParts[1]) <= 12
              /* validate year */ && dateYear.length() == 4) {
            
            
            String[] data_time = date.split("\\s");
            String[] time = data_time[1].split("\\:");
            if (/* validate hours */ Integer.valueOf(time[0]) < 24 
                /* validate minutes */ && Integer.valueOf(time[1]) < 60 
                /* validate secondes */ && Integer.valueOf(time[2]) < 60) {
              result = format.parse(date);
            } else {
              throw new Exception(exceptionMessage);
            }
          } else {
            throw new Exception(exceptionMessage);
          }
        } catch (Exception exceptionMessage1) {
          throw new Exception(exceptionMessage1);
        }
        
        break;
      }
      case FORMAT_MONTH_AND_YEAR_ONLY: {
        if (/* validate month */ 1 <= Integer.valueOf(dateParts[0]) && Integer.valueOf(dateParts[0]) <= 12
            /* validate year */ && dateParts[1].length() == 4) {
          result = format.parse(date);
        } else {
          throw new Exception(exceptionMessage);
        }
        break;
      }
      
      case FORMAT_YEAR_ONLY: {
        if (/* validate year */ dateParts[0].length() == 4) {
          result = format.parse(date);
        } else {
          throw new Exception(exceptionMessage);
        }
          break;
        }
      }
    }
    
    return result; 
  }
  
  /**
   * Проверяем нажатые клавиши
   * @param code
   * @return вернет Истину, если введен допустимый код, иначе Ложь
   */
  
  protected boolean passCodeKey(int code) {
    if (KeyCodes.KEY_NUM_ZERO <= code && code <= KeyCodes.KEY_NUM_NINE
         || KeyCodes.KEY_ZERO <= code && code <= KeyCodes.KEY_NINE  
         || KeyCodes.KEY_BACKSPACE == code
         || KeyCodes.KEY_DELETE == code) {
      return true;
    }
    return false;
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
    
    return inRangeDate(getValue());
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
