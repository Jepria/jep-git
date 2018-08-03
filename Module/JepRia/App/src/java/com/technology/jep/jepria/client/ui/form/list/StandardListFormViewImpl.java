package com.technology.jep.jepria.client.ui.form.list;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HeaderPanel;
import com.technology.jep.jepria.client.widget.list.GridManager;
import com.technology.jep.jepria.client.widget.list.JepColumn;
import com.technology.jep.jepria.client.widget.list.JepGrid;
import com.technology.jep.jepria.client.widget.toolbar.PagingStandardBar;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Стандартная списочная форма.<br/>
 * В отличие от {@link com.technology.jep.jepria.client.ui.form.list.ListFormViewImpl}, при создании представления списка 
 * не требуется ручное добавление графических элементов (Panel, Grid, PagingBar) -- требуется только 
 * определение конфигураций столбцов.
 */
@SuppressWarnings("rawtypes")
public abstract class StandardListFormViewImpl extends ListFormViewImpl<GridManager> {
  
  /**
   * Grid для возможности управления столбцамии и строками
   * (например, раскраска строк или добавление/удаление столбцов).
   */
  protected JepGrid<JepRecord> grid;
  
  @Deprecated
  public StandardListFormViewImpl(String gridStorageId) {
    this(gridStorageId, null);
  }
  
  @SuppressWarnings("unchecked")
  public StandardListFormViewImpl(String gridStorageId, String gridIdAsWebEl) {
    super(new GridManager());
    
    HeaderPanel gridPanel = new HeaderPanel();
    setWidget(gridPanel);
    
    gridPanel.setHeight("100%");
    gridPanel.setWidth("100%");
    
    List<JepColumn> columnConfigurations = getColumnConfigurations();
    if (columnConfigurations == null) { // эта проверка на всякий случай - ничего страшного, если тут будет null
        columnConfigurations = new ArrayList<JepColumn>();
    }
    
    grid = new JepGrid<JepRecord>(gridStorageId, gridIdAsWebEl, columnConfigurations);
    PagingStandardBar pagingBar = new PagingStandardBar(25);
    
    gridPanel.setContentWidget(grid);
    gridPanel.setFooterWidget(pagingBar);

    list.setWidget(grid);
    list.setPagingToolBar(pagingBar);
  }
  
  /**
  * Метод должен быть переопределен в наследниках и возвращать список столбцов.
  */
  protected abstract List<JepColumn> getColumnConfigurations();
}
