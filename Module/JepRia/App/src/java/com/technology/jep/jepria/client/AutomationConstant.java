package com.technology.jep.jepria.client;

import com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant;


public class AutomationConstant {
	/**
	 * ID тестового поля ввода username для Login-страницы JavaSSO
	 */
	public static final String JAVASSO_LOGIN_USERNAME_FIELD_ID = "j_username";
	
	/**
	 * ID тестового поля ввода пароля для Login-страницы JavaSSO
	 */
	public static final String JAVASSO_LOGIN_PASSWORD_FIELD_ID = "j_password";
	
	/**
	 * ID тестового поля ввода username для login
	 */
	public static final String LOGIN_USERNAME_FIELD_ID = "LOGIN_USERNAME_FIELD";
	
	/**
	 * ID тестового поля ввода пароля для login
	 */
	public static final String LOGIN_PASSWORD_FIELD_ID = "LOGIN_PASSWORD_FIELD";
	
	/**
	 * ID кнопки входа (login)
	 */
	public static final String LOGIN_BUTTON_ID = "LOGIN_BUTTON";
	
//	/**
//	 * ID кнопки выхода
//	 */
//	public static final String EXIT_BUTTON_ID = "EXIT_BUTTON";
	
	/**
	 * ID текстового поля, содержащего username залогиненного пользователя
	 * В частности, используется для определения факта выполнения входа в приложение (login)  
	 */
	public static final String LOGGED_IN_USER_ID = "LOGGED_IN_USER";

	/**
	 * ID кнопки выхода в Entrance-панели
	 */
	public static final String LOGOUT_BUTTON_ID = "LOGOUT_BUTTON";
	
	/**
	 * ID Entrance-панели
	 */
	public static final String ENTRANCE_PANEL_ID = "ENTRANCE_PANEL";

	/**
	 * ID кнопки выхода в Entrance-панели
	 */
	public static final String ENTRANCE_PANEL_LOGOUT_BUTTON_ID = LOGOUT_BUTTON_ID;

	/**
	 * ID панели вкладок дочерних модулей
	 */
	public static final String MODULE_TAB_PANEL_ID = "MODULE_TAB_PANEL";

	/**
	 * ID панели форм-контейнера модуля
	 */
	public static final String MODULE_PANEL_ID = "MODULE_PANEL";

	/**
	 * ID панели детальной формы модуля
	 */
	public static final String DETAIL_FORM_PANEL_ID = "DETAIL_FORM_PANEL";

	/**
	 * ID элементов HEADER и BODY грида списочной формы модуля
	 */
	public static final String GRID_HEADER_POSTFIX = "_HEADER";
	public static final String GRID_BODY_POSTFIX = "_BODY";
	
	/**
	 * ID панели ToolBar модуля
	 */
	public static final String TOOLBAR_PANEL_ID = "TOOLBAR_PANEL";

	/**
	 * ID панели StatusBar модуля
	 */
	public static final String STATUSBAR_PANEL_ID = "STATUSBAR_PANEL";

	/**
	 * ID кнопки "Создать" панели ToolBar модуля
	 */
	public static final String TOOLBAR_ADD_BUTTON_ID = ToolBarConstant.ADD_BUTTON_ID;

	/**
	 * ID кнопки возврата на предыдущий уровень панели ToolBar модуля
	 */
	public static final String TOOLBAR_UP_BUTTON_ID = ToolBarConstant.UP_BUTTON_ID;

	/**
	 * ID кнопки "Сохранить" панели ToolBar модуля
	 */
	public static final String TOOLBAR_SAVE_BUTTON_ID = ToolBarConstant.SAVE_BUTTON_ID;

	/**
	 * ID кнопки "Изменить" панели ToolBar модуля
	 */
	public static final String TOOLBAR_EDIT_BUTTON_ID = ToolBarConstant.EDIT_BUTTON_ID;

	/**
	 * ID кнопки "Удалить" панели ToolBar модуля
	 */
	public static final String TOOLBAR_DELETE_BUTTON_ID = ToolBarConstant.DELETE_BUTTON_ID;

	/**
	 * ID кнопки "Просмотр" панели ToolBar модуля
	 */
	public static final String TOOLBAR_VIEW_DETAILS_BUTTON_ID = ToolBarConstant.VIEW_DETAILS_BUTTON_ID;

	/**
	 * ID кнопки "Список" панели ToolBar модуля
	 */
	public static final String TOOLBAR_LIST_BUTTON_ID = ToolBarConstant.LIST_BUTTON_ID;
	
	/**
	 * ID кнопки "Поиск" панели ToolBar модуля
	 */
	public static final String TOOLBAR_SEARCH_BUTTON_ID = ToolBarConstant.SEARCH_BUTTON_ID;

	/**
	 * ID кнопки "Найти" панели ToolBar модуля
	 */
	public static final String TOOLBAR_FIND_BUTTON_ID = ToolBarConstant.FIND_BUTTON_ID;

	/**
	 * ID кнопки "Обновить" панели ToolBar модуля
	 */
	public static final String TOOLBAR_REFRESH_BUTTON_ID = ToolBarConstant.REFRESH_BUTTON_ID;

	/**
	 * ID кнопки "Excel" панели ToolBar модуля
	 */
	public static final String TOOLBAR_EXCEL_BUTTON_ID = ToolBarConstant.EXCEL_BUTTON_ID;

	/**
	 * ID кнопки "Report" панели ToolBar модуля
	 */
	public static final String TOOLBAR_REPORT_BUTTON_ID = ToolBarConstant.REPORT_BUTTON_ID;

	/**
	 * ID кнопки "UP_RIGHT"-разделитель кнопок панели ToolBar модуля
	 */
	public static final String TOOLBAR_UP_RIGHT_SEPARATOR_ID = ToolBarConstant.UP_RIGHT_SEPARATOR_ID;

	/**
	 * ID кнопки "UP_RIGHT"-разделитель кнопок панели ToolBar модуля
	 */
	public static final String TOOLBAR_SEARCH_SEPARATOR_ID = ToolBarConstant.SEARCH_SEPARATOR_ID;

	/**
	 * ID popup-окна сообщения Alert
	 */
	public static final String ALERT_MESSAGEBOX_ID = "ALERT_MESSAGEBOX";

	/**
	 * ID popup-окна сообщения Confirm
	 */
	public static final String CONFIRM_MESSAGEBOX_ID = "CONFIRM_MESSAGEBOX";

	/**
	 * ID кнопки "Yes" окна ConfirmMessageBox
	 */
	public static final String CONFIRM_MESSAGE_BOX_YES_BUTTON_ID = "CONFIRM_MESSAGE_BOX_YES_BUTTON";

	/**
	 * ID кнопки "No" окна ConfirmMessageBox
	 */
	public static final String CONFIRM_MESSAGE_BOX_NO_BUTTON_ID = "CONFIRM_MESSAGE_BOX_NO_BUTTON";

	/**
	 * ID popup-окна сообщения Error
	 */
	public static final String ERROR_MESSAGEBOX_ID = "ERROR_MESSAGEBOX";

	/**
	 * ID кнопки "Yes" окна ErrorMessageBox
	 */
	public static final String ERROR_MESSAGE_BOX_OK_BUTTON_ID = "ERROR_MESSAGE_BOX_OK_BUTTON";

	/**
	 * ID кнопки "No" окна ErrorMessageBox
	 */
	public static final String ERROR_MESSAGE_BOX_NO_BUTTON_ID = "ERROR_MESSAGE_BOX_NO_BUTTON";

	/**
	 * Инфикс-идентификатор элементов меню комбобоксов
	 */
	public static final String JEP_COMBO_BOX_FIELD_MENU_ITEM_INFIX = "_JEP_COMBOBOX_MENU_ITEM_";
	
	/**
	 * Постфикс-идентификатор input-поля Jep-полей
	 */
	public static final String JEP_FIELD_INPUT_POSTFIX = "_JEP_FIELD_INPUT";
	
	/**
	 * Постфикс-идентификатор allow-blank-маркера (*) Jep-поля
	 */
	public static final String JEP_FIELD_ALLOW_BLANK_POSTFIX = "_JEP_FIELD_ALLOW_BLANK";
	
	/**
	 * HTML-атрибут и его значения editableCard и viewCard Jep-полей
	 */
	public static final String JEP_CARD_TYPE_HTML_ATTR = "jep-card-type";
	public static final String JEP_CARD_TYPE_VALUE_EDTB = "editable";
	public static final String JEP_CARD_TYPE_VALUE_VIEW = "view";
	
	/**
	 * Постфикс-идентификатор кнопки 'развернуть' комбобоксов
	 */
	public static final String JEP_COMBO_BOX_FIELD_DROPDOWN_BTN_POSTFIX = "_JEP_COMBO_BOX_FIELD_DROPDOWN_BTN";
	
	/**
	 * Постфикс-идентификатор PopupPanel всплывающего меню комбобоксов
	 */
	public static final String JEP_COMBO_BOX_FIELD_POPUP_POSTFIX = "_JEP_COMBO_BOX_FIELD_POPUP";
	
	/**
	 * Инфикс элементов меню DualListField
	 */
	public static final String JEP_DUAL_LIST_FIELD_MENU_ITEM_INFIX = "_JEP_DUAL_LIST_FIELD_MENU_ITEM_";
	
	/**
	 * Постфикс-идентификаторы списков DualListField
	 */
	public static final String JEP_DUAL_LIST_FIELD_LEFTPART_POSTFIX = "_JEP_DUAL_LIST_FIELD_LEFTPART";
	public static final String JEP_DUAL_LIST_FIELD_RIGHTPART_POSTFIX = "_JEP_DUAL_LIST_FIELD_RIGHTPART";
	
	/**
	 * Постфикс-идентификаторы кнопок перемещения опций между двумя списками DualListField
	 */
	public static final String JEP_DUAL_LIST_FIELD_MOVERIGHT_BTN_POSTFIX = "_JEP_DUAL_LIST_FIELD_MOVERIGHT_BTN";
	public static final String JEP_DUAL_LIST_FIELD_MOVELEFT_BTN_POSTFIX = "_JEP_DUAL_LIST_FIELD_MOVELEFT_BTN";
	public static final String JEP_DUAL_LIST_FIELD_MOVEALLRIGHT_BTN_POSTFIX = "_JEP_DUAL_LIST_FIELD_MOVEALLRIGHT_BTN";
	public static final String JEP_DUAL_LIST_FIELD_MOVEALLLEFT_BTN_POSTFIX = "_JEP_DUAL_LIST_FIELD_MOVEALLLEFT_BTN";
	
	/**
	 * Инфикс элементов списка поля JepListField
	 */
	public static final String JEP_LIST_FIELD_ITEM_CHECKBOX_INFIX = "_JEP_LIST_FIELD_ITEM_CHECKBOX_";
	
	/**
	 * Постфикс-идентификатор флажка "Выделить все" поля JepListField
	 */
	public static final String JEP_LIST_FIELD_CHECKALL_POSTFIX = "_JEP_LIST_FIELD_CHECKALL";
	
	/**
	 * HTML-атрибут элемента, который требует хранения значения опции, которой он однозначно соответствует.
	 */
	public static final String JEP_OPTION_VALUE_HTML_ATTR = "jep-option-value";
	
	
	/**
	 * ID первой строки списка списочной формы
	 */
	public static final String LIST_FORM_GRID_ROW_ID = "LIST_FORM_GRID_ROW_ID";
}
