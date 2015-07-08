package com.technology.jep.jepria.client.widget.list;

import static com.technology.jep.jepria.shared.load.PagingConfig.DEFAULT_PAGE_SIZE;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.technology.jep.jepria.client.widget.list.header.ResizableHeader;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

@SuppressWarnings({"rawtypes", "unchecked"})
public class JepGrid<T> extends DataGrid<T> {

	private String gridId = null;
	private List<JepColumn> columns;
	private boolean wrapHeaders;
	private boolean isColumnConfigurable;
	private static final String CHARACTERISTIC_SEPARATOR = "=";

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
		this(gridId, columns, wrapHeaders, true, null);
	}
	
	public JepGrid(String gridId, final List<JepColumn> columns, boolean wrapHeaders, boolean isColumnConfigurable) {
		this(gridId, columns, wrapHeaders, isColumnConfigurable, null);
	}

	public JepGrid(String gridId, final List<JepColumn> columns, boolean wrapHeaders, boolean isColumnConfigurable, ProvidesKey<T> keyProvider) {
		this(DEFAULT_PAGE_SIZE, keyProvider);

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
	
	protected JepGrid(int pageSize, ProvidesKey<T> keyProvider) {

		super(pageSize, (DataGridResource) GWT.create(DataGridResource.class), keyProvider);

		setAutoHeaderRefreshDisabled(true);
		setMinimumTableWidth(300, Unit.PX);
		setHeight("100%");
		
		addCellPreviewHandler(new Handler<T>() {
			@Override
			public void onCellPreview(CellPreviewEvent<T> event) {
				if ("mouseover".equals(event.getNativeEvent().getType())) {
					onMouseOver(event);
				}
			}
		});

		final SelectionModel<T> selectionModel = new MultiSelectionModel<T>();
		setSelectionModel(selectionModel);
	}
	
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
	
	@SuppressWarnings("deprecation")
	public void columnCharacteristicsChanged() {
		// сохраняем в Cookie пользовательские настройки ширины столбцов
		Date expires = new Date();
		expires.setYear(expires.getYear() + 1);

		Cookies.setCookie(gridId, getColumnCharacteristicsAsString(), expires);
	}
	
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
		
	public List<JepColumn> getColumns(){
		return this.columns;
	}
	
	public int getIndexColumnById(String id){
		for (JepColumn c : columns){
			if (c.getFieldName().equals(id))
				return columns.indexOf(c);
		}
		return -1;
	}
	
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
		cellElement.setTitle(toolTip);
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
}
