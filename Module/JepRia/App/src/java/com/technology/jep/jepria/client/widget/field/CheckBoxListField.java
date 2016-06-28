package com.technology.jep.jepria.client.widget.field;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;
import static com.technology.jep.jepria.client.AutomationConstant.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.technology.jep.jepria.client.AutomationConstant;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.shared.field.option.JepOption;
 
/**
 * This widget will create a Checkbox List view.
 */
public class CheckBoxListField<T extends JepOption> extends Composite implements HasWidgets, HasValueChangeHandlers<T> {
	
	/**
	 * Флаг "Выбрать все".
	 */
	protected CheckBox selectAllCheckBox;
	
	/**
	 * Панель для размещения списка и флага "Выделить все".
	 */
	protected VerticalPanel widgetPanel;
	
	/**
	 * Наименование класса стилей для флага "Выделить все".
	 */
	private static final String SELECT_ALL_CHECK_BOX_STYLE = "jepRia-ListField-SelectAllCheckBox";
	
	/**
	 * Панель для горизонтального размещения панели с виджетами и индикатора загрузки.
	 */
	private HorizontalPanel panel;
	
	private int fieldId = Random.nextInt();
	protected DataGrid<T> table;
	private HasCell<T, String> textCell;
	private List<T> data = new ArrayList<T>();
	
	/**
	 * Multi Selection Model. 
	 */
	private MultiSelectionModel<T> selectionModel = new MultiSelectionModel<T>();
	
	/**
	 * An html string representation of a checked input box.
	 */
	private static final String CHECKBOX_HTML = "<input type=\"checkbox\" tabindex=\"-1\" id=\"{0}\" optiontext=\"{1}\" style='float:left;cursor:pointer;' {2}/>";
	
	/**
	 * Наименование селектора (класса стилей) компонента, в который помещен чекбокс и текстовый лейбл.
	 */
	private static final String LIST_FIELD_STYLE = "jepRia-ListField-Input";
	
	/**
	 * Наименование селектора (класса стилей) главного компонента.
	 */
	private static final String LIST_FIELD_COMMON_STYLE = "jepRia-ListField-Input-common";
	
	/**
	 * Create a singleton instance of the templates used to render the cell.
	 */
	private DefaultRenderer cellRenderer = GWT.create(DefaultRenderer.class);
	
	/**
	 * Default text layout
	 */
	public interface DefaultRenderer extends SafeHtmlTemplates {
		@Template("<label for='" + DETAIL_FORM_LIST_ITEM_CHECKBOX_INFIX + "{1}' title='{0}' class='item " + MAIN_FONT_STYLE + "'>&nbsp;{0}</label>")
			public SafeHtml render(String pName, String index);
	}
	
	/**
	 * ID объемлющего Jep-поля как Web-элемента.
	 */
	private final String fieldIdAsWebEl;
	
	@Deprecated
	public CheckBoxListField() {
		this("");
	}
	
	/**
	 * Default constructor. Uses default text cell implementation of this class
	 */
	public CheckBoxListField(String fieldIdAsWebEl) {
		this.fieldIdAsWebEl = fieldIdAsWebEl;
		
		textCell = new TextCellImpl();
		
		// Now create a Table which takes an object i.e BaseDataMode
		table = new DataGrid<T>();
		table.addStyleName(LIST_FIELD_COMMON_STYLE);
		table.getElement().setId(fieldIdAsWebEl);
		
		// Create a list of cell. These cells will make up the composite cell
		// Here I am constructing a composite cell with 2 parts that includes a checkbox.
		List<HasCell<T, ?>> cellComponents = new ArrayList<HasCell<T, ?>>();
	
		// 1st part of Composite cell - Show a checkbox and select it "selected property is true
		cellComponents.add(new HasCell<T, Boolean>() {
		
			// These booleans (false,true) are very important for right behavior of CBCell selection.
			private CheckboxCell cell = new CheckBoxCellImpl();
				
				public Cell<Boolean> getCell() {
					return cell;
				}
			
			public FieldUpdater<T, Boolean> getFieldUpdater() {
				return new FieldUpdater<T, Boolean>() {
					public void update(int index, T object, Boolean isCBChecked) {
						selectionModel.setSelected(object, isCBChecked);
						ValueChangeEvent.fire(CheckBoxListField.this, object);
					}
				};
			}
			
			public Boolean getValue(T object) {
				return selectionModel.isSelected(object);
			}
		});
		
		// 2nd part of Composite cell - Show Text for the CB Cell
		cellComponents.add(textCell);
		
		// Create a composite cell and pass the definition of
		// individual cells that the composite cell should render.
		CompositeCell<T> compositeCell = new CompositeCell<T>(cellComponents);
		
		Column<T, T> columnCell = new Column<T, T>(compositeCell) {
			@Override
			public T getValue(T object) {
				return object;
			}
		};
		columnCell.setCellStyleNames(LIST_FIELD_STYLE);
		
		// Here we are adding a Column. This column is rendered using the composite cell
		table.addColumn(columnCell);
		table.setColumnWidth(columnCell, "100%");
		
		table.setSelectionModel(selectionModel);
		
		selectAllCheckBox = new CheckBox(JepTexts.listField_selectAll());
		selectAllCheckBox.addStyleName(SELECT_ALL_CHECK_BOX_STYLE);
		selectAllCheckBox.addStyleName(MAIN_FONT_STYLE);
		
		setSelectAllCheckBoxVisible(false);
		
		selectAllCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				onSelectAll();
			}
		});
		
		addValueChangeHandler(new ValueChangeHandler<T>() {
			@Override
			public void onValueChange(ValueChangeEvent<T> event) {
				selectAllCheckBox.setValue(data.size() == getSelection().size());
			}
		});
		
		widgetPanel = new VerticalPanel();		
		populateWidgetPanel();
		
		panel = new HorizontalPanel();
		panel.add(widgetPanel);
		
		initWidget(panel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(Widget w) {
		panel.add(w);
	}

	/**
	 * Метод не поддерживается.
	 */
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Метод не поддерживается.
	 */
	@Override
	public Iterator<Widget> iterator() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Widget w) {
		return panel.remove(w);
	}

	/**
	 * Управление доступностью компонента.
	 * @param enabled доступность компонента
	 */
	public void setEnabled(boolean enabled) {
		// TODO реализовать блокировку списка
		selectAllCheckBox.setEnabled(enabled);
	}
	
	/**
	 * Установка высоты.<br>
	 * Устанавливает высоту виджета. Если флаг "Выделить все" показан,
	 * то его высота не учитывается.
	 * @param height значение высоты.
	 */
	@Override
	public void setHeight(String height) {
		table.setHeight(height);
	}
	
	/**
	 * Установка видимости флага "Выделить все".<br>
	 * По умолчанию флаг невидим.
	 * @param visible если true, то показать, в противном случае - скрыть
	 */
	public void setSelectAllCheckBoxVisible(boolean visible) {
		selectAllCheckBox.setVisible(visible);
	}

	/**
	 * Установка ширины.<br>
	 * Метод перегружен, т.к. необходимо задать ширину не только списка,
	 * но и флага "Выделить все".
	 * @param width ширина
	 */
	@Override
	public void setWidth(String width) {
		table.setWidth(width);
		selectAllCheckBox.setWidth(width);
	}

	/**
	 * Обработчик события щелчка по флагу "Выделить все".<br>
	 * Если флаг проставлен, выделяются все опции. В противном случае все опции сбрасываются.
	 * После этого вызывается событие 
	 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_SELECTION_EVENT}.
	 */
	protected void onSelectAll() {
		if (selectAllCheckBox.getValue()) {
			selectAll();
		}
		else {
			setSelection(null);
		}

		ValueChangeEvent.fire(CheckBoxListField.this, null);
	}

	/**
	 * Заполнение панели списком и флагом "Выделить все".<br>
	 * При необходимости изменить стандартный порядок следования виджетов,
	 * метод следует переопределить в классе-наследнике.
	 */
	protected void populateWidgetPanel() {
		widgetPanel.add(table);
		widgetPanel.add(selectAllCheckBox);
	}
	
	/**
	 * Set options.
	 * 
	 * @param data		options
	 */
	public void setData(List<T> data) {
		this.data = data;
		refreshData();
	}
	
	public void refreshData(){
		if (data != null){	
			this.table.setRowData(this.data);
		}		
	}
	/**
	 * Get selected values
	 * 
	 * @return		selected values
	 */
	public List<T> getSelection(){
		return new ArrayList<T>(selectionModel.getSelectedSet());
	}
	
	/**
	 * Set selected options in list field
	 * 
	 * @param selectedOptions	options
	 */
	public void setSelection(List<T> selectedOptions){
		for (T option : data){
			selectionModel.setSelected(option, selectedOptions != null && selectedOptions.contains(option));
		}
		selectAllCheckBox.setValue(selectedOptions != null && selectedOptions.size() == data.size());
	}
		
	/**
	 * Add value change handler.
	 * 
	 * @param handler	specified handler for {@link com.google.gwt.event.logical.shared.ValueChangeEvent}
	 */
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler){
		return addHandler(handler, ValueChangeEvent.getType());
	}
	
	/**
	 * Change content widget.
	 * 
	 * @param widget	specified widget
	 */	
	public void replaceWidget(Widget widget){
		table.setVisibleRangeAndClearData(table.getVisibleRange(), true);
		table.setLoadingIndicator(widget);
	}
	
	/**
	 * Проверяет, выбраны ли все опции.
	 * @return true, если выбраны все опции, false в противном случае
	 */
	public boolean isAllSelected() {
		return getSelection().size() == data.size();
	}
	
	/**
	 * Принудительно выбирает все опции.
	 */
	public void selectAll() {
		setSelection(data);
	}

	/**
	 * This is the default TextCell implementation. Will be used if the user don't plan to customize the text cell
	 */
	class TextCellImpl implements HasCell<T, String> {
		
		private int inc = 0;
		private SafeHtmlRenderer<String> renderer = new AbstractSafeHtmlRenderer<String>(){
			@Override
			public SafeHtml render(String object) {
				return (object == null) ? SafeHtmlUtils.EMPTY_SAFE_HTML : SafeHtmlUtils.fromTrustedString(object);
			}
			
		};
		
		TextCell txtCell = new TextCell(renderer) {
			@Override
			public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
				if (value == null) return;
				SafeHtml rendered = cellRenderer.render(value.asString(), fieldId + "_???_" + inc++);
				sb.append(rendered);
			}
		};
		
		public Cell<String> getCell() {
			return txtCell;
		}
		
		public FieldUpdater<T, String> getFieldUpdater() {
			return null;
		}
		
		@Override
		public String getValue(T object) {
			return object.getName();
		}
	}
	
	/**
	 * Implementation of Checkbox Cell.
	 */
	class CheckBoxCellImpl extends CheckboxCell {
		
		public CheckBoxCellImpl(){
			super(false, true);
		}
		
		@Override
		public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
			// Get the view data.
			Object key = context.getKey();
			Boolean viewData = getViewData(key);
			if (viewData != null && viewData.equals(value)) {
				clearViewData(key);
				viewData = null;
			}
			
			String checkBoxString;
			if (value != null && ((viewData != null) ? viewData : value)) {
				checkBoxString = JepClientUtil.substitute(CHECKBOX_HTML, fieldIdAsWebEl + DETAIL_FORM_LIST_ITEM_CHECKBOX_INFIX + ((JepOption)key).getName(), ((JepOption)key).getName(), "checked");
			} else {
				checkBoxString = JepClientUtil.substitute(CHECKBOX_HTML, fieldIdAsWebEl + DETAIL_FORM_LIST_ITEM_CHECKBOX_INFIX + ((JepOption)key).getName(), ((JepOption)key).getName(), "");
			}
			sb.append(SafeHtmlUtils.fromSafeConstant(checkBoxString));
		}
	}
	
	/**
	 * Установка ID внутренних компонентов CheckBoxListField: table-списка как INPUT
	 * @param fieldIdAsWebEl ID JepListField'а, который берется за основу ID внутренних компонентов
	 */
	public void setInnerIds(String fieldIdAsWebEl) {
		table.getElement().setId(fieldIdAsWebEl + AutomationConstant.FIELD_INPUT_POSTFIX);
	}
}
