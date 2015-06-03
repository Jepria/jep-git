package com.technology.jep.jepria.client.ui;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.security.ClientSecurity.CHECK_ROLES_BY_AND;
import static com.technology.jep.jepria.client.security.ClientSecurity.CHECK_ROLES_BY_OR;
import static com.technology.jep.jepria.client.ui.ActionEnum.DELETE;
import static com.technology.jep.jepria.client.ui.ActionEnum.PRINT;
import static com.technology.jep.jepria.client.ui.ActionEnum.SAVE;
import static com.technology.jep.jepria.client.ui.ActionEnum.SHOW_EXCEL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.technology.jep.jepria.client.history.place.JepWorkstatePlace;
import com.technology.jep.jepria.client.message.JepMessageBoxImpl;
import com.technology.jep.jepria.client.security.ClientSecurity;
import com.technology.jep.jepria.client.ui.eventbus.ActionEvent;
import com.technology.jep.jepria.client.ui.eventbus.BusEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.DoDeleteEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.PrepareReportEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SaveEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.SearchEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.ShowExcelEvent;

/**
 * Класс отвечает за проверку допустимости событий (EventBus) клиентского модуля.
 */
public class UiSecurity {
	public static final String CHANGE_BODY_EVENT_NAME = "changeBody";
	public static final String CHANGE_CURRENT_RECORD_EVENT_NAME = "cr";	// Сокращено ввиду участия в History URL
	public static final String CHANGE_WORKSTATE_EVENT_NAME = "cw";
	public static final String DELETE_EVENT_NAME = "delete";
	public static final String DO_DELETE_EVENT_NAME = "doDelete";
	public static final String DO_PRINT_EVENT_NAME = "doPrint";
	public static final String DO_SEARCH_EVENT_NAME = "doSearch";
	public static final String DO_SHOW_HELP_EVENT_NAME = "doShowHelp";
	public static final String ENTER_SCOPE_EVENT_NAME = "enterScope";
	public static final String EXIT_SCOPE_EVENT_NAME = "exitScope";
	public static final String GO_BACK_EVENT_NAME = "goBack";
	public static final String REFRESH_EVENT_NAME = "refresh";
	public static final String SEARCH_EVENT_NAME = "search";
	public static final String SELECT_OPTION_EVENT_NAME = "selectOption";
	public static final String SET_DETAIL_FORM_EVENT_NAME = "setDetailForm";
	public static final String SET_FORM_EVENT_NAME = "setForm";
	public static final String SET_LIST_FORM_EVENT_NAME = "setListForm";
	public static final String SET_NEW_RECORD_EVENT_NAME = "setNewRecord";
	public static final String SET_SERVICE_EVENT_NAME = "setService";
	public static final String SET_STATUSBAR_WIDGET_EVENT_NAME = "setStatusBarWidget";
	public static final String SET_TOOLBAR_WIDGET_EVENT_NAME = "setToolbarWidget";
	public static final String SAVE_EVENT_NAME = "save";
	public static final String SHOW_EXCEL_EVENT_NAME = "showExcel";
	public static final String START_EVENT_NAME = "start";
	public static final String UPDATE_SCOPE_EVENT_NAME = "updateScope";
	
	/**
	 * Карта соответствия <событие, множество ролей OR>
	 */
	private Map<Object, List<String>> accessMapOR = new HashMap<Object, List<String>>();
	
	/**
	 * Карта соответствия <событие, множество ролей AND>
	 */
	private Map<Object, List<String>> accessMapAND = new HashMap<Object, List<String>>();

	/**
	 * Проверка прав работы с событием EventBus
	 * 
	 * @param key тип события или состояние
	 * @return true, если есть право, иначе - false
	 */
	public boolean checkAccess(Object key) {
		List<String> checkedRoles;
		// Проверка ролей OR
		checkedRoles = accessMapOR.get(key); // TODO Закончить реализацию на JepPlace
		if(checkedRoles == null || ClientSecurity.isUserHaveRoles(checkedRoles)) {
			// Проверка ролей AND
			checkedRoles = accessMapAND.get(key);
			if(checkedRoles == null || ClientSecurity.isUserHaveAllRoles(checkedRoles)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Получение первой отсутствующей роли 
	 * Сделано по аналогии с версией Struts (в сообщении показывается первая отсутствующая роль)
	 * для поддержки сложившихся процессов сопровождения и администрирования
	 * 
	 * @param key
	 * @return первая отсутствующая роль
	 */
	public String getFirstRequiredRole(Object key) {
		String result = getRequiredRole(accessMapOR, key);
		if (result == null) {
			result = getRequiredRole(accessMapAND, key);
		}
		
		return result;
	}

	private String getRequiredRole(Map<Object, List<String>> accessMap, Object key) {
		String result = null;
		List<String> checkedRoles = accessMap.get(key);
		List<String> roles = ClientSecurity.instance.getRoles();
		if(checkedRoles != null && roles != null) {
			for(String role: checkedRoles) {
				if(!roles.contains(role)) {
					result = role;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Добавление ограничения прав на обработку модулем событий
	 * 
	 * @param key
	 * @param checkRolesMethod способ проверки ролей (по 'И' или по 'ИЛИ')
	 */
	public void addProtection(Object key, String strRoles, int checkRolesMethod) {
		if(checkRolesMethod == CHECK_ROLES_BY_OR) {
			accessMapOR.put(key, ClientSecurity.getRoles(strRoles));
		} else if(checkRolesMethod == CHECK_ROLES_BY_AND) {
			accessMapAND.put(key, ClientSecurity.getRoles(strRoles));
		} else {
			throw new IllegalArgumentException("Security constraint definition error: wrong checkRolesMethod: " + checkRolesMethod);
		}
	}

	public boolean checkEvent(GwtEvent event) {
		ActionEnum action = null;
		if(event instanceof PlaceChangeEvent) {
			Place place = ((PlaceChangeEvent) event).getNewPlace();
			WorkstateEnum workstate = ((JepWorkstatePlace)place).getWorkstate();
			if(!this.checkAccess(workstate)) {
				JepMessageBoxImpl.instance.alert(JepTexts.errors_security_title(), workstate.name());
				return false;
			}
//		} else if(event instanceof SaveEvent) {
		} else if(event instanceof ActionEvent) {
			action = buildActionFromEvent(event);
			if(!this.checkAccess(action)) {
				JepMessageBoxImpl.instance.alert(JepTexts.errors_security_title(), action.name());
				return false;
			}
		} else if(!this.checkAccess(event)) {
			String eventDisplayName = null;
			if(event instanceof BusEvent) {
				eventDisplayName = ((BusEvent)event).getDisplayName();
			} else {
				eventDisplayName = event.getClass().getName();
			}
			String message = JepTexts.errors_security_eventType() + eventDisplayName;

			Log.error(JepTexts.errors_security_title() + ": " + message);
			JepMessageBoxImpl.instance.alert(JepTexts.errors_security_title(), message);
			
			return false;
		}

		
		return true;
	}

	private ActionEnum buildActionFromEvent(GwtEvent event) {
		ActionEnum action = null;
		if(event instanceof SaveEvent) {
			action = SAVE;
		} else if(event instanceof DoDeleteEvent) {
			action = DELETE;
		} else if(event instanceof PrepareReportEvent) {
			action = PRINT;
		} else if(event instanceof SearchEvent) {
			action = ActionEnum.SEARCH;
		} else if(event instanceof ShowExcelEvent) {
			action = SHOW_EXCEL;
		}
		return action;
	}

}
