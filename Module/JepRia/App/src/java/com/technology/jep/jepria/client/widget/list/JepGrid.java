package com.technology.jep.jepria.client.widget.list;

import static com.technology.jep.jepria.client.JepRiaClientConstant.DND_DATA_PROPERTY;
import static com.technology.jep.jepria.shared.load.PagingConfig.DEFAULT_PAGE_SIZE;

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
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.container.ElementSimplePanel;
import com.technology.jep.jepria.client.widget.list.event.RowOrderChangeEvent;
import com.technology.jep.jepria.client.widget.list.header.ResizableHeader;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

@SuppressWarnings({"rawtypes", "unchecked"})
public class JepGrid<T> extends DataGrid<T> {
	
	private ScrollPanel contentWidget;
	private String gridId = null;
	private List<JepColumn> columns;
	private boolean wrapHeaders;
	private boolean isColumnConfigurable;
	
	private static final String CHARACTERISTIC_SEPARATOR = "=";
	private static final String LINEAR_GRADIENT_FROM_TOP_TO_BOTTOM = "linear-gradient(#D0DEF0, #FFFFFF)";
	private static final String LINEAR_GRADIENT_FROM_BOTTOM_TO_TOP = "linear-gradient(#FFFFFF, #D0DEF0)";
	
	protected HandlerRegistration dragStartHandler;
	protected HandlerRegistration dragOverHandler;
	protected HandlerRegistration dragLeaveHandler;
	protected HandlerRegistration dropHandler;
	
	public interface MyStyle extends DataGrid.Style {
	}

	public interface DataGridResource extends DataGrid.Resources {
		@Source({ DataGrid.Style.DEFAULT_CSS, "DataGridOverride.css" })
		MyStyle dataGridStyle();
	}

	public JepGrid(String gridId, List<JepColumn> columns) {
		this(gridId, columns, false);
	}

	public JepGrid(String gridId, final List<JepColumn> columns, boolean wrapHeaders) {
		this(gridId, columns, wrapHeaders, true);
	}
	
	public JepGrid(String gridId, final List<JepColumn> columns, boolean wrapHeaders, boolean isColumnConfigurable) {
		this(gridId, columns, wrapHeaders, isColumnConfigurable, null);
	}
	
	public JepGrid(String gridId, final List<JepColumn> columns, boolean wrapHeaders, boolean isColumnConfigurable, boolean dndEnabled) {
		this(gridId, columns, wrapHeaders, isColumnConfigurable, dndEnabled, null);
	}

	public JepGrid(String gridId, final List<JepColumn> columns, boolean wrapHeaders, boolean isColumnConfigurable, ProvidesKey<T> keyProvider) {
		this(gridId, columns, wrapHeaders, isColumnConfigurable, false, keyProvider);
	}
	
	public JepGrid(String gridId, final List<JepColumn> columns, boolean wrapHeaders, boolean isColumnConfigurable, boolean dndEnabled, ProvidesKey<T> keyProvider) {
		this(DEFAULT_PAGE_SIZE, dndEnabled, keyProvider);

		this.gridId = gridId;
		this.getElement().setId(gridId);
		
		this.columns = columns;
		this.wrapHeaders = wrapHeaders;
		
		this.isColumnConfigurable = isColumnConfigurable;
		
		final Map<String, ColumnCharasteristic> customColumnCharacteristics = parseColumnCharacteristics(Cookies.getCookie(gridId));
		
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
	}

	/**
	 * Добавление колонки с заголовком
	 * 
	 * @param col			колонка
	 * @param toggle		признак переключения
	 */
	public void addColumnWithHeader(JepColumn col, boolean toggle) {
		Header<String> header = new ResizableHeader<T>(col.getHeaderText(), this, col, isColumnConfigurable);
		
		if (wrapHeaders)
			header.setHeaderStyleNames(JepColumn.NORMAL_WRAP_STYLE);
		
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
	 * Переключение видимости колонки (добавление/удаление)
	 * 
	 * @param currentColumn			переключаемая колонка
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
	
	protected JepGrid(int pageSize, boolean dndEnabled, ProvidesKey<T> keyProvider) {
		super(pageSize, (DataGridResource) GWT.create(DataGridResource.class), keyProvider);
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
		
		// добавим системных слушателей DragAndDrop события
		if (dndEnabled){
			bindDragAndDropListeners();
		}
		
		final SelectionModel<T> selectionModel = new MultiSelectionModel<T>();
		setSelectionModel(selectionModel);
	}
	
	/**
	 * Возврат характеристик колонки в виде строки
	 * 
	 * @return			строковое представление характеристик колонок
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

		Cookies.setCookie(gridId, getColumnCharacteristicsAsString(), expires);
	}
	
	/**
	 * Метод извлечения данных о гриде из строки
	 * 
	 * @param cookieString			строка, хранящая информация по гриду
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
	 * Получение списка колонок грида
	 * 
	 * @return	список колонок
	 */
	public List<JepColumn> getColumns(){
		return this.columns;
	}
	
	/**
	 * Получение точного индекса колонки по ее идентификатору
	 * 
	 * @param id			идентификатор колонки
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
	 * @param column		искомая колонка грида
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
	 * @param id		идентификатор искомой колонки
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
	
	/**
	 * Add a handler to handle {@link com.technology.jep.jepria.client.widget.list.event.RowOrderChangeEvent}s.
	 * 
	 * @param handler the {@link com.technology.jep.jepria.client.widget.list.event.RowOrderChangeEvent.Handler} to add
	 * @return a {@link com.google.gwt.event.shared.HandlerRegistration} to remove the handler
	 */
	public HandlerRegistration addRowOrderChangerHandler(RowOrderChangeEvent.Handler handler) {
		return addHandler(handler, RowOrderChangeEvent.getType());
	}

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
	
	public TableRowElement getRowElement(DomEvent<?> event){
		int rowIndex = getRowIndexByEvent(event);
		return rowIndex == -1 ? null : getRowElement(rowIndex);
	}
	
	public HandlerRegistration addDragStartHandler(DragStartHandler handler){
		return contentWidget.addDomHandler(handler, DragStartEvent.getType());
	}
	
	public HandlerRegistration addDragOverHandler(DragOverHandler handler){
		return contentWidget.addDomHandler(handler, DragOverEvent.getType());
	}
	
	public HandlerRegistration addDragLeaveHandler(DragLeaveHandler handler){
		return contentWidget.addDomHandler(handler, DragLeaveEvent.getType());
	}
	
	public HandlerRegistration addDropHandler(DropHandler handler){
		return contentWidget.addDomHandler(handler, DropEvent.getType());
	}
	
	public ScrollPanel getContentWidget(){
		return contentWidget;
	}
	
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
				// optional: show copy of the image
				dataTransfer.setDragImage(getRowElement(event), 10, 10);
			}
		});
		
		this.dragOverHandler = addDragOverHandler(new DragOverHandler() {
		    @Override
		    public void onDragOver(DragOverEvent event) {
		    	TableRowElement rowElement = getRowElement(event);
		    	if (rowElement != null){
		    		rowElement.getStyle().setBackgroundImage(isCursorAboveElementCenter(rowElement, event.getNativeEvent()) ? LINEAR_GRADIENT_FROM_TOP_TO_BOTTOM : LINEAR_GRADIENT_FROM_BOTTOM_TO_TOP);
		    		// строка должна оказаться в области видимости пользователя
		    		contentWidget.ensureVisible(new ElementSimplePanel(rowElement));
		    	}
		    }
		});
		
		this.dragLeaveHandler = addDragLeaveHandler(new DragLeaveHandler() {
			@Override
			public void onDragLeave(DragLeaveEvent event) {
				TableRowElement rowElement = getRowElement(event);
		    	if (rowElement != null){
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
		        if (selectedIndex != -1 && droppedIndexRow != -1){
		        	// замена элементов осуществляем в последнюю очередь
		        	Scheduler.get().scheduleFinally(new ScheduledCommand() {
						@Override
						public void execute() {
							TableRowElement rowElement = getRowElement(droppedIndexRow);
							RowOrderChangeEvent.fire(JepGrid.this, selectedIndex, droppedIndexRow, LINEAR_GRADIENT_FROM_TOP_TO_BOTTOM.equals(rowElement.getStyle().getBackgroundImage()));
							rowElement.getStyle().clearBackgroundColor();
						}
					});
		        }		        
		    }
		});
	}
	
	boolean isDNDEnabled(){
		return this.dragStartHandler != null;
	}
	
	private boolean isCursorAboveElementCenter(Element rowElement, NativeEvent event){
		// Calculate top position for the popup
        int top = rowElement.getAbsoluteTop(), height = rowElement.getOffsetHeight(), 
        	clientY = event.getClientY();
        return clientY - top < height / 2;	
	}
	
	/**
	 * {@inheritDoc}
	 * Особенности:<br/>
	 * После переинициализации новых данных, каждая строка грида становится доступной для перетаскивания.
	 */
	@Override
	protected void replaceAllChildren(List<T> values, SafeHtml html) {
		super.replaceAllChildren(values, html);
		if (isDNDEnabled()) {
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
		if (isDNDEnabled()) {
			getRowElement(start).setDraggable(Element.DRAGGABLE_TRUE);
		}
	}
}
