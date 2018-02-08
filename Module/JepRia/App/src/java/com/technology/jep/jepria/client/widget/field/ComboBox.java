package com.technology.jep.jepria.client.widget.field;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_OPTION_VALUE_HTML_ATTR;
import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_DEFAULT_HEIGHT;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.shared.field.JepLikeEnum.FIRST;
import static com.technology.jep.jepria.shared.field.option.JepOption.OPTION_NAME;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle.MultiWordSuggestion;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionCallback;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.field.multistate.event.AfterExpandEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.AfterExpandEvent.AfterExpandHandler;
import com.technology.jep.jepria.client.widget.field.multistate.event.AfterExpandEvent.HasAfterExpandHandlers;
import com.technology.jep.jepria.client.widget.field.multistate.event.BeforeExpandEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.BeforeExpandEvent.BeforeExpandHandler;
import com.technology.jep.jepria.client.widget.field.multistate.event.BeforeExpandEvent.HasBeforeExpandHandlers;
import com.technology.jep.jepria.client.widget.field.multistate.event.BeforeSelectEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.BeforeSelectEvent.BeforeSelectHandler;
import com.technology.jep.jepria.client.widget.field.multistate.event.BeforeSelectEvent.HasBeforeSelectHandlers;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Выпадающий список.
 * 
 * Особенности:<br>
 * Отличается от стандартного возможностью его гибкой настройки, что весьма проблематично, а иногда и вовсе невозможно с нативным комбобоксом.
 */
public class ComboBox<T extends JepOption> extends Composite 
      implements HasValue<T>, HasBeforeExpandHandlers, HasAfterExpandHandlers, HasBeforeSelectHandlers<T> {
  
  /**
   * Текстовая область выпадающего списка.
   */
  private SuggestBox suggestBox;
  
  /**
   * Изображение кнопки выбора элементов выпадающего списка.
   */
  private Image selectImage;
  
  /**
   * Выбранная текущая опция.
   */
  private T selectedOption;
  
  /**
   * Признак нажатия на кнопку выбора элементов выпадающего списка.
   */
  private boolean isSelectClicked = false;
  
  /**
   * Значение ширины кнопки выбора элементов.
   */
  private static final int ICON_WIDTH = 17;
  
  /**
   * Значение высоты опций выпадающего списка.
   */
  private static final int MENU_ITEM_HEIGHT = 22;
  
  /**
   * Количество опций для выбора, доступных по умолчанию.
   */
  private static final int OPTION_COUNT = 15;
  
  /**
   * Режим фильтрации, согласно которому будет произоводиться отбор опций при пользовательском
   * вводе данных (FIRST - поиск по первым символам опций, LAST - по последним, CONTAINS - по вхождению, EXACT - точное совпадение).
   */
  private JepLikeEnum filterMode = FIRST;
  
  /**
   * Наименование поля опции, по которому будет осуществлена фильтрация элементов списка (по умолчанию, наименование).
   */
  private String filterProperty = OPTION_NAME;
  
  /**
   * Список опций, не доступных для фильтрации.
   */
  private List<T> unfilteredOptions;
  
  /**
   * Элемент меню выбранной опции.
   */
  private SuggestionMenuItem selectedItem;
  
  /**
   * Наименование селектора (класса стилей) кнопки выбора в комбобоксе.
   */
  private static final String COMBOBOX_FIELD_SELECT_IMAGE_STYLE = "jepRia-ComboBox-selectImage";
  
  /**
   * Наименование селектора (класса стилей) для недоступных элементов.
   */
  private static final String DISABLED_FIELD_STYLE = "jepRia-Field-disabled";
  
  /**
   * Признак доступности поля.
   */
  private boolean enabled = true;
  
  /**
   * ID объемлющего Jep-поля как Web-элемента.
   */
  private String fieldIdAsWebEl;
  
  @Deprecated
  public ComboBox() {
    this("");
  }
  
  private JepOptionSuggestionDisplay<T> suggestionDisplay;
  
  public ComboBox(String fieldIdAsWebEl) {
    this.fieldIdAsWebEl = fieldIdAsWebEl;
    
    suggestionDisplay = new JepOptionSuggestionDisplay<T>();
    
    suggestBox = new SuggestBox(new JepOptionSuggestOracle<T>(), new TextBox(), suggestionDisplay);
    
    //не выставляем по умолчанию выделенную первую опцию
    suggestBox.setAutoSelectEnabled(false);
    suggestBox.setHeight(FIELD_DEFAULT_HEIGHT + Unit.PX.getType());
    suggestBox.setLimit(OPTION_COUNT);
    
    FlowPanel layout = new FlowPanel();
    
    selectImage = new Image(JepImages.openIcon());
    selectImage.addStyleName(COMBOBOX_FIELD_SELECT_IMAGE_STYLE);
    
    selectImage.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            if (enabled) {
              toggle();
            }
          }
      });
        
    suggestBox.addSelectionHandler(
      new SelectionHandler<Suggestion>(){
        @Override
        @SuppressWarnings("unchecked")
        public void onSelection(SelectionEvent<Suggestion> event) {
          T chosenOption = ((JepOptionSuggestion<T>) event.getSelectedItem()).getOption();
          setValue(chosenOption, true);
        }
      });
    
    suggestBox.addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        switch (event.getNativeKeyCode()) {
          case KeyCodes.KEY_ENTER:
          case KeyCodes.KEY_TAB:
            /* 
             * Обработка события сбрасывается, только когда список раскрыт.
             * Это необходимо, чтобы обрабатывалось нажатие на ENTER на форме поиска,
             * когда комбобокс имеет фокус.
             */
            if (isExpanded()) {
              event.stopPropagation();
            }
            break;
          case KeyCodes.KEY_ESCAPE:
            collapseIf(event.getNativeEvent());
            break;
        }
      }
    });
    
    layout.add(suggestBox);
    layout.add(selectImage);
    
    initWidget(layout);
    
    getElement().getStyle().setFloat(Float.LEFT);
    
    Event.addNativePreviewHandler(new NativePreviewHandler() {
      @Override
      public void onPreviewNativeEvent(NativePreviewEvent event) {
          if (event.getTypeInt() == Event.ONMOUSEWHEEL){
          collapseIf(event.getNativeEvent());
        }
      }
    });
  }

  /**
   * Установка ID внутренних компонентов Комбобокса: поля ввода, кнопки 'развернуть', PopupPanel всплывающего меню.
   * @param fieldIdAsWebEl ID JepComboBoxField'а, который берется за основу ID внутренних компонентов
   */
  public void setCompositeWebIds(String fieldIdAsWebEl) {
    this.fieldIdAsWebEl = fieldIdAsWebEl;
    suggestBox.getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_FIELD_INPUT_POSTFIX);
    selectImage.getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_COMBO_BOX_FIELD_DROPDOWN_BTN_POSTFIX);
    suggestionDisplay.setPopupPanelId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_COMBO_BOX_FIELD_POPUP_POSTFIX);
  }
  
  /**
   * Установка опций компонента.
   * 
   * @param options  список опций для выбора
   */
  public void setOptions(List<T> options){
    boolean isExpanded = isExpanded();
    if (isExpanded){
      getDisplay().hideSuggestions();
    }
    getOracle().clear();
    getOracle().addAll(options);
    if (isExpanded){
      suggestBox.showSuggestionList();
    }
  }
  
  /**
   * Получение списка доступных для выбора опций.
   * 
   * @return  список доступных опций
   */
  public List<T> getOptions(){
    return getOracle().getSuggestions();
  }
  
  /**
   * {@inheritDoc}
   * 
   * @return  текущее выбранное значение из списка
   */
  @Override
  public T getValue(){
    return selectedOption;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @param value  устанавливаемое новое значение
   */
  @Override
  public void setValue(T value){
    setValue(value, false);
  }
  
  /**
   * Установка свойства фильтрующего значения опции.
   * 
   * @param filterProperty  - новое фильтрующее свойство
   */
  public void setFilterProperty(String filterProperty) {
    this.filterProperty = filterProperty;
  }

  /**
   * Проверка раскрыт ли комбобокс.
   * 
   * @return  true - список опций отображается пользователю, в противном случае - false
   */
  public boolean isExpanded(){
    return getDisplay().isShowing();
  }
  
  /**
   * Установка состояния открытости/свернутости списка опций.
   * 
   * @param expanded  true - раскрываем комбобокс, иначе - сворачиваем список опций
   */
  public void setExpanded(boolean expanded){
    suggestBox.setFocus(true);
    if (beforeExpandEventCancelled()){
      return;
    }
    
    JepOptionSuggestionDisplay<T> display = getDisplay();
    if (expanded){
      isSelectClicked = true;
      suggestBox.showSuggestionList();
      display.initPopupHeight();
      
      fireEvent(new AfterExpandEvent());
    }
    else {
      display.hideSuggestions();
    }
  }
  
  /**
   * Изменяем состояние раскрытости/свернутости списка опций на противоположное.
   */
  public void toggle(){
    setExpanded(!isExpanded());
  }
  
  public void collapseIf(NativeEvent event){
    Element target = DOM.eventGetTarget(Event.as(event));
    if (!getElement().isOrHasChild(target)
          && !getDisplay().getMenuBar().getElement().isOrHasChild(target)) {
      if (isExpanded()) {
        setExpanded(false);
      }
    }
    
  }
  
  /**
   * {@inheritDoc}
   * 
   * @param value    новое устанавливаемое значение
   * @param fireEvents  выбрасываем событие установки нового значения
   */
  @Override
  public void setValue(T value, boolean fireEvents) {
    final T oldValue = getValue();
    
    suggestBox.setValue(!JepRiaUtil.isEmpty(value) ? value.getName() : null);
    this.selectedOption = value;

    if (fireEvents) {
      ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
    }
  }
  
  /**
   * Установка количества видимых опций (без скроллирования).
   * 
   * @param limit  новое значение количества отображаемых опций
   */
  public void setLimit(int limit){
    suggestBox.setLimit(limit);
  }
  
  /**
   * Получение количества видимых опций.
   * 
   * @return  значение количества видимых опций (без скроллирования)
   */
  public int getLimit(){
    return suggestBox.getLimit();
  }
  
  /**
   * {@inheritDoc}
   * 
   * @param handler  обработчик события изменения значения выпадающего списка
   * @return  ссылка на соответстующую запись обработчика
   */
  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }
  
  /**
   * Получение компонента текстового поля.
   * 
   * @return  ссылки на текстовое поле
   */
  public ValueBoxBase<String> getValueBoxBase(){
    return suggestBox.getValueBox();
  }
  
  /**
   * Добавление обработчика на событие, срабатывающего до раскрытия списка опций.
   * 
   * @param handler  обработчик события
   * @return  ссылка на соответстующую запись обработчика
   */
  public HandlerRegistration addBeforeExpandHandler(BeforeExpandHandler handler){
    return addHandler(handler, BeforeExpandEvent.getType());
  }
  
  /**
   * Добавление обработчика на событие, срабатывающего после раскрытия списка опций.
   * 
   * @param handler  обработчик события
   * @return  ссылка на соответстующую запись обработчика
   */
  public HandlerRegistration addAfterExpandHandler(AfterExpandHandler handler){
    return addHandler(handler, AfterExpandEvent.getType());
  }
  
  /**
   * Добавление обработчика на событие, срабатывающего до выбора опции выпадающего списка.
   * 
   * @param handler  обработчик события
   * @return  ссылка на соответстующую запись обработчика
   */
  public HandlerRegistration addBeforeSelectHandler(BeforeSelectHandler<T> handler){
    return addHandler(handler, BeforeSelectEvent.getType());
  }
  
  /**
   * Проверка наличия обработчиков события, срабатывающего до выбора опции выпадающего списка.
   * 
   * @return  true - если имеются обработчики событий такого типа, в противном случае - false 
   */
  public boolean hasBeforeSelectHandler(){
    return getHandlerCount(BeforeSelectEvent.getType()) != 0;
  }
  
  /**
   * Проверка наличия обработчиков события, срабатывающего после раскрытия опций выпадающего списка.
   * 
   * @return  true - если имеются обработчики событий такого типа, в противном случае - false 
   */
  public boolean hasAfterExpandHandler(){
    return getHandlerCount(AfterExpandEvent.getType()) != 0;
  }
  
  /**
   * Признак будет ли раскрытие списка отменено.
   * 
   * @return  true - если событие будет прервано, иначе - false
   */
  protected boolean beforeExpandEventCancelled(){
    BeforeExpandEvent event = new BeforeExpandEvent();
    fireEvent(event);
    return event.isCancelled();
  }
  
  /**
   * Получение режима фильтрации.
   * 
   * @return режим фильтрации в виде {@link com.technology.jep.jepria.shared.field.JepLikeEnum} 
   */
  public JepLikeEnum getFilterMode() {
    return filterMode;
  }
  
  /**
   * Установка режима фильтрации.
   * 
   * @param filterMode режим фильтрации
   */
  public void setFilterMode(JepLikeEnum filterMode) {
    this.filterMode = filterMode;
  }
  
  /**
   * Установка списка нефильтруемых опций.
   * 
   * @param options  нефильтруемые опции
   */
  public void setUnfilteredOptions(List<T> options) {
    this.unfilteredOptions = options;
  }
  
  /**
   * Получение абстракции "отображения" возможных опций выпадающего списка.
   * 
   * @return  абстракция "отображения" возможных опций
   */
  @SuppressWarnings("unchecked")
  public JepOptionSuggestionDisplay<T> getDisplay(){
    return (JepOptionSuggestionDisplay<T>) suggestBox.getSuggestionDisplay();
  }
  
  /**
   * Получение абстракции "логического представления" возможных опций выпадающего списка.
   * 
   * @return  абстракция "логического представления" возможных опций
   */
  @SuppressWarnings("unchecked")
  public JepOptionSuggestOracle<T> getOracle(){
    return (JepOptionSuggestOracle<T>) suggestBox.getSuggestOracle();
  }
  
  /**
   * Установка css-атрибута для конкретной опции выпадающего списка.
   * 
   * @param option  опция с изменяемым стилем
   * @param attr    стилевой атрибут
   * @param value    значение атрибута
   */
  public void applyStyleForOption(T option, String attr, String value){
    if (!JepRiaUtil.isEmpty(option)) {
      getDisplay().applyStyleToOption(option, attr, value);
    }
  }
  
  /**
   * {@inheritDoc}
   * 
   * @param width  новое значение ширины поля
   */
  @Override
  public void setWidth(String width){
    double newWidth = JepClientUtil.extractLengthValue(width);
    suggestBox.setWidth((newWidth - ICON_WIDTH) + Unit.PX.getType());
    super.setWidth((newWidth + 2) + Unit.PX.getType());
  }
  
  /**
   * Установка признака доступности/недоступности элемента. 
   * 
   * @param enabled  true - поле становится доступным, в противном случае - блокируется.
   */
  public void setEnabled(boolean enabled){
    this.enabled = enabled;
    getValueBoxBase().setEnabled(enabled);
    
    if (!enabled){
      selectImage.addStyleName(DISABLED_FIELD_STYLE);
    }
    else{
      selectImage.removeStyleName(DISABLED_FIELD_STYLE);
    }
  }

  /**
   * Логическое представление опций выпадающего списка.
   * 
   * По умолчанию, предполагается, что выбираемыми значениями списка опций могут быть строки, что
   * для прикладных модулей противоречиво. Значениями опций определяем класс, порожденный от {@link JepOption}.
   * 
   * @param <T>  тип, попрожденный от {@link JepOption}
   */
  @SuppressWarnings("hiding")
  class JepOptionSuggestion<T extends JepOption> extends MultiWordSuggestion {
    private final T option;

    public JepOptionSuggestion(T option, String displayString) {
      super(option.getName(), displayString);
      this.option = option;
    }
    
    public JepOptionSuggestion(T option) {
      super(option.getName(), option.getName());
      this.option = option;
    }

    public T getOption() {
      return this.option;
    }
  }
  
  /**
   * Общий список доступных опций выпадающего списка.
   * Необходимость в переопределении обусловлена тем, что значения опций списка является типами {@link JepOption}
   * 
   * @param <T>  параметризуемый тип
   */
  @SuppressWarnings("hiding")
  class JepOptionSuggestOracle<T extends JepOption> extends MultiWordSuggestOracle {
    
    // важен порядок следования элементов опций, чаще всего возвращаемых из БД, 
    // что гарантируется реализацией LinkedHashSet
    private final Set<T> suggestionSet = new LinkedHashSet<T>();

    public void add(T option) {
      String suggestion = option.getName();
      if (JepRiaUtil.isEmpty(suggestion)) suggestion = "";
      suggestionSet.add(option);
      super.add(suggestion);
    }
    
    public void addAll(List<T> options) {
      for (T option : options){
        add(option);
      }
    }
    
    @Override
    public void clear(){
      super.clear();
      suggestionSet.clear();
    }
    
    @Override
    public void requestDefaultSuggestions(Request request, Callback callback) {
      if (beforeExpandEventCancelled()){
        return;
      }
      
      getDisplay().initPopupHeight();
      
      Response response = new Response(getAllSuggestions());
      callback.onSuggestionsReady(request, response);
    }
    
    @Override
    public void requestSuggestions(Request request, final Callback callback) {
      if (beforeExpandEventCancelled()){
        return;
      }
      
      super.requestSuggestions(request, new Callback() {
        @Override
        public void onSuggestionsReady(Request request, Response response) {
          callback.onSuggestionsReady(request, modifyResponse(response, request.getQuery()));
          
          getDisplay().adjustPopupSize(response.getSuggestions().size());
        }
      });
    }
    
    protected List<JepOptionSuggestion<T>> getAllSuggestions(){
      List<JepOptionSuggestion<T>> optionSuggestions = new ArrayList<JepOptionSuggestion<T>>();
      
      for (T option : suggestionSet) {
        optionSuggestions.add(new JepOptionSuggestion<T>(option));
      }
      return optionSuggestions;
    }
    
    private Response modifyResponse(Response response, String filterBeginsWith) {
      List<JepOptionSuggestion<T>> optionSuggestions = new ArrayList<JepOptionSuggestion<T>>();
      
      for(T option : this.suggestionSet){
        if ((JepRiaUtil.isEmpty(unfilteredOptions) || !unfilteredOptions.contains(option)) && !JepRiaUtil.isEmpty(filterProperty) && !JepRiaUtil.isEmpty(filterBeginsWith)){
          Object o = option.get(filterProperty);
          if(o != null) {
            String objectAsString = o.toString();
            switch(filterMode) {
              case CONTAINS : 
                if(!objectAsString.toLowerCase().contains(filterBeginsWith.toLowerCase())) {
                  continue;
                };
                break;
              case LAST :
                if(!objectAsString.toLowerCase().endsWith(filterBeginsWith.toLowerCase())) {
                  continue;
                };
                break;
              case EXACT : 
                if(!objectAsString.toLowerCase().equals(filterBeginsWith.toLowerCase())) {
                  continue;
                };
                break; 
              case FIRST : // Умолчательное значение.
              default : 
                if(!objectAsString.toLowerCase().startsWith(filterBeginsWith.toLowerCase())) {
                  continue;
                };
            }
          }
        }
        
        optionSuggestions.add(new JepOptionSuggestion<T>(option));
      }
      response.setSuggestions(optionSuggestions);
      return response;
    }
    
    public int getSuggestionsCount(){
      return suggestionSet.size();
    }
    
    public List<T> getSuggestions(){
      return new ArrayList<T>(suggestionSet);
    }
  }
  
  /**
   * "Отображение" списка опций. Требует переопределения в силу, того, что значения опций
   * выпадающего списка являются типами {@link JepOption}.
   *
   * @param <T>  параметризуемый тип
   */
  @SuppressWarnings("hiding")
  class JepOptionSuggestionDisplay<T extends JepOption> extends DefaultSuggestionDisplay {
    
    private Map<T, SuggestionMenuItem> menuItems = new HashMap<T, SuggestionMenuItem>();
    private Map<T, Pair<String, String>> styledOptions = new HashMap<T, Pair<String, String>>();
        
    /**
     * Mouse events that occur within an autoHide partner will not hide a
     * panel set to autoHide.
     * 
     * @param partner the auto hide partner to add
     */
    public void addAutoHidePartner(Element partner) {
      getPopupPanel().addAutoHidePartner(partner);
    }
    
    /**
     * Determines whether or not this popup is showing.
     * 
     * @return <code>true</code> if the popup is showing
     * @see com.google.gwt.user.client.ui.PopupPanel#show()
     * @see com.google.gwt.user.client.ui.PopupPanel#hide()
     */
    public boolean isShowing() {
      return getPopupPanel().isShowing();
    }
    
    public MenuBar getMenuBar(){
      return (MenuBar) getPopupPanel().getWidget();
    }
    
    @Override
      protected void showSuggestions(final SuggestBox suggestBox,
          Collection<? extends Suggestion> suggestions,
          boolean isDisplayStringHTML, boolean isAutoSelectEnabled,
          final SuggestionCallback callback) {
      
      // если раскрываем список по кнопке выбора опций, покажем все доступные опции
      if (isSelectClicked){
        isSelectClicked = false;
        suggestions = getOracle().getAllSuggestions();
      }
      else {
        selectedOption = null;
      }
      
      super.showSuggestions(suggestBox, suggestions, isDisplayStringHTML, isAutoSelectEnabled, callback);
      getDisplay().addAutoHidePartner(selectImage.getElement());
      // перенаполним меню
      repopulateOptions(suggestBox, suggestions, isDisplayStringHTML, callback);
      // наложим стилевое оформление
      restyleOptions(suggestBox);
      
    }

    protected void restyleOptions(final SuggestBox suggestBox) {
      for (Entry<T, Pair<String, String>> entry : styledOptions.entrySet()){
        Pair<String, String> styleNameAndValue = entry.getValue();
        applyStyleToOption(entry.getKey(), styleNameAndValue.getKey(), styleNameAndValue.getValue());
      }
      
      Style popupStyle = getPopupPanel().getElement().getStyle();
      
      // TODO удалить когда баг Chrome будет пофикшен
      // Временный обход бага: некорректное позиционирование выпадающего списка опций Combobox в Chrome
      int shiftLeft_chromeFix = isChrome() ? Window.getScrollLeft() : 0;
      
      popupStyle.setPropertyPx("left", suggestBox.getAbsoluteLeft() - 3 + shiftLeft_chromeFix);
    }

    @SuppressWarnings("unchecked")
    private void repopulateOptions(
        final SuggestBox suggestBox,
        Collection<? extends Suggestion> suggestions,
        boolean isDisplayStringHTML, final SuggestionCallback callback) {
      
      final MenuBar menuBar = getMenuBar();
      menuBar.setFocusOnHoverEnabled(false);
      // удалим список опций
      menuBar.clearItems();
      menuItems.clear();
      
      String rawValue = suggestBox.getValue();

      // Выберем подходящую ширину элементов меню в зависимости от наличия или отсутсвия скролл-бара
      int offsetWidth = suggestBox.getOffsetWidth();
      if (isScrollable(suggestions.size())){
        offsetWidth -= 13;
      }
      else {
        offsetWidth += 3;
      }
      
      for (final Suggestion curSuggestion : suggestions) {
            final SuggestionMenuItem menuItem = new SuggestionMenuItem(curSuggestion, isDisplayStringHTML);
            final T currentOption = ((JepOptionSuggestion<T>) curSuggestion).getOption();
            
            menuItems.put(currentOption, menuItem);
            
            menuItem.setScheduledCommand(new ScheduledCommand() {
          public void execute() {
            BeforeSelectEvent<T> beforeSelectEvent = new BeforeSelectEvent<T>(currentOption);
            fireEvent(beforeSelectEvent);
            if (beforeSelectEvent.isCancelled()){
              menuBar.selectItem(menuItem);
              return;
            }
            
            callback.onSuggestionSelected(curSuggestion);
          }
            });
            menuItem.setTitle(currentOption.getName());
            menuItem.setWidth(offsetWidth + Unit.PX.getType());
            
            menuBar.addItem(menuItem);
            
            // if selected option or raw value exactly matches current option name
            if ((!JepRiaUtil.isEmpty(selectedOption) && currentOption.equals(selectedOption))
                || (!JepRiaUtil.isEmpty(rawValue) && rawValue.equals(currentOption.getName()))){
              menuBar.selectItem(menuItem);
            }
        }
      
      getPopupPanel().setPopupPositionAndShow(new PositionCallback() {
        public void setPosition(int offsetWidth, int offsetHeight) {
          position(suggestBox, offsetWidth, offsetHeight);
        }
      });
    }
    
    private void position(final UIObject relativeObject, int offsetWidth,
        int offsetHeight) {
      // Calculate left position for the popup. The computation for
      // the left position is bidi-sensitive.
      int textBoxOffsetWidth = relativeObject.getOffsetWidth();

      // Compute the difference between the popup's width and the textbox's width
      int offsetWidthDiff = offsetWidth - textBoxOffsetWidth;

      int left;

      if (LocaleInfo.getCurrentLocale().isRTL()) { // RTL case

        int textBoxAbsoluteLeft = relativeObject.getAbsoluteLeft();

        // Right-align the popup. Note that this computation is
        // valid in the case where offsetWidthDiff is negative.
        left = textBoxAbsoluteLeft - offsetWidthDiff;

        // If the suggestion popup is not as wide as the text box, always
        // align to the right edge of the text box. Otherwise, figure out whether
        // to right-align or left-align the popup.
        if (offsetWidthDiff > 0) {

          // Make sure scrolling is taken into account, since
          // box.getAbsoluteLeft() takes scrolling into account.
          int windowRight = Window.getClientWidth() + Window.getScrollLeft();
          int windowLeft = Window.getScrollLeft();

          // Compute the left value for the right edge of the textbox
          int textBoxLeftValForRightEdge = textBoxAbsoluteLeft + textBoxOffsetWidth;

          // Distance from the right edge of the text box to the right edge
          // of the window
          int distanceToWindowRight = windowRight - textBoxLeftValForRightEdge;

          // Distance from the right edge of the text box to the left edge of the window
          int distanceFromWindowLeft = textBoxLeftValForRightEdge - windowLeft;

          // If there is not enough space for the overflow of the popup's
          // width to the right of the text box and there IS enough space for the
          // overflow to the right of the text box, then left-align the popup.
          // However, if there is not enough space on either side, stick with
          // right-alignment.
          if (distanceFromWindowLeft < offsetWidth
              && distanceToWindowRight >= offsetWidthDiff) {
            // Align with the left edge of the text box.
            left = textBoxAbsoluteLeft;
          }
        }
      } else { // LTR case

        // Left-align the popup.
        left = relativeObject.getAbsoluteLeft();

        // If the suggestion popup is not as wide as the text box, always align to
        // the left edge of the text box. Otherwise, figure out whether to
        // left-align or right-align the popup.
        if (offsetWidthDiff > 0) {
          // Make sure scrolling is taken into account, since
          // box.getAbsoluteLeft() takes scrolling into account.
          int windowRight = Window.getClientWidth() + Window.getScrollLeft();
          int windowLeft = Window.getScrollLeft();

          // Distance from the left edge of the text box to the right edge
          // of the window
          int distanceToWindowRight = windowRight - left;

          // Distance from the left edge of the text box to the left edge of the
          // window
          int distanceFromWindowLeft = left - windowLeft;

          // If there is not enough space for the overflow of the popup's
          // width to the right of hte text box, and there IS enough space for the
          // overflow to the left of the text box, then right-align the popup.
          // However, if there is not enough space on either side, then stick with
          // left-alignment.
          if (distanceToWindowRight < offsetWidth
              && distanceFromWindowLeft >= offsetWidthDiff) {
            // Align with the right edge of the text box.
            left -= offsetWidthDiff;
          }
        }
      }

      // Calculate top position for the popup

      int top = relativeObject.getAbsoluteTop();

      // Make sure scrolling is taken into account, since
      // box.getAbsoluteTop() takes scrolling into account.
      int windowTop = Window.getScrollTop();
      int windowBottom = Window.getScrollTop() + Window.getClientHeight();

      // Distance from the top edge of the window to the top edge of the
      // text box
      int distanceFromWindowTop = top - windowTop;

      // Distance from the bottom edge of the window to the bottom edge of
      // the text box
      int distanceToWindowBottom = windowBottom - (top + relativeObject.getOffsetHeight());

      // If there is not enough space for the popup's height below the text
      // box and there IS enough space for the popup's height above the text
      // box, then then position the popup above the text box. However, if there
      // is not enough space on either side, then stick with displaying the
      // popup below the text box.
      if (distanceToWindowBottom < offsetHeight
          && distanceFromWindowTop >= offsetHeight) {
        top -= offsetHeight;
        // Add the height of invisible suggestions (all suggestions minus visible ones)
        top += (getOracle().getAllSuggestions().size() - menuItems.size()) * MENU_ITEM_HEIGHT;
      } else {
        // Position above the text box
        top += relativeObject.getOffsetHeight();
      }

      // TODO удалить когда баг Chrome будет пофикшен
      // Временный обход бага: некорректное позиционирование выпадающего списка опций Combobox в Chrome
      int shiftTop_chromeFix = isChrome() ? Window.getScrollTop() : 0;
      
      getPopupPanel().setPopupPosition(left, top + shiftTop_chromeFix);
    }
    
    @Override
      protected Suggestion getCurrentSelection() {
      return selectedItem == null ? null : selectedItem.getSuggestion();
      }
    
    public Collection<SuggestionMenuItem> getMenuItems(){
      return menuItems.values();
    }
    
    public void applyStyleToOption(T option, String attr, String value){
      if (!styledOptions.containsKey(option)){
        styledOptions.put(option, new Pair<String, String>(attr, value));
      }
      SuggestionMenuItem menuItem = menuItems.get(option);
      if (!JepRiaUtil.isEmpty(menuItem)){
        menuItem.getElement().setPropertyString(attr, value);
      }
    }
    
    public void adjustPopupSize(int optionCount){
      NodeList<Element> nodes = getPopupPanel().getElement().getElementsByTagName("div");
      for (int i = 0; i < nodes.getLength(); i++){
        Element element = nodes.getItem(i);
        if (element.getClassName().contains("suggestPopupContent")){
          Style popupStyle = element.getStyle();
          boolean moreSuggestions = isScrollable(optionCount);
          if (moreSuggestions){
            popupStyle.setOverflowY(Overflow.SCROLL);
            popupStyle.setOverflowX(Overflow.HIDDEN);
            popupStyle.setHeight(getLimit() * MENU_ITEM_HEIGHT, Unit.PX);
          }
          else {
            popupStyle.clearOverflowY();
            popupStyle.setHeight(optionCount * MENU_ITEM_HEIGHT, Unit.PX);
          }
        }
      }
    }
    
    @Override
      protected void moveSelectionDown() {
      if (!isSuggestionListShowing()) {
        toggle();
      }
      else {
        super.moveSelectionDown();
      }
    }
    
    private boolean isScrollable(int optionCount){
      return getLimit() < optionCount;
    }
    
    public void initPopupHeight(){
      adjustPopupSize(getOracle().getSuggestionsCount());
    }
    
    class Pair<K, V> {
      private final K key;
      private final V value;

      public Pair(K a, V b) {
        this.key = a;
        this.value = b;
      }

      public K getKey() {
        return key;
      }

      public V getValue() {
        return value;
      }
    }
    
    /**
     * Установка ID PopupPanel всплывающего меню.
     */
    public void setPopupPanelId(String id) {
      getPopupPanel().getElement().setId(id);
    }
  }
  
  /**
   * TODO удалить когда баг Chrome будет пофикшен
   * 
   * Временный обход бага: некорректное позиционирование выпадающего списка опций Combobox в Chrome 
   */
  private native boolean isChrome() /*-{
    return /Chrome/.test(navigator.userAgent);
  }-*/;
  
  /**
   * Меню опции выпадающего списка. Добавлен для возможности кастомизации стилевого оформления стилей. 
   */
  class SuggestionMenuItem extends MenuItem {

      private static final String SUGGESTION_MENU_ITEM_STYLE = "item";

      private Suggestion suggestion;

      public SuggestionMenuItem(Suggestion suggestion, boolean asHTML) {
        super(suggestion.getDisplayString(), asHTML, (ScheduledCommand) null);
        if (fieldIdAsWebEl != null) {
          getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_COMBO_BOX_FIELD_MENU_ITEM_INFIX + suggestion.getDisplayString());
        }
        
        String jepOptionValue = suggestion.getDisplayString();
        getElement().setAttribute(JEP_OPTION_VALUE_HTML_ATTR, (jepOptionValue != null) ? jepOptionValue : "");
        
        // Each suggestion should be placed in a single row in the suggestion
        // menu. If the window is resized and the suggestion cannot fit on a
        // single row, it should be clipped (instead of wrapping around and
        // taking up a second row).
        getElement().setPropertyString("whiteSpace", "nowrap");
        setStyleName(SUGGESTION_MENU_ITEM_STYLE);
        setSuggestion(suggestion);
      }

      public Suggestion getSuggestion() {
        return suggestion;
      }

      public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
      }
      
      @Override
      protected void setSelectionStyle(boolean selected) {
        super.setSelectionStyle(selected);
        
        if (selected){
          selectedItem = this;
          // show selected element
          JepClientUtil.adjustToTop(getElement());
        }
      }
  }
}
