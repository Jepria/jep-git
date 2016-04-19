package com.technology.jep.jepria.client.widget.toolbar;

import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.widget.event.JepEventType.PAGING_GOTO_EVENT;
import static com.technology.jep.jepria.client.widget.event.JepEventType.PAGING_REFRESH_EVENT;
import static com.technology.jep.jepria.client.widget.event.JepEventType.PAGING_SIZE_EVENT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.widget.button.JepButton;
import com.technology.jep.jepria.client.widget.button.Separator;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.event.JepObservable;
import com.technology.jep.jepria.client.widget.event.JepObservableImpl;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Обычная инструментальная панель управления листанием набора данных.<br/>
 * <br/>
 * Концепция поддержки обработки событий отражена в описании пакета
 * {@link com.technology.jep.jepria.client.widget}.
 * <dl>
 * <dt>Поддерживаемые типы событий
 * {@link com.technology.jep.jepria.client.widget.event.JepEvent}:</dt>
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
public class PagingStandardBar extends SimplePanel implements PagingToolBar {

	
	String DEFAULT_HEIGHT = "22px";
	
	private static final String PAGING_BAR_STYLE = "jepRia-PagingBar";
	private static final String PAGING_BAR_NUMBER_STYLE = "jepRia-PagingBar-pageNumber";

	protected int activePage = -1, pages;
	protected Label pageNumberPrefix, pageNumberPostfix, displayText, pageSizePrefix, pageSizePostfix;
	protected Button first, prev, next, last, refresh;
	protected TextBox pageNumberText, pageSizeText;

	protected boolean savedEnableState = true;
	protected boolean showToolTips = true;
	protected int start, pageSize, totalLength;

	/**
	 * Хэш-таблица, хранящая соответствие между виджетами и их идентификаторами.
	 */
	private Map<String, Widget> items = new HashMap<String, Widget>();

	/**
	 * Объект для работы со слушателями событий панели.
	 */
	protected JepObservable observable;

	/**
	 * Горизонтальная панель, на которой размещаются кнопки.
	 */
	private HorizontalPanel panel;
	private HorizontalPanel buttonsPanel;
	private HorizontalPanel pageSizePanel;

	/**
	 * Создает инструментальную панель управления листанием набора данных.
	 * 
	 * @param pgSize максимальное количество записей на странице
	 */
	public PagingStandardBar(int pgSize) {
		panel = new HorizontalPanel();
		buttonsPanel = new HorizontalPanel();
		pageSizePanel = new HorizontalPanel();

		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		buttonsPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageSizePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		setStyleName(PAGING_BAR_STYLE);
		addItems();
		setWidget(panel);
		setHeight(DEFAULT_HEIGHT);

		observable = new JepObservableImpl();

		// Установим исходные значения.
		activePage = 1;
		pages = 1;
		pageSize = pgSize;
	}

	/**
	 * Заполнение тулбара стандартными элементами.
	 */
	private void addItems() {
		first = makeButton("first_button_id", JepImages.first(), JepTexts.button_beginning_alt(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				first();
			}
		}, false);

		prev = makeButton("prev_button_id", JepImages.prev(), JepTexts.button_previous_alt(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				previous();
			}
		}, false);

		pageNumberPrefix = new Label(JepTexts.toolbar_paging_pageNumberPrefix());
		pageNumberPostfix = new Label();
		pageNumberText = new TextBox();
		pageNumberText.addStyleName(MAIN_FONT_STYLE);
		pageNumberText.addStyleName(PAGING_BAR_NUMBER_STYLE);
		pageNumberText.setMaxLength(4);
		pageNumberText.setPixelSize(30, 14);
		pageNumberText.setEnabled(false);
		pageNumberText.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					onPageChange();
				}
			}
		});

		next = makeButton("next_button_id", JepImages.next(), JepTexts.button_next_alt(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				next();
			}
		}, false);

		last = makeButton("last_button_id", JepImages.last(), JepTexts.button_ending_alt(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				last();
			}
		}, false);

		refresh = makeButton("refresh_button_id", JepImages.refresh(), JepTexts.button_refresh_alt(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				refresh();
			}
		}, false);

		
		buttonsPanel.add(first);
		buttonsPanel.add(prev);
		buttonsPanel.add(new Separator());
		buttonsPanel.add(pageNumberPrefix);
		buttonsPanel.add(pageNumberText);
		buttonsPanel.add(pageNumberPostfix);
		buttonsPanel.add(new Separator());
		buttonsPanel.add(next);
		buttonsPanel.add(last);
		buttonsPanel.add(new Separator());
		buttonsPanel.add(refresh);
		
		displayText = new Label();
		displayText.setWordWrap(false);
		
		pageSizePrefix = new Label(JepTexts.toolbar_paging_linesPerPage());
		pageSizeText = new TextBox();
		pageSizeText.addStyleName(MAIN_FONT_STYLE);
		pageSizeText.addStyleName(PAGING_BAR_NUMBER_STYLE);
		pageSizeText.setMaxLength(4);
		pageSizeText.setPixelSize(30, 14);
		pageSizeText.setEnabled(false);
		pageSizeText.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					onPageSizeChange();
				}
			}
		});

		pageSizePanel.add(pageSizePrefix);
		pageSizePanel.add(pageSizeText);
		pageSizePrefix.getElement().getStyle().setTextAlign(TextAlign.RIGHT);
		pageSizePanel.setCellWidth(pageSizeText, 30 + Unit.PX.getType());
		pageSizePanel.setWidth("100%");

		panel.add(buttonsPanel);
		panel.add(displayText);
		panel.add(pageSizePanel);
		panel.setWidth("100%");
		
		panel.setCellHorizontalAlignment(buttonsPanel, HasHorizontalAlignment.ALIGN_LEFT);
		panel.setCellHorizontalAlignment(displayText, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellHorizontalAlignment(pageSizePanel, HasHorizontalAlignment.ALIGN_RIGHT);
	}

	public Button makeButton(String buttonId, ImageResource icon, String name, ClickHandler handler) {
		return makeButton(buttonId, icon, name, handler, true);
	}

	public Button makeButton(String buttonId, ImageResource icon, String name, ClickHandler handler, Boolean enabled) {
		JepButton button = new JepButton(buttonId, null, icon);
		button.setTitle(name);
		button.addClickHandler(handler);
		button.setEnabled(enabled);
		return button;
	}

	/**
	 * Установка значения количества строк в наборе данных.
	 * 
	 * @param totalLength
	 *            количество строк в наборе данных
	 */
	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}

	/**
	 * Запрос на обновление набора данных.
	 */
	public void refresh() {
		notifyListeners(PAGING_REFRESH_EVENT, new JepEvent(this));
	}

	/**
	 * Запрос на установку размера страницы набора данных.
	 */
	protected void onPageSizeChange() {
		String value = pageSizeText.getText();
		if (value.equals("") || !JepRiaUtil.isInteger(value)) {
			pageSizeText.setText(String.valueOf((int) pageSize));
			return;
		}
		pageSize = Integer.parseInt(value);

		notifyListeners(PAGING_SIZE_EVENT, new JepEvent(this, pageSize));
	}

	/**
	 * Установка размера страницы набора данных.
	 * 
	 * @param pageSize
	 *            размер страницы набора данных
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Получение размера страницы набора данных.
	 * 
	 * @return размер страницы набора данных
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Запрос перехода на заданную в текстовом поле панели страницу набора
	 * данных.
	 */
	protected void onPageChange() {
		String value = pageNumberText.getText();
		if (value.equals("") || !JepRiaUtil.isInteger(value)) {
			pageNumberText.setText(String.valueOf((int) activePage));
			return;
		}
		int page = Integer.parseInt(value);
		notifyListeners(PAGING_GOTO_EVENT, new JepEvent(this, page));
	}

	/**
	 * Установка текущей активной страницы набора данных.
	 * 
	 * @param activePage
	 *            текущяя активная страница набора данных
	 */
	public void setActivePage(int activePage) {
		this.activePage = activePage;
	}

	/**
	 * Запрос перехода на первую страницу набора данных.
	 */
	public void first() {
		notifyListeners(PAGING_GOTO_EVENT, new JepEvent(this, 1));
	}

	/**
	 * Запрос перехода на последнюю страницу набора данных.
	 */
	public void last() {
		notifyListeners(PAGING_GOTO_EVENT, new JepEvent(this, pages));
	}

	/**
	 * Запрос перехода на предыдущую страницу набора данных.
	 */
	public void previous() {
		notifyListeners(PAGING_GOTO_EVENT, new JepEvent(this, activePage - 1));
	}

	/**
	 * Запрос перехода на следующую страницу набора данных.
	 */
	public void next() {
		notifyListeners(PAGING_GOTO_EVENT, new JepEvent(this, activePage + 1));
	}

	/**
	 * Настройка панели в соответствии с новыми параметрами.<br/>
	 * Реализация скопирована из метода класса-предка
	 * <code>onLoad(LoadEvent event)</code>.
	 */
	public void adjust() {
		String htmlSpace = " "; // &nbsp;
		start = pageSize * (activePage - 1);
		pageNumberText.setText(String.valueOf((int) activePage));
		pageNumberText.setEnabled(true);
		pageSizeText.setText(String.valueOf((int) pageSize));
		pageSizeText.setEnabled(true);
		pages = totalLength < pageSize ? 1 : (int) Math.ceil((double) totalLength / pageSize);
		pageNumberPostfix.setText(JepTexts.toolbar_paging_from() + htmlSpace + pages);

		first.setEnabled(activePage != 1);
		prev.setEnabled(activePage != 1);
		next.setEnabled(activePage != pages);
		last.setEnabled(activePage != pages);
		refresh.setEnabled(true);

		StringBuilder displayTextBuffer = new StringBuilder();
		switch (totalLength) {
			case 0:
				displayTextBuffer.append(JepTexts.toolbar_paging_no_record());
				break;
			case 1:
				displayTextBuffer.append(JepTexts.toolbar_paging_single_record());
				break;
			default:
				int pageEnd = activePage == pages ? totalLength : start + pageSize;
				displayTextBuffer.append(JepTexts.toolbar_paging_records());
				displayTextBuffer.append(htmlSpace);
				displayTextBuffer.append(start + 1);
				displayTextBuffer.append(" - ");
				displayTextBuffer.append(pageEnd);
				displayTextBuffer.append(htmlSpace);
				displayTextBuffer.append(JepTexts.toolbar_paging_from());
				displayTextBuffer.append(htmlSpace);
				displayTextBuffer.append(totalLength);
		}

		displayText.setText(displayTextBuffer.toString());
		displayText.getElement().getStyle().setTextAlign(TextAlign.CENTER);
	}

	/**
	 * Добавление слушателя определенного типа собитий.<br/>
	 * Концепция поддержки обработки событий и пример реализации метода отражен
	 * в описании пакета {@link com.technology.jep.jepria.client.widget}.
	 * 
	 * @param eventType тип события
	 * @param listener слушатель
	 */
	public void addListener(JepEventType eventType, JepListener listener) {
		switch (eventType) {
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

		observable.addListener(eventType, listener);
	}

	/**
	 * Добавление прослушивателей для реализации прослушивания события
	 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#PAGING_REFRESH_EVENT}
	 * .<br/>
	 * В текущей версии - пустая реализация. Добавлено для возможной перегрузки
	 * в наследниках.
	 */
	protected void addPagingRefreshListner() {

	}

	/**
	 * Добавление прослушивателей для реализации прослушивания события
	 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#PAGING_SIZE_EVENT}
	 * .<br/>
	 * В текущей версии - пустая реализация. Добавлено для возможной перегрузки
	 * в наследниках.
	 */
	protected void addPagingSizeListner() {

	}

	/**
	 * Добавление прослушивателей для реализации прослушивания события
	 * {@link com.technology.jep.jepria.client.widget.event.JepEventType#PAGING_GOTO_EVENT}
	 * .<br/>
	 * В текущей версии - пустая реализация. Добавлено для возможной перегрузки
	 * в наследниках.
	 */
	protected void addPagingGotoListener() {

	}

	/**
	 * Удаление слушателя определенного типа событий.<br/>
	 * Реализуется вызовом метода
	 * {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#removeListener(JepEventType eventType, JepListener listener)}
	 * объекта {@link #observable}.
	 * 
	 * @param eventType
	 *            тип события
	 * @param listener
	 *            слушатель
	 */
	public void removeListener(JepEventType eventType, JepListener listener) {
		observable.removeListener(eventType, listener);
	}

	/**
	 * Уведомление слушателей определенного типа о событии.<br/>
	 * Реализуется вызовом метода
	 * {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#notifyListeners(JepEventType eventType, JepEvent event)}
	 * объекта {@link #observable}.
	 * 
	 * @param eventType
	 *            тип события
	 * @param event
	 *            событие
	 */
	public void notifyListeners(JepEventType eventType, JepEvent event) {
		observable.notifyListeners(eventType, event);
	}

	/**
	 * Получение списка слушателей определенного типа собитий.<br/>
	 * Реализуется вызовом метода
	 * {@link com.technology.jep.jepria.client.widget.event.JepObservableImpl#getListeners(JepEventType eventType)}
	 * объекта {@link #observable}.
	 * 
	 * @param eventType
	 *            тип события
	 * 
	 * @return список слушателей
	 */
	public List<JepListener> getListeners(JepEventType eventType) {
		return observable.getListeners(eventType);
	}

	@Override
	public Object mask() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unmask() {
		// TODO Auto-generated method stub
	}
}