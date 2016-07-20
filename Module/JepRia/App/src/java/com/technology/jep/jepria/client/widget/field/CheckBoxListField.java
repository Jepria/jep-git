package com.technology.jep.jepria.client.widget.field;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_LIST_FIELD_ITEM_CHECKBOX_INFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_OPTION_VALUE_HTML_ATTR;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
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
  
  protected DataGrid<T> table;
  private List<T> data = new ArrayList<T>();
  
  /**
   * Флаг свойства disabled данного поля. Необходимо хранить это значение, поскольку оно используется при каждом рендеринге.
   */
  private boolean disabled = false;
  
  /**
   * Multi Selection Model. 
   */
  private MultiSelectionModel<T> selectionModel = new MultiSelectionModel<T>();
  
  /**
   * An html string representation of a checked input box with a label.
   */
  private static final String CHECKBOX_HTML = 
      "<input type='checkbox' tabindex='-1' {4} " + JEP_OPTION_VALUE_HTML_ATTR + "='{1}' style='float:left;cursor:pointer;' {2} {3}/>" +
      "<label for='{0}' title='{1}' class='item " + MAIN_FONT_STYLE + "'>&nbsp;{1}</label>";
  
  /**
   * Наименование селектора (класса стилей) компонента, в который помещен чекбокс и текстовый лейбл.
   */
  private static final String LIST_FIELD_STYLE = "jepRia-ListField-Input";
  
  /**
   * Наименование селектора (класса стилей) главного компонента.
   */
  private static final String LIST_FIELD_COMMON_STYLE = "jepRia-ListField-Input-common";
  
  /**
   * ID объемлющего Jep-поля как Web-элемента. Переменная нужна как поле класса для использования при реднеринге.
   */
  private final String fieldIdAsWebEl;
  
  @Deprecated
  public CheckBoxListField() {
    this("");
  }
  
  /**
   * Вспомогательный класс для хранения данных, представляемых элементом списка.
   * По сути, пара &lt;Boolean, String&gt; = &lt;отмечен?, подпись&gt;.
   * @author RomanovAS
   */
  private class CheckBoxDataAggregator {
    public final Boolean checked;
    public final String label;
    public CheckBoxDataAggregator(Boolean checked, String label) {
      this.checked = checked;
      this.label = label;
    }
  }
  
  /**
   * Default constructor. Uses default text cell implementation of this class
   */
  public CheckBoxListField(String fieldIdAsWebEl) {
    this.fieldIdAsWebEl = fieldIdAsWebEl;
    
    // Now create a Table which takes an object i.e BaseDataMode
    table = new DataGrid<T>();
    table.addStyleName(LIST_FIELD_COMMON_STYLE);
    
    // FIXME TODO Ниже нужно избежать использования CompositeCell, так как после рефакторинга
    // элемент списка состоит из одного, а не двух элементов и поэтому больше не является композитным.
    // Проблема в том, что Column не создается от HasCell (зато создается от CompositeCell(HasCell)),
    // а использование HasCell нужно для задания в нем FieldUpdater.
    
    List<HasCell<T, ?>> cellComponents = new ArrayList<HasCell<T, ?>>();
  
    cellComponents.add(new HasCell<T, CheckBoxDataAggregator>() {
    
      private Cell<CheckBoxDataAggregator> cell = new JepCheckBoxCell();
        
      public Cell<CheckBoxDataAggregator> getCell() {
        return cell;
      }
      
      public FieldUpdater<T, CheckBoxDataAggregator> getFieldUpdater() {
        return new FieldUpdater<T, CheckBoxDataAggregator>() {
          public void update(int index, T object, CheckBoxDataAggregator newData) {
            selectionModel.setSelected(object, newData.checked);
            ValueChangeEvent.fire(CheckBoxListField.this, object);
          }
        };
      }
      
      public CheckBoxDataAggregator getValue(T object) {
        return new CheckBoxDataAggregator(selectionModel.isSelected(object), object.getName());
      }
    });
    
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
        onSelectAll(event != null && event.getValue() != null && event.getValue());
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
    disabled = !enabled;
    
    // Свойство ниже является маркером того, что поле неактивно, при Selenium-тестировании;
    // функционально оно не является необходимым.
    if (disabled) {
      table.getElement().setAttribute("disabled", "true");
    } else {
      table.getElement().removeAttribute("disabled");
    }
    
    table.redraw();
    
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
  protected void onSelectAll(boolean selectAll) {
    if (selectAll) {
      setSelection(data);
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
   * @param data    options
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
   * @return    selected values
   */
  public List<T> getSelection(){
    return new ArrayList<T>(selectionModel.getSelectedSet());
  }
  
  /**
   * Set selected options in list field
   * 
   * @param selectedOptions  options
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
   * @param handler  specified handler for {@link com.google.gwt.event.logical.shared.ValueChangeEvent}
   */
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler){
    return addHandler(handler, ValueChangeEvent.getType());
  }
  
  /**
   * Change content widget.
   * 
   * @param widget  specified widget
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
   * Данный класс является аналогом gwt-класса {@link com.google.gwt.cell.client.CheckboxCell},
   * с единственной существенной разницей:
   * CheckBoxCell extends AbstractEditableCell<Boolean, Boolean>
   * JepCheckBoxCell extends AbstractEditableCell<CheckBoxDataAggregator, CheckBoxDataAggregator>
   */
  private class JepCheckBoxCell extends AbstractEditableCell<CheckBoxDataAggregator, CheckBoxDataAggregator> {
    
    /**
     * Начало блока кода, аналогичного классу com.google.gwt.cell.client.CheckBoxCell
     */
    public JepCheckBoxCell() {
      super(BrowserEvents.CHANGE, BrowserEvents.KEYDOWN);
    }
    
    // These booleans (false,true) are very important for right behavior of CBCell selection.
    @Override
    public boolean dependsOnSelection() {
      return false;
    }
    @Override
    public boolean handlesSelection() {
      return true;
    }

    @Override
    public boolean isEditing(Context context, Element parent, CheckBoxDataAggregator value) {
      return false;
    }
    
    @Override
    public void onBrowserEvent(Context context, Element parent, CheckBoxDataAggregator value,
        NativeEvent event, ValueUpdater<CheckBoxDataAggregator> valueUpdater) {
      
      String type = event.getType();
      
      boolean enterPressed = BrowserEvents.KEYDOWN.equals(type)
          && event.getKeyCode() == KeyCodes.KEY_ENTER;
      if (BrowserEvents.CHANGE.equals(type) || enterPressed) {
        InputElement input = parent.getFirstChild().cast();
        Boolean isChecked = input.isChecked();

        /*
        * Toggle the value if the enter key was pressed and the cell handles
        * selection or doesn't depend on selection. If the cell depends on
        * selection but doesn't handle selection, then ignore the enter key and
        * let the SelectionEventManager determine which keys will trigger a
        * change.
        */
        if (enterPressed && (handlesSelection() || !dependsOnSelection())) {
          isChecked = !isChecked;
          input.setChecked(isChecked);
        }

        /*
        * Save the new value. However, if the cell depends on the selection, then
        * do not save the value because we can get into an inconsistent state.
        */
        if (value.checked != isChecked && !dependsOnSelection()) {
          setViewData(context.getKey(), new CheckBoxDataAggregator(isChecked, value.label));
        } else {
          clearViewData(context.getKey());
        }

        if (valueUpdater != null) {
          valueUpdater.update(new CheckBoxDataAggregator(isChecked, value.label));
        }
      }
    }
    /**
     * Конец блока кода, аналогичного классу com.google.gwt.cell.client.CheckBoxCell
     */
    
    @Override
    public void render(Context context, CheckBoxDataAggregator value, SafeHtmlBuilder sb) {
      Object key = context.getKey();
      Boolean viewData = getViewData(key) != null ? getViewData(key).checked : null;
      if (viewData != null && viewData.equals(value.checked)) {
        clearViewData(key);
        viewData = null;
      }
      
      final String checkBoxHtmlString;
      final String checkBoxId, idAttrWithVal;
      if (fieldIdAsWebEl == null) {
        checkBoxId = idAttrWithVal = "";
      } else {
        checkBoxId = fieldIdAsWebEl + JEP_LIST_FIELD_ITEM_CHECKBOX_INFIX + value.label;
        idAttrWithVal = "id='" + checkBoxId + "'";
      }
      
      boolean checked = value.checked != null && ((viewData != null) ? viewData : value.checked);
      checkBoxHtmlString = JepClientUtil.substitute(CHECKBOX_HTML,
          checkBoxId,
          value.label,
          checked ? "checked" : "",
          disabled ? "disabled" : "",
          idAttrWithVal);
      
      sb.append(SafeHtmlUtils.fromSafeConstant(checkBoxHtmlString));
    }
  }
  
  /**
   * Установка ID внутренних компонентов CheckBoxListField: table-списка как INPUT, кнопки "выделить все"
   * @param fieldIdAsWebEl ID JepListField'а, который берется за основу ID внутренних компонентов
   */
  public void setCompositeWebIds(String fieldIdAsWebEl) {
    table.getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_FIELD_INPUT_POSTFIX);
    selectAllCheckBox.getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_LIST_FIELD_CHECKALL_POSTFIX);
  }
}
