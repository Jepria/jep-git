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
	 * ID панели списочной формы модуля
	 */
	public static final String LIST_FORM_PANEL_ID = "LIST_FORM_PANEL";
	
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
	 * Префикс элементов меню комбобоксов
	 */
	public static final String DETAIL_FORM_COMBOBOX_MENU_ITEM_INFIX = "_DETAIL_FORM_COMBOBOX_MENU_ITEM_";
	
	/**
	 * Постфикс input-поля комбобоксов
	 */
	public static final String FIELD_INPUT_POSTFIX = "_INPUT";
	
	/**
	 * Постфикс кнопки 'развернуть' комбобоксов
	 */
	public static final String DETAIL_FORM_COMBOBOX_DROPDOWN_BTN_POSTFIX = "_DETAIL_FORM_COMBOBOX_SELECT_BTN";
	
	/**
	 * Инфикс элементов меню DualListField
	 */
	public static final String DETAIL_FORM_DUALLIST_MENU_ITEM_INFIX = "_DETAIL_FORM_DUALLIST_MENU_ITEM_";
	
	/**
	 * Постфикс-идентификаторы кнопок перемещения опций между двумя списками DualListField
	 */
	public static final String DETAIL_FORM_DUALLIST_MOVERIGHT_BTN_POSTFIX = "_DETAIL_FORM_DUALLIST_MOVERIGHT_BTN";
	public static final String DETAIL_FORM_DUALLIST_MOVELEFT_BTN_POSTFIX = "_DETAIL_FORM_DUALLIST_MOVELEFT_BTN";
	public static final String DETAIL_FORM_DUALLIST_MOVEALLRIGHT_BTN_POSTFIX = "_DETAIL_FORM_DUALLIST_MOVEALLRIGHT_BTN";
	public static final String DETAIL_FORM_DUALLIST_MOVEALLLEFT_BTN_POSTFIX = "_DETAIL_FORM_DUALLIST_MOVEALLLEFT_BTN";
	
	/**
	 * Инфикс элементов списка поля JepListField
	 */
	public static final String DETAIL_FORM_LIST_ITEM_CHECKBOX_INFIX = "_DETAIL_FORM_LIST_ITEM_CHECKBOX_";
	
	/**
	 * Постфикс-идентификатор флажка "Выделить все" поля JepListField
	 */
	public static final String DETAIL_FORM_LIST_CHECKALL_POSTFIX = "_DETAIL_FORM_LIST_CHECKALL";
	
	
	/**
	 * ID первой строки списка списочной формы
	 */
	public static final String LIST_FORM_GRID_ROW_ID = "LIST_FORM_GRID_ROW_ID";
}
