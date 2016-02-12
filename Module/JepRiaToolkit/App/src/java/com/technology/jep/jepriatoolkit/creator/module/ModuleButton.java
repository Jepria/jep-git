package com.technology.jep.jepriatoolkit.creator.module;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SELECTED;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_LIST;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getFieldIdAsParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.initCap;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.initSmall;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;

import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepriatoolkit.JepRiaToolkitConstant;

public class ModuleButton implements JepRiaToolkitConstant {
	
	private String buttonId;
	private WorkstateEnum[] workStates;
	private String image;
	private String event;
	private String text;
	private String name;
	private String nameEn;
	private boolean isSeparator = false;	
	public static Map<String, ModuleButton> STANDARD_TOOLBAR = new HashMap<String, ModuleButton>();
	public static Map<String, ModuleButton> STANDARD_SEPARATOR = new HashMap<String, ModuleButton>();
	
	static{
		//список стандартных кнопок тулбара
		STANDARD_TOOLBAR.put(ADD_BUTTON_ID, new ModuleButton(
				ADD_BUTTON_ID
				, new WorkstateEnum[]{ SELECTED, EDIT, VIEW_LIST, VIEW_DETAILS, SEARCH }
				, "add"
				, "add();"
				, "button_add_alt"));
		STANDARD_TOOLBAR.put(SAVE_BUTTON_ID, new ModuleButton(
				SAVE_BUTTON_ID
				, new WorkstateEnum[]{ CREATE, EDIT }
				, "save"
				, "save();"
				, "button_save_alt"));
		STANDARD_TOOLBAR.put(EDIT_BUTTON_ID, new ModuleButton(
				EDIT_BUTTON_ID
				, new WorkstateEnum[]{ SELECTED, VIEW_DETAILS }
				, "edit"
				, "edit();"
				, "button_edit_alt"));
		STANDARD_TOOLBAR.put(DELETE_BUTTON_ID, new ModuleButton(
				DELETE_BUTTON_ID
				, new WorkstateEnum[]{ SELECTED, EDIT, VIEW_DETAILS }
				, "delete"
				, "delete();"
				, "button_delete_alt"));
		STANDARD_TOOLBAR.put(VIEW_DETAILS_BUTTON_ID, new ModuleButton(
				VIEW_DETAILS_BUTTON_ID
				, new WorkstateEnum[]{ SELECTED, EDIT }
				, "view"
				, "viewDetails();"
				, "button_view_alt"));
		STANDARD_TOOLBAR.put(LIST_BUTTON_ID, new ModuleButton(
				LIST_BUTTON_ID
				, new WorkstateEnum[]{ CREATE, EDIT, VIEW_DETAILS }
				, null
				, "list();"
				, "button_list_alt"));
		STANDARD_TOOLBAR.put(SEARCH_BUTTON_ID, new ModuleButton(
				SEARCH_BUTTON_ID
				, new WorkstateEnum[]{ CREATE, EDIT, VIEW_LIST, VIEW_DETAILS, SELECTED }
				, "search"
				, "search();"
				, "button_search_alt"));
		STANDARD_TOOLBAR.put(FIND_BUTTON_ID, new ModuleButton(
				FIND_BUTTON_ID
				, new WorkstateEnum[]{ SEARCH } 
				, null
				, "doSearch();"
				, "button_find_alt"));
		STANDARD_TOOLBAR.put(REFRESH_BUTTON_ID, new ModuleButton(
				REFRESH_BUTTON_ID
				, new WorkstateEnum[]{SELECTED, VIEW_LIST}
				, "refresh"
				, "refresh();"
				, "button_refresh_alt"));
		STANDARD_TOOLBAR.put(SELECT_BUTTON_ID, new ModuleButton(
				SELECT_BUTTON_ID
				, new WorkstateEnum[]{ VIEW_LIST }
				, "select"
				, "select();"
				, "button_select_alt"));
		STANDARD_TOOLBAR.put(REPORT_BUTTON_ID, new ModuleButton(
				REPORT_BUTTON_ID
				, new WorkstateEnum[]{ EDIT, VIEW_DETAILS, SELECTED }
				, "report"
				, "report();"
				, "button_report_alt"));
		STANDARD_TOOLBAR.put(EXCEL_BUTTON_ID, new ModuleButton(
				EXCEL_BUTTON_ID
				, new WorkstateEnum[]{ VIEW_LIST, SELECTED }
				, "excel"
				, "excel();"
				, "button_excel_alt"));
		STANDARD_TOOLBAR.put(PRINT_BUTTON_ID, new ModuleButton(
				PRINT_BUTTON_ID
				, new WorkstateEnum[]{ VIEW_LIST, VIEW_DETAILS }
				, "print"
				, "print();"
				, "button_print_alt"));
		STANDARD_TOOLBAR.put(HELP_BUTTON_ID, new ModuleButton(
				HELP_BUTTON_ID
				, new WorkstateEnum[]{ CREATE, EDIT, VIEW_LIST, VIEW_DETAILS, SEARCH }
				, "help"
				, "help();"
				, "button_help_alt"));
		
		//список стандартных разделителей
		STANDARD_SEPARATOR.put(BACK_RIGHT_SEPARATOR_ID, 
				new ModuleButton(BACK_RIGHT_SEPARATOR_ID).setSeparator(true));
		
		STANDARD_SEPARATOR.put(ADD_SEPARATOR_ID, 
				new ModuleButton(ADD_SEPARATOR_ID).setSeparator(true));
		
		STANDARD_SEPARATOR.put(DELETE_SEPARATOR_ID, 
				new ModuleButton(DELETE_SEPARATOR_ID).setSeparator(true));
		
		STANDARD_SEPARATOR.put(SEARCH_SEPARATOR_ID, 
				new ModuleButton(SEARCH_SEPARATOR_ID).setSeparator(true));
		
		STANDARD_SEPARATOR.put(REFRESH_SEPARATOR_ID, 
				new ModuleButton(REFRESH_SEPARATOR_ID).setSeparator(true));
		
		STANDARD_SEPARATOR.put(HELP_SEPARATOR_ID, 
				new ModuleButton(HELP_SEPARATOR_ID).setSeparator(true));
	}
	
	/**
	 * Конструктор для разделителей тулбара
	 * 
	 * @param buttonId				идентификатор кнопки		
	 */
	public ModuleButton(String buttonId){
		setButtonId(buttonId);
	}
	
	public ModuleButton(String buttonId, WorkstateEnum[] workStates){
		this(buttonId);
		setWorkStates(workStates);
	}
	
	/**
	 * Конструктор для кнопок инструментальной панели
	 * 
	 * @param buttonId				идентификатор кнопки
	 * @param workStates			список рабочих состояний, разделенных запятой
	 * @param image					активная кнопка
	 * @param event					событие на реакцию кнопки
	 * @param text					тултип кнопки
	 */
	private ModuleButton(String buttonId, WorkstateEnum[] workStates, String image, String event, String text){
		this(buttonId, workStates);
		setImage(image);
		setEvent(event);
		setText(text);
	}
	
	/**
	 * Конструктор для кнопок инструментальной панели
	 */ 
	public ModuleButton(String buttonId, WorkstateEnum[] workStates, String image, String event, String text, String name, String nameEn){
		this(buttonId, workStates, image, event, text);
		setName(name);
		setNameEn(nameEn);
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	public boolean getIsSeparator() {
		return isSeparator;
	}
	public ModuleButton setSeparator(boolean isSeparator) {
		this.isSeparator = isSeparator;
		return this;
	}
	public String getButtonId() {
		return buttonId;
	}
	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}
	public String getButtonIdAsParameter() {
		return getFieldIdAsParameter(buttonId, null);
	}
	public String getImage() {
		return isEmpty(image) && getIsCustomButton() ? buttonId.toLowerCase() : image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getEvent() {
		if (getIsCustomButton() && isEmpty(event)){
			event = multipleConcat(getFieldIdAsParameter(buttonId, "do"), LEFT_BRACKET, RIGHT_BRACKET);
		}
		return event;
	}
	public String getHandler(){
		return isEmpty(getEvent()) ? (getIsCustomButton() ? "" : STANDARD_TOOLBAR.get(buttonId).getEvent()) : !getIsCustomButton()
				&& getEvent().toLowerCase().startsWith("changeworkstate") ? STANDARD_TOOLBAR.get(buttonId).getEvent() : multipleConcat(
						(getEvent().toLowerCase().startsWith("placecontroller") ? "" : "eventBus."), getEvent(), (getEvent().endsWith(";") ? "" : ";"));
	}
	public String getCustomEvent(){
		String customEvent = "";
		if (!isEmpty(getEvent())){
			customEvent = initCap(getEvent()).trim();
			int indexOfBracket;
			if ((indexOfBracket = customEvent.indexOf(LEFT_BRACKET)) != -1){
				customEvent = customEvent.substring(0, indexOfBracket);
			} 
		}
		return customEvent;
	}	
	public void setEvent(String event) {
		this.event = !isEmpty(event) ?  multipleConcat(event, (event.indexOf(LEFT_BRACKET) == -1 ? multipleConcat(LEFT_BRACKET, RIGHT_BRACKET) : "")) : null;
	}		
	public String getWorkStatesAsString() {
		String workStatesAsString = "";
		if (!isEmpty(workStates)){
			for (WorkstateEnum workstate : workStates){
				workStatesAsString += (!isEmpty(workStatesAsString) ? ", " : "") + workstate.name(); 
			}
		}
		else {
			return getIsCustomButton() ? "" : STANDARD_TOOLBAR.get(buttonId).getWorkStatesAsString();
		}
		return workStatesAsString;
	}
	public void setWorkStates(WorkstateEnum[] workStates) {
		this.workStates = workStates;
	}	
	public boolean getIsCustomButton(){
		return !isSeparator && isEmpty(STANDARD_TOOLBAR.get(this.buttonId));
	}	
	public boolean isCustomSeparator(){
		return isSeparator && isEmpty(STANDARD_SEPARATOR.get(this.buttonId))
							&& isEmpty(STANDARD_TOOLBAR.get(this.buttonId));
	}
	public FORM getPlacedForm() throws UnsupportedDataTypeException {
		List<WorkstateEnum> workstateList = Arrays.asList(this.workStates);
		boolean isDetailForm = false, isListForm = false;
		if ((isDetailForm = !Collections.disjoint(workstateList, Arrays.asList(SEARCH, CREATE, EDIT, VIEW_DETAILS))) & 
				(isListForm = !Collections.disjoint(workstateList, Arrays.asList(SELECTED, VIEW_LIST)))){
			return FORM.FORM_CONTAINER;
		}
		else if (isDetailForm){
			return FORM.DETAIL_FORM;
		}
		else if (isListForm){
			return FORM.LIST_FORM;
		}
		throw new UnsupportedDataTypeException("Specify workstates " + workstateList + " with attribute '" + BUTTON_ENABLE_STATES_ATTRIBUTE + "' for button '" + buttonId + "'");
	}

	public String getImageAsString(String formName) {
		return isEmpty(getImage()) ? (getIsCustomButton() ? "" : isEmpty(STANDARD_TOOLBAR.get(
				getButtonId()).getImage()) ? null : multipleConcat("JepImages.",
				STANDARD_TOOLBAR.get(getButtonId()).getImage(), "()")) : multipleConcat(
				(getIsCustomButton() ? initSmall(formName) : "Jep"), "Images.", getImage(),
				"()");
	}
	
	public String getTextAsString(String formName){
		return isEmpty(getText()) ? 
					(getIsCustomButton() ? 
							multipleConcat("\"\"") : multipleConcat("JepTexts.",
					STANDARD_TOOLBAR.get(getButtonId()).getText(), "()")) : 
					multipleConcat(
							(getIsCustomButton() ? multipleConcat(initSmall(formName), "Text.")
									: "JepTexts."), getText(), "()");
	}
}