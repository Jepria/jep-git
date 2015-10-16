package com.technology.jep.jepria.client.widget.list.cell;

import static com.google.gwt.dom.client.BrowserEvents.BLUR;
import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.DBLCLICK;
import static com.google.gwt.dom.client.BrowserEvents.KEYDOWN;
import static com.google.gwt.dom.client.BrowserEvents.KEYUP;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.shared.field.TreeCellNames.HAS_CHILDREN;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.client.ui.Image;
import com.technology.jep.jepria.client.widget.list.HasTreeGridManager;
import com.technology.jep.jepria.client.widget.list.TreeGridManager;
import com.technology.jep.jepria.shared.record.JepRecord;

public class EditTreeCell extends AbstractEditableCell<String, EditTreeCell.ViewData> 
	implements HasTreeGridManager {

	/**
	 * Количество разделителей равное ширине иконке (+/-) и одному уровню
	 * вложенности
	 */
	private int depthStep = 4;

	/**
	 * Разделитель
	 */
	private String treeDelimeter = "&nbsp;";

	/**
	 * Ссылка на объект, управляющий списком
	 */
	private TreeGridManager<?, ?, ?> treeGridManager;
	
	/**
	 * Ресурс иконки узла, у которого отсутствуют дочерние элементы
	 */
	private final ImageResource treeNoChildrenIcon = JepImages.treeNoChildren();

	/**
	 * Ресурс иконки плюса
	 */
	private final ImageResource plusIcon = JepImages.plus();

	/**
	 * Ресурс иконки минуса
	 */
	private final ImageResource minusIcon = JepImages.minus();

	/**
	 * Ресурс иконки закрытого узла дерева
	 */
	private final ImageResource folderCollapsedIcon = JepImages.folderCollapsed();
	
	private static Template template;

	private final SafeHtmlRenderer<String> renderer;

	/**
	 * Construct a new EditTextCell that will use a {@link SimpleSafeHtmlRenderer}.
	 */
	public EditTreeCell() {
		this(SimpleSafeHtmlRenderer.getInstance());
	}

	/**
	 * Construct a new EditTextCell that will use a given
	 * {@link SafeHtmlRenderer} to render the value when not in edit mode.
	 * 
	 * @param renderer a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
	 */
	public EditTreeCell(SafeHtmlRenderer<String> renderer) {
		super(CLICK, DBLCLICK, KEYUP, KEYDOWN, BLUR);
		if (template == null) {
			template = GWT.create(Template.class);
		}
		if (renderer == null) {
			throw new IllegalArgumentException("renderer == null");
		}
		this.renderer = renderer;
	}
	
	interface Template extends SafeHtmlTemplates {
		@Template("<input type=\"text\" value=\"{0}\" tabindex=\"-1\"></input>")
		SafeHtml input(String value);
	}	

	@Override
	public boolean isEditing(Context context, Element parent, String value) {
		ViewData viewData = getViewData(context.getKey());
		return viewData == null ? false : viewData.isEditing();
	}

	@Override
	public boolean resetFocus(Context context, Element parent, String value) {
		if (isEditing(context, parent, value)) {
			getInputElement(parent).focus();
			return true;
		}
		return false;
	}

	/**
	 * Convert the cell to edit mode.
	 *
	 * @param context the {@link Context} of the cell
	 * @param parent the parent element
	 * @param value the current value
	 */
	protected void edit(Context context, Element parent, String value) {
		setValue(context, parent, value);
		InputElement input = getInputElement(parent);
		input.focus();
		input.select();
	}

	/**
	 * Convert the cell to non-edit mode.
	 * 
	 * @param context the context of the cell
	 * @param parent the parent Element
	 * @param value the value associated with the cell
	 */
	private void cancel(Context context, Element parent, String value) {
		clearInput(getInputElement(parent));
		setValue(context, parent, value);
	}

	/**
	 * Clear selected from the input element. Both Firefox and IE fire spurious
	 * onblur events after the input is removed from the DOM if selection is not cleared.
	 *
	 * @param input the input element
	 */
	private native void clearInput(Element input) /*-{
		if (input.selectionEnd)
			input.selectionEnd = input.selectionStart;
		else if ($doc.selection)
			$doc.selection.clear();
	}-*/;

	/**
	 * Commit the current value.
	 * 
	 * @param context the context of the cell
	 * @param parent the parent Element
	 * @param viewData the {@link ViewData} object
	 * @param valueUpdater the {@link ValueUpdater}
	 */
	private void commit(Context context, Element parent, ViewData viewData, ValueUpdater<String> valueUpdater) {
		String value = updateViewData(parent, viewData, false);
		clearInput(getInputElement(parent));
		setValue(context, parent, viewData.getOriginal());
		if (valueUpdater != null) {
			valueUpdater.update(value);
		}
	}

	private void editEvent(Context context, Element parent, String value,
			ViewData viewData, NativeEvent event,
			ValueUpdater<String> valueUpdater) {
		String type = event.getType();
		boolean keyUp = KEYUP.equals(type);
		boolean keyDown = KEYDOWN.equals(type);
		if (keyUp || keyDown) {
			int keyCode = event.getKeyCode();
			if (keyUp && keyCode == KeyCodes.KEY_ENTER) {
				// Commit the change.
				commit(context, parent, viewData, valueUpdater);
			} else if (keyUp && keyCode == KeyCodes.KEY_ESCAPE) {
				// Cancel edit mode.
				String originalText = viewData.getOriginal();
				if (viewData.isEditingAgain()) {
					viewData.setCurrent(originalText);
					viewData.setEditing(false);
				} else {
					setViewData(context.getKey(), null);
				}
				cancel(context, parent, value);
			} else {
				// Update the text in the view data on each key.
				updateViewData(parent, viewData, true);
			}
		} else if (BLUR.equals(type)) {
			// Commit the change. Ensure that we are blurring the input element
			// and
			// not the parent element itself.
			EventTarget eventTarget = event.getEventTarget();
			if (Element.is(eventTarget)) {
				Element target = Element.as(eventTarget);
				if (InputElement.TAG.equalsIgnoreCase(target.getTagName())) {
					commit(context, parent, viewData, valueUpdater);
				}
			}
		}
	}

	/**
	 * Update the view data based on the current value.
	 *
	 * @param parent the parent element
	 * @param viewData the {@link ViewData} object to update
	 * @param isEditing true if in edit mode
	 * @return the new value
	 */
	private String updateViewData(Element parent, ViewData viewData, boolean isEditing) {
		InputElement input = getInputElement(parent);
		String value = input.getValue();
		viewData.setCurrent(value);
		viewData.setEditing(isEditing);
		return value;
	}

	/**
	 * Устанавливает ссылку на объект, управляющий списком
	 * 
	 * @param treeGridManager объект, управляющий списком
	 */
	public void setTreeGridManager(TreeGridManager<?, ?, ?> treeGridManager) {
		this.treeGridManager = treeGridManager;
	}

	/**
	 * Рендерит ячейку таблицы
	 * 
	 * @param context контекст ячейки (информация о местоположении ячейки на списке,
	 * значение JepRecord для строки, в которой находится ячейчка)
	 * @param value значение ячейки
	 * @param sb builder, в который помещается конечное содержимое ячейки
	 */
	@SuppressWarnings("serial")
	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {

		// Если нет контекста, то не получится корректно отобразить ячейку
		if (context == null) {
			return;
		}

		JepRecord record = (JepRecord) context.getKey();
		ListTreeNode node = treeGridManager.findNode(record);

		int depth = 1;
		boolean isOpen = false;

		if (node != null) {

			depth = node.getDepth();
			isOpen = node.isOpen();
		}

		// Если нет детей, то также сдвигаем на размер иконки (+/-)
		if (Boolean.FALSE.equals(record.get(HAS_CHILDREN))) {
			depth++;
		}

		for (depth *= depthStep; depth > depthStep; depth--) {

			sb.append(SafeHtmlUtils.fromTrustedString(treeDelimeter));
		}

		ImageResource nodeIconResource = treeNoChildrenIcon;

		// Если есть поддерево, ставим иконку управления деревом
		if (Boolean.TRUE.equals(record.get(HAS_CHILDREN))) {
			final Image icon = new Image(isOpen ? minusIcon : plusIcon);
			icon.setWidth("10px");
			icon.setHeight("10px");
			icon.getElement().getStyle().setCursor(Cursor.POINTER);

			sb.append(new SafeHtml() {

				@Override
				public String asString() {
					return icon.toString() + treeDelimeter;
				}
			});

			nodeIconResource = folderCollapsedIcon;
		}

		final Image nodeIcon = new Image(nodeIconResource);
		sb.append(new SafeHtml() {

			@Override
			public String asString() {
				return nodeIcon.toString() + treeDelimeter + treeDelimeter;
			}
		});

		// Get the view data.
		Object key = context.getKey();
		ViewData viewData = getViewData(key);
		if (viewData != null && !viewData.isEditing() && value != null
				&& value.equals(viewData.getCurrent())) {
			clearViewData(key);
			viewData = null;
		}

		String toRender = value;
		if (viewData != null) {
			String text = viewData.getCurrent();
			if (viewData.isEditing()) {
				/*
				 * Do not use the renderer in edit mode because the value of a
				 * text input element is always treated as text. SafeHtml isn't
				 * valid in the context of the value attribute.
				 */
				sb.append(template.input(text));
				return;
			} else {
				// The user pressed enter, but view data still exists.
				toRender = text;
			}
		}

		if (toRender != null && toRender.trim().length() > 0) {
			sb.append(renderer.render(toRender));
		} else {
			/*
			 * Render a blank space to force the rendered element to have a
			 * height. Otherwise it is not clickable.
			 */
			sb.appendHtmlConstant("\u00A0");
		}
	}

	/**
	 * Обработка событий ячейки
	 * 
	 * @see com.google.gwt.cell.client.AbstractCell#onBrowserEvent(com.google.gwt.cell.client.Cell.Context,
	 *      com.google.gwt.dom.client.Element, java.lang.Object,
	 *      com.google.gwt.dom.client.NativeEvent,
	 *      com.google.gwt.cell.client.ValueUpdater)
	 */
	@Override
	public void onBrowserEvent(final Context context, final Element parent, final String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
		Object key = context.getKey();
		ViewData viewData = getViewData(key);
		String type = event.getType();
		int keyCode = event.getKeyCode();
		boolean enterPressed = KEYUP.equals(type)
				&& keyCode == KeyCodes.KEY_ENTER;
		if (viewData != null && viewData.isEditing()) {
			// Handle the edit event.
			editEvent(context, parent, value, viewData, event, valueUpdater);
		} else {
			Element firstChild = parent.getFirstChildElement();
			if (CLICK.equals(type) || enterPressed) {
				// Проверяем, что клик был по иконке +
				if (firstChild.isOrHasChild(Element.as(event.getEventTarget()))) {
					treeGridManager.toggleChildren(context);
				} 
			}
			// Go into edit mode only after double click.
			else if (DBLCLICK.equals(type)){
				if (!firstChild.isOrHasChild(Element.as(event.getEventTarget()))) { // текст
					if (viewData == null) {
						viewData = new ViewData(value);
						setViewData(key, viewData);
					} else {
						viewData.setEditing(true);
					}
					edit(context, parent, value);
				}
			}
		}
	}

	/**
	 * Получение ссылки на элемент input-редактирования текста
	 * 
	 * @param parent родительский узел
	 * @return DOM-элемент для редактирования содержимого узла
	 */
	private InputElement getInputElement(Element parent) {
		return parent.getLastChild().<InputElement>cast();
	}
	
	/**
	 * The view data object used by this cell. We need to store both the text
	 * and the state because this cell is rendered differently in edit mode. If
	 * we did not store the edit state, refreshing the cell with view data would
	 * always put us in to edit state, rendering a text box instead of the new
	 * text string.
	 */
	static class ViewData {

		private boolean isEditing;

		/**
		 * If true, this is not the first edit.
		 */
		private boolean isEditingAgain;

		/**
		 * Keep track of the original value at the start of the edit, which
		 * might be the edited value from the previous edit and NOT the actual
		 * value.
		 */
		private String original;

		private String current;

		/**
		 * Construct a new ViewData in editing mode.
		 *
		 * @param text the text to edit
		 */
		public ViewData(String text) {
			this.original = text;
			this.current = text;
			this.isEditing = true;
			this.isEditingAgain = false;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null) {
				return false;
			}
			if (o instanceof ViewData) {
				ViewData vd = (ViewData) o;
				return equalsOrBothNull(original, vd.original)
					&& equalsOrBothNull(current, vd.current)
					&& isEditing == vd.isEditing
					&& isEditingAgain == vd.isEditingAgain;
			}
			return false;
		}

		public String getOriginal() {
			return original;
		}

		public String getCurrent() {
			return current;
		}

		@Override
		public int hashCode() {
			return original.hashCode() + current.hashCode()
					+ Boolean.valueOf(isEditing).hashCode() * 29
						+ Boolean.valueOf(isEditingAgain).hashCode();
		}

		public boolean isEditing() {
			return isEditing;
		}

		public boolean isEditingAgain() {
			return isEditingAgain;
		}

		public void setEditing(boolean isEditing) {
			boolean wasEditing = this.isEditing;
			this.isEditing = isEditing;

			// This is a subsequent edit, so start from where we left off.
			if (!wasEditing && isEditing) {
				isEditingAgain = true;
				original = current;
			}
		}

		public void setCurrent(String text) {
			this.current = text;
		}

		private boolean equalsOrBothNull(Object o1, Object o2) {
			return (o1 == null) ? o2 == null : o1.equals(o2);
		}
	}
}
