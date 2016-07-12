package com.technology.jep.jepria.client.widget.list;

import static com.technology.jep.jepria.client.widget.event.JepEventType.CHANGE_SORT_EVENT;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.view.client.SetSelectionModel;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.toolbar.PagingToolBar;
import com.technology.jep.jepria.shared.load.SortConfig;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Класс управления таблицей наследником
 * <code>com.extjs.gxt.ui.client.widget.grid.AbstractCellTable</code>.<br/>
 * <br/>
 * Концепция поддержки обработки событий отражена в описании пакета
 * {@link com.technology.jep.jepria.client.widget}.
 * <dl>
 * <dt>Поддерживаемые типы событий
 * {@link com.technology.jep.jepria.client.widget.event.JepEvent}:</dt>
 * <dd>
 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_SORT_EVENT
 * CHANGE_SORT_EVENT}</dd>
 * </dl>
 */
public class GridManager<W extends AbstractCellTable<JepRecord>, P extends PagingToolBar, S extends SetSelectionModel<JepRecord>> extends
    PagingManager<W, P, S> {

  /**
   * Очистка списка значений в компоненте-списка.
   */
  public void clear() {
    super.clear();
    
    widget.getColumnSortList().clear();
  }

  /**
   * Получение информации о колонках.
   * 
   * @return информация о колонках в виде
   *         {@link com.technology.jep.jepria.client.widget.list.JepColumnConfig};
   */
  public List<JepColumnConfig> getColumnModel() {
    List<JepColumnConfig> columnModel = new ArrayList<JepColumnConfig>();
    for (int i = 0; i < widget.getColumnCount(); i++) {
      Column<JepRecord, ?> column = widget.getColumn(i);
      Header header  = widget.getHeader(i);
      if (header != null) {
        columnModel.add(new JepColumnConfig(header, column));
      }
    } 
    return columnModel;
  }

  /**
   * Добавление слушателя определенного типа событий.<br/>
   * Концепция поддержки обработки событий и пример реализации метода отражен
   * в описании пакета {@link com.technology.jep.jepria.client.widget}.
   * 
   * @param eventType
   *            тип события
   * @param listener
   *            слушатель
   */
  public void addListener(JepEventType eventType, JepListener listener) {
    switch (eventType) {
    case CHANGE_SORT_EVENT:
      addChangeSortListener();
      break;
    }

    super.addListener(eventType, listener);
  }

  /**
   * Добавление sпрослушивателей для реализации прослушивания события
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_SORT_EVENT}
   * .<br/>
   */
  protected void addChangeSortListener() {

    widget.addColumnSortHandler(new ListHandler<JepRecord>(null) {
      @Override
      public void onColumnSort(ColumnSortEvent event) {
        Log.debug("PagingManager.onColumnSort");
        Column<?, ?> column = event.getColumn();
        String sortField = column.getDataStoreName();
        SortConfig.SortDir jepSortDir = SortConfig.SortDir.NONE;
        if (sortField != null) {
          if (event.isSortAscending()) {
            jepSortDir = SortConfig.SortDir.ASC;
          } else {
            jepSortDir = SortConfig.SortDir.DESC;

          }
        }

        notifyListeners(CHANGE_SORT_EVENT, new JepEvent(widget, new SortConfig(sortField, jepSortDir)));
      }
    });
  }

}
