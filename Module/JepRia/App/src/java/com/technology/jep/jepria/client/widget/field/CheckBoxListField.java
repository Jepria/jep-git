package com.technology.jep.jepria.client.widget.field;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.shared.field.option.JepOption;

public class CheckBoxListField<T extends JepOption> extends Composite implements HasSelectionChangedHandlers {

  /**
   * Наименование селектора (класса стилей) главного компонента.
   */
  private static final String LIST_FIELD_COMMON_STYLE = "jepRia-ListField-Input-common";
  
  /**
   * Наименование селектора (класса стилей) ячеек таблицы.
   */
  private static final String LIST_FIELD_STYLE = "jepRia-ListField-Input";
  
  /**
   * Наименование класса стилей для флага "Выделить все".
   */
  private static final String SELECT_ALL_CHECK_BOX_STYLE = "jepRia-ListField-SelectAllCheckBox";
  
  /**
   * Шаблон для содержимого ячейки с чекбоксом.
   */
  private static final String CHECKBOX_CELL_HTML_PATTERN = "<input type=\"checkbox\" tabindex=\"-1\" {0} id='{1}'/>";
  
  /**
   * Шаблон для содержимого ячейки с меткой.
   */
  private static final String LABEL_CELL_HTML_PATTERN = 
      "<label for='{0}' style='user-select: none; -moz-user-select: none; -ms-user-select: none' "
      + "unselectable='on' class='item " + MAIN_FONT_STYLE + "'>&nbsp;{1}</label>";
  
  /**
   * Таблица с чекбоксами и подписями.
   */
  private AbstractCellTable<T> table = new DataGrid<>();
  
  /**
   * Ячейка для чекбоксов. Особенность: указывается id для того, чтобы связать с label.
   */
  private Cell<Boolean> checkBoxCell = new CheckboxCell(true, false){
    @Override
    public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
      // Get the view data.
      Object key = context.getKey();
      Boolean viewData = getViewData(key);
      if (viewData != null && viewData.equals(value)) {
        clearViewData(key);
        viewData = null;
      }
      
      String checkBoxId = getCheckBoxId(key.toString());

      if (value != null && ((viewData != null) ? viewData : value)) {
        sb.append(() -> JepClientUtil.substitute(CHECKBOX_CELL_HTML_PATTERN, "checked", checkBoxId));
      } else {
        sb.append(() -> JepClientUtil.substitute(CHECKBOX_CELL_HTML_PATTERN, "", checkBoxId));
      }
    }
  };
  
  /**
   * Колонка с чекбоксами.
   */
  private Column<T, Boolean> checkBoxColumn = new Column<T, Boolean>(checkBoxCell){
    @Override
    public Boolean getValue(T object) {
      return selectionModel.isSelected(object);
    }};

  /**
   * Ячейка для меток (label) чекбоксов. Особенность: указывается id чекбокса, что необходимо
   * для обработки клика.
   * Shift-click и Ctrl-click по элементу <label> принудительно игнорируются в Firefox, поэтому
   * в этом браузере работать не будет, см. https://bugzilla.mozilla.org/show_bug.cgi?id=559506
   */
  private Cell<String> labelCell = new TextCell(){
    @Override
    public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
      if (value != null) {
        sb.append(() -> JepClientUtil.substitute(LABEL_CELL_HTML_PATTERN, getCheckBoxId(context.getKey().toString()), value.asString()));
      }
    }
  };

  /**
   * Получение id чекбокса.
   * @param key ключ
   * @return id
   */
  private String getCheckBoxId(String key) {
    return fieldIdAsWebEl + JepRiaAutomationConstant.JEP_LIST_FIELD_ITEM_CHECKBOX_INFIX + key.replaceAll("\\s+","_");
  }
  
  /**
   * Колонка для меток.
   */
  private Column<T, String> labelColumn = new Column<T, String>(labelCell){
    @Override
    public String getValue(T object) {
      return object.getName();
    }};
  
  /**
   * Флаг "Выбрать все".
   */
  private CheckBox selectAllCheckBox;
  
  /**
   * Панель для размещения виджетов.
   */
  private VerticalPanel widgetPanel = new VerticalPanel();
  
  /**
   * Модель выделения. 
   */
  private MultiSelectionModel<T> selectionModel = new MultiSelectionModel<T>();
  
  /**
   * Флаг свойства disabled данного поля. Необходимо хранить это значение, поскольку оно используется при каждом рендеринге.
   */
  private boolean disabled = false;

  /**
   * Id поля.
   */
  private String fieldIdAsWebEl;
  
  /**
   * Список хранимых опций.
   */
  private List<T> options = new ArrayList<T>();
  
  /**
   * Флаг, определяющий, нужно ли предотвращать вызов обработчика события {@link SelectionChangeEvent}.
   */
  private boolean preventSelectionChange = false;
  
  public CheckBoxListField() {
    checkBoxColumn.setCellStyleNames(LIST_FIELD_STYLE);
    labelColumn.setCellStyleNames(LIST_FIELD_STYLE);
    
    table.addStyleName(LIST_FIELD_COMMON_STYLE);
    table.setSelectionModel(selectionModel, DefaultSelectionEventManager.createCheckboxManager());
    table.addColumn(checkBoxColumn);
    table.addColumn(labelColumn);
    table.setWidth("100" + Unit.PCT);
    // Необходимо принудительно задать ширину колонки с чекбоксами (любую фиксированную).
    table.setColumnWidth(checkBoxColumn, "20px");
    
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
    
    selectionModel.addSelectionChangeHandler(event -> {
      selectAllCheckBox.setValue(options.size() == getSelection().size());
    });
    
    widgetPanel.add(table);
    widgetPanel.add(selectAllCheckBox);
    initWidget(widgetPanel);
  }

  /**
   * Обрабатывает клик по флагу "Выделить все"
   * @param selectAll true, если флаг был установлен; false, если снят
   */
  private void onSelectAll(boolean selectAll) {
    if (selectAll) {
      setSelection(options, true);
    }
    else {
      setSelection(null, true);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerRegistration addSelectionChangeHandler(Handler handler) {
    return selectionModel.addSelectionChangeHandler(event -> {
      if (preventSelectionChange) {
        preventSelectionChange = false;
      } else {
        handler.onSelectionChange(event);
      }
    });
  }

  /**
   * Получение списка выделенных элементов. Создаётся копия списка, поэтому дальнейшие манипуляции
   * с результатом должны быть безопасными.
   * @return список выделенных элементов
   */
  public List<T> getSelection() {
    return new ArrayList<T>(selectionModel.getSelectedSet());
  }

  /**
   * Принудительное обновление данных в таблице.
   */
  public void refreshData() {
    if (options != null) {
      table.setRowData(this.options);
    }
  }

  /**
   * Установка id элементов, из которых состоит виджет.
   * @param fieldIdAsWebEl id поля
   */
  public void setCompositeWebIds(String fieldIdAsWebEl) {
    this.fieldIdAsWebEl = fieldIdAsWebEl;
    
    table.getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_FIELD_INPUT_POSTFIX);
    selectAllCheckBox.getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_LIST_FIELD_CHECKALL_POSTFIX);
  }
  
  /**
   * Управление доступностью компонента.
   * @param enabled true - доступен, false - нет
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
   * Установка выделенных опций.
   * @param selectedOptions список выделенных опций
   * @param fireEvent true - вызвать событие {@link SelectionChangeEvent}, false - нет
   */
  public void setSelection(List<T> selectedOptions, boolean fireEvent) {
    if (!fireEvent) {
      preventSelectionChange = true;
    }
    for (T option : options){
      selectionModel.setSelected(option, selectedOptions != null && selectedOptions.contains(option));
    }
    selectAllCheckBox.setValue(selectedOptions != null && selectedOptions.size() == options.size());
  }

  /**
   * Установка списка доступных опций.
   * @param options список опций
   */
  public void setOptions(List<T> options) {
    this.options = new ArrayList<>(options);
    refreshData();
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
  
}
