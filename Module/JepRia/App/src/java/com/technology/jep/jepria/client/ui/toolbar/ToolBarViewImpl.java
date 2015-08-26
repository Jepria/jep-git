package com.technology.jep.jepria.client.ui.toolbar;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.TOOLBAR_DEFAULT_STYLE;
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.AutomationConstant;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.widget.button.JepButton;
import com.technology.jep.jepria.client.widget.button.JepButton.IconPosition;
import com.technology.jep.jepria.client.widget.button.Separator;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

/**
 * Реализация инструментальной панели.
 * TODO Разобраться с generics (много предупреждений компилятора)
 */
public class ToolBarViewImpl implements ToolBarView {

	/**
	 * Горизонтальная панель, на которой размещаются кнопки.
	 */
	private HorizontalPanel panel;
	
	/**
	 * Хэш-таблица, хранящая соответствие между виджетами и их идентификаторами.
	 */
	private Map<String, Widget> items = new HashMap<String, Widget>();
	
	protected JepPresenter presenter = null;
	
	protected SimplePanel toolBar;
	
	/**
	 * Создаёт стандартный тулбар.
	 */
	public ToolBarViewImpl() {
		
		toolBar = new SimplePanel();
		panel = new HorizontalPanel();
		panel.getElement().setId(AutomationConstant.TOOLBAR_PANEL_ID);
		
		/* Стили для корректного отображения границ совместно с панелью вкладок */
		toolBar.getElement().getStyle().setProperty("backgroundPosition", "0 5%");
		toolBar.getElement().getStyle().setProperty("borderTop", "0");
		toolBar.setStyleName(TOOLBAR_DEFAULT_STYLE);
		toolBar.setWidget(panel);
		
		setHeight(DEFAULT_HEIGHT);
		addDefaultItems();
	}
	
	public void setHeight(int height) {
		toolBar.setHeight(height + Unit.PX.getType());
	}

	public void setWidget(Widget widget) {
		toolBar = (SimplePanel)widget;
	}
	
	/**
	 * Заполнение тулбара стандартными элементами.
	 */
	protected void addDefaultItems() {
		// Закомментированные блоки используются для указания принятого порядка следования кнопок и разделителей.
	
		addButton(
			UP_BUTTON_ID,
			JepImages.up(),
			JepTexts.button_up_alt());
		
		addSeparator(UP_RIGHT_SEPARATOR_ID);
		
		addButton(
			ADD_BUTTON_ID,
			JepImages.add(),
			JepTexts.button_add_alt());
		addButton(
			SAVE_BUTTON_ID,
			JepImages.save(),
			JepTexts.button_save_alt());
		addButton(
			EDIT_BUTTON_ID,
			JepImages.edit(),
			JepTexts.button_edit_alt());
		addButton(
			DELETE_BUTTON_ID,
			JepImages.delete(),
			JepTexts.button_delete_alt());
		addButton(
			VIEW_DETAILS_BUTTON_ID,
			JepImages.view(),
			JepTexts.button_view_alt());
		
		addSeparator(SEARCH_SEPARATOR_ID);
		
		addButton(
			LIST_BUTTON_ID,
			JepTexts.button_list_alt());
		addButton(
			SEARCH_BUTTON_ID,
			JepImages.search(),
			JepTexts.button_search_alt());
		addButton(
			FIND_BUTTON_ID,
			JepTexts.button_find_alt());
		
//		addButton(
//			REPORT_BUTTON_ID,
//			JepImages.report(),
//			JepTexts.button_report_alt());
		
//		addSeparator(TOOLBAR_REFRESH_SEPARATOR_ID);
		
//		addButton(
//			REFRESH_BUTTON_ID,
//			JepImages.refresh(),
//			JepTexts.button_refresh_alt());
//		addButton(
//			EXCEL_BUTTON_ID,
//			JepImages.excel(),
//			JepTexts.button_excel_alt());
		
//		addSeparator(TOOLBAR_HELP_SEPARATOR_ID);
		
//		addButton(
//			TOOLBAR_HELP_BUTTON_ID,
//			JepImages.help(),
//			JepTexts.button_help_alt());
	}
	
	/**
	 * Добавление кнопки с текстом.<br>
	 * Текст используется как для кнопки, так и для всплывающей подсказки.
	 * 
	 * @param id идентификатор кнопки
	 * @param text текст на кнопке и для всплывающей подсказки
 	 * @return добавленная кнопка
	 */
	@Override
	public JepButton addButton(String id, String text) {
		return addButton(id, text, text);
	}
	
	/**
	 * Добавление кнопки с текстом и всплывающей подсказкой.
	 * 
	 * @param id идентификатор кнопки
	 * @param text текст на кнопке
	 * @param title всплывающая подсказка для кнопки
 	 * @return добавленная кнопка
	 */
	@Override
	public JepButton addButton(String id, String text, String title) {
		JepButton button = new JepButton(id, text);
		button.setTitle(title);
		panel.add(button);
		items.put(id, button);

		return button;
	}
	
	/**
	 * Добавление кнопки с иконкой и всплывающей подсказкой.
	 *
	 * @param id идентификатор кнопки
	 * @param icon иконка
	 * @param title всплывающая подсказка для кнопки
 	 * @return добавленная кнопка
	 */
	@Override
	public JepButton addButton(String id, ImageResource icon, String title) {
		return addButton(id, icon, null, title);
	}
	
	/**
	 * Добавление кнопки с иконкой, текстом и всплывающей подсказкой.
	 *
	 * @param id идентификатор кнопки
	 * @param icon иконка
	 * @param text текст на кнопке
	 * @param title всплывающая подсказка для кнопки
 	 * @return добавленная кнопка
	 */
	@Override
	public JepButton addButton(String id, ImageResource icon, String text, String title) {
		JepButton button = new JepButton(id, text, icon);
		button.setTitle(title);
		panel.add(button);
		items.put(id, button);

		return button;
	}
	
	/**
	 * Добавление кнопки с текстом и иконкой, расположенной в указанной позиции.<br>
	 * Текст используется как для кнопки, так и для всплывающей подсказки.
	 * 
	 * @param id идентификатор кнопки
	 * @param icon иконка
	 * @param text текст на кнопке
	 * @param position расположение иконки
 	 * @return добавленная кнопка
	 */
	@Override
	public JepButton addButton(String id, ImageResource icon, String text, IconPosition position) {
		return addButton(id, icon, text, text, position);
	}
	
	/**
	 * Добавление кнопки с текстом, всплывающей подсказкой и иконкой, расположенной в указанной позиции.
	 * 
	 * @param id идентификатор кнопки
	 * @param icon иконка
	 * @param text текст на кнопке
	 * @param title всплывающая подсказка для кнопки
	 * @param position расположение иконки
 	 * @return добавленная кнопка
	 */
	@Override
	public JepButton addButton(String id, ImageResource icon, String text, String title, IconPosition position) {
		JepButton button = new JepButton(id, text, icon, position);
		button.setTitle(title);
		panel.add(button);
		items.put(id, button);

		return button;
	}
	
	/**
	 * Добавление разделителя.
	 *
	 * @param separatorId идентификатор разделителя
 	 * @return добавленный разделитель
	 */
	@Override
	public Separator addSeparator(String separatorId) {
		Separator separator = new Separator();
		panel.add(separator);
		items.put(separatorId, separator);

		return separator;
	}
	
	@Override
	public Widget asWidget() {
		return toolBar;
	}

	/**
	 * Получение кнопки по идентификатору.
	 *
	 * @param buttonId идентификатор
	 * @return кнопка
	 */
	@Override
	public JepButton getButton(String buttonId) {
		JepButton button = null;
		Widget item = getItem(buttonId);
		
		if(item instanceof JepButton) {
			button = (JepButton)item;
		}
		
		return button;
	}

	/**
	 * Установка доступности кнопки.
	 *
	 * @param buttonId идентификатор
	 * @param enabled true &mdash; доступна, false &mdash; недоступна
	 * @return true в случае успеха, false в случае неудачи (кнопка не существует)
	 */
	@Override
	public boolean setButtonEnabled(String buttonId, boolean enabled) {
		JepButton button = getButton(buttonId);
		if (button != null) {
			button.setEnabled(enabled);
		}
		return (button != null);
	}
	
	/**
	 * Установка доступности кнопок только переданного набора.
	 *
	 * @param buttonIds кнопки, которые должны быть доступны
	 */
	public void setButtonsEnabling(Set<String> buttonIds) {
		Set<Map.Entry<String, Widget>> entries = items.entrySet();
		for(Map.Entry<String, Widget> entry: entries) {
			String buttonId = entry.getKey();
			Widget item = entry.getValue();
			if(item instanceof JepButton) {
				// Кнопка "Наверх" показывается и скрывается, в зависимости от текущего уровня стека JepScopeStack.
				// Поэтому здесь делаем для нее исключение.
				if(!buttonId.equals(UP_BUTTON_ID)) {
					JepButton button = (JepButton) item;
					button.setEnabled(buttonIds != null && buttonIds.contains(buttonId));
				}
			}
		}
	}
	
	/**
	 * Добавление виджета на тулбар.
	 *
	 * @param id идентификатор
	 * @param w виджет
	 */
	@Override	
	public void addItem(String id, Widget w) {
		items.put(id, w);
		panel.add(w);
	}
	
	/**
	 * Вставка виджета на тулбар в заданной позиции.
	 *
	 * @param id идентификатор
	 * @param w виджет
	 * @param beforeIndex индекс, перед которым виджет будет вставлен
	 */
	@Override	
	public void insertItem(String id, Widget w, int beforeIndex) {
		items.put(id, w);
		panel.insert(w, beforeIndex);
	}
	
	/**
	 * Получение виджета по идентификатору.
	 *
	 * @param id идентификатор
	 * @return виджет
	 */
	@Override	
	public Widget getItem(String id) {
		return items.get(id);
	}
	
	/**
	 * Удаление виджета с тулбара.
	 *
	 * @param id идентификатор
	 */
	@Override
	public void removeItem(String id) {
		Widget item = getItem(id);
		panel.remove(item);
		items.remove(id);
	}

	/**
	 * Установка видимости виджета.
	 *
	 * @param id идентификатор виджета
	 * @param visible true - виджет отображается, false - виджет скрыт
	 * @return true - виджет присутствует на тулбаре, false - виджет отсутствует на тулбаре
	 */
	@Override
	public boolean setItemVisible(String id, boolean visible) {
		Widget item = getItem(id);
		if (item != null)
			item.setVisible(visible);
		return (item != null);
	}

	/**
	 * Удаление всего содержимого тулбара.
	 */
	@Override
	public void removeAll() {
		String[] keys = items.keySet().toArray(new String[0]);
		for (String id : keys) {
			removeItem(id);
		}
	}

	@Override
	public void setPresenter(JepPresenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void addEnterClickListener(final JepListener listener) {
		RootPanel.get().addDomHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				// Для формы поиска очевидной на данное событие является реакция - поиск информации.
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					listener.handleEvent(new JepEvent(event));
				}
			}
		}, KeyDownEvent.getType());
	}
	
}
