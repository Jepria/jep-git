package com.technology.jep.jepria.client.widget.list;

import static com.technology.jep.jepria.client.widget.event.JepEventType.PAGING_GOTO_EVENT;
import static com.technology.jep.jepria.client.widget.event.JepEventType.PAGING_REFRESH_EVENT;
import static com.technology.jep.jepria.client.widget.event.JepEventType.PAGING_SIZE_EVENT;
import static com.technology.jep.jepria.client.widget.event.JepEventType.ROW_CLICK_EVENT;
import static com.technology.jep.jepria.client.widget.event.JepEventType.ROW_DOUBLE_CLICK_EVENT;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SetSelectionModel;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.toolbar.PagingToolBar;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.load.PagingResult;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Класс управления списком наследником
 * <code>com.extjs.gxt.ui.client.widget.grid.AbstractHasData</code>.<br/>
 * <br/>
 * Концепция поддержки обработки событий отражена в описании пакета
 * {@link com.technology.jep.jepria.client.widget}.
 * <dl>
 * <dt>Поддерживаемые типы событий
 * {@link com.technology.jep.jepria.client.widget.event.JepEvent}:</dt>
 * <dd>
 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#ROW_CLICK_EVENT
 * ROW_CLICK_EVENT}</dd>
 * <dd>
 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#ROW_DOUBLE_CLICK_EVENT
 * ROW_DOUBLE_CLICK_EVENT}</dd>
 * <dd>
 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#PAGING_REFRESH_EVENT
 * PAGING_REFRESH_EVENT}</dd>
 * <dd>
 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#PAGING_SIZE_EVENT
 * PAGING_SIZE_EVENT}</dd>
 * <dd>
 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#PAGING_GOTO_EVENT
 * PAGING_GOTO_EVENT}</dd>
 * </dl>
 */
public class PagingManager<W extends AbstractHasData<JepRecord>, P extends PagingToolBar, S extends SetSelectionModel<JepRecord>> extends
		ListManager<W, P, S> {

	/**
	 * Локальная ссылка на хранилище данных в виджете
	 */
	protected ListDataProvider<JepRecord> dataProvider;

	public PagingManager() {
		dataProvider = new ListDataProvider<JepRecord>();
	}

	/**
	 * Установка компонента-списка, действиями с которым, управляет класс.
	 * 
	 * @param widget
	 *            компонент-список, действиями с которым, управляет класс
	 */
	public void setWidget(W widget) {
		super.setWidget(widget);

		dataProvider.addDataDisplay(widget);
	}

	/**
	 * Установка списка значений в компонент-списка.
	 * 
	 * @param list
	 *            список значений
	 */
	public void set(List<JepRecord> list) {
		dataProvider.setList(list);
		if (getSelectionModel() != null)
			getSelectionModel().clear();

		int size = list.size();

		// Установим значения по умолчанию.
		pagingToolBar.setPageSize(getWidget().getPageSize());
		pagingToolBar.setTotalLength(size);
		pagingToolBar.setActivePage(1);
		pagingToolBar.adjust();

	}

	/**
	 * Установка объекта-результата поиска в компонент-списка.
	 * 
	 * @param pagingResult
	 *            объект-результат поиска
	 */
	public void set(PagingResult<JepRecord> pagingResult) {
		List<JepRecord> data = pagingResult.getData();

		this.set(data);

		Integer pageSize = pagingResult.getPageSize();
		Integer size = pagingResult.getSize();
		Integer activePage = pagingResult.getActivePage();

		// Установим значения по умолчанию, в случае, если параметры листания не
		// определены.
		// Кроме, того значения по умолчанию необходимы, чтобы избежать
		// NullPointerException, возникающий в случае попытки перевести пустой
		// объект
		// Integer в простой тип int.
		if (pageSize != null) {
			getWidget().setPageSize(pageSize);
		}
		pagingToolBar.setPageSize(getWidget().getPageSize());
		pagingToolBar.setTotalLength(size != null ? size : data.size());
		pagingToolBar.setActivePage(activePage != null ? activePage : 1);
		pagingToolBar.adjust();
	}

	/**
	 * Очистка списка значений в компоненте-списка.
	 */
	public void clear() {
		dataProvider.getList().clear();
		if (getSelectionModel() != null)
			getSelectionModel().clear();
	}

	/**
	 * Получение выделенных записей списка
	 * 
	 * @return модель выделения в списке
	 */
	public S getSelectionModel() {
		return (S) widget.getSelectionModel();
	}

	/**
	 * Добавление записи в список
	 * 
	 * @param record
	 *            запись
	 */
	public void add(JepRecord record) {
		dataProvider.getList().add(record);
		
		updatePagingToolBar();
	}

	/**
	 * Добавление записей в список
	 * 
	 * @param list
	 *            список записей
	 */
	public void add(List<JepRecord> list) {
		dataProvider.getList().addAll(list);
		
		updatePagingToolBar();
	}

	/**
	 * Удаление записи из списка
	 * 
	 * @param index
	 *            номер записи
	 */
	public void remove(int index) {
		dataProvider.getList().remove(index);
		
		updatePagingToolBar();
	}

	/**
	 * Удаление записи из списка
	 * 
	 * @param record
	 *            запись
	 */
	public void remove(JepRecord record) {
		dataProvider.getList().remove(record);
		
		updatePagingToolBar();
	}

	/**
	 * Изменение записи в списке
	 * 
	 * @param record
	 *            запись (экземпляр, предварительно полученный с помощью get(int
	 *            index))
	 */
	public void update(JepRecord record) {
		int i = dataProvider.getList().indexOf(record);
		dataProvider.getList().set(i, record);
	}

	/**
	 * Изменение записи в списке (по порядковому номеру)
	 * 
	 * @param index
	 *            номер записи
	 * @param record
	 *            запись
	 */
	public void update(int index, JepRecord record) {
		JepRecord oldRecord = dataProvider.getList().get(index);
		oldRecord.setProperties(record.getProperties());
		dataProvider.getList().set(index, oldRecord);
	}

	/**
	 * Получение записи из списка (по порядковому номеру)
	 * 
	 * @param index
	 *            номер записи
	 * 
	 * @return запись
	 */
	public JepRecord get(int index) {
		return dataProvider.getList().get(index);
	}

	/**
	 * Получение размера страницы набора данных.
	 * 
	 * @return размер страницы набора данных
	 */
	public int getPageSize() {
		return pagingToolBar != null ? pagingToolBar.getPageSize() : 0;
	}

	private void updatePagingToolBar() {
		if (pagingToolBar != null) {
			pagingToolBar.setTotalLength(size());
			pagingToolBar.adjust();
		}
	}
	
	/**
	 * Получение количества записей
	 * 
	 * @return количество записей
	 */
	public int size() {
		return dataProvider.getList().size();
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
		case ROW_CLICK_EVENT:
			addRowClickListner();
			break;
		case ROW_DOUBLE_CLICK_EVENT:
			addRowDoubleClickListener();
			break;
		case PAGING_REFRESH_EVENT:
			addPagingRefreshListner();
			break;
		case PAGING_SIZE_EVENT:
			addPagingSizeListner();
			break;
		case PAGING_GOTO_EVENT:
			addPagingGotoListener();
			break;
		}

		super.addListener(eventType, listener);
	}

	/**
	 * Добавление прослушивателей для реализации прослушивания события
	 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#ROW_CLICK_EVENT}
	 * .<br/>
	 */
	protected void addRowClickListner() {
		widget.addCellPreviewHandler(new CellPreviewEvent.Handler<JepRecord>() {
			@Override
			public void onCellPreview(final CellPreviewEvent<JepRecord> event) {
				//Log.debug("PagingManager.onCellPreview." + event.getNativeEvent().getType());
				if (BrowserEvents.CLICK.equalsIgnoreCase(event.getNativeEvent().getType())) {
					Log.debug("PagingManager.onCellPreview.CLICK");
					notifyListeners(ROW_CLICK_EVENT, new JepEvent(widget, event.getValue()));
				}
			}
		});
	}

	/**
	 * Добавление прослушивателей для реализации прослушивания события
	 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#ROW_DOUBLE_CLICK_EVENT}
	 * .<br/>
	 */
	protected void addRowDoubleClickListener() {
		widget.addDomHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(final DoubleClickEvent event) {
				Log.debug("PagingManager.(domHandler)." + event.getRelativeElement());
				//notifyListeners(ROW_DOUBLE_CLICK_EVENT, new JepEvent(widget, null));
			}
		}, DoubleClickEvent.getType());
		
		
		widget.addCellPreviewHandler(new CellPreviewEvent.Handler<JepRecord>() {
			@Override
			public void onCellPreview(final CellPreviewEvent<JepRecord> event) {
				if (BrowserEvents.DBLCLICK.equalsIgnoreCase(event.getNativeEvent().getType())) {
					Log.debug("PagingManager.onCellPreview.DBLCLICK");
					notifyListeners(ROW_DOUBLE_CLICK_EVENT, new JepEvent(widget, event.getValue()));
				}
			}
		});

	}

	/**
	 * Добавление прослушивателей для реализации прослушивания события
	 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#PAGING_REFRESH_EVENT}
	 * .
	 */
	protected void addPagingRefreshListner() {
		pagingToolBar.addListener(PAGING_REFRESH_EVENT, new JepListener() {
			public void handleEvent(JepEvent event) {
				PagingToolBar pagingToolBar = (PagingToolBar) event.getSource();
				Integer pageSize = pagingToolBar.getPageSize();

				notifyListeners(PAGING_REFRESH_EVENT, new JepEvent(event.getSource(), new PagingConfig(pageSize, 1)));
			}
		});
	}

	/**
	 * Добавление прослушивателей для реализации прослушивания события
	 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#PAGING_SIZE_EVENT}
	 * .
	 */
	protected void addPagingSizeListner() {
		pagingToolBar.addListener(PAGING_SIZE_EVENT, new JepListener() {
			public void handleEvent(JepEvent event) {
				PagingToolBar pagingToolBar = (PagingToolBar) event.getSource();
				Integer pageSize = (Integer) event.getParameter();

				notifyListeners(PAGING_SIZE_EVENT, new JepEvent(event.getSource(), new PagingConfig(pageSize, 1)));
			}
		});
	}

	/**
	 * Добавление прослушивателей для реализации прослушивания события
	 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#PAGING_GOTO_EVENT}
	 * .
	 */
	protected void addPagingGotoListener() {
		pagingToolBar.addListener(PAGING_GOTO_EVENT, new JepListener() {
			public void handleEvent(JepEvent event) {
				PagingToolBar pagingToolBar = (PagingToolBar) event.getSource();
				Integer pageSize = pagingToolBar.getPageSize();
				Integer activePage = (Integer) event.getParameter();

				notifyListeners(PAGING_GOTO_EVENT, new JepEvent(event.getSource(), new PagingConfig(pageSize, activePage)));
			}
		});
	}

}
