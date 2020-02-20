package com.technology.jep.jepria.client.widget.field.masked;

import static com.technology.jep.jepria.client.JepRiaClientConstant.DEFAULT_DATE_FORMAT_MASK;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.field.multistate.customized.JepDatePicker;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Аналог GWT-класса {@link com.google.gwt.user.datepicker.client.DateBox},
 * в котором виджет {@link com.google.gwt.user.client.ui.TextBox} заменён на {@link MaskedTextBox}.<br>
 * Имеет следующие особенности:
 * <ul>
 *   <li>Виджет {@link com.google.gwt.user.client.ui.TextBox} заменён на {@link MaskedTextBox}.</li>
 *   <li>Для парсинга даты используется класс {@link XDefaultFormat}, в котором дата парсится
 *   с помощью метода parseStrict(), что исключает значения наподобие 31.02.2013 55:66:77.</li>
 *   <li>В методе setValue() вместо {@link com.google.gwt.user.datepicker.client.DateChangeEvent}
 *   вызывается событие {@link com.google.gwt.event.logical.shared.ValueChangeEvent}.</li>
 *   <li>Во всех методах вместо интерфейса {@link com.google.gwt.user.datepicker.client.DateBox.Format}
 *   используется класс {@link XDefaultFormat}.</li>
 *   <li> Спецификатор доступа метода {@link #parseDate(boolean)}
 *   изменён с private на protected.</li>
 *   <li> Значение параметра {@link #fireNullValues} по умолчанию true, а не false,
 *   что необходимо для корректной обработки обнуления даты с клавиатуры.</li>
 *   <li>Стиль в конструкторе задаётся не с помощью <code>setStyleName()</code>, а с помощью
 *   <code>addStyleName()</code>.</li>
 *   <li>Конструктор принимает дополнительный параметр - маску.</code>.</li>
 * </ul>
 */
public class MaskedDateBox extends Composite implements HasEnabled,
        HasValue<Date>, IsEditor<LeafValueEditor<Date>> {

  /**
   * Аналог GWT-класса {@link com.google.gwt.user.datepicker.client.DateBox.DefaultFormat}.<br>
   * Не наследует интерфейс {@link com.google.gwt.user.datepicker.client.DateBox.Format}.
   */
  public static class XDefaultFormat {

    private final DateTimeFormat dateTimeFormat;

    /**
     * Creates a new default format instance.
     */
    @SuppressWarnings("deprecation")
    public XDefaultFormat() {
      dateTimeFormat = DateTimeFormat.getMediumDateTimeFormat();
    }

    /**
     * Creates a new default format instance.
     *
     * @param dateTimeFormat the {@link DateTimeFormat} to use with this format.
     */
    public XDefaultFormat(DateTimeFormat dateTimeFormat) {
      this.dateTimeFormat = dateTimeFormat;
    }

    public String format(MaskedDateBox box, Date date) {
      if (date == null) {
        return "";
      } else {
        return dateTimeFormat.format(date);
      }
    }

    /**
     * Gets the date time format.
     *
     * @return the date time format
     */
    public DateTimeFormat getDateTimeFormat() {
      return dateTimeFormat;
    }

    /**
     * Метод, осуществляющий парсинг даты.<br>
     * Использует для этих целей метод {@link com.google.gwt.i18n.client.DateTimeFormat#parseStrict(String)}.
     * Может устанавливать на поле ввода стиль, сигнализирующий об ошибке.
     *
     * @param dateBox     поле ввода
     * @param dateText    текстовое значение
     * @param reportError если true, сигнализировать об ошибке, в противном случае - нет
     * @return дата или null в случае ошибки
     */
    @SuppressWarnings("deprecation")
    public Date parse(MaskedDateBox dateBox, String dateText, boolean reportError) {
      Date date = null;
      try {
        if (dateText.length() > 0) {
          date = dateTimeFormat.parseStrict(dateText);
        }
      } catch (IllegalArgumentException exception) {
        try {
          date = new Date(dateText);
        } catch (IllegalArgumentException e) {
          if (reportError) {
            dateBox.addStyleName(DATE_BOX_FORMAT_ERROR);
          }
          return null;
        }
      }
      return date;
    }

    public void reset(MaskedDateBox dateBox, boolean abandon) {
      dateBox.removeStyleName(DATE_BOX_FORMAT_ERROR);
    }
  }

  private class DateBoxHandler implements ValueChangeHandler<Date>,
          FocusHandler, BlurHandler, ClickHandler, KeyDownHandler, KeyUpHandler,
          CloseHandler<PopupPanel> {

    @Override
    public void onBlur(BlurEvent event) {
      if (isDatePickerShowing() == false) {
        updateDateFromTextBox();
      }
    }

    @Override
    public void onClick(ClickEvent event) {
      showDatePicker();
    }

    @Override
    public void onClose(CloseEvent<PopupPanel> event) {
      // If we are not closing because we have picked a new value, make sure the
      // current value is updated.
      if (allowDPShow) {
        updateDateFromTextBox();
      }
    }

    @Override
    public void onFocus(FocusEvent event) {
      if (allowDPShow && isDatePickerShowing() == false) {
        showDatePicker();
      }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
      switch (event.getNativeKeyCode()) {
        case KeyCodes.KEY_ENTER:
        case KeyCodes.KEY_TAB:
          updateDateFromTextBox();
          // Deliberate fall through
        case KeyCodes.KEY_ESCAPE:
        case KeyCodes.KEY_UP:
          hideDatePicker();
          break;
        case KeyCodes.KEY_DOWN:
          showDatePicker();
          break;
      }
    }


    @Override
    public void onValueChange(ValueChangeEvent<Date> event) {
      Date date = event.getValue();
      if (getDatePicker() instanceof JepDatePicker) {
        Date dateTime = ((JepDatePicker) getDatePicker()).getActualDate();
        if (date != null && dateTime != null) {
          date.setHours(dateTime.getHours());
          date.setMinutes(dateTime.getMinutes());
          date.setSeconds(dateTime.getSeconds());
        }
        setValue(null, date, true, true);
        if (((JepDatePicker) getDatePicker()).isVisibleDaysPanel()) {
          hideDatePicker();
        }
        preventDatePickerPopup();
        box.setFocus(true);
      } else {
        setValue(parseDate(false), date, true, true);
        hideDatePicker();
        preventDatePickerPopup();
        box.setFocus(true);
      }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
      if ((event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE
              || event.getNativeKeyCode() == KeyCodes.KEY_DELETE
              || (event.isControlKeyDown() && event.getNativeKeyCode() == KeyCodes.KEY_X))
              && box.isEmpty()) {
        setValue(null, null, true, true);
      }
    }
  }

  /**
   * Default style name added when the date box has a format error.
   */
  private static final String DATE_BOX_FORMAT_ERROR = "dateBoxFormatError";

  /**
   * Default style name.
   */
  public static final String DATE_BOX_DEFAULT_STYLE = "gwt-DateBox";

  /**
   * Datebox popup style name.
   */
  public static final String DATE_BOX_POPUP_STYLE = "jepRia-dateBoxPopup";

  private static final XDefaultFormat DEFAULT_FORMAT = GWT.create(XDefaultFormat.class);
  private final PopupPanel popup;

  /**
   * Поддерживаем текущую клиентскую мобильную платформу ?
   */
  private boolean isSupportMobilePlatform = MaskedTextBoxMobile.isSupportedMobilePlatform() && JepClientUtil.isMobile();

  /**
   * Текстовое поле с маской для ввода даты.
   */
  private final MaskedTextBox box = isSupportMobilePlatform ? new MaskedTextBoxMobile("") : new MaskedTextBox("");
  private final DatePicker picker;
  private LeafValueEditor<Date> editor;
  private XDefaultFormat format;
  private boolean allowDPShow = true;
  private boolean fireNullValues = true;

  /**
   * Create a date box with a new {@link DatePicker}.
   */
  public MaskedDateBox() {
    this(new DatePicker(), null, DEFAULT_FORMAT, new Mask(DEFAULT_DATE_FORMAT_MASK));
  }

  /**
   * Создаёт поле для выбора даты.<br>
   * Метод отличается от оригинального использованием маски, а также
   * использованием <code>addStyleName()<code> вместо <code>setStyleName()</code>.
   *
   * @param picker выпадающий виджет выбора даты
   * @param date   дата по умолчанию
   * @param format формат парсинга и представления даты
   * @param mask   маска для ввода даты (должна быть согласована с форматом)
   */
  public MaskedDateBox(DatePicker picker, Date date, XDefaultFormat format, Mask mask) {
    this.picker = picker;
    this.popup = new PopupPanel(true);
    assert format != null : "You may not construct a date box with a null format";
    this.format = format;

    popup.addAutoHidePartner(box.getElement());
    popup.setWidget(picker);
    popup.setStyleName(DATE_BOX_POPUP_STYLE);

    initWidget(box);
    /*
     * Используется метод addStyleName() вместо setStyleName(),
     * чтобы не затирать стиль MaskedDateBox.
     */
    addStyleName(DATE_BOX_DEFAULT_STYLE);

    DateBoxHandler handler = new DateBoxHandler();
    picker.addValueChangeHandler(handler);
    box.addFocusHandler(handler);
    box.addBlurHandler(handler);
    box.addClickHandler(handler);
    box.addKeyDownHandler(handler);
    box.addKeyUpHandler(handler);
    box.setDirectionEstimator(false);
    box.setMask(mask);
    box.getElement().addClassName(MAIN_FONT_STYLE);
    popup.addCloseHandler(handler);
    setValue(date);

    Event.addNativePreviewHandler(new NativePreviewHandler() {
      @Override
      public void onPreviewNativeEvent(NativePreviewEvent event) {
        if (event.getTypeInt() == Event.ONMOUSEWHEEL || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
          if (isDatePickerShowing()) {
            hideDatePicker();
          }
        }
      }
    });
  }

  public void setStyleClassName(String className) {
    box.getElement().addClassName(className);
  }

  public HandlerRegistration addValueChangeHandler(
          ValueChangeHandler<Date> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /**
   * Returns a {@link TakesValueEditor} backed by the DateBox.
   */
  public LeafValueEditor<Date> asEditor() {
    if (editor == null) {
      editor = TakesValueEditor.of(this);
    }
    return editor;
  }

  /**
   * Gets the current cursor position in the date box.
   *
   * @return the cursor position
   */
  public int getCursorPos() {
    return box.getCursorPos();
  }

  /**
   * Gets the date picker.
   *
   * @return the date picker
   */
  public DatePicker getDatePicker() {
    return picker;
  }

  /**
   * Returns true iff the date box will fire {@code ValueChangeEvents} with a
   * date value of {@code null} for invalid or empty string values.
   */
  public boolean getFireNullValues() {
    return fireNullValues;
  }

  /**
   * Gets the format instance used to control formatting and parsing of this
   * {@link DateBox}.
   *
   * @return the format
   */
  public XDefaultFormat getFormat() {
    return this.format;
  }

  /**
   * Gets the date box's position in the tab index.
   *
   * @return the date box's tab index
   */
  public int getTabIndex() {
    return box.getTabIndex();
  }

  /**
   * Get text box.
   *
   * @return the text box used to enter the formatted date
   */
  public TextBox getTextBox() {
    return box;
  }

  /**
   * Get the date displayed, or null if the text box is empty, or cannot be
   * interpreted.
   *
   * @return the current date value
   */
  public Date getValue() {
    return parseDate(true);
  }

  /**
   * Hide the date picker.
   */
  public void hideDatePicker() {
    popup.hide();
  }

  /**
   * Returns true if date picker is currently showing, false if not.
   */
  public boolean isDatePickerShowing() {
    return popup.isShowing();
  }

  /**
   * Returns true if the date box is enabled, false if not.
   */
  public boolean isEnabled() {
    return box.isEnabled();
  }

  /**
   * Проверяет, содержит ли поле корректную дату.<br>
   * Возвращает true, если значение пусто, либо если оно соответствует формату
   * и корректно распознаётся как дата.
   *
   * @return true, если содержит, и false в противном случае
   */
  public boolean isValid() {
    if (isSupportMobilePlatform) {
      return box.isValid();
    } else {
      return box.isValid() && (JepRiaUtil.isEmpty(box.getValue()) || format.parse(this, box.getValue(), false) != null);
    }
  }

  /**
   * Sets the date box's 'access key'. This key is used (in conjunction with a
   * browser-specific modifier key) to automatically focus the widget.
   *
   * @param key the date box's access key
   */
  public void setAccessKey(char key) {
    box.setAccessKey(key);
  }

  /**
   * Sets whether the date box is enabled.
   *
   * @param enabled is the box enabled
   */
  public void setEnabled(boolean enabled) {
    box.setEnabled(enabled);
  }

  /**
   * Sets whether or not the date box will fire {@code ValueChangeEvents} with a
   * date value of {@code null} for invalid or empty string values.
   */
  public void setFireNullValues(boolean fireNullValues) {
    this.fireNullValues = fireNullValues;
  }

  /**
   * Explicitly focus/unfocus this widget. Only one widget can have focus at a
   * time, and the widget that does will receive all keyboard events.
   *
   * @param focused whether this widget should take focus or release it
   */
  public void setFocus(boolean focused) {
    box.setFocus(focused);
  }

  /**
   * Sets the format used to control formatting and parsing of dates in this
   * {@link DateBox}. If this {@link DateBox} is not empty, the contents of date
   * box will be replaced with current contents in the new format.
   *
   * @param format the new date format
   */
  public void setFormat(XDefaultFormat format) {
    assert format != null : "A Date box may not have a null format";
    if (this.format != format) {
      Date date = getValue();

      // This call lets the formatter do whatever other clean up is required to
      // switch formatters.
      //
      this.format.reset(this, true);

      // Now update the format and show the current date using the new format.
      this.format = format;
      setValue(date);
    }
  }

  /**
   * Sets the date box's position in the tab index. If more than one widget has
   * the same tab index, each such widget will receive focus in an arbitrary
   * order. Setting the tab index to <code>-1</code> will cause this widget to
   * be removed from the tab order.
   *
   * @param index the date box's tab index
   */
  public void setTabIndex(int index) {
    box.setTabIndex(index);
  }

  /**
   * Set the date.
   */
  public void setValue(Date date) {
    setValue(date, false);
  }

  public void setValue(Date date, boolean fireEvents) {
    Date currentDate = picker instanceof JepDatePicker ? ((JepDatePicker) picker).getActualDate() : picker.getValue();
    setValue(currentDate, date, fireEvents, true);
  }

  /**
   * Необходимо управлять видимостью всплывающей панели
   */
  private boolean popupShowForceFireEvent = true;

  public void setVisiblePopup(boolean isShow) {
    popupShowForceFireEvent = isShow;
  }

  /**
   * Parses the current date box's value and shows that date.
   */
  public void showDatePicker() {
    Date current = parseDate(false);
    if (current == null) {
      current = new Date();
    }
    picker.setCurrentMonth(current);

    if (popupShowForceFireEvent) {
      popup.showRelativeTo(this);
    } else {
      popup.hide();
    }

  }

  /**
   * Спецификатор доступа изменён с private на protected для доступа из наследников.
   */
  protected Date parseDate(boolean reportError) {
    if (reportError) {
      getFormat().reset(this, false);
    }
    String text = box.getValue() != null ? box.getValue().trim() : "";
    if (!box.isValid()) {
      box.addStyleName(DATE_BOX_FORMAT_ERROR);
    }
    return getFormat().parse(this, text, reportError);
  }

  private void preventDatePickerPopup() {
    allowDPShow = false;
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      public void execute() {
        allowDPShow = true;
      }
    });
  }

  /**
   * Данный метод отличается от оригинального метода
   * {@link com.google.gwt.user.datepicker.client.DateBox#setValue(Date)}
   * вызовом события {@link com.google.gwt.event.logical.shared.ValueChangeEvent}
   * вместо {@link com.google.gwt.user.datepicker.client.DateChangeEvent}.
   */
  private void setValue(Date oldDate, Date date, boolean fireEvents, boolean updateText) {
    if (picker instanceof JepDatePicker && date != null) {
      ((JepDatePicker) picker).refresh(date);
    } else {
      picker.setCurrentMonth(date != null ? date : new Date());
      picker.setValue(date, false);
    }

    if (updateText) {
      format.reset(this, false);
      box.setValue(getFormat().format(this, date));
    }

    if (fireEvents) {
      // В оригинальной версии вызывалось событие DateChangeEvent
      ValueChangeEvent.fireIfNotEqual(this, oldDate, date);
    }
  }

  private void updateDateFromTextBox() {
    Date parsedDate = parseDate(true);
    if (fireNullValues || parsedDate != null) {
      Date currentDate = picker instanceof JepDatePicker ? ((JepDatePicker) picker).getActualDate() : picker.getValue();
      setValue(currentDate, parsedDate, true, false);
    }
  }
}