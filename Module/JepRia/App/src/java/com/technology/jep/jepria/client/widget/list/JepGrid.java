package com.technology.jep.jepria.client.widget.list;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.GRID_BODY_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.GRID_HEADER_POSTFIX;
import static com.technology.jep.jepria.client.JepRiaClientConstant.DND_DATA_PROPERTY;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.shared.load.PagingConfig.DEFAULT_PAGE_SIZE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.DataTransfer;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DragEvent;
import com.google.gwt.event.dom.client.DragHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SetSelectionModel;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.container.ElementSimplePanel;
import com.technology.jep.jepria.client.widget.list.event.RowPositionChangeEvent;
import com.technology.jep.jepria.client.widget.list.header.ResizableHeader;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Класс для создания таблиц данных, в качестве строк которых выступают записи указанного типа T
 * 
 * @param <T>    тип записей строк таблицы
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class JepGrid<T> extends DataGrid<T> {
  
  /**
   * Виджет со строками таблицы
   */
  private ScrollPanel contentWidget;
  
  /**
   * Идентификатор таблицы данных для сохранения конфигурации столбцов в cookies
   */
  private final String cookieId;
  
  /**
   * Список колонок таблица данных
   */
  private List<JepColumn> columns;
  
  /**
   * Флаг допустимости переноса наименований колонок таблицы данных
   */
  private boolean wrapHeaders = true;
  
  /**
   * Флаг допустимости настройки порядка следования колонок и их отображения в таблице данных 
   */
  private boolean isColumnConfigurable = true;
  
  /**
   * Флаг доступности переноса строк таблицы данных
   */
  private boolean dndEnabled = false;
  
  /**
   * Текущий режим работы Drag&Drop
   */
  private DndMode dndMode = DndMode.NONE;
  
  /**
   * Флаг полной инициализации виджета
   */
  private boolean initialized = false;
  
  /**
   * Разделитель в строке, хранящей информацию о характеристиках таблицы
   */
  private static final String CHARACTERISTIC_SEPARATOR = "=";
  
  /**
   * Линейный градиент перехода от голубого цвета к белому
   */
  private static final String LINEAR_GRADIENT_FROM_TOP_TO_BOTTOM = "linear-gradient(to bottom, #ACCBF2 , #FFFFFF 50%)";
  
  /**
   * Линейный градиент перехода от голубого цвета к белому и снова к голубому
   */
  private static final String LINEAR_GRADIENT_TRIPLE_COLOR = "linear-gradient(#FFFFFF, #ACCBF2, #FFFFFF)";
  
  /**
   * Линейный градиент перехода от белого цвета к голубому
   */
  private static final String LINEAR_GRADIENT_FROM_BOTTOM_TO_TOP = "linear-gradient(to top, #ACCBF2 , #FFFFFF 50%)";
  
  /**
   * Обработчик системного события начала переноса строки таблицы
   */
  protected HandlerRegistration dragStartHandler;
  
  /**
   * Обработчик системного события нахождения строки таблицы над другой строкой
   */
  protected HandlerRegistration dragOverHandler;
  
  /**
   * Обработчик системного события покидания строки таблицы другой строки
   */
  protected HandlerRegistration dragLeaveHandler;
  
  /**
   * Обработчик системного события завершения переноса строки таблицы
   */
  protected HandlerRegistration dropHandler;
  
  /**
   * Флаги позиции курсора при Drag&Drop.
   * Курсор над строкой.
   */
  protected Boolean isDraggedOver = false;
  
  /**
   * Флаги позиции курсора при Drag&Drop.
   * Курсор перед строкой.
   */
  protected Boolean isDraggedBefore = false;
  
  /**
   * Флаги позиции курсора при Drag&Drop.
   * Курсор после строки.
   */
  protected Boolean isDraggedAfter = false;

  /**
   * Флаг отключенного выделения текста в ячейках </br>
   * (важно значение "true" при Drag&Drop, иначе вместо перетаскиваний будет выделяться текст).
   */
  private boolean isTextSelectionDisabled = false;
  
  /**
   * Флаг начала события DragStart
   */
  private boolean isDragStarted = false;
  
  /**
   * Таймер обрабатывающий скроллинг
   */
  private Timer scrollTimer = null;
  
  protected static String RESIZABLE_HEADER_LABEL_STYLE = "jepRia-ResizableHeader-Label";
  
  /**
   * Интерфейс для стилизации виджета
   */
  public interface MyStyle extends DataGrid.Style {
  }

  /**
   * Интерфейс ресурсов для кастомизации внешнего отображения виджета
   */
  public interface DataGridResource extends DataGrid.Resources {
    @Source({ DataGrid.Style.DEFAULT_CSS, "DataGridOverride.css" })
    MyStyle dataGridStyle();
  }
  
  /**
   * Режимы работы Drag&Drop
   */
  public enum DndMode {
    /**
     * Отключен.
     */
    NONE, 
    /**
     * Допускается только вставка в узел (TreeGrid).
     */
    APPEND, 
    /**
     * Допускается только вставка между строками.
     */
    INSERT, 
    /**
     * Допускается как вставка в узел, так и между строками.
     */
    BOTH;
  }
  
  /**
   * Создает таблицу данных на списочной форме.
   * 
   * @param cookieId      идентификатор таблицы данных для сохранения в Cookies
   * @param gridIdAsWebEl    идентификатор таблицы данных как веб-элемента
   * @param columns      список колонок
   */
  public JepGrid(String cookieId, String gridIdAsWebEl, List<JepColumn> columns) {
    this(cookieId, gridIdAsWebEl, columns, null);
  }
  
  /**
   * Создает таблицу данных на списочной форме.
   * 
   * @param cookieId      идентификатор таблицы данных для сохранения в Cookies
   * @param gridIdAsWebEl    идентификатор таблицы данных как веб-элемента
   * @param columns      список колонок
   * @param keyProvider    провайдер ключей таблицы данных
   */
  public JepGrid(String cookieId, String gridIdAsWebEl, List<JepColumn> columns, ProvidesKey<T> keyProvider) {
    super(DEFAULT_PAGE_SIZE, (DataGridResource) GWT.create(DataGridResource.class), keyProvider);
    
    this.cookieId = cookieId;
    
    if (gridIdAsWebEl != null) {
      this.getElement().setId(gridIdAsWebEl);
      getTableHeadElement().setId(gridIdAsWebEl + GRID_HEADER_POSTFIX);
      getTableBodyElement().setId(gridIdAsWebEl + GRID_BODY_POSTFIX);
    }
    
    this.columns = columns;
    
    this.contentWidget = (ScrollPanel) ((HeaderPanel) getWidget()).getContentWidget();

    setAutoHeaderRefreshDisabled(true);
    setMinimumTableWidth(300, Unit.PX);
    setHeight("100%");
    
    addCellPreviewHandler(new Handler<T>() {
      @Override
      public void onCellPreview(CellPreviewEvent<T> event) {
        if (BrowserEvents.MOUSEOVER.equals(event.getNativeEvent().getType())) {
          onMouseOver(event);
        }
      }
    });
    
    final SelectionModel<T> selectionModel = new MultiSelectionModel<T>();
    setSelectionModel(selectionModel, DefaultSelectionEventManager.<T>createDefaultManager());
  }

  /**
   * Создает таблицу данных на списочной форме.
   * 
   * @param cookieId      идентификатор таблицы данных для сохранения в Cookies
   * @param gridIdAsWebEl    идентификатор таблицы данных как веб-элемента
   * @param columns      список колонок
   * @param wrapHeaders    допустимость переноса наименования колонок
   * 
   * Особенность: следует использовать альтернативные перегруженные конструкторы 
   */
  @Deprecated
  public JepGrid(String cookieId, String gridIdAsWebEl, List<JepColumn> columns, boolean wrapHeaders) {
    this(cookieId, gridIdAsWebEl, columns, wrapHeaders, null);
  }
  
  /**
   * Создает таблицу данных на списочной форме.
   * 
   * @param cookieId      идентификатор таблицы данных для сохранения в Cookies
   * @param gridIdAsWebEl    идентификатор таблицы данных как веб-элемента
   * @param columns      список колонок
   * @param wrapHeaders    допустимость переноса наименования колонок
   * @param keyProvider    провайдер ключей таблицы данных
   * 
   * Особенность: следует использовать альтернативные перегруженные конструкторы
   */
  @Deprecated
  public JepGrid(String cookieId, String gridIdAsWebEl, List<JepColumn> columns, boolean wrapHeaders, ProvidesKey<T> keyProvider) {
    this(cookieId, gridIdAsWebEl, columns, keyProvider);
    this.wrapHeaders = wrapHeaders;
  }

  /**
   * Добавление колонки с заголовком
   * 
   * @param col      колонка
   * @param toggle    признак переключения
   */
  public void addColumnWithHeader(JepColumn col, boolean toggle) {
    Header<String> header = new ResizableHeader<T>(col.getHeaderText(), this, col, this.isColumnConfigurable);
    String headerStyles = RESIZABLE_HEADER_LABEL_STYLE;
    if (this.wrapHeaders){
      headerStyles += " " + JepColumn.NORMAL_WRAP_STYLE;
    }
    header.setHeaderStyleNames(headerStyles);
    
    int currentIndex = indexOf(col), columnCount = getColumnCount();
    if (toggle) columnCount--;
    
    if (currentIndex == -1){
      currentIndex = columnCount;
    }
    else if (currentIndex > columnCount){
      currentIndex = columnCount;
    }
    insertColumn(currentIndex, col, header);
  }
  
  /**
   * Добавление колонки по индексу
   * 
   * @param col       колонка
   * @param beforeId  id колонки, перед которой будет установлена новая колонка
   */
  public void addColumn(JepColumn col, int beforeId) {
    Header<String> header = new ResizableHeader<T>(col.getHeaderText(), this, col, this.isColumnConfigurable);
    String headerStyles = RESIZABLE_HEADER_LABEL_STYLE;
    if (this.wrapHeaders){
      headerStyles += " " + JepColumn.NORMAL_WRAP_STYLE;
    }
    header.setHeaderStyleNames(headerStyles);
    setColumnWidth(col, col.getWidth(), Unit.PX);
    insertColumn(beforeId, col, header);
  }
  
  /**
   * Переключение видимости колонки (добавление/удаление)
   * 
   * @param currentColumn      переключаемая колонка
   * @return признак включаемости колонки
   */
  public boolean toggleColumn(JepColumn currentColumn) {
    boolean unchecked = false;
    if (unchecked = (getColumnIndex(currentColumn) > -1)){
      removeColumn(currentColumn);
    }
    else {
      addColumnWithHeader(currentColumn, true);
    }
    return unchecked;
  }
  
  /**
   * Возврат характеристик колонки в виде строки
   * 
   * @return      строковое представление характеристик колонок
   */
  public String getColumnCharacteristicsAsString() {
    StringBuilder colCharacteristics = new StringBuilder();

    for (JepColumn column : this.columns){
      String fieldName = column.getDataStoreName();
      if (JepRiaUtil.isEmpty(fieldName)) continue;
      
      if (columns.indexOf(column) != 0) colCharacteristics.append(";");
      colCharacteristics.append(fieldName);
      colCharacteristics.append(CHARACTERISTIC_SEPARATOR);
      colCharacteristics.append(Boolean.toString(isColumnVisible(fieldName)));
      colCharacteristics.append(CHARACTERISTIC_SEPARATOR);
      colCharacteristics.append(Integer.toString(indexOf(column)));
      colCharacteristics.append(CHARACTERISTIC_SEPARATOR);
      colCharacteristics.append(getColumnWidth(column));
    }
    return colCharacteristics.toString();
  }
  
  /**
   * Метод перезаписи изменившихся данных о колонках в Cookie
   */
  @SuppressWarnings("deprecation")
  public void columnCharacteristicsChanged() {
    // сохраняем в Cookie пользовательские настройки ширины столбцов
    Date expires = new Date();
    expires.setYear(expires.getYear() + 1);

    Cookies.setCookie(cookieId, getColumnCharacteristicsAsString(), expires);
  }
  
  /**
   * Метод извлечения данных о таблице данных из строки
   * 
   * @param cookieString      строка, хранящая информация по таблице данных
   * @return карта соответствий идентификатора колонки и ее характеристик
   */
  private Map<String, ColumnCharasteristic> parseColumnCharacteristics(String cookieString) {
    Map<String, ColumnCharasteristic> columnVisibles = new HashMap<String, ColumnCharasteristic>();

    if (cookieString != null) {
      String[] columnVisibleList = cookieString.split(";");
      for (String columnCharacteristicAsStr : columnVisibleList) {
        String[] characteristics = columnCharacteristicAsStr.split(CHARACTERISTIC_SEPARATOR);
        if (characteristics.length < 4) continue;
        String fieldName = characteristics[0];
        Boolean visible = Boolean.valueOf(characteristics[1]);
        Integer order = Integer.decode(characteristics[2]);
        String width = characteristics[3];
        columnVisibles.put(fieldName, new ColumnCharasteristic(fieldName, width, order, visible));
      }
    }

    return columnVisibles;
  }
    
  /**
   * Получение списка колонок таблицы данных
   * 
   * @return  список колонок
   */
  public List<JepColumn> getColumns(){
    return this.columns;
  }
  
  /**
   * Получение точного индекса колонки по ее идентификатору
   * 
   * @param id      идентификатор колонки
   * @return индекс колонки
   */
  public int getIndexColumnById(String id){
    for (JepColumn c : columns){
      if (c.getFieldName().equals(id))
        return columns.indexOf(c);
    }
    return -1;
  }
  
  /**
   * Получение точного индекса колонки
   * 
   * @param column    искомая колонка таблицы данных
   * @return индекс колонки
   */
  public int indexOf(JepColumn column){
    for (JepColumn c : columns){
      if (c.equals(column)){
        int index = columns.indexOf(c), colIndex = index;
        while (index > 0){
          index--;
          if (!isColumnVisible(columns.get(index).getFieldName())){
            colIndex--;
          }
        }
        return colIndex;
      }
    }
    return -1;
  }
  
  /**
   * Проверка видимости колонку
   * 
   * @param id    идентификатор искомой колонки
   * @return признак видимости
   */
  public boolean isColumnVisible(String id){
    for (int i = 0; i < getColumnCount(); i++){
      if (id.equalsIgnoreCase(((JepColumn) getColumn(i)).getFieldName())){
        return true;
      }
    }
    return false;
  }
  
  /**
   * Обработчик события наведения мыши на элементы таблицы.
   * @param event событие
   */
  protected void onMouseOver(CellPreviewEvent<T> event) {
    Element cellElement = event.getNativeEvent().getEventTarget().cast();
    String toolTip = cellElement.getInnerText();
    cellElement.setTitle(JepClientUtil.jsTrim(toolTip)); // убираем первые пробелы для древовидного справочника
  }
  
  /**
   * Add a handler to handle {@link com.technology.jep.jepria.client.widget.list.event.RowPositionChangeEvent}s.
   * 
   * @param handler the {@link com.technology.jep.jepria.client.widget.list.event.RowPositionChangeEvent.Handler} to add
   * @return a {@link com.google.gwt.event.shared.HandlerRegistration} to remove the handler
   */
  public HandlerRegistration addRowPositionChangeEventHandler(RowPositionChangeEvent.Handler handler) {
    return addHandler(handler, RowPositionChangeEvent.getType());
  }

  /**
   * Перегруженная версия получения индекса строки таблицы по DOM-событию
   * 
   * @param event      случившееся DOM-событие
   * @return    индекс строки таблицы данных
   */
  public int getRowIndexByEvent(DomEvent<?> event) {
    Element tableRow = Element.as(event.getNativeEvent().getEventTarget());
    // index by default which corresponds an unmatched row
    int selectedIndexRow = -1;
    //look for a necessary row
    for (int index = 0; index < getRowCount(); index++){
      if (getRowElement(index).isOrHasChild(tableRow)){
        selectedIndexRow = index;
        break;
      }
    }
    return selectedIndexRow;
  }
  
  /**
   * Получение строки таблицы по DOM-событию
   * 
   * @param event      случившееся DOM-событие
   * @return DOM-элемент строки таблицы данных
   */
  public TableRowElement getRowElement(DomEvent<?> event){
    int rowIndex = getRowIndexByEvent(event);
    return rowIndex == -1 ? null : getRowElement(rowIndex);
  }
  
  /**
   * Add a handler to handle {@link com.google.gwt.event.dom.client.DragStartEvent}s.
   * 
   * @param handler the {@link com.google.gwt.event.dom.client.DragStartHandler} to add
   * @return a {@link com.google.gwt.event.shared.HandlerRegistration} to remove the handler
   */
  public HandlerRegistration addDragStartHandler(DragStartHandler handler){
    return contentWidget.addDomHandler(handler, DragStartEvent.getType());
  }
  
  /**
   * Add a handler to handle {@link com.google.gwt.event.dom.client.DragOverEvent}s.
   * 
   * @param handler the {@link com.google.gwt.event.dom.client.DragOverHandler} to add
   * @return a {@link com.google.gwt.event.shared.HandlerRegistration} to remove the handler
   */
  public HandlerRegistration addDragOverHandler(DragOverHandler handler){
    return contentWidget.addDomHandler(handler, DragOverEvent.getType());
  }
  
  /**
   * Add a handler to handle {@link com.google.gwt.event.dom.client.DragLeaveEvent}s.
   * 
   * @param handler the {@link com.google.gwt.event.dom.client.DragLeaveHandler} to add
   * @return a {@link com.google.gwt.event.shared.HandlerRegistration} to remove the handler
   */
  public HandlerRegistration addDragLeaveHandler(DragLeaveHandler handler){
    return contentWidget.addDomHandler(handler, DragLeaveEvent.getType());
  }
  
  /**
   * Add a handler to handle {@link com.google.gwt.event.dom.client.DropEvent}s.
   * 
   * @param handler the {@link com.google.gwt.event.dom.client.DropHandler} to add
   * @return a {@link com.google.gwt.event.shared.HandlerRegistration} to remove the handler
   */
  public HandlerRegistration addDropHandler(DropHandler handler){
    return contentWidget.addDomHandler(handler, DropEvent.getType());
  }
  
  /**
   * Получить ссылку на виджет со строками таблицы
   * 
   * @return  виджет со строками записей
   */
  public ScrollPanel getContentWidget(){
    return contentWidget;
  }
  
  /**
   * Возвращает флаг доступности переноса строк на таблице данных 
   * 
   * @return  флаг доступности переноса строк
   */
  boolean isDndEnabled(){
    return this.dndMode != DndMode.NONE;
  }
  
  /**
   * Возвращает текущий режим работы Drag&Drop 
   * 
   * @return  режим работы Drag&Drop
   */
  DndMode getCurrentDndMode(){
    return this.dndMode;
  }
  
  /**
   * Установка или отключение возможности переноса наименований колонок.
   * 
   * @param wrapHeaders    флаг допустимости переноса наменований колонок
   */
  public void setWrapHeaders(boolean wrapHeaders) {
    if (initialized) {
      throw new IllegalStateException(JepTexts.errors_list_preConfigurationError());
    }
    this.wrapHeaders = wrapHeaders;
  }
  
  /**
   * Установка или отключение возможности конфигурирования характеристик колонок.
   * 
   * @param isColumnConfigurable    флаг возможности конфигурирования колонок
   */
  public void setColumnConfigurable(boolean isColumnConfigurable) {
    if (initialized) {
      throw new IllegalStateException(JepTexts.errors_list_preConfigurationError());
    }
    this.isColumnConfigurable = isColumnConfigurable;
  }

  /**
   * Установка или отключение возможности переноса строк в таблице данных.
   * Установка режима работы Drag&Drop.
   * 
   * @param mode режим работы Drag&Drop.
   */
  public void setDndMode(DndMode mode) {
    if (initialized) {
      throw new IllegalStateException(JepTexts.errors_list_preConfigurationError());
    }
    this.dndMode = mode;
    if(mode == DndMode.NONE) {
      isTextSelectionDisabled = false;
      this.getElement().setClassName(JepRiaUtil.removeStrIfPresent(this.getElement().getClassName(), " noselect"));
    }
    else {
      this.getElement().setClassName(this.getElement().getClassName()+" noselect");
      isTextSelectionDisabled = true;
    }
  }
  
  /**
   * Определяет находится ли курсор мыши между строками таблицы.
   * 
   * @param rowElement      текущая строка таблицы
   * @param event          событие перетаскивания
   * @return флаг нахождения курсора в верхней части элемента
   */
  private boolean isCursorBetweenElements(Element rowElement, NativeEvent event){
    int top = rowElement.getAbsoluteTop(), bottom = rowElement.getAbsoluteBottom(); 
    int height = rowElement.getOffsetHeight(), clientY = event.getClientY();
    this.isDraggedOver = !((clientY - top < height * 0.3) || (clientY > bottom - (height * 0.3)));
    this.isDraggedBefore = clientY - top < height * 0.3;
    this.isDraggedAfter = clientY > bottom - (height * 0.3);
    return (clientY - top < height * 0.3) || (clientY > bottom - (height * 0.3));  
  }
  
  /**
   * Определяет находится наверху или внизу таблицы
   *
   * @param event  событие перетаскивания
   * @return флаг нахождения курсора в верхней части таблицы
   */
  private boolean isCursorOnTableTop(int currentClientY){
    Element tableElement = this.contentWidget.getElement();
    int top = tableElement.getAbsoluteTop();
    int height = tableElement.getOffsetHeight();
    return currentClientY - top > 0 && currentClientY - top < height * 0.05; 
  }
  
  /**
   * Определяет находится наверху или внизу таблицы
   *
   * @param event  событие перетаскивания
   * @return флаг нахождения курсора в верхней части таблицы
   */
  private boolean isCursorOnTableBottom(int currentClientY){
    Element tableElement = this.contentWidget.getElement();
    int bottom = tableElement.getAbsoluteBottom(); 
    int height = tableElement.getOffsetHeight();
    return currentClientY > bottom - (height * 0.05);  
  }
  /**
   * {@inheritDoc}
   * Особенности:<br/>
   * После переинициализации новых данных, каждая строка таблицы данных становится доступной для перетаскивания.
   */
  @Override
  protected void replaceAllChildren(List<T> values, SafeHtml html) {
    super.replaceAllChildren(values, html);
    if (isDndEnabled()) {
      for (int row = 0; row < getRowCount(); row++){
        final TableRowElement tableRow = getRowElement(row);
        tableRow.setDraggable(Element.DRAGGABLE_TRUE);
      }
    }
  }
  
  /**
   * {@inheritDoc}
   * Особенности:<br/>
   * После выбора строки, выбранная строка вновь доступна для перетаскивания.
   */
  @Override
  protected void replaceChildren(List<T> values, int start, SafeHtml html) {
    super.replaceChildren(values, start, html);
    if (isDndEnabled()) {
      for (int row = 0; row < getRowCount(); row++){
        final TableRowElement tableRow = getRowElement(row);//Требуется устанавливать свойство DRAGGABLE
        tableRow.setDraggable(Element.DRAGGABLE_TRUE);//для всех строк таблицы, иначе периодически 
      }                      //находятся строки с отсутствующим свойством => не работает DnD
    }
  }
  
  /**
   * {@inheritDoc}
   * 
   * Особенности:<br/>
   * Конфигурирование виджета происходит в момент его добавления к DOM-дереву и единожды.
   */
  @Override
  protected void onLoad() {
    super.onLoad();
        
    if (!initialized){
      initialize();
    }
  }

  /**
   * Возвращается список выделенных строк.
   *
   * @return Список выделенных строк.
   */
  public List<T> getSelection(){
    return new ArrayList<T>(((SetSelectionModel)JepGrid.super.getSelectionModel()).getSelectedSet());
  }
  
  /**
   * Инициализируется список колонок таблицы данных и в случае необходимости добавляются слушатели 
   * событий {@link com.technology.jep.jepria.client.widget.event.JepEventType#DRAG_START_EVENT}, {@link com.technology.jep.jepria.client.widget.event.JepEventType#DRAG_OVER_EVENT},
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#DRAG_LEAVE_EVENT}, {@link com.technology.jep.jepria.client.widget.event.JepEventType#DROP_EVENT}
   */
  protected void initialize(){
    if (initialized) {
      throw new IllegalStateException(JepTexts.errors_list_preConfigurationError());
    }
    
    initialized = true;
    
    final Map<String, ColumnCharasteristic> customColumnCharacteristics = parseColumnCharacteristics(Cookies.getCookie(cookieId));
    
    if (!customColumnCharacteristics.isEmpty()){
      Collections.sort(columns, new Comparator<JepColumn>() {
        @Override
        public int compare(JepColumn col1, JepColumn col2) {
          ColumnCharasteristic characteristicCol1 = customColumnCharacteristics.get(col1.getDataStoreName()),
              characteristicCol2 = customColumnCharacteristics.get(col2.getDataStoreName());
          return (JepRiaUtil.isEmpty(characteristicCol1) ? columns.indexOf(col1) : characteristicCol1.order) - 
                (JepRiaUtil.isEmpty(characteristicCol2) ? columns.indexOf(col2) : characteristicCol2.order);
        }
      });
    }
    
    for (JepColumn col : columns) {
      ColumnCharasteristic characteristic = customColumnCharacteristics.get(col.getDataStoreName());
      if (characteristic != null) {
        setColumnWidth(col, characteristic.width);
        if (characteristic.visible) {
          addColumnWithHeader(col, false);
        }
      }
      else {
        setColumnWidth(col, col.getWidth(), Unit.PX);
        addColumnWithHeader(col, false);
      }
    }

    // последний столбец-заглушка, для корректного отображения ширины столбцов
    addColumn(new JepColumn<T, String>(new TextCell()));
    
    if (this.dndMode != DndMode.NONE){
      bindDragAndDropListeners();
    }
  }
  
  /**
   * Привязка слушателей переноса строк к таблице данных. 
   */
  private void bindDragAndDropListeners(){
    // grid content divided into rows 
    this.dragStartHandler = addDragStartHandler(new DragStartHandler() {
      @Override
      public void onDragStart(DragStartEvent event) {
        //Required: set data for the event
        //Without that DND doesn't work in FF
        DataTransfer dataTransfer = event.getDataTransfer();
        //row which cause this event  
        int rowIndex = getRowIndexByEvent(event);
        dataTransfer.setData(DND_DATA_PROPERTY, rowIndex + "");
        T selectedRow = getVisibleItems().get(rowIndex);
        if (!getSelectionModel().isSelected(selectedRow)) {
          ((SetSelectionModel) getSelectionModel()).clear();
          getSelectionModel().setSelected(selectedRow, true);
        }
        dataTransfer.setDragImage(getRowElement(event), 10, 10);
        isDragStarted = true;
      }
    });
    
    this.dragOverHandler = addDragOverHandler(new DragOverHandler() {
        @Override
        public void onDragOver(DragOverEvent event) {
          TableRowElement rowElement = getRowElement(event);
          if (rowElement != null){
            isCursorBetweenElements(rowElement, event.getNativeEvent());
            if(isDraggedBefore) {
              rowElement.getStyle().setBackgroundImage(LINEAR_GRADIENT_FROM_TOP_TO_BOTTOM);
            }
            if(isDraggedAfter){
              rowElement.getStyle().setBackgroundImage(LINEAR_GRADIENT_FROM_BOTTOM_TO_TOP);
            }
            if(isDraggedOver){
              rowElement.getStyle().setBackgroundImage(LINEAR_GRADIENT_TRIPLE_COLOR);
            }
          }
        }
    });
    
    contentWidget.addDomHandler(new DragHandler(){
      @Override
      public void onDrag(final DragEvent event) {
        if (isDragStarted) {
          final int currentClientY = event.getNativeEvent().getClientY();
          if (isCursorOnTableTop(currentClientY) && (scrollTimer == null || !scrollTimer.isRunning())
              && contentWidget.getVerticalScrollPosition() > contentWidget.getMinimumVerticalScrollPosition()) {
            scrollTimer = new Timer() {
              @Override
              public void run() {
                if (isCursorOnTableTop(currentClientY) 
                    && contentWidget.getVerticalScrollPosition() > contentWidget.getMinimumVerticalScrollPosition()) {
                  contentWidget.setVerticalScrollPosition(contentWidget.getVerticalScrollPosition() - 15);
                  this.schedule(100);
                } else {
                  this.cancel();
                }
              }
            };
            scrollTimer.run();
          } else {
            if (isCursorOnTableBottom(currentClientY) 
                && (scrollTimer == null || !scrollTimer.isRunning())
                && contentWidget.getVerticalScrollPosition() < contentWidget.getMaximumVerticalScrollPosition()) {
              scrollTimer = new Timer() {
                @Override
                public void run() {
                  if (isCursorOnTableBottom(currentClientY)
                      && contentWidget.getVerticalScrollPosition() < contentWidget.getMaximumVerticalScrollPosition()) {
                    contentWidget.setVerticalScrollPosition(contentWidget.getVerticalScrollPosition() + 15);
                    this.schedule(100);
                  } else {
                    this.cancel();
                  }
                }
              };
              scrollTimer.run();
            } else {
              if (!isCursorOnTableTop(currentClientY) && !isCursorOnTableBottom(currentClientY) && scrollTimer != null) {
                scrollTimer.cancel();
                scrollTimer = null;
              }
            }
          }
        }
      }
    }, DragEvent.getType());
    
    this.dragLeaveHandler = addDragLeaveHandler(new DragLeaveHandler() {
      @Override
      public void onDragLeave(DragLeaveEvent event) {
        TableRowElement rowElement = getRowElement(event);
          if (rowElement != null){
          isDraggedBefore=false;
          isDraggedAfter=false;
          isDraggedOver=false; 
            rowElement.getStyle().clearBackgroundImage();
          }
      }
    });
     
    // add drop handler
    this.dropHandler = addDropHandler(new DropHandler() {
        @Override
        public void onDrop(final DropEvent event) {
            // prevent the native text drop
            event.preventDefault();
            // index by default which corresponds an unmatched row
            final int droppedIndexRow = getRowIndexByEvent(event);
        // get the data out of the event
            final int selectedIndex = Integer.decode(event.getData(DND_DATA_PROPERTY));
            if (selectedIndex != -1){
              // замена элементов осуществляем в последнюю очередь
              Scheduler.get().scheduleFinally(new ScheduledCommand() {
                @Override
                public void execute() {
                  TableRowElement rowElement = null;
                  if (droppedIndexRow != -1) {
                    rowElement = getRowElement(droppedIndexRow);
                    rowElement.getStyle().clearBackgroundImage();
                  }
                  if (dndMode == DndMode.INSERT && !(selectedIndex != droppedIndexRow && !isDraggedOver)) return;
                  if (dndMode == DndMode.APPEND && !(selectedIndex != droppedIndexRow && isDraggedOver)) return;
                  if (dndMode == DndMode.BOTH && !(selectedIndex != droppedIndexRow)) return;
                  RowPositionChangeEvent.fire(JepGrid.this, (List<Object>) getSelection(), droppedIndexRow, isDraggedOver,
                        isDraggedBefore, isDraggedAfter);
                  if (droppedIndexRow != -1) rowElement.getStyle().clearBackgroundImage();
                }
              });
            }     
            isDragStarted = false;       
        }
    });
  }
  
  /**
   * Получение значения левого отступа прокручиваемой области данных грида.
   * 
   * @return  целочисленное значение левого отступа
   */
  public final int getScrollLeft() {
    HeaderPanel headerPanel = (HeaderPanel) getWidget();
    ScrollPanel dataPanel = (ScrollPanel) headerPanel.getContentWidget();
    return dataPanel.getHorizontalScrollPosition();
  }

  
  /**
   * Класс для хранения характеристик колонки таблица данных
   */
  class ColumnCharasteristic {
    String fieldName;
    String width;
    int order;
    boolean visible;
    
    public ColumnCharasteristic(String fieldName, String width, int order, boolean visible) {
      this.fieldName = fieldName;
      this.width = width;
      this.order = order;
      this.visible = visible;
    }
  }
}
